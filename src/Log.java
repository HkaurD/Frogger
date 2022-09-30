import java.awt.*;
import javax.swing.*;

// This class is used to create a Log object; contains methods that draw, move and check if the frog is on the log
public class Log {
    private Image log;
    private int x, y;
    public static int speed, LEFT, RIGHT;
    private Rectangle logRect; // the area contained by the log (Rectangle) to check for collisions

    public Log(int x, int y){
        log = new ImageIcon("resources/log.png").getImage();
        this.x = x;
        this.y = y;
    }

    // this method draws the log
    public void draw(Graphics g){
        g.drawImage(log, x, y, null);
    }

    // this method moves the log based on the speed passed as a parameter
    public void move(int speed){
        x += speed;
        if(speed > 0) { // if the log moves to the right
            if (x > 780) { // if it gets off the screen, move it to the left side
                x = 0;
            }
        }
        else{
            if(x < 0){ // if the log is moving left and gets off the screen, move it to the right side
                x = 780;
            }
        }
        this.speed = speed;
    }

    // this method is used to create and return the log's Rectangle which is the area it contains
    public Rectangle getRect(){
        int width = log.getWidth(null) - 15;
        int height = log.getHeight(null) - 10;
        if(speed == LEFT) {
            logRect = new Rectangle(x + 30, y, width - 10, height); // a reduced Rectangle to make the collision look realistic
        }
        else{
            logRect = new Rectangle(x + 20, y, width - 20, height);
        }
        return logRect;
    }

    // this method is used to check if the frog is standing on a log and takes in the frog's rectangle as a parameter
    // it returns true if the frog is standing on the log
    public boolean standOnLog(Frog frog){
        boolean stand = false;
        if(frog.getRect().intersects(getRect())){ // if the frog is intersecting the log, it's standing on it
            stand = true;
        }
        return stand;
    }
}