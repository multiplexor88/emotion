/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.helpclass;

import logic.imageloader.USBCamera;

/**
 * This is a help class, that contains all used parameters in all functions
 * @author Igor Dumchykov
 */
public class Parameters 
{
    //debug parameters
    public static boolean       IS_DEBUG                                        = false;
    public static boolean       IS_DEBUG_DETECT_FACE_FEATURES                   = false;
    public static boolean       IS_DEBUG_ROTATE_FRAME                           = true;
    public static boolean       IS_DEBUG_CAPTURE_FRAME                          = false;
    
    public static boolean       isCaptureFrame                                  = false;
    public static boolean       isDetectSingleFace                              = true;
    public static boolean       isDetectSingleEyePair                           = true;
    public static boolean       isShowAllImages                                 = false;
    
    public static boolean       isDrawEyeRect                                   = false;
    
    public static String        imagePathStr                                    = "ImageDB/";
        
    public static int           nuberOfDetectedIrises                           = 1;

    //track eyes parameters
    public static int           preparingFramesNumber                           = 1;
    public static double        eyeRectAndBaseDiffMinThresh                     = 0.5;
    public static double        eyeRectAndBaseDiffMaxThresh                     = 0.8;
    public static double        maxIrisAngle                                    = 50.0;
    public static double        maxPrevAngDiff                                  = 5.0;
    public static double        templateSize                                    = 25.0;
    public static double        alphaITreshold                                  = 2.0;
    public static double        irisYDifferencesThreshold                       = 2.0;//pix
    public static double        irisXDifferencesThreshold                       = 0.6;//perc
    
    public enum CAMERA_TYPE{                                                    USB_MONO, 
                                                                                USB_STEREO, 
                                                                                KINECT};
    
    public enum answerEnum{                                                     CAPTUREFRAME,
                                                                                LOADFRAME,
                                                                                CAPTUREANDDETECTFEATURES}
    
    //eyebrow parameters
    public static double        eyebrowBoundRectThresh                          = 0.7;
    
    //mouth parameters
    public static double        enlargeMouthRect                                = 0.8;
}
