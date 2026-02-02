/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package role;

import Role.Texture;
import role.Role.STATE;
import role.object.Oak;
import role.object.Player;
import role.object.Sea;
import role.framework.GameObject;
import role.framework.KeyInput;
import role.framework.ObjectId;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import role.object.EuclideanCircle;
import role.object.OpenForest;
import role.object.Plain;





/**
 * The main class, that initiates the whole app.
 * @author J
 */
public class Role extends Canvas implements Runnable{

    private static final long serialVersionUID = 2458693544678111048L;
    
    /** Spilleren */
    public static GameObject explorer;
    public static EuclideanCircle euclideanCircle;
    public static boolean debugRole;
    public static int frames;
    public static int FPS;

    public static boolean isDebugRole() {
        return debugRole;
    }

    public static void setDebugRole(boolean debugRole) {
        Role.debugRole = debugRole;
    }
    public static boolean appIsRunning = false;
    private Thread thread;
    //GameObject object;
    /** Skalert størrelse på bilder i ruter. */
    public final static int SCALED_SIZE = 96;
    public static int H;
    public static int W;
    private BufferedImage world = null;
    static Texture tex;
    Camera cam;
    /** Ser på posisjonen til spilleren via den euklidiske forskjellen. Hvis denne verdien er høyere enn loadingRange vil objektene bli lastet inn på nytt. */
    public static int loadingPos = 0;
    /** Avstanden i euklidisk forstand før innlastning av objekter gjøres på nytt. */
    public static int loadingRange = 5;
    /** Hvor langt unna objekter vil bli lastet inn fra spillerens posisjon. */
    public static int chunk = 12;
    /** Denne variabelen blir brukt til å se på spillerens x posisjon forrige gang WorldLoaderen ble brukt.*/
    static float loadPosX;
    /** Denne variabelen blir brukt til å se på spillerens y posisjon forrige gang WorldLoaderen ble brukt.*/
    static float loadPosY;
    
    public enum STATE {
        menu,
        game
    };
    
    public STATE gameState = STATE.menu;
    Handler handler;
    Handler debugHandler;
    WorldLoader worldLoader;
    WorldLoaderFromList worldLoaderFromList;
    
    private void init() {
        
        W = getWidth();
        H = getHeight();
        
        handler = new Handler();
        debugHandler = new Handler();
        
        tex = new Texture();
        
        BufferedImageLoader loader = new BufferedImageLoader();
        world = loader.loadImage("/world.png");
        
        cam = new Camera(0, 0);
        
        gameState = STATE.game;
        
        if(gameState == STATE.game) {
        
        // Spilleren blir plassert 15 plasser ned og til høyre.         
        explorer = new Player(SCALED_SIZE*15, SCALED_SIZE*15, ObjectId.Player, handler);
        euclideanCircle = new EuclideanCircle(0,0,ObjectId.Oak);
        
        
        
        // Ser ut til å kopiere "handleren" fra Role-objektet til KeyInput-objektet.
        this.addKeyListener(new KeyInput(handler));
        
        debugHandler.addObject(euclideanCircle);
        handler.addObject(explorer);
        // Posisjonen til spilleren når WorldLoaderen blir kjørt.
        loadPosX = (int) explorer.getX()/SCALED_SIZE;
        loadPosY = (int) explorer.getY()/SCALED_SIZE;
        worldLoaderFromList = new WorldLoaderFromList();
        worldLoaderFromList.init();
        //worldLoaderFromList.loadImageLevel(world);
        worldLoaderFromList.loadOnlyThousandFromImage(world);
        //worldLoaderFromList.loadFivehundredRandomLands(handler);
        //worldLoaderFromList.addItAll(handler);
        worldLoaderFromList.loadLandscape(handler);
        //worldLoader = new WorldLoader();
        //worldLoader.loadImageLevel(world, handler);
        //loadingpos = ((int)explorer.getX()*(int)explorer.getX() + (int)explorer.getY()*(int)explorer.getY())/96;
        
        
        }
    }
    
    public synchronized void start(){
        if(appIsRunning)
            return;
        
        appIsRunning = true;
        thread = new Thread(this);
        thread.start();
    }
    
    /** Objektene har en funksjon i sin klasse som bestemmer når de blir tegnet. */
    private void renderContent(){
            BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
            }
        
        Graphics g = bs.getDrawGraphics();
        Graphics2D g2d = (Graphics2D) g;
        
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        g2d.translate(cam.getX(), cam.getY());
        
        handler.render(g);
        if(debugRole) {
            debugHandler.render(g);
        }
        
        g2d.translate(-cam.getX(), -cam.getY());
        
        if(gameState == STATE.menu) {
            g.setColor(Color.red);
            g.drawString("menu", 500, 500);
        }
        
        g.dispose();
        bs.show();
    }
    
    @Override
    public void run(){
        init();
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        frames = 0;
        FPS = 0;
        while(appIsRunning){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1){
            tick();
            delta--;
            }
            if(appIsRunning)
                renderContent();
            frames++;
            
            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                FPS = frames;
                //System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
    }
    
        
        /**
         * Kjøres et gitt antall ganger(amountOfTicks i "run()") for hver gang en viss mengde tid har gått(1000ms)
         * Bruker den euklidiske forskjellen for å finne ut når objektene skal stokkes om.
         */
    private void tick(){
            handler.tick();
            // Hver gang listen treffer på spilleren, oppdateres kameraposisjonen, og hvor langt unna spilleren er fra forrige kartoppdatering.
            for(int i = 0; i < handler.objects.size(); i++) {
            if(handler.objects.get(i).getId() == ObjectId.Player) {
                cam.tick(handler.objects.get(i));
                //worldLoader.checkForRearrange(world,handler);
                worldLoaderFromList.checkForRearrange(handler);
                
                
            }
        }
    }
    /** Utfører utregning på hvor spilleren befinner seg på kartet,
     * og legger ser om spilleren har beveget seg en gitt distanse fra det punktet.
     * Hvis spilleren har beveget seg den gitte distansen, nevn ovenfor, vil landskap bli
     * stokket om.
     */
    public void checkForRearrange() {
        int py = (int) explorer.getY()/SCALED_SIZE;
        int px = (int) explorer.getX()/SCALED_SIZE;
        loadingPos = (int)Math.sqrt((double)((px-loadPosX)*(px-loadPosX)+(py-loadPosY)*(py-loadPosY)));
        if (loadingRange < loadingPos) {
            //worldLoader.removeTerrain(handler);
            //worldLoader.loadImageAgain(world,handler);
            worldLoaderFromList.removeTerrain(handler);
            worldLoaderFromList.loadLandscape(handler);
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
        
    public static Texture getInstance() {
        return tex;
    }
    
    /** Ikke brukt og kanskje ikke nødvendig. */
    public void runLoadLevelImage() {
        worldLoader.loadImageLevel(world, handler);
    }
    /** Finn ut hvilke fonts som er tilgjengelige. */
    public static void listAvailableFonts() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontNames = ge.getAvailableFontFamilyNames();

        System.out.println("Available Font Families:");
        for (String fontName : fontNames) {
            System.out.println(fontName);
        }
    }
    
    public static void main(String[] args) {           
        //listAvailableFonts();
        new Window(1228,720, "demo",new Role());
        
        List<List<String>> listOfLists = Arrays.asList(
            Arrays.asList("Reflection", "Collection", "Stream"),
            Arrays.asList("Structure", "State", "Flow"),
            Arrays.asList("Sorting", "Mapping", "Reduction", "Stream")
        );

        // Create a set to hold intermediate results
        Set<String> intermediateResults = new HashSet<>();

        // Stream pipeline demonstrating various intermediate operations
        List<String> result = listOfLists.stream()
            .flatMap(List::stream)              
            .filter(s -> s.startsWith("S"))      
            .map(String::toUpperCase)          
            .distinct()                          
            .sorted()                            
            .peek(s -> intermediateResults.add(s))
            .collect(Collectors.toList());      

        // Print the intermediate results
        System.out.println("Intermediate Results:");
        intermediateResults.forEach(System.out::println);

        // Print the final result
        System.out.println("Final Result:");
        result.forEach(System.out::println);
    }
    
}
