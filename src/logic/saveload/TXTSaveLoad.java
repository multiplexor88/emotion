/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.saveload;

import org.opencv.core.Point;

/**
 *
 * @author Igor Dumchykov
 */
public class TXTSaveLoad implements SaveLoadIF
{

    @Override
    public boolean save(String sPath, Point[] data)
    {
        return false;
    }

    @Override
    public Point[] save(String sPath) 
    {
        return null;
    }
    
}
abstract class A
{
    
}
