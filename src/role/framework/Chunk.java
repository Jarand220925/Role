/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package role.framework;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Objects;
import role.Handler;
import static role.Role.SCALED_SIZE;
import role.object.Oak;
import role.object.OpenForest;
import role.object.Plain;
import role.object.Sea;

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
    //Ikke sikker på om x og y verdien kommer til å bli brukt.
    public int x;
    public int y;
    public GameObject[][] landscapes;
    
    public Chunk(int size, int xPos, int yPos){
        this.size = size;
        x = xPos;
        y = yPos;
        landscapes = new GameObject[size][size];
    }
    
    public void createChunk(BufferedImage image,int width, int height){
        System.out.println(String.format("Chunk: x%d y%d is loading", x,y));
        int sizeX = 0;
        for(int x = this.x * size; x < (this.x + 1) * size; x++){
            int sizeY = 0;
            for(int y = this.y * size; y < (this.y + 1) * size ; y++){
                
                int pixel = image.getRGB(x, y);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;
                
                //if(landscapes[sizeX][sizeY] == null){System.out.println("This tile is null");}
                if(red == 63 && green == 72 & blue == 204) landscapes[sizeX][sizeY] = new Sea(x*SCALED_SIZE, y*SCALED_SIZE, 1, ObjectId.Sea);
                if(red == 34 && green == 177 & blue == 76) landscapes[sizeX][sizeY] = new Oak(x*SCALED_SIZE, y*SCALED_SIZE, 0, ObjectId.Oak);
                if(red == 255 && green == 242 & blue == 0) {landscapes[sizeX][sizeY] = new Plain(x*SCALED_SIZE, y*SCALED_SIZE, 2, ObjectId.Plain);}
                if(red == 181 && green == 230 & blue == 29) {landscapes[sizeX][sizeY] = new OpenForest(x*SCALED_SIZE, y*SCALED_SIZE, 3, ObjectId.OpenForest);}
                //else if(red == 255 && green == 255 & blue == 255) {landscapes[sizeX][sizeY] = null;}
                sizeY++;
            }
            sizeX++;
        }
        System.out.println(String.format("Chunk: x%d y%d has finished loading", x,y));
    }
    
    public void insertToHandler(Handler handler){
        handler.addObjectCollection(Arrays.stream(landscapes)
                .filter(Objects::nonNull)
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .toArray(GameObject[]::new));
    }
    
}
