/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.helpclass;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayDeque;
import java.util.Deque;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;

/**
 * MatContainer class contains all Mat, Rect, Point need for processing
 * in FaceFeatureDetector
 * @author Igor Dumchykov
 */
public class MatContainer 
{
    public Mat              origFrame               = null, 
                            grayFrame               = null, 
                            grayFaceMat             = null, 
                            colorFaceMat            = null, 
                            grayEyePairMat          = null, 
                            rotationMat             = null,                   
                            mouthMat                = null,
                            noseMat                 = null,
                            mouthProcessedMat       = new Mat();//temporary Mat
    
    public Rect             faceRect                = null, 
                            eyePairRect             = null, 
                            eyePair                 = null,
                            eyePairGlobalRect       = null,
                            mouthRect               = null,
                            noseRect                = null,
                            mouthBoundRect          = null;
    
    public Mat []           eyeBrowMatArr           = new Mat[2],
                            eyeTrackingTemplateArr  = new Mat[2],
                            eyeBrowTrackingTemplateArr  = new Mat[2],
                            eyeBrowBinMatArr        = new Mat[2];
                                         
    
    public Rect[]           eyeBrowRectArr          = new Rect[2],
                            eyeTrackingRectArr      = new Rect[2],
                            eyeBrowBoundRectArr     = new Rect[2],
                            eyeBrowTrackingRectArr  = new Rect[2];
    
    public Point[]          irisPointsArr           = null;
    public Point[]          eyeBrowCentersPointsArr = null;
    
    //distance between eye pupils (interrocular distance)
    public double           eyeBaseDst               = 0.0;     
    //distance between eyebrows centers
    public double           eyeBrowBaseDst           = 0.0;  
    
    //mouth feature points
    public Point[]          mouthMatOfPoint          = null;
    
    //detected rotated angle
    public double angle;
    
    //detected features
    //Designed as deque: analyzer adds features, meantime consumers can extract
    //features 
    public Deque<DetectedFeatures> featuresDeque    = new ArrayDeque<>();
    
    public DetectedFeatures features                 = new DetectedFeatures();
    
}
