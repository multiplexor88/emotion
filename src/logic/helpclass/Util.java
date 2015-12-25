/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.helpclass;

import org.opencv.core.Core;
import static org.opencv.core.CvType.CV_32FC1;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/**
 * Some small functions, solved small tasks, such as: conver image to gray scale, 
 * equalize image histogram, calculate rotation matrix, etc.
 * @author Igor Dumchykov
 */
public class Util 
{
    
     /**
     * Convert color and equalize histogram
     * @param frame - clor frame   
     * @return grayMat - gray-scaled frame
     */
    static public Mat preprocessFrame(Mat frame)
    {
        Mat grayMat = new Mat();
        
        //Preprocessing
        Imgproc.cvtColor(frame, grayMat, Imgproc.COLOR_RGBA2GRAY);           
        Imgproc.equalizeHist(grayMat, grayMat);                                 
        
        if(Parameters.IS_DEBUG)System.out.println("Frame was processed");
        
        return grayMat;
    }
    
    /**
     * Track template within the image
     * @param grayFrame
     * @param rect
     * @param temp
     * @return 
     */
    static public Rect trackTemplate(Mat grayFrame, Rect rect, Mat temp)
    {
        Rect searchRect = new Rect( new Point(rect.x - rect.width/2, rect.y - rect.height/2),
                                    new Point(rect.x + rect.width*3/2, rect.y + rect.height*3/2));
        
        Mat dst = new Mat(searchRect.width - temp.width() + 1, searchRect.height - temp.height() + 1, CV_32FC1);
        
        if((searchRect.x < 0 || searchRect.y < 0) || (searchRect.x + searchRect.width > grayFrame.cols() || searchRect.y + searchRect.height > grayFrame.rows()))
            return null;
        
        Imgproc.matchTemplate(grayFrame.submat(searchRect), temp, dst, Imgproc.TM_SQDIFF_NORMED);
               
        Core.MinMaxLocResult result = Core.minMaxLoc(dst);

        //check new location: if coordinates change so variously, remain previous location
        if(true)
        {
            rect.x = (int) (searchRect.x + result.minLoc.x);
            rect.y = (int) (searchRect.y + result.minLoc.y);
            return rect;
        }
        else
        {
            return null;
        }
    }
}
