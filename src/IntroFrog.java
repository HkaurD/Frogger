import javax.swing.*;
import java.awt.*;

// This class is used to create the frog object that jumps across the intro screen
public class IntroFrog {
    private final Image[] frogSprites = new Image[8];
    public static final int WAIT = 5; // used to make a pause while changing the frog's jump sprites
    private int x = -100;
    private int frame, delay = 0;

    public IntroFrog(){
        for(int i = 0; i < 8; i++){
            frogSprites[i] = new ImageIcon("resources/introfrogsprites/introfrog" + i + ".png").getImage();
        }
    }
    // this method is used to move the intro frog and draw all the sprites
    public void move(){
        x += 5; // moving right
        delay += 1;
        if(delay % WAIT == 0){
            frame++;
            frame %= frogSprites.length;
        }
        if(x > 800){ // if the frog is off the screen, move it back to the left side
            x = -200;
        }
    }
    // this method is used to draw the intro frog using all the sprites
    public void draw(Graphics g){
        move();
        g.drawImage(frogSprites[frame], x, 350, null);
    }
}