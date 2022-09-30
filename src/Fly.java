import javax.swing.*;
import java.awt.*;

// this class is used to create a fly that will provide 200 bonus points
public class Fly {
    public static final int PAUSE = 100; // this is used to decide how long the fly appears in one of the frog homes unit it changes
    private int x, y;
    private Image flyImg;
    private Rectangle flyRect; // the area contained by the fly; used to check for collisions
    private int countDraw = 0; // used to keep the fly on the screen for only a few seconds
    private int rand; // to make the frog appear in one of the five random homes
    private boolean isAlive = true; // this stores whether the fly has already been eaten or if a frog is in the home the fly is moving to

    public Fly(){
        rand = (int)(Math.random()*5); // used to draw the fly randomly in one of the five frog homes

        // endDest stores the Points of the five frog homes; where the fly will appear
        x = GamePanel.endDest[rand].x;
        y = GamePanel.endDest[rand].y;
        flyImg = new ImageIcon("resources/flyimg.png").getImage();
    }

    // this method draws the fly and randomly changes its spot in levels one and two if it is alive
    public void drawFly(Graphics g) {
        if(isAlive) { // fly must be alive to be drawn
            countDraw++;
            if (!(countDraw % PAUSE == 0)) { // the fly is drawn in the same frog home during the delay
                g.drawImage(flyImg, x - flyImg.getWidth(null) / 2, y - flyImg.getHeight(null) / 2, null);
            } else { // when the delay is reached, the fly's position changes
                while (true) {
                    int rand2 = (int) (Math.random() * 5);
                    if (rand != rand2) { // the fly can only move to a new frog home
                        rand = rand2;
                        if(GamePanel.endDestVals[rand]){ // if there is already a frog at the generated end destination, "remove" the fly
                            isAlive = false;
                        }
                        else {
                            // moving the frog to a new frog home
                            x = GamePanel.endDest[rand].x;
                            y = GamePanel.endDest[rand].y;
                        }
                        break;
                    }
                }
            }
        }
    }

    // this method generates and returns the Rectangle of the fly based on the x and y position and the width and height
    public Rectangle getRect(){
        int width = flyImg.getWidth(null);
        int height = flyImg.getHeight(null);
        flyRect = new Rectangle(x, y, width, height);
        return flyRect; // the fly's rectangle is used to check for collisions; i.e. if the frog eats the fly
    }

    // this method gets the Frog's Rectangle as a parameter and checks if it touches the fly; if they intersect, it returns true
    public boolean frogEatFly(Frog frog) {
        if (isAlive) { // can only eat the fly once
            boolean hit = false;
            if (frog.getRect().intersects(this.getRect())) { // if the frog touches the fly, it is eaten and is not drawn
                hit = true;
                isAlive = false;
            }
            return hit;
        }
        return false;
    }
}