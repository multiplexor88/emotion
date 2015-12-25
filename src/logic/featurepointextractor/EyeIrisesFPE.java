/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.featurepointextractor;

import logic.helpclass.MatContainer;
import logic.helpclass.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

/**
 * Detects eye irises, desides where is left and right eye
 * @see FeaturePointsExtractorIF
 * @author Igor Dumchykov
 */
public class EyeIrisesFPE implements FeaturePointsExtractorIF
{
    private static final Logger LOG = LogManager.getLogger(EyeIrisesFPE.class);
    
    @Override
    public Point[] detect(MatContainer mc)
    {
        Mat  eyePairMat = mc.grayEyePairMat;
        Rect eyePairRect = mc.eyePairRect;
        Rect faceRect = mc.faceRect;
        
        //search for eye centers
        Mat circlesMat = new Mat();
        double minDist = 2 * eyePairRect.width / 5;
        int minRad = eyePairRect.height / 5;
        int maxRad = 2 * eyePairRect.height / 3;
        
        Imgproc.HoughCircles(eyePairMat, circlesMat, Imgproc.CV_HOUGH_GRADIENT, 3.0, minDist, 200.0, 20.0, minRad, maxRad);
        
        float arr1[] = new float[3];
        float arr2[] = new float[3];
        
        if(circlesMat.size().width == 2)
        {
            circlesMat.get(0, 0, arr1); circlesMat.get(0, 1, arr2);
            
            float f11 = arr1[0], f12 = arr1[1], f21 = arr2[0], f22 = arr2[1];
            
            if(Math.abs(f11 - f21) < Parameters.irisXDifferencesThreshold * eyePairRect.width && Math.abs(f12 - f22) > Parameters.irisYDifferencesThreshold)
            {             
           
                //find where left and right eye
                if(f11 < f21)
                    //left-right
                    return new Point[]{ new Point(f11 + faceRect.x + eyePairRect.x, f12 + faceRect.y + eyePairRect.y),
                                        new Point(f21 + faceRect.x + eyePairRect.x, f22 + faceRect.y + eyePairRect.y)      
                                    };
                else
                    //right-left
                    return new Point[]{ new Point(f21 + faceRect.x + eyePairRect.x, f22 + faceRect.y + eyePairRect.y),
                                        new Point(f11 + faceRect.x + eyePairRect.x, f12 + faceRect.y + eyePairRect.y)      
                                    };
            }
        }
        
       LOG.warn("Extract eye iris: FAIL");
       
       return null;
    }
    
}

