/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.analyzer;

import logic.helpclass.Util;
import logic.imageloader.USBCamera;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Core;
import org.opencv.core.Point;
import org.opencv.core.Scalar;


/**
 * Analyzes dynamic images, obtained from cameras.
 * @see imageloader.CameraLoaderIF
 * @author Igor Dumchykov
 */
public class VideoAnalyzer extends AnalyzerIF
{
    private static final Logger LOG = LogManager.getLogger(VideoAnalyzer.class);
    private boolean isByKey;
    
    public VideoAnalyzer(logic.helpclass.Parameters.CAMERA_TYPE camType, boolean isByKey, int nPort)
    {
        prevAlpha = new Point(0.0, 0.0);
        
        switch(camType)
        {
            case USB_MONO:
            {
                imLoader = new USBCamera(nPort, isByKey);
                this.isByKey = isByKey;                
                break;
            }
            case USB_STEREO:
            {
                break;
            }
            case KINECT:
            {
                break;
            }
        }
    }
    
    /**
     * Algorithm:       INITIAL LOCALIZATION
     *              Open port           
     *              Take frame          
     *              Preprocess frame    
     *              Localize face       
     *              Localize eyepair  
     *              Localize mouth 
     *                  OBJECT TRACKING
     *              Track eyebrows      
     *              Rotate frame  
     *              Track mouth         
     *                  FEATURE POINT EXTRACTION
     *              Extract mouth Features         
     */
    @Override
    public void analyze() 
    {
        //open port
        if(!this.imLoader.open())
            return;
        
        //define counter for retrived Frames. If count = 0, then run 
        //INITIAL LOCALIZATION, other wise skip INITIAL LOCALIZATION
        boolean isRunInit = true;
        
        //set status flag in container.features to false
        //set to false when will be error.
        //It means that drawing process must be run from the begining
                
        //localize ROI and extract FP
retrieve_frame:
        do 
        {            
            //take frame
            container.origFrame = this.imLoader.loadImage();
            if(container.origFrame == null)
                continue;
            
            //Preprocess frame
            container.grayFrame = Util.preprocessFrame(container.origFrame);
            
            //run INITIAL LOCALIZATION 
initial_localization:            
            while(isRunInit)
            {
                if(!detectFaceFeatures())
                {
                   //clear deque
                    container.features.isStocked = true;
                    container.featuresDeque.clear();
                    container.featuresDeque.add(container.features);
                    
                    continue retrieve_frame;
                }

                isRunInit = false;
            }
            
//track face and eye pair
            
            //rotate frame
            int rotRes = rotateFrameAndTrackTemplate(   container.eyeBrowBaseDst, 
                                                        container.eyePairRect.width, 
                                                        container.eyeBrowBoundRectArr,
                                                        container.eyeBrowTrackingTemplateArr,
                                                        container.eyeBrowCentersPointsArr,
                                                        new Scalar(0,0,255));
                
            if(rotRes != 1)
            {
                container.features.isStocked = true;
                container.featuresDeque.clear();
                container.featuresDeque.add(container.features);
                
                isRunInit = true;
                continue retrieve_frame;
            }   
              
            //track mouth (AFTER PUT TO INITIALIZATION STEP)
            container.mouthRect = Util.trackTemplate(container.grayFrame, container.mouthRect, container.mouthMat);

            //track nose
            container.noseRect = Util.trackTemplate(container.grayFrame, container.noseRect, container.noseMat);
            
            if(container.mouthRect == null)
            {
                container.features.isStocked = true;
                container.featuresDeque.clear();
                container.featuresDeque.add(container.features);
                
                LOG.warn("Tracking pattern of nose is out of image scope");
                isRunInit = true;
                continue retrieve_frame;
            } 
            
            container.features.noseCenterPoint = new Point(container.noseRect.x + container.noseRect.width/2,
                    container.noseRect.y + container.noseRect.height/2);
            
            Core.circle(container.origFrame, container.features.noseCenterPoint, 5, new Scalar(0,0,255), -1);
            
            Core.rectangle(container.origFrame, container.noseRect.tl(), container.noseRect.br(), new Scalar(255,0,0),1);
            Core.rectangle(container.origFrame, container.mouthRect.tl(), container.mouthRect.br(), new Scalar(255,0,0),1);            

            //detect mouth FPE and show results
            mouthFPE.detect(container);
            
            //all features have been detected, therefore add features to deque
            if(container.features.isStocked)
                container.features.isStocked = false;
            
            container.featuresDeque.add(container.features);
        
//            imShowOrig.showImage(container.mouthProcessedMat);
            imShowProc.showImage(container.origFrame);
            
        } while (!isByKey); 
    } 
}
