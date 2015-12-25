/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import logic.helpclass.DetectedFeatures;
import org.opencv.core.Point;

/**
 *
 * @author igor
 */
public class DrawedEyeBrow extends AbstractDrawedShape
{
    private boolean     isLeft;
    
    private Line        browLine;
    
    private double      endX,
                        endY;
    
    protected double    localAnchorX,       //coordinates of local anchor (if exists (eyebrows can be rotated))
                        localAnchorY,
                        centerX,            //center point
                        centerY,
                        _centerX,           //first detected center point:  
                        _centerY,           //center = detected - _center
                        halfWidth;
    
    protected  double   localRotAng;        //rotation around eyebrow's local anchor
    protected int       label;              //for rotating
    
    protected Rotate    localRotate;//some shapes do not have
    
    public DrawedEyeBrow(Pane drawingRegion, Node parent, boolean isLeft)
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
            originX = parentX + parentWidth * FaceSizes.EYEBROW_LEFT_START_X;
            originY = parentY + parentHeight * FaceSizes.EYEBROW_LEFT_START_Y;
            
            endX = parentX + parentWidth * FaceSizes.EYEBROW_LEFT_END_X;
            endY = parentY + parentHeight * FaceSizes.EYEBROW_LEFT_END_Y; 
            
            localAnchorX = originX;
            label = -1;
        }
        else
        {
            originX = parentX + parentWidth * FaceSizes.EYEBROW_RIGHT_START_X;
            originY = parentY + parentHeight * FaceSizes.EYEBROW_RIGHT_START_Y;
            
            endX = parentX + parentWidth * FaceSizes.EYEBROW_RIGHT_END_X;
            endY = parentY + parentHeight * FaceSizes.EYEBROW_RIGHT_END_Y;
            
            localAnchorX = endX;
            
            label = 1;
        }
        
        halfWidth = (originX - endX) / 2;
        
        //set local anchor
        localAnchorY = originY;
        
        localRotate = new Rotate(0.0, localAnchorX, localAnchorY);
        
        browLine = new Line(originX, originY, endX, endY);       
        browLine.setStroke(Color.WHITE);
        browLine.setStrokeWidth(4.0);
               
        browLine.getTransforms().addAll(globalRotate, localRotate);
        
        drawingRegion.getChildren().add(browLine);    
    }

    @Override
    public void recomputePosition(DetectedFeatures df) 
    {
        //save local rotAngle
        Point[] points = df.eyeBrowCenterPointArr;
        
        if(points[0] == null || points[1] == null)
            return;
        
        double X1 = points[0].x - shiftHoriz;
        double Y1 = points[0].y - shiftVert;
        double X2 = points[1].x - shiftHoriz;
        double Y2 = points[1].y - shiftVert;
        
        if(isLeft)
            centerX = Math.min(X1, X2);
        else
            centerX = Math.max(X1, X2);
        
        centerY = centerX == X1 ? Y1 : Y2;
        
        localRotAng = Math.tan((centerY - _centerY) / halfWidth) * 180. / Math.PI  * label - rotAngle;
        
        System.err.println("Angle: " + localRotAng);
        
        localRotate.setAngle(localRotAng);
        
    }
                            
    @Override
    public void saveOriginShifting(DetectedFeatures df) 
    {
        //save local rotAngle
        Point[] points = df.eyeBrowCenterPointArr;
        
        if(points == null && (points[0] == null || points[1] == null))
            return;
        
        double X1 = points[0].x;
        double Y1 = points[0].y;
        double X2 = points[1].x;
        double Y2 = points[1].y;
        
        if(isLeft)
            _centerX = Math.min(X1, X2);
        else
            _centerX = Math.max(X1, X2);
        
        _centerY = _centerX == X1 ? Y1 : Y2;
    }
}
