/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package role;

import java.awt.image.BufferedImage;
import static role.Role.SCALED_SIZE;
import static role.Role.chunk;
import static role.Role.euclideanCircle;
import static role.Role.explorer;
import static role.Role.loadPosX;
import static role.Role.loadPosY;
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
public class WorldLoader {
    
    /** Ser på posisjonen til spilleren via den euklidiske forskjellen. Hvis denne verdien er høyere enn loadingRange vil objektene bli lastet inn på nytt. */
    private float loadingPos = 0;
    /** Avstanden i euklidisk forstand før innlastning av objekter gjøres på nytt. */
    public static float loadingRange = 5;
    
    /**
     * Sjekker størrelsen på bilde, og legger inn ruter(objects) som representerer landskap.
     * @param image 
     * Bilde som skal skannes.
     * @param handler
     * Hvilken listebehandler som skal brukes.
     */
    public void loadImageLevel(BufferedImage image, Handler handler) {
        int w = image.getWidth();
        int h = image.getHeight();
        
        //int chunk = this.chunk; //tvilsomt
        int py = (int)explorer.getY()/SCALED_SIZE;
        int px = (int)explorer.getX()/SCALED_SIZE;
//        float newx = getX();
//        float newy = getY();
//        float range = 500.0f;
        
        System.out.println("Placing land");
        System.out.println("Image width and height: " + w + " " + h);
        System.out.println("Player x: " + px);
        
        //for(int yy = 0; yy < w; yy++) Brukes for å legge inn ruter basert på størrelsen av bilde.
        int landsmade = 0;
        for(int xx = (int) px - chunk; xx <= px + chunk; xx++) {
            for(int yy = (int) py - chunk; yy <= py + chunk; yy++) {
                
                if(xx < 0 || yy < 0 || xx > h || yy > w) {
                    //System.out.println("skipped: " + xx + " " + yy);
                } else {
                
                int pixel = image.getRGB(xx, yy);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;
                
                if(red == 63 && green == 72 & blue == 204) handler.addObject(new Sea(xx*SCALED_SIZE, yy*SCALED_SIZE, 1, ObjectId.Sea));
                if(red == 34 && green == 177 & blue == 76) handler.addObject(new Oak(xx*SCALED_SIZE, yy*SCALED_SIZE, 0, ObjectId.Oak));
                if(red == 255 && green == 242 & blue == 0) handler.addObject(new Plain(xx*SCALED_SIZE, yy*SCALED_SIZE, 2, ObjectId.Plain));
                if(red == 181 && green == 230 & blue == 29) handler.addObject(new OpenForest(xx*SCALED_SIZE, yy*SCALED_SIZE, 3, ObjectId.OpenForest));
                
                //if(px >= GameObject - range && px <=  + add && py >= newy - add && py <= newy + add )
                //System.out.println("xx: " + xx);
                landsmade++;
                }
            }
        }
        //System.out.println("objects-list size: " + handler.objects.size());
        System.out.println("tiles looped: " + landsmade);
        //System.out.println("px: " +  px );
    }
    
    public void loadImageAgain(BufferedImage image,Handler handler) {
        int w = image.getWidth();
        int h = image.getHeight();
        
        int chunk = Role.chunk;
        int py = (int) explorer.getY()/SCALED_SIZE;
        int px = (int) explorer.getX()/SCALED_SIZE;
        float range = 500.0f;
        int look;
        System.out.println("width, height: " + w + " " + h);
        System.out.println("Player x: " + px + " y: " + py);
        
        //for(int yy = 0; yy < w; yy++) Brukes for å legge inn ruter basert på størrelsen av bilde.
        
        for(int xx = (int) px - chunk; xx <= px + chunk; xx++) {
            for(int yy = (int) py - chunk; yy <= py + chunk; yy++) {
               
                look = 0;
                GameObject lookobject = handler.objects.get(look);
                // Hvis koordinatet er utenfor bilde, skal ikke noe lages og legges til i lister.
                if(xx < 0 || yy < 0 || xx > h || yy > w) {
                    //System.out.println("skipped: " + xx + " " + yy);
                } else {
                    
                    for(int o = 0; o < handler.objects.size(); o++) {
                        lookobject = handler.objects.get(o);
                        if(lookobject.getX() == xx*SCALED_SIZE && lookobject.getY() == yy*SCALED_SIZE) {
                            o = handler.objects.size();
                            //System.out.println("lookobjid: " + handler.objects.indexOf(lookobject));
                        }
                        
                    }
                    //System.out.println("lookobjid: " + handler.objects.indexOf(lookobject));
                    
                    if (lookobject.getX() == xx*SCALED_SIZE && lookobject.getY() == yy*SCALED_SIZE) {
                        //System.out.println("skipped: " + xx + " " + yy + "  Land filled");
                    } else {
                        int pixel = image.getRGB(xx, yy);
                        int red = (pixel >> 16) & 0xff;
                        int green = (pixel >> 8) & 0xff;
                        int blue = (pixel) & 0xff;
                
                        if(red == 63 && green == 72 & blue == 204) handler.addObject(new Sea(xx*SCALED_SIZE, yy*SCALED_SIZE, 1, ObjectId.Sea));
                        if(red == 34 && green == 177 & blue == 76) handler.addObject(new Oak(xx*SCALED_SIZE, yy*SCALED_SIZE, 0, ObjectId.Oak));
                        if(red == 255 && green == 242 & blue == 0) handler.addObject(new Plain(xx*SCALED_SIZE, yy*SCALED_SIZE, 2, ObjectId.Plain));
                        if(red == 181 && green == 230 & blue == 29) handler.addObject(new OpenForest(xx*SCALED_SIZE, yy*SCALED_SIZE, 3, ObjectId.OpenForest));
                        //System.out.println(look);
                
                
                        //if(px >= GameObject - range && px <=  + add && py >= newy - add && py <= newy + add )
                    }
                }
            }
        }
        System.out.println("euclidian distance: " + Math.sqrt((double)((px-loadPosX)*(px-loadPosX)+(py-loadPosY)*(py-loadPosY))));
        System.out.println("objects in world: " + handler.objects.size());
        //System.out.println(handler.objects.indexOf(explorer));
    }
    
    public void removeTerrain(Handler handler) {
        
        //float chunk = this.chunk;
        int py = (int) explorer.getY()/SCALED_SIZE;
        int px = (int) explorer.getX()/SCALED_SIZE;
        
        System.out.println("removing terrain");
        //loadingPos = 0;
        
        //Markering
        for(int i = 0; i < handler.objects.size(); i++) {
            
            float ox = handler.objects.get(i).getX()/SCALED_SIZE;
            float oy = handler.objects.get(i).getY()/SCALED_SIZE;
            int countedObjs;
            //System.out.println("ox: " + ox + " px: " + px);
            
            if(handler.objects.get(i).getId() != ObjectId.Player) {
                if(ox < px - chunk || oy < py - chunk || ox > px + chunk || oy > py + chunk) {
                    GameObject objectForRemoval = handler.objects.get(i);
                    /* Kan være det er for tidlig å slette objektet, fordi det objektet som nå har akkurat samme plass i listen blir hoppet over,
                    selv om det er oppfyller kravene til å bli fjernet. */
                    handler.objects.remove(objectForRemoval);
                    objectForRemoval.setDelete(true);
//                    System.out.println("Marked: " + objectForRemoval.getId() + " Index size: " + handler.objects.size());
//                    System.out.println("info: " + ox + " x " + oy + " y ");
//                    System.out.println("index: " + handler.objects.indexOf(oy));
//                    System.out.println("marked?: " + objectForRemoval.getDelete());
                } else if(handler.objects.size() <= 10) {
                    System.out.println("Ended removal");
                    return;
                } else {
                    //System.out.println("within player zone");
                }
            } else {System.out.println("this is a player" + "  " + "where i is: " + i);}
            
            countedObjs = handler.objects.size()-i;
            
        }
        
                
        int stuffInObjectList = handler.objects.size() - 1;
        int bottom = 0;
        System.out.println("Deletion commencing");
                
        //Sletting
//        while(bottom < stuffInObjectList) {
//        //System.out.println("int bottom " + bottom + "  index size: " + handler.objects.size());
//        if(handler.objects.get(bottom).getDelete() == true) {
//        handler.objects.remove(bottom);
//        stuffInObjectList = handler.objects.size() - 1;
//        //System.out.println("int bottom " + bottom + "  stuffInObjectList: " + stuffInObjectList);
//        } else {
//        bottom++;
//        stuffInObjectList = handler.objects.size() - 1;
//        //System.out.println("objects left:  " + stuffInObjectList);
//        }
//        }
//        System.out.println("Index size after removal: " + handler.objects.size());
        //runLoadImageLevel();
                
                
    }
    
    /** Utfører utregning på hvor spilleren befinner seg på kartet,
     * og legger ser om spilleren har beveget seg en gitt distanse fra det punktet.
     * Hvis spilleren har beveget seg den gitte distansen, nevn ovenfor, vil landskap bli
     * stokket om.
     * @param worldImg Bilde som skal leses fra.
     * @param handler Hvilken objektbehandler(Handler) som skal brukes.
     */
    public void checkForRearrange(BufferedImage worldImg,Handler handler) {
        int py = (int) explorer.getY()/SCALED_SIZE;
        int px = (int) explorer.getX()/SCALED_SIZE;
        loadingPos = (float)Math.sqrt((double)((px-loadPosX)*(px-loadPosX)+(py-loadPosY)*(py-loadPosY)));
        if (loadingRange < loadingPos) {
            removeTerrain(handler);
            loadImageAgain(worldImg,handler);
            loadPosX = explorer.getX()/SCALED_SIZE;
            loadPosY = explorer.getY()/SCALED_SIZE;
            euclideanCircle.rearrange();
            loadingPos = 0;
        }
//        if ( (px*px+py*py) < loadingpos - loadingrange || (int) (px*px+py*py) > loadingpos + loadingrange) {
//                    removeTerrain();
//                }
    }
    
    
}
