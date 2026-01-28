/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package role;

import java.awt.image.BufferedImage;
import static role.Role.explorer;
import role.framework.GameObject;
import role.framework.ObjectId;
import role.object.Oak;
import role.object.Sea;
import role.object.OpenForest;
import role.object.Plain;



/**
 *
 * @author Jarand
 */
public class WorldLoaderStatic {
    
    private static Handler handler;
    
    public static void loadImageAgain(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        
        int chunk = 5;
        float py = explorer.getY()/96;
        float px = explorer.getX()/96;
        int look = 0;
        float range = 500.0f;
        
        System.out.println("width, height: " + w + " " + h);
        System.out.println("Player x: " + px);
        
        //for(int yy = 0; yy < w; yy++) Brukes for å legge inn ruter basert på størrelsen av bilde.
        
        for(int xx = (int) px - chunk; xx < px + chunk*2; xx++) {
            for(int yy = (int) py - chunk; yy < py + chunk*2; yy++) {
                
                GameObject lookobject = handler.objects.get(look);
                if(xx < 0 || yy < 0 || xx > h || yy > w) {
                    System.out.println("skipped: " + xx + " " + yy);
                } else {
                    if (lookobject.getX() == xx && lookobject.getY() == yy) {
                        System.out.println("skipped: " + xx + " " + yy + "  Land filled");
                        look++;
                    } else {
                int pixel = image.getRGB(xx, yy);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;
                
                if(red == 63 && green == 72 & blue == 204) handler.addObject(new Sea(xx*96, yy*96, 1, ObjectId.Sea));
                if(red == 34 && green == 177 & blue == 76) handler.addObject(new Oak(xx*96, yy*96, 0, ObjectId.Oak));
                if(red == 255 && green == 242 & blue == 0) handler.addObject(new Plain(xx*96, yy*96, 2, ObjectId.Plain));
                if(red == 181 && green == 230 & blue == 29) handler.addObject(new OpenForest(xx*96, yy*96, 3, ObjectId.OpenForest));
                
                
                //if(px >= GameObject - range && px <=  + add && py >= newy - add && py <= newy + add )
                    }
                }
            }
        }
    }
}
