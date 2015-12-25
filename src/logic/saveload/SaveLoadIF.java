/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.saveload;

import org.opencv.core.Point;

/**
 * Interface saves obtained feature points in file with special format.
 * @author Igor Dumchykov
 */
public interface SaveLoadIF 
{
    public boolean save(String sPath, Point[] data);
    public Point[] save(String sPath);
}
