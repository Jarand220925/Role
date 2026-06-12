/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package role;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import static role.Role.SCALED_SIZE;
import static role.Role.appIsRunning;
import static role.Role.chunkLoadRange;
import static role.Role.euclideanCircle;
import static role.Role.explorer;
import static role.Role.loadPosX;
import static role.Role.loadPosY;
import static role.Role.loadingPos;
import static role.Role.loadingRange;
import role.framework.Chunk;
import role.framework.GameObject;
import role.framework.ObjectId;
import role.object.Oak;
import role.object.OpenForest;
import role.object.Plain;
import role.object.Sea;
import static role.Role.tileLoadRange;

/**
 * Denne versjonen har en egen objektbehandler(Handler) som bare tar inn landskap.
 * @author Jarand
 */
public class WorldLoaderFromList {
    Handler worldHandler;
    GameObject[][] landscapeArray;
    BufferedImage worldImage;
    Chunk[][] chunkArray;
    int worldChunkSize;
    
    private int h;
    private int w;
    private int prevLowX;
    private int prevLowY;
    private int prevHighX;
    private int prevHighY;
    private Chunk prevChunk;
    public boolean firstUse = true;
    /** Bare hvor mange land i begge akser. */
    private int setAmountOfLands;
    
    public WorldLoaderFromList(BufferedImage image) {
        this.h = image.getHeight();
        this.w = image.getWidth();
    }
    //List<GameObject> wH;
    /**
     * Initialiserer objektbehandleren knyttet til dette objektet.
     */
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
        
        landscapeArray = new GameObject[w][h];
        
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
                
                if(red == 63 && green == 72 & blue == 204) landscapeArray[xx][yy] = new Sea(xx*SCALED_SIZE, yy*SCALED_SIZE, 1, ObjectId.Sea);
                if(red == 34 && green == 177 & blue == 76) landscapeArray[xx][yy] = new Oak(xx*SCALED_SIZE, yy*SCALED_SIZE, 0, ObjectId.Oak);
                if(red == 255 && green == 242 & blue == 0) {landscapeArray[xx][yy] = new Plain(xx*SCALED_SIZE, yy*SCALED_SIZE, 2, ObjectId.Plain);}
                if(red == 181 && green == 230 & blue == 29) {landscapeArray[xx][yy] = new OpenForest(xx*SCALED_SIZE, yy*SCALED_SIZE, 3, ObjectId.OpenForest);}
                //else if(red == 255 && green == 255 & blue == 255) {landscapeArray[xx][yy] = null;}
                //else if(landscapeArray[xx][yy] == null) {landscapeArray[xx][yy] = null;}
                
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
    
    public void loadWorldFromImage(BufferedImage image, int chunkSize){
        int w = image.getWidth();
        int h = image.getHeight();
        
        int wm = w % chunkSize;
        int hm = h % chunkSize;
        if(wm != 0 || hm != 0){
            System.err.print("Invalid chunk size");
            System.exit(1);
        }
        
        int amountOfChunksX = w/chunkSize;
        int amountOfChunksY = h/chunkSize;
        
        worldChunkSize = chunkSize;
        chunkArray = new Chunk[amountOfChunksX][amountOfChunksY];
        
        System.out.println("Loading chunks");
        
        int chunksMade = 0;
        for(int xx = 0; xx < amountOfChunksX; xx++) {
            for(int yy = 0; yy < amountOfChunksY; yy++) {
                
                if(xx < 0 || yy < 0 || xx > amountOfChunksX || yy > amountOfChunksY) {
                    //System.out.println("skipped tile: " + xx + " " + yy);
                } else {
                //System.out.println("tile xx, yy: " + xx + " " + yy);
                chunkArray[xx][yy] = new Chunk(worldChunkSize,xx,yy);
                chunkArray[xx][yy].createChunk(image, w, h);
                
                //if(px >= GameObject - range && px <=  + add && py >= newy - add && py <= newy + add )
                //System.out.println("xx: " + xx);
                chunksMade++;
                }
            }
        }
        //System.out.println("objects-list size: " + handler.objects.size());
        System.out.println("chunks made: " + chunksMade);
    }
    
    public void loadLandscapeChunks(Handler handler){
        
        
        
        // Spillerens x og y posisjon.
        int scaledPX = (int) explorer.getX();
        int scaledPY = (int) explorer.getY();
        int px = scaledPX/SCALED_SIZE;
        int py = scaledPY/SCALED_SIZE;
        
        //Chunken spilleren er inne i.
        int currChunkX = (int) Math.floor(px/worldChunkSize);
        int currChunkY = (int) Math.floor(py/worldChunkSize);
        
        //Chunk currChunk = chunkArray[][];
        
        //De to chunkene som danner en firkant og begynnes med når innlastning starter.
        int currHighCX = currChunkX + chunkLoadRange; 
        int currHighCY = currChunkY + chunkLoadRange;
        int currLowCX = currChunkX - chunkLoadRange;
        int currLowCY = currChunkY - chunkLoadRange;
        
        for(int xx = currLowCX; xx <= currHighCX; xx++) {
            
            if(xx < 0 || xx >= chunkArray.length) {continue;}
            
            for(int yy = currLowCY; yy <= currHighCY; yy++) {
                
                if(yy < 0 || yy >= chunkArray[xx].length) {continue;}
                
                Chunk insertChunk;
                   
                //Hvis denne x og y posisjonen er innenfor felles innlastnings-område som den forrige.
//                if((xx >= biggestLowX && xx <= smallestHighX && yy >= biggestLowY && yy <= smallestHighY) && !firstUse) {
//                    continue;
//                }
                    
                insertChunk = chunkArray[xx][yy];
                insertChunk.insertToHandler(handler);
                // Hvis objektet eksisterer.
//                if(insertObj != null) {
//                    if(insertObj.getX() <= scaledPX + scaledChunk && insertObj.getY() <= scaledPY + scaledChunk){
//
//                            handler.addObject(insertObj);
//                    }
//                }
            }
        }
        
        //Lagre chunken spilleren var i når innlastning av landskap ble gjort
        //til bruk for neste gang innlastning skjer.
        
        
    }
    /**
     * Laster inn landskap fra landskapslisten.
     * Sjekker om landskap i landskapslisten til WorldLoaderen allerede er
     * innlastet i objektbehandleren som tar i mot landskap.
     * @param handler 
     */
    public void loadLandscape(Handler handler) {
        // Spillerens posisjon ved forrige innlastning.
        int scaledLoadPosX = (int) loadPosX * SCALED_SIZE;
        int scaledLoadPosY = (int) loadPosY * SCALED_SIZE;
        // Spillerens x og y posisjon.
        int scaledPX = (int) explorer.getX();
        int scaledPY = (int) explorer.getY();
        int px = scaledPX/SCALED_SIZE;
        int py = scaledPY/SCALED_SIZE;
        
        int scaledChunk = tileLoadRange * SCALED_SIZE;
        
        // Unøyaktig, trodde jeg?
        int scaledCurrHighX = scaledPX + scaledChunk;
        int scaledCurrHighY = scaledPY + scaledChunk;
        int scaledCurrLowX = scaledPX - scaledChunk;
        int scaledCurrLowY = scaledPY - scaledChunk;
        int currHighX = px + tileLoadRange; 
        int currHighY = py + tileLoadRange;
        int currLowX = px - tileLoadRange;
        int currLowY = py - tileLoadRange;
        
        int scaledSmallestHighX = Math.min(prevHighX, scaledCurrHighX);
        int scaledBiggestLowX = Math.max(prevLowX, scaledCurrLowX);
        int scaledSmallestHighY = Math.min(prevHighY, scaledCurrHighY);
        int scaledBiggestLowY = Math.max(prevLowY, scaledCurrLowY);
        
        int smallestHighX = Math.min(prevHighX/SCALED_SIZE, currHighX);
        int biggestLowX = Math.max(prevLowX/SCALED_SIZE, currLowX);
        int smallestHighY = Math.min(prevHighY/SCALED_SIZE, currHighY);
        int biggestLowY = Math.max(prevLowY/SCALED_SIZE, currLowY);
        
        //Hvis boksene med koordinater ikke har noen felles ruter.
        if(scaledBiggestLowX > scaledSmallestHighX || scaledBiggestLowY > scaledSmallestHighY) {firstUse = true;} else {firstUse = false;}
        
        for(int xx = currLowX; xx <= currHighX; xx++) {
            
            if(xx < 0 || xx >= landscapeArray.length) {continue;}
            
            for(int yy = currLowY; yy <= currHighY; yy++) {
                
                if(yy < 0 || yy >= landscapeArray[xx].length) {continue;}
                
                GameObject insertObj;
                   
                //Hvis denne x og y posisjonen er innenfor felles innlastnings-område som den forrige.
                if((xx >= biggestLowX && xx <= smallestHighX && yy >= biggestLowY && yy <= smallestHighY) && !firstUse) {
                    continue;
                }
                    
                insertObj = landscapeArray[xx][yy];
                // Hvis objektet eksisterer.
                if(insertObj != null) {
                    if(insertObj.getX() <= scaledPX + scaledChunk && insertObj.getY() <= scaledPY + scaledChunk){

                            handler.addObject(insertObj);
                    }
                }
            }
        }
        prevHighX = scaledPX + scaledChunk;
        prevHighY = scaledPY + scaledChunk;
        prevLowX = scaledPX - scaledChunk;
        prevLowY = scaledPY - scaledChunk;
        firstUse = false;
    }
    /**
     * Laster inn en gitt mengde med land fra tilfeldige koordinater.
     * @param handler 
     * @param amount 
     */
    public void loadFivehundredRandomLands(Handler handler, int amount) {
        
        int arraySizeX = landscapeArray.length;
        
        for(int i = 0; i < 500; i++) {
            Random rnd = new Random();
            int rangeX = rnd.nextInt(arraySizeX - 1);
            int rangeY = rnd.nextInt(landscapeArray[rangeX].length - 1);
            GameObject randomObject = landscapeArray[rangeX][rangeY];
            handler.addObject(randomObject);
        }
    }
    
    /** Lagt til for å teste uten å vente på at alle landene må bli lastet inn.
     * @param image bilde som skal skannes. */
    public void loadLimitedAmountFromImage(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        
        setAmountOfLands = 50;
        
        landscapeArray = new GameObject[setAmountOfLands][setAmountOfLands];
        
        int py = (int)explorer.getY()/SCALED_SIZE;
        int px = (int)explorer.getX()/SCALED_SIZE;

        System.out.println("Loading land");
        System.out.println("Image width and height: " + w + " " + h);
        System.out.println("Player x and y: " + px + " " + py);
        
        //for(int yy = 0; yy < w; yy++) Brukes for å legge inn ruter basert på størrelsen av bilde.
        int landsmade = 0;
        for(int xx = 0; xx < setAmountOfLands; xx++) {
            for(int yy = 0; yy < setAmountOfLands; yy++) {
                
                if(xx < 0 || yy < 0 || xx > h || yy > w) {
                    System.out.println("skipped: " + xx + " " + yy);
                } else {
                System.out.println("xx yy: " + xx + " " + yy);
                int pixel = image.getRGB(xx, yy);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;
                
                if(red == 63 && green == 72 & blue == 204) landscapeArray[xx][yy] = new Sea(xx*SCALED_SIZE, yy*SCALED_SIZE, 1, ObjectId.Sea);
                if(red == 34 && green == 177 & blue == 76) landscapeArray[xx][yy] = new Oak(xx*SCALED_SIZE, yy*SCALED_SIZE, 0, ObjectId.Oak);
                if(red == 255 && green == 242 & blue == 0) landscapeArray[xx][yy] = new Plain(xx*SCALED_SIZE, yy*SCALED_SIZE, 2, ObjectId.Plain);
                if(red == 181 && green == 230 & blue == 29) {landscapeArray[xx][yy] = new OpenForest(xx*SCALED_SIZE, yy*SCALED_SIZE, 3, ObjectId.OpenForest);}
                else if(red == 255 && green == 255 & blue == 255) {landscapeArray[xx][yy] = null;}
                
                landsmade++;
                }
            }
        }
        //System.out.println("objects-list size: " + handler.objects.size());
        System.out.println("tiles looped: " + landsmade);
        //System.out.println("px: " +  px );
    }
    
    /** Genererer et begrenset antall land i landskapstabellen med en gang og fullfører generering av resten i bakgrunnen.
     * Lagt til for å teste uten å vente på at alle landene må bli lastet inn.
     * @param image bilde som skal skannes. */
    public void loadLimitedAmountFromImageFuture(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        
        setAmountOfLands = 50;
        
        landscapeArray = new GameObject[w][h];
        
        int py = (int)explorer.getY()/SCALED_SIZE;
        int px = (int)explorer.getX()/SCALED_SIZE;

        System.out.println("Loading land");
        System.out.println("Image width and height: " + w + " " + h);
        System.out.println("Player x and y: " + px + " " + py);
        
        //for(int yy = 0; yy < w; yy++) Brukes for å legge inn ruter basert på størrelsen av bilde.
        int landsLoaded = 0;
        for(int xx = 0; xx < setAmountOfLands; xx++) {
            for(int yy = 0; yy < setAmountOfLands; yy++) {
                
                if(xx < 0 || yy < 0 || xx > h || yy > w) {
                    System.out.println("skipped: " + xx + " " + yy);
                } else {
                System.out.println("xx yy: " + xx + " " + yy);
                int pixel = image.getRGB(xx, yy);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;
                
                if(red == 63 && green == 72 & blue == 204) landscapeArray[xx][yy] = new Sea(xx*SCALED_SIZE, yy*SCALED_SIZE, 1, ObjectId.Sea);
                if(red == 34 && green == 177 & blue == 76) landscapeArray[xx][yy] = new Oak(xx*SCALED_SIZE, yy*SCALED_SIZE, 0, ObjectId.Oak);
                if(red == 255 && green == 242 & blue == 0) landscapeArray[xx][yy] = new Plain(xx*SCALED_SIZE, yy*SCALED_SIZE, 2, ObjectId.Plain);
                if(red == 181 && green == 230 & blue == 29) {landscapeArray[xx][yy] = new OpenForest(xx*SCALED_SIZE, yy*SCALED_SIZE, 3, ObjectId.OpenForest);}
                else if(red == 255 && green == 255 & blue == 255) {landscapeArray[xx][yy] = null;}
                
                landsLoaded++;
                }
            }
        }
        //System.out.println("objects-list size: " + handler.objects.size());
        System.out.println("tiles looped: " + landsLoaded);
        //System.out.println("px: " +  px );
        CompletableFuture backgroundLoading = backgroundLoad(image,setAmountOfLands);
    }
    
    /**
     * Skal generere landskap og legge det i landskapstabellen. Kjøres i bakgrunnen.
     * @param image bilde som skal skannes.
     * @param begin Dette er hvor innlasting av landskap stoppet, og hvor innlasting av flere landskap skal starte fra.
     * @return 
     */
    private CompletableFuture<Void> backgroundLoad(BufferedImage image,int begin){
        CompletableFuture<Void> future = new CompletableFuture();
        
        Executors.newCachedThreadPool().submit(() -> {
            int landsLoaded = 0;
            boolean reachedCrossingPoint = false;
            try {
                for (int xx = 0; xx < h; xx++) {
                    boolean newIterationXx = true;
                    for (int yy = begin; yy < w; yy++) {
                        
                        if (xx == begin && yy == begin && reachedCrossingPoint == false){
                            reachedCrossingPoint = true;
                        }
                        if (reachedCrossingPoint == true && newIterationXx == true){
                            newIterationXx = false;
                            yy = yy - begin;
                        }
                        
                        if (xx < 0 || yy < 0 || xx > h || yy > w) {
                            System.out.println("skipped: " + xx + " " + yy);
                        } else {
                            System.out.println("xx yy: " + xx + " " + yy);
                            int pixel = image.getRGB(xx, yy);
                            int red = (pixel >> 16) & 0xff;
                            int green = (pixel >> 8) & 0xff;
                            int blue = (pixel) & 0xff;
                            
                            if (red == 63 && green == 72 & blue == 204) {
                                landscapeArray[xx][yy] = new Sea(xx * SCALED_SIZE, yy * SCALED_SIZE, 1, ObjectId.Sea);
                            }
                            if (red == 34 && green == 177 & blue == 76) {
                                landscapeArray[xx][yy] = new Oak(xx * SCALED_SIZE, yy * SCALED_SIZE, 0, ObjectId.Oak);
                            }
                            if (red == 255 && green == 242 & blue == 0) {
                                landscapeArray[xx][yy] = new Plain(xx * SCALED_SIZE, yy * SCALED_SIZE, 2, ObjectId.Plain);
                            }
                            if (red == 181 && green == 230 & blue == 29) {
                                landscapeArray[xx][yy] = new OpenForest(xx * SCALED_SIZE, yy * SCALED_SIZE, 3, ObjectId.OpenForest);
                            } else if (red == 255 && green == 255 & blue == 255) {
                                landscapeArray[xx][yy] = null;
                            }
                            
                            landsLoaded++;
                        }
                    }
                }
            } catch (Throwable t) {
        // Log the exception securely
        System.err.println("Error in task: " + t.getMessage());
                System.out.println(t);
                System.err.println(t);
                //System.exit(1);
            }

        System.out.println(String.format("Hello"));
        future.complete(null);
        
        });
        
        return future;
        
    }
    /**
     * Tar alle land fra WorldLoaderen sin liste og legger det inn
     * i spillets objektbehandler.
     * Ikke anbefalt metode for å laste inn landskap i objektbehandler.
     * @param handler 
     */
    public void addItAll(Handler handler) {
        
        int countIteration = 0;
        // Dimensjoner?
        int arraySize = landscapeArray.length;
        for(int ix = 0; ix < w; ix++){
            for(int iy = 0; iy < h; iy++) {
                
                countIteration++;
                
                if (ix + 1 > arraySize) {
                    return;
                }
                if(iy + 1 > landscapeArray[ix].length) {
                    continue;
                }
                handler.addObject(landscapeArray[ix][iy]);
            }
        }
    }
    /** Ikke brukt
     * Skal markere objekter for fjerning fra objektbehandler i spill og
     * sletter dem også.
     * @param handler */
    public void removeTerrain(Handler handler) {
        
        //float tileLoadRange = this.tileLoadRange;
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
            
            while(handler.objects.get(i).getId() != ObjectId.Player || handler.objects.size() < tileLoadRange*tileLoadRange || handlerSize >= objsIterated) {
                objsIterated++;
                if(ox < px - tileLoadRange || oy < py - tileLoadRange || ox > px + tileLoadRange || oy > py + tileLoadRange) {
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
            if(handler.objects.size() <= tileLoadRange*tileLoadRange) {System.out.println(String.format("Handler reached tileLoadRange*tileLoadRange size: ", tileLoadRange*tileLoadRange));}
            //??
            countedObjs = handler.objects.size()-i;
            System.out.println(String.format("hello %d", tileLoadRange*tileLoadRange));
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
    /** Fjerner landskapsobjekter fra listen som blir gitt. Dette gjøres i stigende rekkefølge.
     * @param handler listebehandler*/
    public void removeTerrainWhile(Handler handler) {
        
        int py = (int) explorer.getY()/SCALED_SIZE;
        int px = (int) explorer.getX()/SCALED_SIZE;
        
        System.out.println("removing terrain");
        int index = 0;
        int objsIterated = 0;
        int handlerSize = handler.objects.size();
        
        
        while(objsIterated < handlerSize) {
            objsIterated++;
            if(handler.objects.get(index).getId() == ObjectId.Player) {
                index++;
                //System.out.println("this is a player" + "  " + "where index is: " + index);
                continue;
            }
            
            float ox = handler.objects.get(index).getX()/SCALED_SIZE;
            float oy = handler.objects.get(index).getY()/SCALED_SIZE;
            
            if(ox < px - tileLoadRange || oy < py - tileLoadRange || ox > px + tileLoadRange || oy > py + tileLoadRange) {
                GameObject objectForRemoval = handler.objects.get(index);
//                    System.out.println("index: " + handler.objects.indexOf(objectForRemoval));
//                objectForRemoval.setDelete(true);
                handler.tempRemoveObject(objectForRemoval);
                    
//                    System.out.println("Marked and removed: " + objectForRemoval.getId() + " Index size is now: " + handler.objects.size());
//                    System.out.println("Removed object with coordinates: " + ox + " x " + oy + " y ");
                    
//                    System.out.println("marked?: " + objectForRemoval.getDelete());
            } else if (handler.objects.size() <= 1) {
                System.out.println("Ended removal");
                return;
            } else {
                //System.out.println("within player zone, or objectlist is bigger than set size.");
                index++;
            }
        } 
    }

//    public void run() {
//        while(appIsRunning) {
//        
//        }
//    }
    /** Utfører utregning på hvor spilleren befinner seg på kartet,
     * og legger ser om spilleren har beveget seg en gitt distanse fra det punktet.
     * Hvis spilleren har beveget seg den gitte distansen, nevnt ovenfor, vil landskap bli
     * stokket om.
     * @param handler listebehandler
     */
    public void checkForRearrange(Handler handler) {
        int py = (int) explorer.getY()/SCALED_SIZE;
        int px = (int) explorer.getX()/SCALED_SIZE;
        loadingPos = (int)Math.sqrt((double)((px-loadPosX)*(px-loadPosX)+(py-loadPosY)*(py-loadPosY)));
        if (loadingRange < loadingPos) {
            removeTerrainWhile(handler);
            loadLandscape(handler);
            loadPosX = (int) explorer.getX()/SCALED_SIZE;
            loadPosY = (int) explorer.getY()/SCALED_SIZE;
            euclideanCircle.rearrange();
            loadingPos = 0;
        }
    }

/** Ikke implementert
 Skal sjekke når spilleren går over til en annen chunk.
 * @param handler 
 */    
    public void checkForRearrangeChunks(Handler handler) {
        int py = (int) explorer.getY()/SCALED_SIZE;
        int px = (int) explorer.getX()/SCALED_SIZE;
        loadingPos = (int)Math.sqrt((double)((px-loadPosX)*(px-loadPosX)+(py-loadPosY)*(py-loadPosY)));
        if (loadingRange < loadingPos) {
            removeTerrainWhile(handler);
            loadLandscape(handler);
            loadPosX = (int) explorer.getX()/SCALED_SIZE;
            loadPosY = (int) explorer.getY()/SCALED_SIZE;
            euclideanCircle.rearrange();
            loadingPos = 0;
        }
    }
}
