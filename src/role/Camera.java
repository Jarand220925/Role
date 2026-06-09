/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package role;

import static role.Role.SCALED_SIZE;
import role.framework.GameObject;



/**
 *
 * @author J
 */
public class Camera {
    
    private float x, y;
    
    public Camera(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public void tick(GameObject player) {
        x = -player.getX() + Role.W/2 - SCALED_SIZE/2;
        y = -player.getY() + Role.H/2 - SCALED_SIZE/2;
    }
    
    public void setX(float x){
        this.x = x;
    }
    
    public void setY(float y){
        this.y = y;
    }
    
    public float getX(){
        return x;
    }
    
    public float getY(){
        return y;
    }
    
}
