import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// This class contains all the game's components including the graphics, movements and interactions with other classes.
// It also checks for any actions, keys pressed and mouse actions.
public class GamePanel extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener{
    // the different stages of the game represented as constants
    public static final int INTRO = 0, HOWTOPLAY = 1, HOWTOPLAY2 = 2, HOWTOPLAY3 = 3, BUFFER = 4, BUFFER2 = 5, LEVEL1 = 6, LEVEL2 = 7, LOSE = 8, WIN = 9;
    public static final int WAIT = 30; // used for the buffer screen showing the countdown until the game starts
    public static Point[] endDest = new Point[5]; // stores all the Points of the five end destinations
    // declares each Point of the same index in endDest as true if the frog has reached that destination
    public static boolean[] endDestVals = new boolean[5];

    private int stage = INTRO; // the game starts from the intro
    private int lastKey = -1; // the player can only move once when they press on a key; no spamming and no key pressed means -1
    private int delay = 0; // used to slow down the count down in the buffer screen
    private int count = 5; // used for the count down in the buffer screen between levels
    private int turtlePairCnt = 0; // used to sink pairs of turtles swimming together
    private int score;

    // whether the frog can jump based on if the player has already pressed a key and the sprites have finished
    private boolean canJump = true;

    private final Point mouse = new Point(); // stores the x and y-coordinates of the mouse; used for hovering buttons

    // the areas contained by each button used to check if the mouse is "on" the button or if a button is pressed
    private final Rectangle introPlay, introHowToPlay, introHowToPlayNext, introBack;

    private final int[] carsx = {0, 190, 600, 700, 700, 92,
                          218, 518, 200, 500, 400, 307,
                          600, 30, 390, 300, 0, 600};
    private final int[] logsx = {0, 0, 200, 200, 400, 400, 600, 600};
    private final int[] turtlesx = {0, 50, 100, 250, 300, 350, 500, 550, 600, 0, 50, 200, 250, 400, 450, 600, 650};

    // all the images for the backgrounds
    Image introbg; // the main intro's background

    // the three how to play backgrounds
    Image instructionsbg;
    Image instructionsbg2;
    Image instructionsbg3;

    Image settingsbg;
    Image levelOneBack;
    // the "side" portions for the level one background; they "cover" the cars as they move across the screen
    Image backleft;
    Image backright;
    Image levelOneBufferBg;
    Image levelTwoBufferBg;
    Image losebg;
    Image winbg;

    Font myFont;

    // objects of classes that are used in the game
    Timer timer;
    IntroFrog introFrog;
    Frog gameFrog;
    Vehicle[] cars = new Vehicle[24];
    Log[] logs = new Log[8];
    Crocodile[] crocs = new Crocodile[2];
    Turtle[] turtles = new Turtle[17];
    Fly fly;
    GameTimer gameTimer;

    // all sound effects
    SoundEffect jumpSound;
    SoundEffect jumpInWaterSound;
    SoundEffect hitCarSound;
    SoundEffect reachDestSound;
    SoundEffect loseSound;
    SoundEffect winGameSound;

    public GamePanel(){
        score = 0;

        // constructors for all the classes that are used in the game
        introFrog = new IntroFrog(); // frog jumping in the intro
        gameFrog = new Frog(Frog.XORIG, Frog.YORIG); // the main frog; character used by player
        gameTimer = new GameTimer(); // the timer during the levels

        jumpSound = new SoundEffect("resources/froggersounds/sound-frogger-hop.wav");
        jumpInWaterSound = new SoundEffect("resources/froggersounds/sound-frogger-plunk.wav");
        hitCarSound = new SoundEffect("resources/froggersounds/sound-frogger-squash.wav");
        reachDestSound = new SoundEffect("resources/froggersounds/sound-frogger-coin-in.wav");
        winGameSound = new SoundEffect("resources/froggersounds/sound-frogger-win.wav");
        loseSound = new SoundEffect("resources/froggersounds/sound-frogger-lose.wav");

        // creating the Points of the five frog homes; end destinations
        for(int i = 0; i < 5; i++){
            endDest[i] = new Point();
            endDest[i].x = 280 + i * 100;
            endDest[i].y = 40;
        }
        // creating car objects on different lanes with different starting positions
        for(int i = 0; i < 6; i++){
            cars[i] = new Vehicle("car" + i + ".png", carsx[i], 335 + i * 45);
            cars[i + 6] = new Vehicle("car" + (i + 6) + ".png", carsx[i + 6], 335 + i * 45);
            cars[i + 12] = new Vehicle("car" + i + ".png", carsx[i + 12], 335 + i * 45);
        }
        // the top and bottom logs on two different "lanes"
        for(int i = 0; i < 2; i++){
            logs[i] = new Log(logsx[i], 175 - i * 40);
            logs[i + 2] = new Log(logsx[i + 2], 175 - i * 40);
            logs[i + 4] = new Log(logsx[i + 4], 175 - i * 40);
            logs[i + 6] = new Log(logsx[i + 6], 175 - i * 40);
        }
        // The crocodiles will take the place of the logs with x-coordinates of index 1 and 2 in level two
        crocs[0] = new Crocodile(logs[0].getRect().x, 175);
        crocs[1] = new Crocodile(logs[1].getRect().x, 135);

        fly = new Fly(); // creating a fly object; this will randomly appear in of the five frog homes

        // creating the turtles that swim in groups of three
        for(int i = 0; i < 9; i++){
            turtles[i] = new Turtle(turtlesx[i], 220, false);
        }
        // creating the turtles that swim in groups of two
        for(int i = 9; i < 17; i+=2){
            turtlePairCnt++;
            boolean sink = turtlePairCnt % 2 == 0; // every other turtle pair will sink
            turtles[i] = new Turtle(turtlesx[i], 70, sink);
            turtles[i + 1] = new Turtle(turtlesx[i + 1], 70, sink);
        }
        // loading all the images for the game
        introbg = new ImageIcon("resources/introbg.png").getImage();
        instructionsbg = new ImageIcon("resources/instructionsbg.png").getImage();
        instructionsbg2 = new ImageIcon("resources/instructionsbg2.png").getImage();
        instructionsbg3 = new ImageIcon("resources/instructionsbg3.png").getImage();
        settingsbg = new ImageIcon("resources/settingsbg.png").getImage();
        levelOneBack = new ImageIcon("resources/leveloneback.png").getImage();
        backleft = new ImageIcon("resources/lvl1backleft.png").getImage();
        backright = new ImageIcon("resources/lvl1backright.png").getImage();
        levelOneBufferBg = new ImageIcon("resources/levelOneBufferBg.png").getImage();
        levelTwoBufferBg = new ImageIcon("resources/levelTwoBufferBg.png").getImage();
        losebg = new ImageIcon("resources/gameoverbg.png").getImage();
        winbg = new ImageIcon("resources/winbg.png").getImage();

        setPreferredSize(new Dimension(950, 780)); // setting the size of the screen

        timer = new Timer(20, this);
        timer.start();

        // the area contained by each button used in the game
        introPlay = new Rectangle(295, 555, 105, 50);
        introHowToPlay = new Rectangle(455, 555, 235, 50);
        introHowToPlayNext = new Rectangle(810, 30, 105, 40);
        introBack = new Rectangle(10, 10, 105, 40);

        myFont = new Font("Optima", Font.PLAIN, 30); // changing the font for the buttons' text

        setFocusable(true);
        requestFocus();
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        saveScore(0); // the default high score is always 0 in the score.txt file
    }
    // this method draws the high score on the intro screen
    public void drawHighScore(Graphics g){
        int highsc = loadScore(); // reading the score from the score.txt file
        g.setFont(myFont);
        g.setColor(Color.WHITE);
        g.drawString("" + highsc, 500, 695); // drawing the high score
    }

    // I had help from Mr. McKenzie for the loadScore() and saveScore() methods
    // this method is used to read from the score.txt file; stores and returns the high score and checks if the new score is higher
    public int loadScore(){
        int sc=0;
        try{
            Scanner inFile = new Scanner(new File("score.txt")); // reading from the file
            sc = inFile.nextInt();
        }
        catch(IOException ex){
            System.out.println(ex);
        }
        return sc; // returns the high score
    }

    // this method is used to save a new score as the high score and it does return anything; takes in an integer score
    public void saveScore(int score){
        try{
            PrintWriter outFile = new PrintWriter(new File("score.txt"));
            outFile.println(score); // prints the score to the score.txt file
            outFile.close(); // closing the file so that the score is added to the file
        }
        catch(IOException ex){
            System.out.println(ex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(stage == LEVEL1 || stage == LEVEL2) {
            move(); // all the moving of the cars, logs, turtles and crocodiles are done if the game has started
        }
        repaint(); // redraws the components of the game
    }

    @Override
    public void keyPressed(KeyEvent ke){
        if((stage == LEVEL1 || stage == LEVEL2) && canJump) { // if the frog is allowed to jump; if all the sprites have been drawn and a new key is getting pressed
            // https://stackoverflow.com/questions/23642854/make-a-keyevent-in-java-only-happen-once-even-when-key-is-held; I used this link as inspiration
            if (lastKey == -1 || lastKey != ke.getKeyCode()) { // this is used so that the the player can only move once on a key press; -1 means that no current key is pressed
                lastKey = ke.getKeyCode();
                moveFrog(); // moving the frog based on what key is pressed
                canJump = false; // the frog cannot jump while the sprites are still being drawn sequentially
            }
        }
    }
    @Override
    public void keyTyped(KeyEvent ke){
    }
    @Override
    public void keyReleased(KeyEvent ke){ lastKey = -1; } // no key means the key value is -1
    @Override
    public void mouseClicked(MouseEvent mo){ }
    @Override
    public void mouseEntered(MouseEvent mo) { }
    @Override
    public void mouseExited(MouseEvent mo){ }
    @Override
    public void mouseReleased(MouseEvent mo){ }
    @Override
    public void mousePressed(MouseEvent mo){
        if(stage == INTRO) {
            if (introPlay.contains(mouse)) { // if the play button is pressed, then start the show the buffer screen
                stage = BUFFER;
            }
            else if(introHowToPlay.contains(mouse)){ // if how to play button is pressed
                stage = HOWTOPLAY;
            }
        }
        else if(stage == HOWTOPLAY){ // if the how to play screen is displayed and the next button is pressed, move to instructions part two
            if(introHowToPlayNext.contains(mouse)){
                stage = HOWTOPLAY2;
            }
        }
        else if(stage == HOWTOPLAY2){ // if the instructions part two screen is displayed and the next button is pressed, move to instructions part three
            if(introHowToPlayNext.contains(mouse)){
                stage = HOWTOPLAY3;
            }
        }
        else if(introBack.contains(mouse)){ // if the back button is pressed, go back to the intro screen
            stage = INTRO;
        }
    }
    @Override
    public void	mouseDragged(MouseEvent e){}
    @Override
    public void	mouseMoved(MouseEvent e){ // this method is used to constantly update and store the mouse's x and y-coordinates
        mouse.x = e.getX();
        mouse.y = e.getY();
    }

    // this method is used to draw the main intro screen and nothing is returned; the Graphics class allows for the drawing to occur on the screen
    public void drawMainIntro(Graphics g){
        g.drawImage(introbg, 0, 0, this); // drawing the intro background
        g.setFont(myFont);
        // creating the play button from the introButtons class
        Button play = new Button("PLAY", getWidth() / 2 - 180, getHeight() / 2 + 165, 105, 50, Color.BLACK);
        play.drawButton(g);
        // creating the how to play button
        Button howToPlay = new Button("HOW TO PLAY", getWidth() / 2 - 20, getHeight() / 2 + 165, 235, 50, Color.BLACK);
        howToPlay.drawButton(g);
        if(introPlay.contains(mouse)) {
            // redrawing the play button with a hover effect if the mouse's coordinates are inside the play button's rectangle
            play.hoverButton(g);
        }
        if(introHowToPlay.contains(mouse)) {
            // redrawing the how to play button with the hover effect
            howToPlay.hoverButton(g);
        }
        drawHighScore(g); // drawing the high score on the intro screen
        introFrog.draw(g); // drawing the frog that jumps across the screen in the intro
    }

    // this method draws the how to play section; three sets of instructions
    public void drawHowToPlay(Graphics g, Image img){
        g.drawImage(img, 0, 0, this); // drawing the how to play background
        g.setFont(myFont);
        if(stage == HOWTOPLAY) { // instructions part one
            // creating the next button
            Button howToPlayNext = new Button("NEXT", 810, 30, 105, 40, Color.BLACK);
            howToPlayNext.drawButton(g);
            if (introHowToPlayNext.contains(mouse)) {
                // creating the hover effect with the next button
                howToPlayNext.hoverButton(g);
            }
        }
        if(stage == HOWTOPLAY2){ // instructions part two
            // creating the next button
            Button howToPlayNext = new Button("NEXT", 810, 30, 105, 40, Color.BLACK);
            howToPlayNext.drawButton(g);
            if (introHowToPlayNext.contains(mouse)) {
                // creating the hover effect with the next button
                howToPlayNext.hoverButton(g);
            }
        }
        if(stage == HOWTOPLAY3){ // instructions part three
            // creating the back button
            Button back = new Button("BACK", 10, 10, 105, 40, Color.BLACK);
            back.drawButton(g);
            if (introBack.contains(mouse)) {
                // creating the hover effect with the back button
                back.hoverButton(g);
            }
        }
    }

    // this method is used to draw the buffer screen before the next level starts
    // Parameter includes the level which stores which level is about to begin
    public void bufferScreen(Graphics g, int level){
        // re-initializing the objects at the same positions as in the GamePanel constructor each time a new level begins
        for(int i = 0; i < 6; i++){
            cars[i] = new Vehicle("car" + i + ".png", carsx[i], 335 + i * 45);
            cars[i + 6] = new Vehicle("car" + (i + 6) + ".png", carsx[i + 6], 335 + i * 45);
            cars[i + 12] = new Vehicle("car" + i + ".png", carsx[i + 12], 335 + i * 45);
        }
        for(int i = 0; i < 2; i++){
            // re-initializing the logs at their starting positions whenever the level changes
            logs[i] = new Log(logsx[i], 175 - i * 40);
            logs[i + 2] = new Log(logsx[i + 2], 175 - i * 40);
            logs[i + 4] = new Log(logsx[i + 4], 175 - i * 40);
            logs[i + 6] = new Log(logsx[i + 6], 175 - i * 40);
        }
        // re-initializing the crocodiles at their starting positions since they may re-play the game again
        crocs[0] = new Crocodile(logs[0].getRect().x, 175);
        crocs[1] = new Crocodile(logs[1].getRect().x, 135);

        fly = new Fly(); // re-drawing the fly for levels one and two if the game is played again

        if(level == LEVEL1) {
            g.drawImage(levelOneBufferBg, 0, 0, this);
        }
        else if(level == LEVEL2) {
            g.drawImage(levelTwoBufferBg, 0, 0, this);
        }
        GameTimer.timeLimit = 100; // re-setting the timer which "counts" from 100 to 0 which is how long the player has to complete each level
        delay++; // used to help the countdown slow down
        if(count > 0) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Optima", Font.PLAIN, 100));
            g.drawString(String.valueOf(count), 475, 390); // drawing the countdown on the screen
            if (delay % WAIT == 0) {
                count--;
            }
        }
        if(count == 0){ // if the countdown is done
            Frog.lives = 5; // res-setting the number of lives that the player has in each level
            Arrays.fill(endDestVals, false); // initially no frog home has been reached
            count = 5;
            stage = level; // starting the next level
        }
    }

    // this method is used to call the draw method and checks whether a house has been reached, a fly has been eaten, the level has been passed etc.
    // The parameters include the level being played; nothing is returned
    public void mainGame(Graphics g, int level) {
        if(level == LEVEL1){
            // the objects move slower in level one than level two; setting their speeds to the left or right
            Vehicle.SPEED = 3;
            Log.LEFT = -2;
            Log.RIGHT = 3;
            Turtle.SPEED = -1;
        }
        else if(level == LEVEL2){
            // moving the objects faster in level two
            Vehicle.SPEED = 4;
            Log.LEFT = -3;
            Crocodile.LEFT = -3;
            Log.RIGHT = 4;
            Crocodile.RIGHT = 4;
            Turtle.SPEED = -3;
        }
        g.drawImage(levelOneBack,0, 0, this);

        drawLevel(g); // calling the draw method

        if(gameFrog.ifReached()){ // if the frog reaches one of the end destinations they score 1000 points
            score += 1000;
            if(fly.frogEatFly(gameFrog)){ // if the frog eats the fly, they get 200 bonus points
                score += 200;
            }
            reachDestSound.play();
            gameFrog = new Frog(Frog.XORIG, Frog.YORIG); // the frog moves back to the starting position to reach the next home
        }

        // the sides of the background are drawn again so that vehicles can pass "underneath" the bridge
        g.drawImage(backleft, -30, 0, this);
        g.drawImage(backright, 765, 0, this);

        // this is used to check if all the frog homes have been reached and are true in the endDestVals array
        boolean allTrue = true;
        for(int i = 0; i < endDestVals.length; i++){
            if(!endDestVals[i]){
                allTrue = false;
                break;
            }
        }
        if(!gameFrog.isAlive() || gameTimer.countDown()){ // if the player loses all of their lives or they have used up all of their time, they lose
            loseSound.play();
            stage = LOSE;
        }
        if(level == LEVEL1) {
            if (allTrue) { // if all frog homes have been reached
                score += GameTimer.timeLimit * 10; // the more time they have left, the greater their score and points gained
                score += Frog.lives * 10; // the more lives they have left, the greater their score and points gained
                stage = BUFFER2; // buffer screen before level two
            }
        }
        else if(level == LEVEL2){
            if(allTrue){ // if all frog homes have been reached
                score += GameTimer.timeLimit * 10;
                score += Frog.lives * 10;
                winGameSound.play();
                stage = WIN; // the player won the game
            }
        }
    }

    // this method controls the movement of all the objects in the game
    public void move(){
        boolean stand = false; // this is used to store whether the frog is standing on the logs, turtles or crocodiles in the water
        for(int i = 0; i < 18; i++){
            // moving half the cars to the right and the other to the left
            if(i % 2 == 0) {
                cars[i].move(cars[i].RIGHT);
            }
            else{
                cars[i].move(cars[i].LEFT);
            }
            // if the frog hits a car, they lose a live
            if(cars[i].hitFrog(gameFrog)){
                hitCarSound.play();
                gameFrog.loseLive();
            }
        }
        if(stage == LEVEL1) {
            // not all the logs are drawn in level two; only in level one
            for(int i = 0; i < 8; i++) {
                if (i % 2 == 0) {
                    logs[i].move(logs[i].RIGHT); // moving half the logs to the right
                    if(logs[i].standOnLog(gameFrog)) { // if the frog is standing on a log
                        gameFrog.move(logs[i].RIGHT, 0); // they move "on" on the log; same speed right
                        stand = true;
                    }
                } else {
                    logs[i].move(logs[i].LEFT); // half the logs move left
                    if(logs[i].standOnLog(gameFrog)) {
                        gameFrog.move(logs[i].LEFT, 0); // they move "on" on the log; same speed left
                        stand = true;
                    }
                }
            }
        }
        if(stage == LEVEL2){
            // crocodiles are only drawn and moved in level two
            crocs[0].move(Crocodile.RIGHT);
            crocs[1].move(Crocodile.LEFT);
            for(int i = 2; i < 8; i++) {
                if (i % 2 == 0) {
                    // same as in level one; halfs the logs move right while the others move left
                    logs[i].move(logs[i].RIGHT);
                    if (logs[i].standOnLog(gameFrog)) { // if the frog is on a log, it moves "on" the log with the same direction and speed
                        gameFrog.move(logs[i].RIGHT, 0);
                        stand = true;
                    }
                } else {
                    logs[i].move(logs[i].LEFT);
                    if (logs[i].standOnLog(gameFrog)) {
                        gameFrog.move(logs[i].LEFT, 0);
                        stand = true;
                    }
                }
            }
            if(crocs[0].standOnCrocodile(gameFrog)) { // if the frog is on the crocodile moving right, it also moves right at the same speed
                gameFrog.move(Crocodile.RIGHT, 0);
                stand = true;
            }
            if (crocs[1].standOnCrocodile(gameFrog)) {// if the frog is on the crocodile moving left, it also moves left at the same speed
                gameFrog.move(Crocodile.LEFT, 0);
                stand = true;
            }
        }
        for(int i = 0; i < 17; i++){
            // moving all the turtles
            turtles[i].move();
            if(turtles[i].standOnTurtle(gameFrog)){ // if the frog is standing on a turtle, it moves "on" the turtle in the same speed and direction
                gameFrog.move(turtles[i].SPEED, 0);
                stand = true;
            }
        }
        // if the frog is in the water region, it is not standing on the turtles, logs, or crocodiles and it isn't in a frog home, the frog sinks
        if(gameFrog.getRect().y < 212 && !stand && !gameFrog.ifReached()){
            gameFrog.loseLive();
            jumpInWaterSound.play(); // frog jumping and sinking in water sound
        }
    }

    // this method is used to move the frog based on the key being pressed
    public void moveFrog(){
        if(lastKey == 39) { // right key -> move right
            gameFrog.move(48, 0);
        }
        else if(lastKey == 37) { // left key -> move left
            gameFrog.move(-48, 0);
        }
        else if(lastKey == 38){ // down key -> move down
            gameFrog.move(0, -48);
        }
        else if(lastKey == 40){ // up key -> move up
            gameFrog.move(0, 48);
        }
        jumpSound.play(); // frog jumping sound
    }

    // this method is used to draw the level; contains separate cases for level one and two
    public void drawLevel(Graphics g){
        for(int i = 0; i < 18; i++){
            cars[i].draw(g); // drawing all the cars
        }
        for(int i = 0; i < 17; i++){
            turtles[i].draw(g); // drawing all the turtles
        }
        if(stage == LEVEL1) {
            for(int i = 0; i < 8; i++) {
                logs[i].draw(g); // drawing all the logs in level one
            }
        }
        else if(stage == LEVEL2){
            for(int i = 2; i < 8; i++) {
                logs[i].draw(g); // drawing most of the logs in level one; not the ones replaced by the crocodiles
            }
            // drawing the crocodiles
            crocs[0].draw(g);
            crocs[1].draw(g);
        }
        if(!canJump){ // if the player has pressed a key and they cannot jump yet
            gameFrog.changeSprite(); // changing the sprites of the frog's jump and drawing the frog at a new frame
            if(gameFrog.changeSprite()){ // if all sprites have been drawn, the frog can jump again
                canJump = true;
            }
        }
        gameFrog.draw(g); // drawing the main game frog

        // if the frog reaches one of the end "spots" then they passed one level and a frog is drawn at that spot
        for(int i = 0; i < endDestVals.length; i++){
            if(endDestVals[i]) {
                gameFrog.drawImg(g, endDest[i].x - gameFrog.getWidth()/2, endDest[i].y - gameFrog.getHeight()/2);
            }
        }
        drawPanel(g); // draws the lives, score and timer
        fly.drawFly(g); // drawing the fly
    }

    // this method draws a rectangle around the lives, score and timer which is separately draw inside this region
    public void drawPanel(Graphics g){
        gameFrog.drawLives(g);
        drawScore(g);
        g.drawRect(210, 710, 510, 50);
    }

    // this method draws the current score of the player on the screen during the level
    public void drawScore(Graphics g){
        g.setFont(myFont);
        g.drawString("Score: " + score, 450, 745);
    }

    // this method draws the losing screen
    public void drawLose(Graphics g){
        g.drawImage(losebg, 0, 0, this); // drawing the losing background
        g.setFont(myFont); // changing the font for the buttons' text
        // creating the back button
        Button back = new Button("BACK", 10, 10, 105, 40, Color.WHITE);
        back.drawButton(g);
        if(introBack.contains(mouse)){
            // creating a hover effect with the back button
            back.hoverButton(g);
        }
        // if the score currently saved as the high score is less than the score the player received, then that is the new high score
        if(loadScore() < score){
            saveScore(score);
        }
    }

    // this method draws the winning screen
    public void drawWin(Graphics g){
        g.drawImage(winbg, 0, 0, this); // draws the winning background
        g.setFont(myFont); // changing the font for the buttons' text
        // creating the back button
        Button back = new Button("BACK", 10, 10, 105, 40, Color.BLACK);
        back.drawButton(g);
        if(introBack.contains(mouse)){
            // creating a hovering effect with the back button
            back.hoverButton(g);
        }
        g.setFont(new Font("Courier New", Font.BOLD, 50));
        g.setColor(new Color(255, 0, 153));
        g.drawString("" + score, 450, 720); // drawing the score on the screen

        // if the score currently saved as the high score is less than the score the player received, that is the new high score
        if(loadScore() < score){
            saveScore(score);
        }
    }

    // this method contains all possible stages in the game and decides which methods are to be run; nothing is returned
    @Override
    public void paint(Graphics g){
        if(stage == INTRO){
            drawMainIntro(g);
        }
        else if(stage == HOWTOPLAY){
            drawHowToPlay(g, instructionsbg);
        }
        else if(stage == HOWTOPLAY2){
            drawHowToPlay(g, instructionsbg2);
        }
        else if(stage == HOWTOPLAY3){
            drawHowToPlay(g, instructionsbg3);
        }
        else if(stage == BUFFER){
            score = 0; // re-setting the score to 0 when starting level one
            bufferScreen(g, LEVEL1);
        }
        else if(stage == LEVEL1){
            mainGame(g, LEVEL1);
            // drawing the timer on the screen and decreasing the time left
            gameTimer.drawTimer(g);
        }
        else if(stage == BUFFER2){
            bufferScreen(g, LEVEL2);
        }
        else if(stage == LEVEL2){
            mainGame(g, LEVEL2);
            // re-drawing the timer in level two
            gameTimer.drawTimer(g);
        }
        else if(stage == LOSE){
            drawLose(g);
        }
        else if(stage == WIN){
            drawWin(g);
        }
    }
}