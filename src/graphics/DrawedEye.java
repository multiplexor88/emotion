/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import static graphics.AbstractDrawedShape.globalAnchorX;
import static graphics.AbstractDrawedShape.globalAnchorY;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import logic.helpclass.DetectedFeatures;

/**
 *
 * @author igor
 */
public class DrawedEye extends AbstractDrawedShape
{

    private boolean     isLeft;
    private Circle      eyeCircle;
    
    public DrawedEye(Pane drawingRegion, Node parent, boolean isLeft)
    {
        super(drawingRegion, parent);
        this.isLeft = isLeft;
        instanciate();
    }
    


    @Override
    public void instanciate() 
    {
        if(isLeft)
        {
            originX = parentX + parentWidth * FaceSizes.EYE_LEFT_CENTER_X;
            originY = parentY + parentHeight * FaceSizes.EYE_LEFT_CENTER_Y;
        }
        else
        {
            originX = parentX + parentWidth * FaceSizes.EYE_RIGHT_CENTER_X;
            originY = parentY + parentHeight * FaceSizes.EYE_RIGHT_CENTER_Y;
        }
        
        eyeCircle = new Circle(originX, originY, parentWidth * FaceSizes.EYE_RADIUS);

        eyeCircle.setFill(Color.WHITE);
        
        eyeCircle.getTransforms().add(globalRotate);
        
        drawingRegion.getChildren().add(eyeCircle);
    }

    @Override
    public void recomputePosition(DetectedFeatures df) {
        
    }

    @Override
    public void saveOriginShifting(DetectedFeatures df) {
        
    }
    
}
