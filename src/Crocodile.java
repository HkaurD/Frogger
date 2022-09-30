import javax.swing.*;
import java.awt.*;

// this class is used to create two Crocodile objects in level two
public class Crocodile {
    // one crocodile moves right while the other moves left
    private Image crocRight;
    private Image crocLeft;

    private int x, y;
    public static int LEFT, RIGHT;
    private int speed; // the speed the crocodile moves left or right
    private Rectangle crocRect; // stores the area stored by the crocodile; used to check a collision with the main game frog

    // constructor
    public Crocodile(int x, int y){
        crocRight = new ImageIcon("resources/crocodileRight.png").getImage();
        crocLeft = new ImageIcon("resources/crocodileLeft.png").getImage();
        this.x = x;
        this.y = y;
    }
    // this method is used to move the crocodile; it takes in the speed of the crocodiles
    public void move(int speed){
        x += speed; //  moving the crocodile
        if(speed > 0) { // if the crocodile is off the screen while moving right, move them back to the left side of the screen
            if (x > 780) {
                x = 0;
            }
        }
        if(speed < 0) { // if the crocodile is off the screen while moving left, move them back to the right side of the screen
            if (x < 0) {
                x = 780;
            }
        }
        this.speed = speed;
    }
    // this method is used to draw the crocodile based on whether it's moving left or right
    public void draw(Graphics g){
        if(speed > 0) { // drawing the crocodile moving right
            g.drawImage(crocRight, x, y, null);
        }
        else if(speed < 0){ // drawing the crocodile moving left
            g.drawImage(crocLeft, x, y, null);
        }
    }
    // this method is used to return the Rectangle contained by a crocodile to check if the Frog is standing on it
    public Rectangle getRect(){
        int width = crocRight.getWidth(null) - 15;
        int height = crocRight.getHeight(null) - 10;

        if(speed == LEFT) {
            crocRect = new Rectangle(x + 50, y, width - 40, height); // the Rectangle of the crocodile moving left
        }
        else{
            crocRect = new Rectangle(x - 10, y, width - 30, height); // the Rectangle of the crocodile moving right
        }
        return crocRect;
    }
    // this method is used to check if the main frog is standing on a crocodile and returns true if so
    // takes in the Rectangle of the frog as a parameter
    public boolean standOnCrocodile(Frog frog){
        boolean stand = false;
        if(frog.getRect().intersects(getRect())){ // if the frog is standing on the crocodile, return true
            stand = true;
        }
        return stand;
    }
}