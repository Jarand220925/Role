
package role.object;

import role.framework.GameObject;
import role.framework.ObjectId;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;
import role.Handler;
import static role.Role.FPS;
import static role.Role.H;
import static role.Role.SCALED_SIZE;
import static role.Role.W;
import static role.Role.debugRole;
import static role.Role.frames;


public class Player extends GameObject{
    
    private float w = SCALED_SIZE, h = SCALED_SIZE;
    private final Handler handler;
    private int index;
    
    private final float gravity = 0.5f;
    private final float maxSpeed = 10;   
 

    public Player(float x, float y, ObjectId id, Handler handler) {
        super(x, y, id);
        this.handler = handler;
    }
    
    

    @Override
    public void tick(ArrayList<GameObject> objects) {
        x += velX;
        y += velY;
        
        Collision(objects);
        
        if(debugRole) {
            index = objects.indexOf(this);
        }
    }

    private void Collision(ArrayList<GameObject> object) {
        for(int i = 0; i < handler.objects.size(); i++) {
            GameObject tempObject = handler.objects.get(i);
    
            if(tempObject.getId() == ObjectId.Sea) {
    
                if(getBoundsTop().intersects(tempObject.getBounds())) {
                    y = tempObject.getY() + 32;
                    velY = 0;
                }
    
                if(getBounds().intersects(tempObject.getBounds())) {
                    y = tempObject.getY() - h;
                    velY = 0;
                    falling = false;
                    jumping = false;
                } else {falling = true;}
    
                if(getBoundsRight().intersects(tempObject.getBounds())) {
                    x = tempObject.getX() - w;
                }
    
                if(getBoundsLeft().intersects(tempObject.getBounds())) {
                    x = tempObject.getX() + 35;
                }
            }
        }
    }
    
    @Override
    public void render(Graphics g) {
        g.setColor(Color.cyan);
        g.drawRect((int)x, (int)y, (int)w, (int)h);
        if(debugRole) {
            
            g.setColor(Color.red);
            g.drawRect((int) x, (int) y, 1, 1);
            
            g.setColor(Color.blue);
            g.drawOval((int) x-SCALED_SIZE, (int) y-SCALED_SIZE, SCALED_SIZE*2, SCALED_SIZE*2);
        }
        if(debugRole) {
            String debugNumber = String.format("index: %d", index);
            g.setColor(Color.LIGHT_GRAY);
            
            g.drawString(debugNumber, (int)x, (int)y);
        }
        if(debugRole) {
            Font font = new Font("Courier New", Font.BOLD, 16);
            g.setFont(font);
            g.drawString(String.format("FPS: %d frame: %d", FPS, frames), (int) (x - 5*SCALED_SIZE), (int) (y - 3*SCALED_SIZE));
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) ((int)x+(w/2)-((w/2)/2)), (int) ((int)y+(h/2)), (int)w/2, (int)h/2);
    }
    
    public Rectangle getBoundsTop() {
        return new Rectangle((int) ((int)x+(w/2)-((w/2)/2)), (int)y, (int)w/2, (int)h/2);
    }
    
    public Rectangle getBoundsRight() {
        return new Rectangle((int) ((int)x+w-5), (int)y+5, (int)5, (int)h-10);
    }
    
    public Rectangle getBoundsLeft() {
        return new Rectangle((int)x, (int)y+5, (int)5, (int)h-10);
    }
    
    
    
}
