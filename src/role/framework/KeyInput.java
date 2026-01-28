/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package role.framework;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import role.Handler;
import static role.Role.SCALED_SIZE;
import static role.Role.debugRole;
import static role.Role.euclideanCircle;




 
public class KeyInput extends KeyAdapter {
    
    private Handler handler;
    
    public KeyInput(Handler handler){
        this.handler = handler;
    }
    
    int speed = 10;
    
    /** Dette kan nok gjøres bedre ved å unngå å se igjennom listen kontinuerlig. Iallefall for styring av spilleren.
     * @param e - Hvilken tast som blir gjenkjent. **/
    @Override
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        
        for(int i = 0; i < handler.objects.size(); i++)
        {
            GameObject tempObject = handler.objects.get(i);
            GameObject chooseObject = handler.objects.get(0);
            //System.out.println("for loop in keyPressed: " + i);
            
            if(tempObject.getId() == ObjectId.Player)
            {
                if(key == KeyEvent.VK_W) tempObject.setVelY(-speed);
                if(key == KeyEvent.VK_S) tempObject.setVelY(speed);
                if(key == KeyEvent.VK_D) {
                    tempObject.setVelX(speed);
                    if(e.isControlDown()) {
                        System.out.println("ctrl + d");
                        if(euclideanCircle.isVisible()) {
                            euclideanCircle.setVisible(false);
                        } else {
                            euclideanCircle.setVisible(true);
                        }
                    }
                }
                if(key == KeyEvent.VK_A) tempObject.setVelX(-speed);
                if(key == KeyEvent.VK_2) {
                    if(euclideanCircle.isVisible()) {
                            euclideanCircle.setVisible(false);
                        } else {
                            euclideanCircle.setVisible(true);
                        }
                    if(debugRole == false) {
                        debugRole = true;
                    } else {
                        debugRole = false;
                    }
                }
                if(key == KeyEvent.VK_X && chooseObject.getId() != ObjectId.Player) {
                    System.out.println(handler.objects.get(0));
                    System.out.println("X " + chooseObject.getX()/SCALED_SIZE + " Y " + chooseObject.getY()/SCALED_SIZE);
                    System.out.println(tempObject.getX() + " " + tempObject.getY());
                    System.out.println(handler.objects.size());
                    delOneland(chooseObject);
                      
                } else if(key == KeyEvent.VK_X && handler.objects.size() > 1 && chooseObject.getId() == ObjectId.Player) {
                    System.out.println(handler.objects.get(1).getId());
                    delOneland(handler.objects.get(1));
                }
                /*if(key == KeyEvent.VK_SPACE && !tempObject.isJumping()) {
                tempObject.setJumping(true);
                tempObject.setVelY(-10);
                }*/
//                if(key == KeyEvent.VK_CONTROL) {
//                    if(key == KeyEvent.VK_D)
//                    System.out.println("ctrl + d");
//                }
            }
        }
        
        
        //System.out.println(key);
        //if(key == KeyEvent.VK_ESCAPE){
            
        //}
    }
    
    
    @Override
    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();
        
        for(int i = 0; i < handler.objects.size(); i++)
        {
            GameObject tempObject = handler.objects.get(i);
            
            if(tempObject.getId() == ObjectId.Player)
            {
                if(key == KeyEvent.VK_W) tempObject.setVelY(0);
                if(key == KeyEvent.VK_S) tempObject.setVelY(0);
                if(key == KeyEvent.VK_D) tempObject.setVelX(0);
                if(key == KeyEvent.VK_A) tempObject.setVelX(0);
            }
        }
    }
    
    private void delOneland(GameObject o) {
        //GameObject chooseObject = o; //tvilsomt
        
        System.out.println(String.format("Type: %s", o.getClass()));
        System.out.println("object's X " + o.getX());
        System.out.println("object's Y " + o.getY());
        handler.objects.remove(o);
        System.out.println("objects left " + handler.objects.size());
    }
}
