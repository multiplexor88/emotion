/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.localizator;

import logic.helpclass.MatContainer;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

/**
 *
 * @author Igor Dumchykov
 */
public class NoseLocalizator implements LocalizatorIF
{
    private static final Logger LOG = LogManager.getLogger(NoseLocalizator.class);
    
    private String HAAR_NOSE = "haarcascades//haarcascade_mcs_nose.xml";
    private CascadeClassifier noseCascade;
    
    public NoseLocalizator()
    {
        noseCascade = new CascadeClassifier(HAAR_NOSE);
    }
    
    @Override
    public boolean localize(MatContainer mc)  
    {
        //consider rectangle between eye pair and mouth
        int startX, startY, width, height;
        width = mc.mouthRect.width / 2;
        startX = mc.mouthRect.x + width/2;
        height = (int)Math.round(0.4 * (mc.mouthRect.y - mc.eyePairGlobalRect.y));
        startY = mc.eyePairGlobalRect.y + mc.eyePairGlobalRect.height + height / 4;
        
        mc.noseRect = new Rect( startX, startY, width, height );
        
        //Extract Mat from botRect
        mc.noseMat = mc.grayFrame.submat(mc.noseRect);

        //save to features in container
        mc.features.noseCenterPoint = new Point(mc.noseRect.x + mc.noseRect.width/2,
                                                mc.noseRect.y + mc.noseRect.height/2);
        return true;
    }
    
}
