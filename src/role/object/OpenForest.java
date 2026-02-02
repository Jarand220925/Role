/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package role.object;

import Role.Texture;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;
import role.Role;
import static role.Role.debugRole;
import static role.Role.explorer;
import role.framework.GameObject;
import role.framework.ObjectId;
import role.interfaces.LandscapeObject;

/**
 *
 * @author Jarand
 */
public class OpenForest extends GameObject implements LandscapeObject {
    
    private final int type;
    private boolean seen;
    private int index;

    public OpenForest(float x, float y, int type, ObjectId id) {
        super(x, y, id);
        this.type = type;
    }
    
    Texture tex = Role.getInstance();

    public void tick(ArrayList<GameObject> objects) {
        if(debugRole) {
            index = objects.indexOf(this);
        }
    }
    
    @Override
    public void render(Graphics g) {
    
    vision();    
        
        if(type == 3 && seen == true) //openforest
        {
            g.drawImage(tex.block[3], (int)x, (int)y, 96, 96, null);
        } else {
          
        }
        
        if(debugRole) {
            String debugNumber = String.format("index: %d", index);
            g.setColor(Color.red);
            
            g.drawString(debugNumber, (int)x, (int)y);
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, (int)64, (int)64);
    }

    @Override
    public void vision() {
    float py = explorer.getY();
    float px = explorer.getX();
    float newx = getX();
    float newy = getY();
    float range = 600.0f;
        
        if(px >= newx - range && px <= newx + range && py >= newy - range && py <= newy + range){
        this.seen = true;
        } else {
        this.seen = false;
        }
    }
    
}
