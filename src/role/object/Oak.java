/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package role.object;

import Role.Texture;
import java.awt.Color;
import role.framework.GameObject;
import role.framework.ObjectId;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.LinkedList;
import role.Role;
import static role.Role.debugRole;
import static role.Role.explorer;
import role.interfaces.LandscapeObject;


/**
 *
 * @author Jarand
 */
public class Oak extends GameObject implements LandscapeObject {

    
    private final int type;
    private boolean seen;
    private int index;
    
    public Oak(float x, float y, int type, ObjectId id) {
        super(x, y, id);
        this.type = type;
    }

    Texture tex = Role.getInstance();
    
    @Override
    public void tick(LinkedList<GameObject> objects) {
        //Objektet slettes
//        if(delnow == true) {
//            objects.remove(this);
//        }
        if(debugRole) {
            index = objects.indexOf(this);
        }
    }

    
    @Override
    public void render(Graphics g) {
        
        vision();
        
        if(type == 0 && seen == true) //oak
        {
            g.drawImage(tex.block[0], (int)x, (int)y, 96, 96, null);
        } else {
          //return;
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

    boolean delnow=false;
    
    @Override
    public void vision() {
    
    float py = explorer.getY();
    float px = explorer.getX();
    float ox = getX();
    float oy = getY();
    float rangeX = 600.0f;
    float rangeY = 600.0f;
    float chunk = (Role.chunk)*96;
    
    // Her sjekkes det om objektet skal rendres(tegnes? gjengis?).
    if(px >= ox - rangeX && px <= ox + rangeX && py >= oy - rangeY && py <= oy + rangeY){
        seen = true;
        } else {
        seen = false;
        }
    // Denne skjekken gjør at objektet slettes av seg selv, når den er utenfor   
    if(px >= ox - chunk && px <= ox + chunk && py >= oy - chunk  && py <= oy + chunk) {
         // +(chunk/2)
            } else { 
        delnow=true;
        //System.out.println((chunk*2+newx));
        }
    }
    
    
    
}
