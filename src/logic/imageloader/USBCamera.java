/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.imageloader;

import logic.helpclass.KeyBoardListener;
import com.atul.JavaOpenCV.Imshow;
import logic.helpclass.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

/**
 * Usb camera interface for taking frame.
 * @author Igor Dumchykov
 */
public class USBCamera extends CameraLoaderIF
{
    private static final Logger LOG = LogManager.getLogger(USBCamera.class);
    
    public USBCamera(int nPort, boolean isByKey)
    {
        this.nPort = nPort;
        this.isByKey = isByKey;
        this.imShowOrig = new Imshow("Original Image");
        
        capture = new VideoCapture();
        
    }
    
    @Override
    public Mat loadImage() 
    {
        Mat frame = new Mat();
        
        if(isByKey)
        {
            KeyBoardListener kbl = new KeyBoardListener();
            imShowOrig.Window.addKeyListener(kbl);

            while (!kbl.isAPressed) 
            {            
                capture.retrieve(frame);
                
                if(!frame.empty())
                   imShowOrig.showImage(frame); 
            }
        }
        else
            capture.retrieve(frame);

        
        return frame;
    }

    @Override
    public boolean open()
    {
        if(!capture.open(nPort))
        {
            LOG.error("Can not open port " + nPort);
            return false;
        }
        else
        {
            LOG.trace("Port " + nPort + " was opened");
            return true;
        }
    }
    
}
