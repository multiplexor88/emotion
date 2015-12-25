/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.imageloader;

import logic.helpclass.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

/**
 *
 * @author Igor Dumchykov
 */
public class DiskLoader extends ImageLoaderIF
{
    private static final Logger LOG = LogManager.getLogger(DiskLoader.class);
    
    private String sPath;
    
    public void setPath(String path){sPath = path;}
    
    public DiskLoader(String sPath)
    {
        this.sPath = sPath;
    }
    
    @Override
    public Mat loadImage() 
    {
        if(!open())
        {
            LOG.error("Open frame: FAIL");
            return null;
        }
        
        LOG.trace("Download frame: SUCCESS");
        
        return frame;
    }

    @Override
    public boolean open() 
    {
        frame = Highgui.imread(sPath);
        
        if(!frame.empty())
            return true;
        return false;
    }

}
