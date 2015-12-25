/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.localizator;

import logic.helpclass.MatContainer;
import logic.helpclass.Parameters;
import jdk.nashorn.internal.objects.annotations.Constructor;
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
 * Localizes face, saves rezult in MatContainer as grayFaceMat, colorFaceMat,
 * faceRect
 * @author Igor Dumchykov
 */
public class FaceLocalizator implements LocalizatorIF
{
    private static final Logger LOG = LogManager.getLogger(FaceLocalizator.class);
    
    private String HAAR_FACE = "haarcascades//haarcascade_frontalface_alt.xml";
    private CascadeClassifier faceCascade;
    
    public FaceLocalizator()
    {
        faceCascade = new CascadeClassifier(HAAR_FACE);
    }
    
    @Override
    public boolean localize(MatContainer mc) 
    {
        //Extract face
        MatOfRect faceRectMat = new MatOfRect();
        faceCascade.detectMultiScale(mc.grayFrame, faceRectMat);
        
        Rect rMat[] = faceRectMat.toArray();
        
        LOG.info("Detected faces = " + rMat.length);
        
        if(Parameters.isDetectSingleFace)
        {
            if( rMat.length !=1 )//need just 1 face
            {
                return false;
            }
        }

        LOG.trace("Face rect was detected");
        
        mc.faceRect = rMat[0];
        mc.grayFaceMat = mc.grayFrame.submat(rMat[0]);
        mc.colorFaceMat = mc.origFrame.submat(rMat[0]);

        //save face center
        mc.features.faceCenterPoint = new Point(rMat[0].x + rMat[0].width / 2, rMat[0].y + rMat[0].height / 2);
        
        return true;
    }
    
}
