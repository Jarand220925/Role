/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package role.framework;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

/**
 *
 * @author Jarand
 */
public abstract class GameObject {
    
    protected ObjectId id;
    protected float x, y;
    protected float velX = 0, velY = 0;
    protected boolean falling = true;
    protected boolean jumping = false;
    protected boolean markodelete = false;
    
    public GameObject(float x, float y, ObjectId id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }
    
    public abstract void tick(LinkedList<GameObject> objectList);
    public abstract void render(Graphics g);
    public abstract Rectangle getBounds();
    

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
    
    public void setId(ObjectId id){
        this.id = id;
    }
    
    public ObjectId getId(){
        return id;
    }
    
    public void setVelX(int velX){
        this.velX = velX;
    }
    
    public void setVelY(int velY){
        this.velY = velY;
    }
    
    public float getVelX(){
        return velX;
    }
    
    public float getVelY(){
        return velY;
    }

    public boolean isFalling() {
        return falling;
    }

    public void setFalling(boolean falling) {
        this.falling = falling;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }
    
    /**
     * Lyst til fjerne drit du ikke har minne til?
     * @param delete 
     */
    public void setDelete(boolean delete) {
        this.markodelete = delete;
    }
    
    public boolean getDelete() {
        return markodelete;
    }
    
}
