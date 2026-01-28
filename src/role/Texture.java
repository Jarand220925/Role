

package Role;

import java.awt.image.BufferedImage;
import role.BufferedImageLoader;


public class Texture {
    
    Spritesheet ws;
    private BufferedImage landscape_sheet = null;
    //private BufferedImage player_sheet = null;
    
    public BufferedImage[] block = new BufferedImage[5];
    //public BufferedImage[] player = new BufferedImage[2];
    
    public Texture() {
        
        BufferedImageLoader loader = new BufferedImageLoader();
        try{
            landscape_sheet = loader.loadImage("/landscape_sheet.png");
            //player_sheet = loader.loadImage("/playersheet.png");
        } catch (Exception e){
            e.printStackTrace();
        }

        ws = new Spritesheet(landscape_sheet);
        //ps = new Spritesheet(player_sheet);
        
        getTextures();
        
    }
    
    private void getTextures() {
        block[0] = ws.grabImage(1, 1, 32, 32);//oak forest
        block[1] = ws.grabImage(2, 1, 32, 32);//sea
        block[2] = ws.grabImage(3, 1, 32, 32);//plain
        block[3] = ws.grabImage(4, 1, 32, 32);//openforest
        block[4] = ws.grabImage(5, 1, 32, 32);//nothing
        
        //player[0] = ps.grabImage(1, 1, 32, 64);//player
        //player[1] = ps.grabImage(2, 1, 32, 64);
     }
    
}
