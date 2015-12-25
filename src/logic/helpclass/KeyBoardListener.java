/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.helpclass;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Listens keyboard's key (by now just 'A').
 * Return true if target key was pressed
 * @author Igor Dumchykov
 */
public class KeyBoardListener implements KeyListener
{

    public boolean isAPressed = false;
    
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) 
    {
        if(e.getKeyCode() == KeyEvent.VK_A)
        {
            isAPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    
}