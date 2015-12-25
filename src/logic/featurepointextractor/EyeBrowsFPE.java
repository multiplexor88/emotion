/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.featurepointextractor;

import logic.helpclass.MatContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Core;
import static org.opencv.core.CvType.CV_8UC1;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Detects eyebrows feature points
 * @see FeaturePointsExtractorIF
 * @author Igor Dumchykov
 */
public class EyeBrowsFPE implements FeaturePointsExtractorIF
{

    private static final Logger LOG = LogManager.getLogger(EyeBrowsFPE.class);
    
    @Override
    public Point[] detect(MatContainer mc)
    {
        
        return new Point[0];
    }

    
    /**
     * getSkeleton  obtain thin 1-pixel region from contour. 
     * @param src   input binary image
     * @return      binary image 
     */
    
    
    private Mat getSkeleton(Mat src)
    {
        Mat skel = new Mat(src.rows(), src.cols(), CV_8UC1, new Scalar(0));
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_CROSS, new Size(3,3));
        Mat tmp = new Mat();
        Mat eroded = new Mat();
        boolean done = false;
        
        do
        {
            Imgproc.morphologyEx(src, eroded, Imgproc.MORPH_ERODE, element);
            Imgproc.morphologyEx(eroded, tmp, Imgproc.MORPH_DILATE, element);
            Core.subtract(src, tmp, tmp);
            Core.bitwise_or(skel, tmp, skel);
            eroded.copyTo(src);

            done = (Core.countNonZero(src) == 0);
        }while(!done);
        
        return skel;
    }
}