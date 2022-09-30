import java.awt.*;
import javax.swing.*;

// This class is used to create the main Frog object, including methods that move, draw etc.
public class Frog{
    // all the sprites for the frog where it can move left, right, forward or backwards
    private final Image[] frogforw = new Image[6];
    private final Image[] frogback = new Image[6];
    private final Image[] frogleft = new Image[6];
    private final Image[] frogright = new Image[6];

    // this stores all the frog sprites at the index of the direction they are going in
    private final Image[][] allFrogs = {frogforw, frogback, frogleft, frogright};

    public static int lives = 5; // the frog starts off with five levels in every level when it's drawn
    private int dir;
    private int x, y;
    // these variables are used to change the sprites of the frog when it jumps with a delay so it's distinguishable
    private int frame, delay;

    private Rectangle frogRect; // this stores the area contained by the frog (Rectangle); used to check collisions with other objects
    public static final int XORIG = 475, YORIG = 650; // used to move the frog back to its original position once it reaches a frog home
    public static final int FORWARD = 0, BACK = 1, LEFT = 2, RIGHT = 3, WAIT = 3; // the directions the frog can move in

    public Frog(int x, int y){
        for(int i = 0; i < 6; i++){
            frogforw[i] = new ImageIcon("resources/frogforward/frogforw" + i + ".png").getImage();
            frogback[i] = new ImageIcon("resources/frogback/frogback" + i + ".png").getImage();
            frogleft[i] = new ImageIcon("resources/frogleft/frogleft" + i + ".png").getImage();
            frogright[i] = new ImageIcon("resources/frogright/frogright" + i + ".png").getImage();
        }
        dir = FORWARD; // the frog starts facing forward
        frame = 0; // starts from the idle sprite; just facing a dir
        delay = 0;
        this.x = x;
        this.y = y;
    }

    // this method is used to move the frog based on the key pressed; takes in the number of pixels to move the frog in the x and y direction
    // nothing is returned
    public void move(int dx, int dy){
        // the frog can only move if it is on the main game screen; cannot get off the playable screen
        if(x + dx >= 180 && x + dx <= 750 && y + dy >= 0 && y + dy <= 690) {
            x += dx; // moving the frog to the right or left
            y += dy; // moving the frog up or down
        }
        // changing the dir the frog faces based on its move to draw it facing that direction
        if (dx > 0) {
            dir = RIGHT;
        } else if (dx < 0) {
            dir = LEFT;
        } else if (dy > 0) {
            dir = BACK;
        } else if (dy < 0) {
            dir = FORWARD;
        }
    }

    // this method is used to draw all of the sprites for when the frog jumps; returns true when all the sprites have been reached and
    // the frame is back to 0 (idle)
    public boolean changeSprite(){
        delay++;
        if (delay % WAIT == 0) { // changing the sprite number/frame after a pause to make the jump look more realistic
            frame++;
            if(frame == frogforw.length) { // if all the sprites have been reached, make the frog idle
                frame = 0;
                return true; // all sprites have been reached and the jump is completed
            }
        }
        return false;
    }

    // this method is used to draw the frog based on the direction its facing in and the frame its on; 0 when idle
    public void draw(Graphics g) {
        g.drawImage(allFrogs[dir][frame], x, y, null); // drawing the frog at its x and y position
    }

    // this method is used to return the width of the frog
    public int getWidth(){
        return frogforw[0].getWidth(null);
    }

    // this method is used to return the height of the frog
    public int getHeight(){
        return frogforw[0].getHeight(null);
    }

    // this method is used to return the area contained by the frog (Rectangle); used to check collisions
    public Rectangle getRect(){
        // return a Rectangle with the frog's x-coor,y-coor, width and height
        int width = allFrogs[dir][frame].getWidth(null);
        int height = allFrogs[dir][frame].getHeight(null);
        frogRect = new Rectangle(x + 10, y - 10, width, height);
        return frogRect;
    }

    // this method is used to decrease the number of lives of the frog and moving the frog back to its original position
    public void loseLive(){
        lives--;
        x = XORIG; // when the frog loses a life, it goes back to its original position
        y = YORIG;
    }

    // this method is used to check if the frog is alive; if it has used up all of its lives and returns true if alive
    public boolean isAlive(){
        if(lives > 0){
            return true;
        }
        return false;
    }

    // this method is used to check if the frog reached any of the five frog homes; returns true if did reach a home
    public boolean ifReached(){
        for(int i = 0; i < GamePanel.endDest.length; i++) {
            // if the frog contains the frog home which is stored as a Point, it reached the home
            if (getRect().contains(GamePanel.endDest[i])) {
                GamePanel.endDestVals[i] = true; // making the index which represents each home Point as true if the frog is standing on it
                return true;
            }
        }
        return false;
    }

    // this method is used to draw the frog facing forward when it reaches its frog home
    public void drawImg(Graphics g, int x, int y){
        g.drawImage(frogforw[0], x, y, null);
    }

    // this method is used to draw the number of lives the frog has during the game
    public void drawLives(Graphics g){
        g.drawImage(frogforw[0], 210, 720, null);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Optima", Font.PLAIN, 30));
        g.drawString("×"+lives, 260, 745); // drawing the number of lives
    }
}