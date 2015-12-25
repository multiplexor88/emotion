/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.localizator;

import logic.helpclass.MatContainer;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

/**
 * INterface for detection special region of interest (ROI): face, eye pair, 
 * eyebrows, mouth, nose etc.
 * @author Igor Dumchykov
 */
public interface LocalizatorIF 
{
    /**
     * Virtual fuction.
     * Localizes ROI.
     * @param mc
     * @return false if error occurs
     */
    public boolean localize(MatContainer mc);
}
