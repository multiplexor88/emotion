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
import javafx.scene.shape.Ellipse;
import javafx.scene.transform.Rotate;
import logic.helpclass.DetectedFeatures;
import org.opencv.core.Point;
import org.opencv.core.Rect;

/**
 *
 * @author igor
 */
public class DrawedMouth extends AbstractDrawedShape
{
    private Ellipse mouthEllipse;
    private double smallRadius, bigRadius;
    private Rect mouthBoundRect;
    protected double    localAnchorX,       
                        localAnchorY;
            
    protected Rotate    localRotate;//some shapes do not have
    
    public DrawedMouth(Pane drawingRegion, Node parent)
    {
        super(drawingRegion, parent);
        instanciate();
    }


    @Override
    public void instanciate() 
    {
        originX = parentX + parentWidth * FaceSizes.MOUTH_CENTER_X;
        originY = parentY + parentHeight * FaceSizes.MOUTH_CENTER_Y;
        
        localAnchorX = originX;
        localAnchorY = originY;
        localRotate = new Rotate(0.0, localAnchorX, localAnchorY);
        
        bigRadius = (FaceSizes.MOUTH_RIGHT_X - FaceSizes.MOUTH_LEFT_X) * parentWidth / 3;
        smallRadius = (FaceSizes.MOUTH_DOWN_Y - FaceSizes.MOUTH_UP_Y) * parentHeight / 3;
        
        mouthEllipse = new Ellipse(originX, originY, bigRadius, smallRadius);
        
        mouthEllipse.setFill(Color.WHITE);
        
        drawingRegion.getChildren().add(mouthEllipse);
        
        drawingRegion.getTransforms().addAll(globalRotate, localRotate);
    }

    @Override
    public void recomputePosition(DetectedFeatures df) 
    {
        mouthBoundRect = df.mouthBoundRect;
        if(mouthBoundRect != null)
        {
            mouthEllipse.setRadiusX(mouthBoundRect.width / 2);
            mouthEllipse.setRadiusY(mouthBoundRect.height / 2);
        }
        
        localRotate.setAngle(globalRotate.getAngle());//not so good
    }

    @Override
    public void saveOriginShifting(DetectedFeatures df) 
    {

    }
}
