/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.featurepointextractor;

import logic.helpclass.MatContainer;
import org.opencv.core.Point;

/**
 * Interface for extracting feature points from some region (ROI)
 * Main fuction - detec
 * @author Igor Dumchykov
 */
public interface FeaturePointsExtractorIF 
{
    /**
     * Detects feature points
     * @param mc: contains all images
     * @return array of detected feature points
     * @see helpclass.MatContainer 
     */
    public Point[] detect(MatContainer mc); 
}
