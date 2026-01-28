/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package role.object;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;
import static role.Role.SCALED_SIZE;
import static role.Role.explorer;
import static role.WorldLoader.loadingRange;
import role.framework.GameObject;
import role.framework.ObjectId;

/**
 *
 * @author Jarand
 */
public class EuclideanCircle extends GameObject {

    private float px = explorer.getX();
    private float py = explorer.getY();
    private boolean visible;

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public EuclideanCircle(float x, float y, ObjectId id) {
        super(x, y, id);
        this.y = py;
        this.x = px;
    }

    @Override
    public void tick(LinkedList<GameObject> objectList) {
    }

    @Override
    public void render(Graphics g) {
        if(visible) {
            g.setColor(Color.red);
            g.drawRect((int)(px-loadingRange*SCALED_SIZE), (int)(py-loadingRange*SCALED_SIZE), (int)(loadingRange*SCALED_SIZE), (int)(loadingRange*SCALED_SIZE));
            
            g.setColor(Color.magenta);
            g.drawOval((int)(px-(loadingRange)*SCALED_SIZE), (int)(py-(loadingRange)*SCALED_SIZE), ((int)(loadingRange*2)*SCALED_SIZE), (int) ((loadingRange*2)*SCALED_SIZE));
            
            g.setColor(Color.WHITE);
            Font font = new Font("Courier New", Font.BOLD, 32);
            g.setFont(font);
            g.drawString((String)"euclidean circle(not accurate)", (int)x, (int) y);
        }
        
    }
    
    public void rearrange() {
        px = (int) explorer.getX();
        py = (int) explorer.getY();
        setX((int)px);
        setY((int)py);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, (int)64, (int)64);
    }

    @Override
    public ObjectId getId() {
        return id;
    }

    @Override
    public void setId(ObjectId id) {
        this.id = id;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    @Override
    public float getVelX() {
        return velX;
    }

    public void setVelX(float velX) {
        this.velX = velX;
    }

    @Override
    public float getVelY() {
        return velY;
    }

    public void setVelY(float velY) {
        this.velY = velY;
    }

    @Override
    public boolean isFalling() {
        return falling;
    }

    @Override
    public void setFalling(boolean falling) {
        this.falling = falling;
    }

    @Override
    public boolean isJumping() {
        return jumping;
    }

    @Override
    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public boolean isMarkodelete() {
        return markodelete;
    }

    public void setMarkodelete(boolean markodelete) {
        this.markodelete = markodelete;
    }
    
}
