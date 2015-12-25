/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import logic.helpclass.DetectedFeatures;

/**
 *
 * @author igor
 */
public class DrawedNose extends AbstractDrawedShape
{
    private double      endX,
                        endY;
    
    private Line        noseLine;
    
    public DrawedNose(Pane drawingRegion, Node parent)
    {
        super(drawingRegion, parent);
        instanciate();
    }


    @Override
    public void instanciate() 
    {
        originX = parentX + parentWidth * FaceSizes.NOSE_START_X;
        originY = parentY + parentHeight * FaceSizes.NOSE_START_Y;
            
        endX = parentX + parentWidth * FaceSizes.NOSE_END_X;
        endY = parentY + parentHeight * FaceSizes.NOSE_END_Y;   
        
        noseLine = new Line(originX, originY, endX, endY);

        noseLine.setStroke(Color.WHITE);
        noseLine.setStrokeWidth(4.0);
        
        noseLine.getTransforms().add(globalRotate);
        
        drawingRegion.getChildren().add(noseLine);

    }

    @Override
    public void recomputePosition(DetectedFeatures df) {
        
    }

    @Override
    public void saveOriginShifting(DetectedFeatures df) {
        
    }
}
