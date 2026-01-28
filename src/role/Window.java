/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package role;

import java.awt.Canvas;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 *
 * @author Jarand
 */
public class Window extends Canvas {

    private static final long serialVersionUID = -408240606519619600L;
    
    public Window(int w, int h, String title, Role game) {
        JFrame frame = new JFrame(title);
        
        frame.setPreferredSize(new Dimension(w, h));
        frame.setMaximumSize(new Dimension(w, h));
        frame.setMinimumSize(new Dimension(w, h));
        
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        game.start();
    }
}
