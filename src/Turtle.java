import java.awt.*;
import javax.swing.*;

// This class is used to create a Turtle object; includes the drawing, moving end checking collisions with the frog
public class Turtle {
    private final Image[] turtles = new Image[5]; // the turtles sprites that swim
    private final Image[] turtlesWithSink = new Image[10]; // the turtles sprites including the turtle swimming underwater
    private int x, y, delay = 0, frame = 0; // the delay is used to create a pause between the different sprites and frame is the sprite being drawn
    public static final int WAIT = 10; // creates a pause between sprites
    public static int SPEED;
    private boolean sink; // every other pair of turtles sink; those will be true for sink
    // the area contained by the turtle (Rectangle) to check for collisions with the frog
    private Rectangle turtleRect;

    public Turtle(int x, int y, boolean sink){
        for(int i = 0; i < 5; i++) {
            // normal swimming turtle
            turtles[i] = new ImageIcon("resources/turtlesprites/turtle" + i + ".png").getImage();
        }
        for(int i = 0; i < 10; i++) {
            //turtle going underwater
            turtlesWithSink[i] = new ImageIcon("resources/turtlesprites/turtle" + i + ".png").getImage();
        }
        this.x = x;
        this.y = y;
        this.sink = sink;
    }

    // this method moves the turtle based on its speed
    public void move(){
        x += SPEED; // moving the turtles to the left
        delay += 1;
        if (delay % WAIT == 0) { // after the pause, the next sprite is drawn
            frame++;
            if(sink) { // if the turtles sink, they have to go through all ten sprites
                frame %= turtlesWithSink.length;
            }
            else{ // normal swimming turtles
                frame %= turtles.length;
            }
        }
        if (x <= 0) { // if the turtle is off the screen, move it to the right end of the screen
            x = 780;
        }
    }

    public void draw(Graphics g){
        if(sink) {
            g.drawImage(turtlesWithSink[frame], x, y, null); // drawing the turtle that sinks
        }
        else{
            g.drawImage(turtles[frame], x, y, null); // drawing the normal turtle
        }
    }

    // this method returns the Rectangle of the turtle and the area it contains; used for collisions with the frog
    public Rectangle getRect(){
        turtleRect = new Rectangle(x + 20, y - 10, 27, 18); // reduced Rectangle to make collisions more visible
        return turtleRect;
    }

    // this method is used to check if the frog is standing on a turtle and it returns true if so; takes in the frog's Rectangle
    public boolean standOnTurtle(Frog frog){
        boolean stand = false;
        if(frog.getRect().intersects(getRect())){ // if the frog is touching the turtle, it's standing on it
            if(!(sink && frame >= 5 && frame < 10)){ // the frog can only stand on the turtle if it is not underwater
                stand = true;
            }
        }
        return stand;
    }
}