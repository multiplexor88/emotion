/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import logic.helpclass.DetectedFeatures;

/**
 *
 * @author igor
 */
public class DrawedEntireObject 
{
    protected Pane                          drawingRegion;
    
    protected AbstractDrawedShape           body,
                                            head;
    
    protected DetectedFeatures              features;
    
    public DrawedEntireObject(Pane drawingRegion)
    {
        this.drawingRegion = drawingRegion;
        
    //    body = new DrawedBody(region);
        head = new DrawedHead(drawingRegion, null);
    }
    
    public void firstSaving(DetectedFeatures df)
    {
        features = df;
        
        head.saveOriginShifting(df);
    //    body.saveOriginShifting(df);
    }
    
    public void recomputePosition(DetectedFeatures df)
    {
        features = df;
        head.recomputePosition(df);
//        body.recomputePosition(df);
    }
    
    
}
