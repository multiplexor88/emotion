/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.localizator;

import logic.helpclass.Drawer;
import logic.helpclass.MatContainer;
import logic.helpclass.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.objdetect.CascadeClassifier;

/**
 * Localizes eye pair, saves rezult in MatContainer as eyePairRect, grayEyePairMat
 * @author Igor Dumchykov
 */
public class EyesLocalizator implements LocalizatorIF
{
    private static final Logger LOG = LogManager.getLogger(EyesLocalizator.class);
    
    private String HAAR_EYE_PAIR = "haarcascades//haarcascade_mcs_eyepair_big.xml";
    private CascadeClassifier eyePairCascade;
    
    public EyesLocalizator()
    {
        eyePairCascade = new CascadeClassifier(HAAR_EYE_PAIR);
    }
    
    @Override
    public boolean localize(MatContainer mc) 
    {
        MatOfRect eyeRectMat = new MatOfRect();
        eyePairCascade.detectMultiScale(mc.grayFaceMat, eyeRectMat);
        Rect rMat1[] = eyeRectMat.toArray();
        
        if(Parameters.isDetectSingleEyePair)
        {
            if( rMat1.length !=1 )//need just 1 face
            {
                LOG.warn("More than 1 eye pair were detected");
                return false;
            }
        }
        mc.eyePairRect =rMat1[0];
        
        mc.eyePairGlobalRect = new Rect(rMat1[0].x + mc.faceRect.x, rMat1[0].y + mc.faceRect.y,
                rMat1[0].width, rMat1[0].height);
        
        mc.grayEyePairMat = mc.grayFrame.submat(mc.eyePairGlobalRect);          
        return true;
    }
    
}
