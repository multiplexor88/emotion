/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import javafx.scene.Node;
import javafx.scene.Parent;
import logic.helpclass.DetectedFeatures;

/**
 *
 * @author igor
 */
public interface DrawedShape 
{
    
    //Set origin coordinates for shape
    public void instanciate();
    
    //runs when new position of the shape will be detected
    public void recomputePosition(DetectedFeatures df);
    
    //save first detected feature points
    public void saveOriginShifting(DetectedFeatures df);
}
