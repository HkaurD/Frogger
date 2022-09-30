import java.awt.*;

// This class is used to create a timer in level one and two; it draws the timer and decreases the time
public class GameTimer {
    public static int timeLimit = 100; // this is how long the player has to complete each level

    // these variables are used so the time decreases slowly similar to real life
    public static final int WAIT = 40;
    private int delay = 0;

    // this method is used to check if the time is up and returns true if so
    public boolean countDown(){
        if(timeLimit == 0){ // the time is up
            return true;
        }
        return false;
    }

    // this method is used to decrease the time as the level continues
    private void countDecrease(){
        delay++;
        if(timeLimit > 0) {
            if (delay % WAIT == 0) { // delays the timer
                timeLimit--; // decreasing the time
            }
        }
    }

    // this method is used to draw the timer in each level
    public void drawTimer(Graphics g){
        g.setColor(Color.BLACK);
        g.drawRect(320, 720, 100, 30); // the outline of the timer
        g.fillRect(320, 720, timeLimit, 30); // filling the outline based on how much time is remaining
        countDecrease(); // the time decreases while the timer is being drawn
    }
}