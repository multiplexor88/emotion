/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import static graphics.AbstractDrawedShape.globalRotate;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import logic.helpclass.DetectedFeatures;

/**
 *
 * @author igor
 */
public class DrawedHead extends AbstractDrawedShape
{
    private DrawedShape             eyeLeft, 
                                    eyeRight, 
                                    eyeBrowLeft,
                                    eyeBrowRight,
                                    nose,
                                    mouth;
    
    private Ellipse                 headEllipse;
    
    private double                  radiusX,
                                    radiusY;
    
    public DrawedHead(Pane drawingRegion, Node parent)
    {
        super(drawingRegion, parent);
        instanciate();
    }

    @Override
    public void instanciate() 
    {
        
        //in the middle of the scene
        globalAnchorX = originX = sceneWidth / 2;
        globalAnchorY = originY = sceneHeight / 2;
        
        globalRotate = new Rotate(0.0, globalAnchorX, globalAnchorY);
        globalTranslate = new Translate(0, 0);
        
        radiusX = sceneHeight / 4;
        radiusY = sceneWidth /6;
        
        headEllipse = new Ellipse(originX, originY, sceneHeight / 4, sceneWidth /6);
        
        drawingRegion.getChildren().add(headEllipse);
        
        //initialize children
        eyeBrowLeft = new DrawedEyeBrow(drawingRegion, headEllipse, true);
        eyeBrowRight = new DrawedEyeBrow(drawingRegion, headEllipse, false);
        
        eyeLeft = new DrawedEye(drawingRegion, headEllipse, true);
        eyeRight = new DrawedEye(drawingRegion, headEllipse, false);
        
        nose = new DrawedNose(drawingRegion, headEllipse);
        
        mouth = new DrawedMouth(drawingRegion, headEllipse);
    }

    @Override
    public void recomputePosition(DetectedFeatures df) 
    {
        System.err.println("Center: " + df.faceCenterPoint.y);
        //recompute translation
        shiftHoriz = df.noseCenterPoint.x - _originX;
        shiftVert = df.noseCenterPoint.y - _originY;
        
        globalTranslate.setX(shiftHoriz);
        globalTranslate.setY(shiftVert);
        
        //recompute rotation
        rotAngle = df.faceRotAngle;
        globalRotate.setAngle(rotAngle - _rotAngle);
                
        eyeBrowLeft.recomputePosition(df);
        eyeBrowRight.recomputePosition(df);
        
        mouth.recomputePosition(df);


    }

    @Override
    public void saveOriginShifting(DetectedFeatures df) 
    {
        _rotAngle = df.faceRotAngle;
        
        _originX = df.noseCenterPoint.x;
        _originY = df.noseCenterPoint.y;
        
        eyeBrowLeft.saveOriginShifting(df);
        eyeBrowRight.saveOriginShifting(df);
    }
}
