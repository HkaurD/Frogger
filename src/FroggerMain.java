// Frogger - Recreation of the game
// By: Harbin Kaur Dhillon
// Teacher: Mr. McKenzie
/* This is the code for my version of Frogger. It involves the interaction of different classes, including several
objects that the main frog character must avoid or use, e.g. cars, logs, turtles etc., to reach the five frog homes on the
top section of the screen before all five lives have been used and the timer is done. The game consists of two levels that
differ by speed and the additional caution of a crocodile's mouth. All rules are explained in the "How to Play" section
in the intro and the score is accumulated during the duration of the game. The high score achieved by the player is
displayed on the main intro screen.
*/
import javax.swing.*;

public class FroggerMain extends JFrame {
    GamePanel game =  new GamePanel(); // creating the GamePanel which includes the entire game and interactions with other classes

    public FroggerMain(){
        super("Frogger"); // title of the window/game
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(game); // "adding" the game to the screen
        pack(); // sizing the frame based on the game's and the contents' proportions
        setVisible(true);
    }
    public static void main(String[] args){
        FroggerMain frame = new FroggerMain(); // running the game
    }
}