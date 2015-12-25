/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import logic.helpclass.DetectedFeatures;

/**
 *
 * @author igor
 */
public class DrawedBody extends AbstractDrawedShape
{
    public DrawedBody(Pane drawingRegion, Node parent)
    {
        super(drawingRegion, parent);
    }

    @Override
    public void instanciate() {
       
    }

    @Override
    public void recomputePosition(DetectedFeatures df) {
        
    }

    @Override
    public void saveOriginShifting(DetectedFeatures df) {
        
    }
    
}
