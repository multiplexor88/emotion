/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.analyzer;

import logic.helpclass.Util;
import logic.imageloader.DiskLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Core;
import org.opencv.core.Scalar;

/**
 * Analyzes static images, loaded from disk space.
 * @author Igor Dumchykov
 */
public class PhotoAnalyzer extends AnalyzerIF
{
    private static final Logger LOG = LogManager.getLogger(PhotoAnalyzer.class);
    private String sPath;
    
    public PhotoAnalyzer(String sPath)
    {
        this.sPath = sPath;
        this.imLoader = new DiskLoader(sPath);
    }
    /**
     * See AnalyzeIF::analyze() for more details
     */
    @Override
    public void analyze()
    {
        //open port
        if(!this.imLoader.open())
            return;

        //localize ROI and extract FP

        //take frame
        container.origFrame = this.imLoader.loadImage();
        if(container.origFrame == null)
            return;
            
        //Preprocess frame
        container.grayFrame = Util.preprocessFrame(container.origFrame);
            
        //run INITIAL LOCALIZATION 

        if(!detectFaceFeatures())
            return;

        //rotate frame
        int rotRes = rotateFrameAndTrackTemplate(   container.eyeBrowBaseDst, 
                                                        container.eyePairRect.width, 
                                                        container.eyeBrowBoundRectArr,
                                                        container.eyeBrowTrackingTemplateArr,
                                                        container.eyeBrowCentersPointsArr,
                                                        new Scalar(0,0,255));
                
        if(rotRes != 1)
                return;
            
        Core.rectangle(container.origFrame, container.noseRect.tl(), container.noseRect.br(), new Scalar(0,0,255),2);
        Core.rectangle(container.origFrame, container.mouthRect.tl(), container.mouthRect.br(), new Scalar(0,0,255),2);            
            
        //detect mouth FPE and show results
        mouthFPE.detect(container);
            
        imShowOrig.showImage(container.mouthMat);
        imShowProc.showImage(container.origFrame);
    }; 
}
