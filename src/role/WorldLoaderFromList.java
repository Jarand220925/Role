/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package role;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import static role.Role.SCALED_SIZE;
import static role.Role.appIsRunning;
import static role.Role.chunk;
import static role.Role.euclideanCircle;
import static role.Role.explorer;
import static role.Role.loadPosX;
import static role.Role.loadPosY;
import static role.Role.loadingPos;
import static role.Role.loadingRange;
import role.framework.GameObject;
import role.framework.ObjectId;
import role.object.Oak;
import role.object.OpenForest;
import role.object.Plain;
import role.object.Sea;

/**
 * Denne versjonen har en egen objektbehandler(Handler) som bare tar inn landskap.
 * @author Jarand
 */
public class WorldLoaderFromList {
    Handler worldHandler;
    
    private int prevLowX;
    private int prevLowY;
    private int prevHighX;
    private int prevHighY;
    public boolean firstUse = true;
    
    //List<GameObject> wH;
    public void init() {
        worldHandler = new Handler();
    }
    /**
     * Sjekker størrelsen på bilde, og legger inn ruter(objects) som representerer landskap.
     * @param image 
     * Bilde som skal skannes.
     */
    public void loadImageLevel(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        
        int py = (int)explorer.getY()/SCALED_SIZE;
        int px = (int)explorer.getX()/SCALED_SIZE;
        
        
//        float newx = getX();
//        float newy = getY();
//        float range = 500.0f;
        
        System.out.println("Loading land");
        System.out.println("Image width and height: " + w + " " + h);
        System.out.println("Player x and y: " + px + " " + py);
        
        //for(int yy = 0; yy < w; yy++) Brukes for å legge inn ruter basert på størrelsen av bilde.
        int landsmade = 0;
        for(int xx = 0; xx < h; xx++) {
            for(int yy = 0; yy < w; yy++) {
                
                if(xx < 0 || yy < 0 || xx > h || yy > w) {
                    System.out.println("skipped: " + xx + " " + yy);
                } else {
                System.out.println("xx yy: " + xx + " " + yy);
                int pixel = image.getRGB(xx, yy);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;
                
                if(red == 63 && green == 72 & blue == 204) worldHandler.addObject(new Sea(xx*SCALED_SIZE, yy*SCALED_SIZE, 1, ObjectId.Sea));
                if(red == 34 && green == 177 & blue == 76) worldHandler.addObject(new Oak(xx*SCALED_SIZE, yy*SCALED_SIZE, 0, ObjectId.Oak));
                if(red == 255 && green == 242 & blue == 0) worldHandler.addObject(new Plain(xx*SCALED_SIZE, yy*SCALED_SIZE, 2, ObjectId.Plain));
                if(red == 181 && green == 230 & blue == 29) worldHandler.addObject(new OpenForest(xx*SCALED_SIZE, yy*SCALED_SIZE, 3, ObjectId.OpenForest));
                
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
    
    public void loadLandscape(Handler handler) {
        // Spillerens posisjon ved forrige innlastning.
        int scaledLoadPosX = (int) loadPosX * SCALED_SIZE;
        int scaledLoadPosY = (int) loadPosY * SCALED_SIZE;
        // Spillerens x og y posisjon.
        int scaledPX = (int) explorer.getX();
        int scaledPY = (int) explorer.getY();
        int px = scaledPX/SCALED_SIZE;
        int py = scaledPY/SCALED_SIZE;
        
        int scaledChunk = chunk * SCALED_SIZE;
        
        // Unøyaktig
        int currHighX = scaledPX + scaledChunk;
        int currHighY = scaledPY + scaledChunk;
        int currLowX = scaledPX - scaledChunk;
        int currLowY = scaledPY - scaledChunk;
        
        int smallestHighX = Math.min(prevHighX, currHighX);
        int biggestLowX = Math.max(prevLowX, currLowX);
        int smallestHighY = Math.min(prevHighY, currHighY);
        int biggestLowY = Math.max(prevLowY, currLowY);
        
        int worldHandlerSize = worldHandler.objects.size();
        
        //int intX;
        int prevX = 0;
        int prevY = 0;
        int setXxTo = px * SCALED_SIZE - scaledChunk;
        int setYyTo = py * SCALED_SIZE + scaledChunk;
        int nextIndexX = 0;
        for(int xx = setXxTo; xx <= currHighX; xx+=SCALED_SIZE) {
            
            int nextIndexY = 0;
            final int intX = currHighX - nextIndexX * SCALED_SIZE;
            
            //if (intX == prevX) continue;
            // For hver indeks tellende fra null og oppover, vil y posisjonen endre seg i senkende verdi.
            Optional<GameObject> go = worldHandler.objects.stream().filter(GameObject -> GameObject.getX() == intX).findFirst();
            go = worldHandler.objects.stream().filter(GameObject -> GameObject.getX() <= intX && GameObject.getY() <= currHighY).findFirst();
            GameObject landToCheck = null;

            //if(xx >= biggestLowX && xx <= smallestHighX && currLowY >= biggestLowY && currLowY <= smallestHighY) continue;
                
            if(go.isPresent()){
                landToCheck = go.get();
            }
                
            
            // Indeksen av objektet fra første utspørring.
            int indexOfChkObj = worldHandler.objects.indexOf(landToCheck);
            
            
            for(int yy = setYyTo; yy >= currLowY; yy-=SCALED_SIZE) {
                final int intY = yy;
                //if (intY == prevY) continue;
                
                //if (intY < 0) continue;
                int landToInsert = indexOfChkObj + nextIndexY;
                
                GameObject insertObj;
                //Hvis utspørringen etter første objekt med gyldige x-posisjon er nullifisert,
                // eller om landToInsert er større enn gyldige indekser i worldHandler.
                if(indexOfChkObj > -1 && landToInsert < worldHandlerSize){
                    //Hvis boksene med koordinater ikke har noen felles ruter.
                    if(biggestLowX > smallestHighX || biggestLowY > smallestHighY) {firstUse = true;} else {firstUse = false;}
                    //Hvis denne x og y posisjonen er innenfor felles innlastnings-område som den forrige.
                    if((intX >= biggestLowX && intX <= smallestHighX && yy >= biggestLowY && yy <= smallestHighY) && !firstUse) {
                        //if(yy <= smallestHighY) continue;
                        nextIndexY++;
                        continue;
                    }
                    
                    
                    insertObj = worldHandler.objects.get(landToInsert);
                
                    if(insertObj.getX() <= scaledPX + scaledChunk && insertObj.getY() <= scaledPY + scaledChunk){
                        handler.addObject(insertObj);
                        //GameObject filteredObj = handler.objects.stream().filter(GameObject ->).findFirst();
                        nextIndexY++;
                    }
                }
                prevY = intY;
            }
            prevX = intX;
            nextIndexX++;
        }
        prevHighX = scaledLoadPosX + scaledChunk;
        prevHighY = scaledLoadPosY + scaledChunk;
        prevLowX = scaledLoadPosX - scaledChunk;
        prevLowY = scaledLoadPosY - scaledChunk;
        firstUse = false;
    }
    
    public void loadFivehundredRandomLands(Handler handler) {
        for(int i = 0; i < 500; i++) {
            Random rnd = new Random();
            int range = rnd.nextInt(worldHandler.objects.size() - 1);
            GameObject randomObject = worldHandler.objects.get(range);
            handler.addObject(randomObject);
        }
    }
    
    /** Lagt til for å teste uten å vente på at alle landene må bli lastet inn.
     * @param image bilde som skal skannes. */
    public void loadOnlyThousandFromImage(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        
        int setAmount = 50;
        
        int py = (int)explorer.getY()/SCALED_SIZE;
        int px = (int)explorer.getX()/SCALED_SIZE;

        System.out.println("Loading land");
        System.out.println("Image width and height: " + w + " " + h);
        System.out.println("Player x and y: " + px + " " + py);
        
        //for(int yy = 0; yy < w; yy++) Brukes for å legge inn ruter basert på størrelsen av bilde.
        int landsmade = 0;
        for(int xx = 0; xx < setAmount; xx++) {
            for(int yy = 0; yy < setAmount; yy++) {
                
                if(xx < 0 || yy < 0 || xx > h || yy > w) {
                    System.out.println("skipped: " + xx + " " + yy);
                } else {
                System.out.println("xx yy: " + xx + " " + yy);
                int pixel = image.getRGB(xx, yy);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;
                
                if(red == 63 && green == 72 & blue == 204) worldHandler.addObject(new Sea(xx*SCALED_SIZE, yy*SCALED_SIZE, 1, ObjectId.Sea));
                if(red == 34 && green == 177 & blue == 76) worldHandler.addObject(new Oak(xx*SCALED_SIZE, yy*SCALED_SIZE, 0, ObjectId.Oak));
                if(red == 255 && green == 242 & blue == 0) worldHandler.addObject(new Plain(xx*SCALED_SIZE, yy*SCALED_SIZE, 2, ObjectId.Plain));
                if(red == 181 && green == 230 & blue == 29) worldHandler.addObject(new OpenForest(xx*SCALED_SIZE, yy*SCALED_SIZE, 3, ObjectId.OpenForest));
                
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
    
    public void addItAll(Handler handler) {
        handler.objects.addAll(0, worldHandler.objects);
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
            int objsIterated = 0;
            //System.out.println("ox: " + ox + " px: " + px);
            int handlerSize = handler.objects.size();
            
            while(handler.objects.get(i).getId() != ObjectId.Player || handler.objects.size() < chunk*chunk || handlerSize >= objsIterated) {
                objsIterated++;
                if(ox < px - chunk || oy < py - chunk || ox > px + chunk || oy > py + chunk) {
                    GameObject objectForRemoval = handler.objects.get(i);
//                    System.out.println("index: " + handler.objects.indexOf(objectForRemoval));
                    /* Kan være det er for tidlig å slette objektet, fordi det objektet som nå har akkurat samme plass i listen blir hoppet over,
                    selv om det er oppfyller kravene til å bli fjernet. */
                    objectForRemoval.setDelete(true);
                    handler.tempRemoveObject(objectForRemoval);
                    
//                    System.out.println("Marked and removed: " + objectForRemoval.getId() + " Index size is now: " + handler.objects.size());
//                    System.out.println("Removed object with coordinates: " + ox + " x " + oy + " y ");
                    
//                    System.out.println("marked?: " + objectForRemoval.getDelete());
                } else if (handler.objects.size() <= 4) {
                    System.out.println("Ended removal");
                    return;
                } else {
                    // ikke riktig
                    System.out.println("within player zone, or objectlist is bigger than set size.");
                    return;
                }
            } 
            if(handler.objects.get(i).getId() == ObjectId.Player) {System.out.println("this is a player" + "  " + "where i is: " + i);}
            if(handler.objects.size() <= chunk*chunk) {System.out.println(String.format("Handler reached chunk*chunk size: ", chunk*chunk));}
            //??
            countedObjs = handler.objects.size()-i;
            System.out.println(String.format("hello %d", chunk*chunk));
        }
        
                
        int stuffInObjectList = handler.objects.size() - 1;
        int bottom = 0;
        //System.out.println("Deletion commencing");
                
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
        System.out.println("Index size after removal: " + handler.objects.size());
        //runLoadImageLevel();
                
                
    }
    
    public void removeTerrainWhile(Handler handler) {
        
        //float chunk = this.chunk;
        int py = (int) explorer.getY()/SCALED_SIZE;
        int px = (int) explorer.getX()/SCALED_SIZE;
        
        System.out.println("removing terrain");
        int index = 0;
        int objsIterated = 0;
        int handlerSize = handler.objects.size();
        
        
        while(handlerSize > chunk*chunk || handlerSize-1 > objsIterated) {
            objsIterated++;
            if(handler.objects.get(index).getId() == ObjectId.Player) {
                index++;
                //handlerSize--;
                continue;
            }
            
            float ox = handler.objects.get(index).getX()/SCALED_SIZE;
            float oy = handler.objects.get(index).getY()/SCALED_SIZE;
            
            if(ox < px - chunk || oy < py - chunk || ox > px + chunk || oy > py + chunk) {
                GameObject objectForRemoval = handler.objects.get(index);
//                    System.out.println("index: " + handler.objects.indexOf(objectForRemoval));
                objectForRemoval.setDelete(true);
                handler.tempRemoveObject(objectForRemoval);
                    
//                    System.out.println("Marked and removed: " + objectForRemoval.getId() + " Index size is now: " + handler.objects.size());
//                    System.out.println("Removed object with coordinates: " + ox + " x " + oy + " y ");
                    
//                    System.out.println("marked?: " + objectForRemoval.getDelete());
                } else if (handler.objects.size() <= 4) {
                    System.out.println("Ended removal");
                    return;
                } else {
                    // ikke riktig
                    System.out.println("within player zone, or objectlist is bigger than set size.");
                    index++;
                }
            handlerSize--;
            } 
        if(handler.objects.get(index).getId() == ObjectId.Player) {System.out.println("this is a player" + "  " + "where index is: " + index);}
        if(handler.objects.size() <= chunk*chunk) {System.out.println(String.format("Handler reached chunk*chunk size: ", chunk*chunk));}
        
    }

//    public void run() {
//        while(appIsRunning) {
//        
//        }
//    }
    /** Utfører utregning på hvor spilleren befinner seg på kartet,
     * og legger ser om spilleren har beveget seg en gitt distanse fra det punktet.
     * Hvis spilleren har beveget seg den gitte distansen, nevn ovenfor, vil landskap bli
     * stokket om.
     * @param handler listebehandler
     */
    public void checkForRearrange(Handler handler) {
        int py = (int) explorer.getY()/SCALED_SIZE;
        int px = (int) explorer.getX()/SCALED_SIZE;
        loadingPos = (int)Math.sqrt((double)((px-loadPosX)*(px-loadPosX)+(py-loadPosY)*(py-loadPosY)));
        if (loadingRange < loadingPos) {
            //worldLoader.removeTerrain(handler);
            //worldLoader.loadImageAgain(world,handler);
            //removeTerrain(handler);
            removeTerrainWhile(handler);
            loadLandscape(handler);
            loadPosX = (int) explorer.getX()/SCALED_SIZE;
            loadPosY = (int) explorer.getY()/SCALED_SIZE;
            euclideanCircle.rearrange();
            loadingPos = 0;
            //CompletableFuture.supplyAsync(() -> "Hello").thenApply(s -> s + " World!").thenAccept(System::out::println);
        }
//        if ( (px*px+py*py) < loadingpos - loadingrange || (int) (px*px+py*py) > loadingpos + loadingrange) {
//                    removeTerrain();
//                }
    } 
}
