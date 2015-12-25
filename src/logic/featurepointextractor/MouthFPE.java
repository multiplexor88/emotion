/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.featurepointextractor;

import logic.helpclass.MatContainer;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY;

/**
 * Detects mouth feature points
 * @see FeaturePointsExtractorIF
 * @author Igor Dumchykov
 */
public class MouthFPE implements FeaturePointsExtractorIF
{
    private static final Logger LOG = LogManager.getLogger(MouthFPE.class);
    
    /**
     * Detect mouth feature points
     * Algorithm:           Equalize histogram of mouth rect
     *                      Implement Sobel horizontal filter
     *                      Find corners
     *                      Invert color + Binarization
     *                      Find lip up and down points
     * @param mc
     * @return 
     */
    @Override
    public Point[] detect(MatContainer mc) 
    {
        /**Algorithm
         *                  find pix(i) = (R-G)/R
         *                  normalize: 2arctan(pix(i))/pi
         */
        
        //find pix(i) = (R-G)/R
        Mat mouthRGBMat = mc.origFrame.submat(mc.mouthRect);
        List mouthSplitChannelsList = new ArrayList<Mat>();
        Core.split(mouthRGBMat, mouthSplitChannelsList);
        //extract R-channel
        Mat mouthR = (Mat)mouthSplitChannelsList.get(2);
        mouthR.convertTo(mouthR, CvType.CV_64FC1);
        //extract G-channel
        Mat mouthG = (Mat)mouthSplitChannelsList.get(1);
        mouthG.convertTo(mouthG, CvType.CV_64FC1);
        //calculate (R-G)/R
        Mat dst = new Mat(mouthR.rows(), mouthR.cols(), CvType.CV_64FC1);
        mc.mouthProcessedMat = new Mat(mouthR.rows(), mouthR.cols(), CvType.CV_64FC1);
        
        Core.absdiff(mouthR, mouthG, dst);
//        Core.divide(dst, mouthR, mc.mouthProcessedMat);
        mc.mouthProcessedMat = dst;
        mc.mouthProcessedMat.convertTo(mc.mouthProcessedMat, CvType.CV_8UC1);
        Imgproc.equalizeHist(mc.mouthProcessedMat, mc.mouthProcessedMat);
//       Imgproc.blur(mc.mouthProcessedMat, mc.mouthProcessedMat, new Size(4,4));
//        Imgproc.morphologyEx(mc.mouthProcessedMat, mc.mouthProcessedMat, Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(4,4)));
        Imgproc.threshold(mc.mouthProcessedMat, mc.mouthProcessedMat, 230, 255, THRESH_BINARY);   
        
        
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(mc.mouthProcessedMat, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        
        //find the biggest contour
            int maxSize = -1;
            int tmpSize = -1;
            int index = -1;
            
            Rect centMouthRect = new Rect( mc.mouthRect.x + mc.mouthRect.width/4,
                                           mc.mouthRect.y + mc.mouthRect.height/4, 
                                           mc.mouthRect.width/2,
                                           mc.mouthRect.height/2);
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
                int centX = mc.mouthRect.x + boundRect.x + boundRect.width/2 ;
                int centY = mc.mouthRect.y + boundRect.y + boundRect.height/2;
//                LOG.info("Center = " + centX + "; " + centY);
//                LOG.info("Rect = " + centMouthRect.x + "; " + centMouthRect.y);
                if(!centMouthRect.contains(new Point(centX, centY)))
                    continue;
               
                tmpSize = contours.get(j).toArray().length;
                
                LOG.info("Contour " + j + "; size = " + tmpSize);
                
                if(tmpSize > maxSize)
                {
                    maxSize = tmpSize;
                    index = j;
                }
            }
        
        //appproximate curve
        Point[] p1 = contours.get(index).toArray();
        MatOfPoint2f p2 = new MatOfPoint2f(p1);
        MatOfPoint2f p3 = new MatOfPoint2f();
        Imgproc.approxPolyDP(p2, p3, 1, true);
        
        p1 = p3.toArray();
        
        MatOfInt tmpMatOfPoint = new MatOfInt();
        Imgproc.convexHull(new MatOfPoint(p1), tmpMatOfPoint);
        
        Rect boundRect = Imgproc.boundingRect(new MatOfPoint(p1));
        if(boundRect.area() / mc.mouthRect.area() > 0.3)
            return null;
        
        int size = (int)tmpMatOfPoint.size().height;
        Point[]_p1 = new Point[size];
        int[]a = tmpMatOfPoint.toArray();
        
        _p1[0] = new Point( p1[a[0]].x + mc.mouthRect.x, p1[a[0]].y + mc.mouthRect.y);
        Core.circle(mc.origFrame, _p1[0], 3, new Scalar(0,0,255),-1);
        for(int i = 1; i < size; i++)
        {
            _p1[i] = new Point( p1[a[i]].x + mc.mouthRect.x, p1[a[i]].y + mc.mouthRect.y);
            Core.circle(mc.origFrame, _p1[i], 3, new Scalar(0,0,255),-1);
            Core.line(mc.origFrame, _p1[i-1], _p1[i], new Scalar(255,0,0), 2);            
        }
        Core.line(mc.origFrame, _p1[size-1], _p1[0], new Scalar(255,0,0), 2); 
            
/*        contours.set(index, new MatOfPoint(_p1));
            
        mc.mouthProcessedMat.setTo(new Scalar(0));
        
        Imgproc.drawContours(mc.mouthProcessedMat, contours, index, new Scalar(255), -1);
        
*/        mc.mouthMatOfPoint = _p1;

        MatOfPoint matOfPoint = new MatOfPoint(_p1);
        mc.mouthBoundRect = Imgproc.boundingRect(matOfPoint);
        mc.features.mouthBoundRect = mc.mouthBoundRect;
        
        /**extract feature points:  1 most left
         *                          2 most right
         *                          3,4 up
         *                          5,6 down
         */
        
//        mc.mouthMatOfPoint = extractFeaturePoints(contours.get(index));
                              
                    
        return null;
    }
    
    /**
     * Extract feature points from contour of mouth
     * Algorithm:   For all points find most left and most right
     *              Find 2 pair up and down center points between most left and most right points
     * @param contour
     * @return 6 feature points
     */
    private Point[] extractFeaturePoints(MatOfPoint contour)
    {
        Point[] dst = new Point[6];
        
        int size = (int)contour.size().height;
        
        Point[] cPoints = contour.toArray();
        
        int leftInd = 0;    double leftVal = cPoints[leftInd].x;
        int rightInd = 0;   double rightVal = cPoints[rightInd].x;
        for(int i = 1; i < size; ++i)
        {
            double xVal = cPoints[i].x;
            if( leftVal > xVal )
            {
                leftVal = xVal;
                leftInd = i;
            }
            
            if( rightVal < xVal )
            {
                rightVal = xVal;
                rightInd = i;
            }
        }
        
        dst[0] = cPoints[leftInd];
        dst[1] = cPoints[rightInd];
        
        
        int indDiff = rightInd - leftInd;
        
        
        return dst;
    }
    
}

