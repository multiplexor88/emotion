/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.localizator;

import logic.helpclass.MatContainer;
import logic.helpclass.Parameters;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

/**
 *
 * @author Igor Dumchykov
 */
public class MouthLocalizator implements LocalizatorIF
{
    private static final Logger LOG = LogManager.getLogger(MouthLocalizator.class);
    
    private String HAAR_MOUTH = "haarcascades//haarcascade_mcs_mouth.xml";
    private CascadeClassifier mouthCascade;
    
    public MouthLocalizator()
    {
        mouthCascade = new CascadeClassifier(HAAR_MOUTH);
    }
    
    /**Localizes mouth rectangle
     *      Divide face on upRect end botRect
     *      Extract Mat from botRect
     *      Equalize botMat
     *      Find mouthRect using Haar cascades
     *      Enlarge mouthRect according to face size
     * @param mc
     * @return mouthRect, mouthMat
     */
    @Override
    public boolean localize(MatContainer mc)  
    {
        /* Mouth Localization by Haar cascades*/
        /*
        //Divide face on upRect end botRect
        int start_x = mc.faceRect.x;
        int start_y = mc.faceRect.y + (int)mc.faceRect.height*2/3;
        int end_x = start_x + mc.faceRect.width;
        int end_y = start_y + (int)mc.faceRect.height*2/3;
        
        end_x = end_x >= mc.grayFrame.width() ? mc.grayFrame.width() : end_x;
        end_y = end_y >= mc.grayFrame.height() ? mc.grayFrame.height() : end_y;
        
        mc.mouthRect = new Rect(new Point(start_x, start_y), new Point(end_x, end_y));
        //Extract Mat from botRect
        
        
        LOG.error(mc.mouthRect.tl() + " " + mc.mouthRect.br());
        LOG.error(mc.grayFrame.size());
            
        mc.mouthMat = mc.grayFrame.submat(mc.mouthRect);
        //Equalize botMat
        Imgproc.equalizeHist(mc.mouthMat, mc.mouthMat);
        //Find mouthRect using Haar cascades
        MatOfRect mouthRectMat = new MatOfRect();
        mouthCascade.detectMultiScale(mc.mouthMat, mouthRectMat);
        Rect rMat[] = mouthRectMat.toArray();
          
        //find real mouth within detected mouth array rMat
        List<Rect> newFacesArr = new ArrayList();
        for(Rect rect:rMat)
        {
            rect = new Rect(rect.x + mc.faceRect.x, rect.y + mc.faceRect.y + mc.faceRect.height*2/3, rect.width, rect.height);
            
            //check if rect out of face
            if(!mc.faceRect.contains(rect.br()) || !mc.faceRect.contains(rect.tl()))
                continue;
            
            newFacesArr.add(rect);
        }
        
        if( newFacesArr.size() !=1 )//need just 1 mouth
        {
            LOG.warn("Detected mouth number: " + newFacesArr.size());
            return false;
        }

        //Enlarge mouthRect according to mouth size
        Rect rect = newFacesArr.get(0);
        int enlargeX = (int)Math.round(Parameters.enlargeMouthRect * rect.width);
        int enlargeY = (int)Math.round(Parameters.enlargeMouthRect * rect.height);
        mc.mouthRect  = new Rect(rect.x - enlargeX, rect.y - enlargeY, rect.width + enlargeX*2, rect.height + enlargeY*2);
        
        //save mat for mouth
        mc.mouthMat = mc.grayFrame.submat(mc.mouthRect);
*/
        
        /*Localization by enlarging:
        mouth rectangle can be defined as third lower part of face rect 
        with enlarging over 1/5 of face lower part:
        Mouth_rect = faceY + faceHeight*3/2 + faceHeight/5
        */
        
        int heightFace = mc.faceRect.height;
        int mouthHeight = heightFace*1/3 + heightFace/5;
        int tmp = mc.faceRect.y + heightFace*2/3 + mouthHeight;
        int mouthEndY = tmp < mc.origFrame.width() ? tmp : mc.origFrame.width();
        
        mc.mouthRect = new Rect(
                                    new Point(
                                                mc.faceRect.x, mc.faceRect.y + heightFace*2/3),
                                    new Point(
                                                mc.faceRect.x + mc.faceRect.width, mouthEndY));
        mc.mouthMat = mc.grayFrame.submat(mc.mouthRect);
        //show results
        Core.rectangle(mc.origFrame, mc.mouthRect.tl(), mc.mouthRect.br(), new Scalar(0,0,255));
//        Core.rectangle(mc.origFrame, mc.faceRect.tl(), mc.faceRect.br(), new Scalar(0,0,255));
        return true;
    }
    
}
