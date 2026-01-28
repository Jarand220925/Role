/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package role;

import role.framework.GameObject;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author Jarand
 */
public class Handler {
    
    public LinkedList<GameObject> objects = new LinkedList<>();
    
    private GameObject tempObject;
    
    public void tick() {
        for(int i = 0; i < objects.size(); i++) {
            tempObject = objects.get(i);
            //System.out.println("tick handler: " + i);
            
            tempObject.tick(objects);
        }
    }
    
    public void render(Graphics g) {
        for(int i = 0; i < objects.size(); i++) {
            tempObject = objects.get(i);
            
            tempObject.render(g);
        }
    }
    
    public void addObject(GameObject object) {
        this.objects.add(0, object);
    }
    
    public void removeObject(GameObject object) {
        this.objects.remove(object);
    }
    
    /** Brukt for å fjerne landskap midlertidig.
     * @param object Objekt som skal fjernes midlertidig. */
    public void tempRemoveObject(GameObject object) {
        this.objects.remove(object);
        object.setDelete(false);
    }
    
}
