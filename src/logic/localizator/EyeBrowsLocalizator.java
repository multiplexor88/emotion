/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.localizator;

import logic.helpclass.Drawer;
import logic.helpclass.MatContainer;
import logic.helpclass.Parameters;
import logic.imageloader.USBCamera;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Core;
import static org.opencv.core.CvType.CV_8UC1;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Localizes eyebrow pair, saces rezult in MatContainer as eyeBrowRectArr
 * @author Igor Dumchykov
 */
public class EyeBrowsLocalizator implements LocalizatorIF
{
    private static final Logger LOG = LogManager.getLogger(EyeBrowsLocalizator.class);
    
    @Override
    public boolean localize(MatContainer mc) 
    {
        Rect eyePairRect = mc.eyePairRect;//non global rectangle
        int width = eyePairRect.width;
        int height = eyePairRect.height;
         //build eyebrown search rectangle
        int x, y, w, h;
        double shiftH = 0.13, shiftV = 0.1;
        
        Point browPoint1UpLft = null, browPoint2UpLft = null, browPointDwnRght1 = null, browPointDwnRght2 = null;
        double browROIHeight = eyePairRect.y;
/*            
        if(mc.irisPointsArr[0].y > mc.irisPointsArr[1].y)
        {
            browPoint1 = new Point(mc.irisPointsArr[0].x - eyePairW * shiftH * 2, mc.irisPointsArr[1].y - eyePairH * shiftV);
            browPoint2 = new Point(mc.irisPointsArr[1].x + eyePairW * shiftH * 2, mc.irisPointsArr[1].y - browROIHeight );
        }
        else
        {
            browPoint1 = new Point(mc.irisPointsArr[0].x - eyePairW * shiftH * 2, mc.irisPointsArr[0].y - eyePairH * shiftV);
            browPoint2 = new Point(mc.irisPointsArr[1].x + eyePairW * shiftH * 2, mc.irisPointsArr[0].y - browROIHeight);
        }

        Point middlePointDown = new Point(browPoint1.x + (browPoint2.x - browPoint1.x)/2, browPoint2.y);
        Point middlePointUp = new Point(browPoint1.x + (browPoint2.x - browPoint1.x)/2, browPoint1.y);
        mc.eyeBrowRectArr[0] = new Rect(browPoint1, middlePointDown);
        mc.eyeBrowRectArr[1] = new Rect(middlePointUp, browPoint2);
        
        mc.eyeBrowMatArr[0] = mc.grayFrame.submat(mc.eyeBrowRectArr[0]);
        mc.eyeBrowMatArr[1] = mc.grayFrame.submat(mc.eyeBrowRectArr[1]);
*/
        
        
        //define eyebrow searching rectangle
        browPoint1UpLft = new Point( mc.faceRect.x + eyePairRect.x, mc.faceRect.y + eyePairRect.y - height/2 );
        browPointDwnRght1 = new Point( mc.faceRect.x + eyePairRect.x + width/2, mc.faceRect.y + eyePairRect.y + height/2 );
        
        browPoint2UpLft = new Point( mc.faceRect.x + eyePairRect.x + width/2, mc.faceRect.y + eyePairRect.y - height/2 );       
        browPointDwnRght2 = new Point( mc.faceRect.x + eyePairRect.x + width, mc.faceRect.y + eyePairRect.y + height/2 );
        
        mc.eyeBrowRectArr[0] = new Rect(browPoint1UpLft, browPointDwnRght1);
        mc.eyeBrowRectArr[1] = new Rect(browPoint2UpLft, browPointDwnRght2);
        
        //save Mat from eyeBrowRect
        mc.eyeBrowMatArr[0] = mc.grayFrame.submat(mc.eyeBrowRectArr[0]);
        mc.eyeBrowMatArr[1] = mc.grayFrame.submat(mc.eyeBrowRectArr[1]);
        
        //detect eyebrows
        if(!detectEyeBrowBoundRect(mc))
            return false;
        
        return true;
    }
    
    
    private boolean detectEyeBrowBoundRect(MatContainer mc)
    {
        int eyePairW = mc.eyePairRect.width;
        int eyePairH = mc.eyePairRect.height;

        //contains eyebrow bounding rectangles
        Rect boundRectArr[] = new Rect[2];
        
        //for each eyebrow
        Mat binMat = new Mat();
        for(int i = 0; i < 2; ++i)
        {
            mc.eyeBrowMatArr[i] = mc.grayFrame.submat(mc.eyeBrowRectArr[i]);
            Scalar meanScalar = Core.mean(mc.eyeBrowMatArr[i]);
            //negate image
            Core.convertScaleAbs(mc.eyeBrowMatArr[i], mc.eyeBrowMatArr[i], 1, 255-meanScalar.val[0]);
            Imgproc.equalizeHist(mc.eyeBrowMatArr[i], mc.eyeBrowMatArr[i]);
            Imgproc.blur(mc.eyeBrowMatArr[i], mc.eyeBrowMatArr[i], new Size(4,4));
            
            //obtain binary image
            Imgproc.threshold(mc.eyeBrowMatArr[i], binMat, 70, 255, Imgproc.THRESH_BINARY_INV);
            
            Imgproc.morphologyEx(binMat, binMat, Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(4,4)));

            //find contours
            List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
            Imgproc.findContours(binMat, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
                       
            //find the biggest contour
            int maxSize = -1;
            int tmpSize = -1;
            int index = -1;
            
            if(contours.size() != 0)
            {
                maxSize = contours.get(0).toArray().length;
                tmpSize = 0;
                index = 0;
            }
            
            //find max contour
            for(int j =0; j < contours.size(); ++j)
            {
                //if contour is vertical, exclude it 
                Rect boundRect = Imgproc.boundingRect(contours.get(j));
                if(boundRect.height > boundRect.width)
                    continue;
                
                if((double)boundRect.height/(double)mc.eyeBrowRectArr[i].height > Parameters.eyebrowBoundRectThresh)
                {
                    LOG.warn("Reset brow rect");
                    mc.eyeBrowBoundRectArr[i] = null;
                    return false;
                }
                
                tmpSize = contours.get(j).toArray().length;
                
                LOG.info("Contour " + j + "; size = " + tmpSize);
                
                if(tmpSize > maxSize)
                {
                    maxSize = tmpSize;
                    index = j;
                }
            }
            
            binMat.setTo(new Scalar(0));
            boundRectArr[i] = Imgproc.boundingRect(contours.get(index));
            
            //save eyebrow bounding rectangle
            mc.eyeBrowBoundRectArr[i] = new Rect(   mc.eyeBrowRectArr[i].x + boundRectArr[i].x,
                                                    mc.eyeBrowRectArr[i].y + boundRectArr[i].y, 
                                                    boundRectArr[i].width, 
                                                    boundRectArr[i].height);
            
            //save binary eyebrow Mat for further FP detection (skeletonization)
            mc.eyeBrowBinMatArr[0] = binMat;
      
            //define tracking template for eyebrow
            mc.eyeBrowTrackingTemplateArr[i] = mc.grayFrame.submat(mc.eyeBrowBoundRectArr[i]);  //local rectangle
        }
        
        //compute eyebrow interrocular distance
        mc.eyeBrowBaseDst = Math.abs(mc.eyeBrowBoundRectArr[0].x + mc.eyeBrowBoundRectArr[0].width/2
                        - (mc.eyeBrowBoundRectArr[1].x + mc.eyeBrowBoundRectArr[1].width/2));
        
        LOG.info("eyeBrowBaseDst = " + mc.eyeBrowBaseDst); 
        
        //define new bound rect centers for tracking template
        mc.eyeBrowCentersPointsArr = new Point[2];
        
        
        //save eyebrow centers
        //left-right
        Point p1 = new Point( mc.eyePairGlobalRect.x + mc.eyeBrowBoundRectArr[0].x + mc.eyeBrowBoundRectArr[0].width / 2,
                              mc.eyePairGlobalRect.y + mc.eyeBrowBoundRectArr[0].y + mc.eyeBrowBoundRectArr[0].height / 2);
            
        Point p2 = new Point( mc.eyePairGlobalRect.x + mc.eyeBrowBoundRectArr[1].x + mc.eyeBrowBoundRectArr[1].width / 2,
                              mc.eyePairGlobalRect.y + mc.eyeBrowBoundRectArr[1].y + mc.eyeBrowBoundRectArr[1].height / 2);

        Point[] pointArr = new Point[2];
        pointArr[0] = p1; pointArr[1] = p2;
        
        mc.features.eyeBrowCenterPointArr = pointArr;
        
        return true;
    }
}
