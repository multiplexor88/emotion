/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.imageloader;

import com.atul.JavaOpenCV.Imshow;
import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

/**
 * Interface for camera devices.
 * Supported devices by now: USB mono camera.
 * Controls pick up frame from camera.
 * Main function - loadimage
 * @author Igor Dumchykov
 */
public abstract class CameraLoaderIF extends ImageLoaderIF
{
    protected VideoCapture capture;
    protected boolean isByKey;
    public Imshow imShowOrig;
    protected int nPort;
    
    /**
     * Virtual function according to special device.
     * @return obtained frame from camera
     */
    abstract public Mat loadImage();
}
