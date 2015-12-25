/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.helpclass;

import org.opencv.core.Point;
import org.opencv.core.Rect;

/**
 *
 * @author igor
 */
public class DetectedFeatures 
{    
    public Point        faceCenterPoint,
                        noseCenterPoint,
                        mouthCenterPoint;
    
    public Point[]      eyeCenterPontArr,
                        eyeBrowCenterPointArr;
    
    public Point[]      mouthPoints;
    
    public double       faceRotAngle;
    
    public boolean      isStocked;      //if false, run drawing procedure from the begining
                                        //true if image stocked
    
    public boolean      isEmpty;
    
    public Rect         mouthBoundRect;
}
