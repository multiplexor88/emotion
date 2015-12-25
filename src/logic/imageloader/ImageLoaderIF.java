/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.imageloader;

import logic.featurepointextractor.MouthFPE;
import logic.helpclass.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

/**
 * Takes image from defined space: disk, camera.
 * @author Igor Dumchykov
 */
public abstract class ImageLoaderIF 
{
    private static final Logger LOG = LogManager.getLogger(ImageLoaderIF.class);
    //resulted frame
    protected Mat frame = new Mat();
    
    /**
     * Virtual function. Controls getting connection to frame source.
     * For disk space mode it can be just path to image, for camera - port number, etc.
     * @return false if image does not exist or can not open camera port.
     */
    abstract public boolean open();
    
    /**
     * Virtual function. Load image from defined resource: disk space, camera, etc.
     * @return 
     */
    abstract public Mat loadImage();
    
    public boolean saveImage(String name)
    {
        if(!frame.empty())
        {
            Highgui.imwrite(name, frame);
            LOG.trace("Frame was saved");
            return true;
        }
        else
        {
            LOG.error("Frame is empty");
            return false;
        }
    };
}
