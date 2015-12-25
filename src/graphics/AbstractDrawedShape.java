/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 *
 * @author igor
 */
abstract public class AbstractDrawedShape implements DrawedShape
{
    protected double            originX,    //original position of the shape
                                originY,    //instanciated in this.instanciate()

                                            //all next shiftings will be computed according to this origin
                                newPosX,    //current shifting (new position of shape detected by AnalyzerIF
                                newPosY;    //with respect to shiftedX, shiftedY:
                                            
                                            //FORMULA:
                                            //origin = newPos - shifted )
    protected double            _originX,   //instanciated when first detection will be accomplished
                                _originY;   //it is the origin of the shape and
            
    static public double        shiftHoriz,
                                shiftVert;
    
    static public double        globalAnchorX,    //coordinates of global anchor (rotating center)
                                globalAnchorY;
    
    static public double        rotAngle,
                                _rotAngle;   //rotated angle
    
    static public Rotate        globalRotate;
    static public Translate     globalTranslate;
    
    protected Pane              drawingRegion; 
    
    protected Node              parentNode; //parent, to whom this shape is belonged
    
    protected double            sceneWidth, //width of drawing region
                                sceneHeight;//height of drawing region
    
    protected double            parentX,    //width of drawing region
                                parentY,    //height of drawing region
                                parentWidth,//width of drawing region
                                parentHeight;//height of drawing region
    
    public AbstractDrawedShape(Pane drawingRegion, Node parentNode)
    {
        this.drawingRegion = drawingRegion;
        this.parentNode = parentNode;
               
        //save sizes
        sceneWidth = this.drawingRegion.getPrefWidth();
        sceneHeight = this.drawingRegion.getPrefHeight();
        
        if(parentNode != null)
        {
            Bounds bounds = parentNode.getLayoutBounds();
            parentX = bounds.getMinX();
            parentY = bounds.getMinY();
            parentWidth = bounds.getWidth();
            parentHeight = bounds.getHeight();
        }

        System.out.println(sceneWidth + " " + sceneHeight);
        
        //draw
        //instanciate();
        //draw();
    }
}
