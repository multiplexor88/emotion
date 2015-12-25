/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.helpclass;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

/**
 * Draws previously detected results on image
 * @author Igor Dumchykov
 */
public class Drawer 
{
    static public void drawTrackedEyeCenters(Mat frame, Point p1, Point p2)
    {
        Core.circle(frame, p1, 3, new Scalar(0,0,255), 5);
        Core.circle(frame, p2, 3, new Scalar(0,0,255), 5);
        Core.line(frame, p1, p2, new Scalar(0,255,0), 1);
    }
    
    static public void drawRectanglePair(Mat frame, Rect rect1, Rect rect2, Scalar color)
    {
        Core.rectangle(frame, rect1.tl(), rect1.br(), color);
        Core.rectangle(frame, rect2.tl(), rect2.br(), color);
    }
    
    static public void drawEyePair(Mat frame, Rect rect1, Scalar color)
    {
        Core.rectangle(frame, rect1.tl(), rect1.br(), color);
    }
}
