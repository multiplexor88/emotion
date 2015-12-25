/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.imageloader;

import org.opencv.core.Mat;

/**
 * Kinect interface for taking frame.
 * @author Igor Dumchykov
 */
public class KinectCamera extends CameraLoaderIF
{

    @Override
    public Mat loadImage() {
        return null;
    }

    @Override
    public boolean open() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
