import java.awt.*;
import javax.swing.*;

// This class is used to draw a Vehicle object including methods that draw, move, and check a collision with the frog
public class Vehicle {
    private int xcoor;
    private int ycoor;
    private Image car;
    private Rectangle carRect; // the area stored by the vehicle's Rectangle; used for collisions with the frog
    public static int SPEED;
    public static final int RIGHT = 0, LEFT = 1;

    public Vehicle(String imgFile, int xcoor, int ycoor){
        car = new ImageIcon("resources/vehicles/" + imgFile).getImage();
        this.xcoor = xcoor;
        this.ycoor = ycoor;
    }
    public void draw(Graphics g){
        g.drawImage(car, xcoor, ycoor, null);
    }

    // this method is used to move the vehicle left or right across the screen based on the dir parameter
    public void move(int dir){
        if(dir == RIGHT) {
            xcoor += SPEED;
            if (xcoor > 780) { // if the vehicle going right gets off the screen, move it back to the left side
                xcoor = 0;
            }
        }
        else if(dir == LEFT){
            xcoor -= SPEED;
            if (xcoor < 0) { // if the vehicle going left gets off the screen, move it to the right side
                xcoor = 780;
            }
        }
    }
    // this method is used to return the Rectangle of the vehicle's position to check for a collision with the frog
    public Rectangle getRect(){
        int width = car.getWidth(null);
        int height = car.getHeight(null);
        carRect = new Rectangle(xcoor + 20, ycoor - 10, width - 40, height - 10);
        return carRect;
    }
    // this method is used to check is the frog hit the car; return true if so, else return false
    public boolean hitFrog(Frog frog){
        boolean hit = false;
        if(frog.getRect().intersects(this.getRect())){
            hit = true;
        }
        return hit;
    }
}