/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package role.framework;

import role.interfaces.LandscapeObject;

/**
 * Chunks skal være skalerbare.
 * Avhengig av WorldLoader, så skal chunks med landskap på kanten av innlastnigsbilde føre
 * til at chunks på andre siden av bilde lastes inn.
 * Alle landskap i Chunks skal bli lastet inn asynkront etter at initiell innlastning
 * av verden er ferdig.
 * 
 */

/**
 * 
 * @author Jarand
 */
public class Chunk {
    public int size;
    public int x;
    public int y;
    public LandscapeObject[][] landscapes;
    
    public Chunk(int size, int xPos, int yPos){
        this.size = size;
        x = xPos;
        y = yPos;
    }
    
}
