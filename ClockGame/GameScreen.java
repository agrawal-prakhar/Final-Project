package ClockGame;
import AlarmClock.*;
import Timers.Timer;

import javax.swing.*;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class GameScreen extends JPanel implements KeyListener {

    private Alarm gameAlarm;
    public static Resources loadResources = new Resources();
    private static int screenWidth;
    private static int screenHeight;
    private static int FPS;
    private static final int defaultYspeed = 450; //default player speed
    private static final int defaultXspeed = 35;
    private String startString;
    private int startStringWidth;
    private static int currentHighScore;
    private int newHighScore;
    private static String scoreString;
    private int winCondition;
    private int currentFrame = 0;
    private final int overTime;
    private static final Pair defaultAcceleration = new Pair(0, 1500);

    public World gameWorld;
    private final Thread gameThread;
    private Timer endGraphicTimer;
    private Timer gameTimer; //static - belongs to GameScreen, not instance of Timers.Timer
    private Timer screenTimer;
    private int gameTime; //referenced in other classes
    volatile private Hand hand;
    private boolean endCredits = false;
    private boolean flapWing = true;
    private boolean started;// = false;
    private boolean win = false;// =  set to not won
    private boolean gameOvertime = false;
    private int safetyOff;

    public GameScreen(int initWidth, int initHeight, int initFPS, int hammerGoal, DisplayGameScreen initGame, Alarm alarm, int autoAlarmOff) {
        safetyOff = autoAlarmOff;
        gameAlarm = alarm;
        screenTimer = new Timer();
        started = false;
        winCondition = hammerGoal;
        overTime = (80 * winCondition);
        screenWidth = initWidth;
        screenHeight = initHeight;
        FPS = initFPS;
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setDoubleBuffered(true);
        this.addKeyListener(this);
        this.setFocusable(true);
        gameWorld = new World(screenWidth, screenHeight, hammerGoal, initGame);
        gameThread = new Thread(new GameScreen.Runner());
        gameThread.start();
    }

    class Runner implements Runnable {
        @Override
        public void run() { //LATER: CHANGE SO IT ONLY RUNS UNTIL YOU ACHIEVE MAX NUMBER OF POINTS
            currentHighScore = getCurrentHighScore();
            while (gameThread != null && !win && !gameOvertime) {  //if user hasn't won yet and game is not in overtime
                if (started) {
                    gameWorld.updateObjects(1.0 / (double) FPS);
                    if (gameTimer == null) { //create new game timer if not already create
                        gameTimer = new Timer();
                    }
                    gameTime = gameTimer.getElapsedTime(); //keep track of current time in seconds
                    gameTimer.printTime();
                    stopGameOvertime(); //check if game is in overtime
                    currentFrame++;
                    newHighScore = gameWorld.newRecordCheck(); //consistently check for new high score as game is played
                    win = gameWorld.checkIfWon();
                    if (win || gameOvertime) { //start end credits if game won or overtime reached
                        endCredits = true;
                    }
                }
                repaint();
                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {
                }
            }
            if (win & (currentHighScore < newHighScore)) { //if player collects all the necessary hammers, high score becomes the current win condition
                saveHighScore();
            }
            while (gameThread != null && endCredits) { //if game is over but end credits in progress, draw end credits loop
                if(win) {
                    hand.update(1.0 / (double) FPS);
                }
                repaint();
                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {
                }
            }
        }
    }
    
    public void saveHighScore() {
        try{
            FileWriter overWriteFile = new FileWriter("res/highScore.txt", false);
            overWriteFile.write(Integer.toString(newHighScore));
            overWriteFile.close();
         } catch(IOException i) {
            i.printStackTrace();
        }
    }
    public static int getCurrentHighScore() {
            try {
                File scoreFile = new File("res/highScore.txt");
                Scanner readScore = new Scanner(scoreFile);
                if (readScore.hasNext()) {
                    scoreString = readScore.nextLine().trim();
                    currentHighScore = Integer.parseInt(scoreString);
                    System.out.println("current high score: "+ currentHighScore);
                } else { currentHighScore = 0; } //if file exists but empty, set current high score to 0
                readScore.close();
            }
            catch (FileNotFoundException e) {
                currentHighScore = 0; //if file is not found, set current high score to 0
                System.out.println("No highScore file found. Created highScore file.");
                e.printStackTrace();
            }
        return currentHighScore;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintBackground(g);
        if (!win && !endCredits) { //if game not yet won and end credits have not started
            gameWorld.drawObjects(g); //draw objects to screen
            if (!started) { //if game not started, draw start message
                startMessage(g);
            }
            if (started) { //if started, draw key instructions to corner of screen
                drawWSAD(g);
            }
        }
        if (endCredits) { //if overtime reached, play the end credit graphic
            endGraphic(g);
        }
    }

    public void paintBackground(Graphics g) {
        g.drawImage(loadResources.defaultBackground, 0, 0, screenWidth, screenHeight, null);
    }

    void startMessage(Graphics g) { //draw start screen, including key instructions
        g.setColor(java.awt.Color.BLACK);
        g.fillRect((screenWidth / 2) - 230, (screenHeight / 2) - 165, 445, 300);
        g.setColor(java.awt.Color.WHITE);
        g.drawRect((screenWidth / 2) - 230, (screenHeight / 2) - 165, 445, 300);
        g.drawImage(loadResources.wasd, (screenWidth / 2) - 225, (screenHeight / 2) - 150, 450, 300, null);
        g.setColor(loadResources.lightBlue);
        g.setFont(loadResources.Monospaced);
        g.drawString("default speed", (screenWidth / 2) - 50, (screenHeight / 2) + 80);
        if (screenTimer.getElapsedDecimalSeconds() - screenTimer.getElapsedTime() > 0.5) {
            g.setFont(loadResources.MonospacedBold);
            startString = "PRESS  ' S '  TO START";
            startStringWidth = g.getFontMetrics().stringWidth(startString);
            g.drawString(startString, ((screenWidth / 2) - (startStringWidth / 2)) + 3, (screenHeight / 2) + 115);
            g.setColor(loadResources.turquoise);
            g.drawString(startString, ((screenWidth / 2) - (startStringWidth / 2)), (screenHeight / 2) + 115);
            g.drawRect((screenWidth / 2) - 230, (screenHeight / 2) - 165, 445, 300);
        }
    }

    void drawWSAD(Graphics g) { //create key instructions during playing screen
        g.setColor(java.awt.Color.BLACK);
        g.fillRect(screenWidth - 120, 16, 120, 90);
        g.setColor(loadResources.lightBlue);
        g.drawRect(screenWidth - 120, 16, 120, 90);
        g.setFont(GameScreen.loadResources.Monospaced);
        g.drawImage(loadResources.wasd, screenWidth - 114, 20, 112, 75, null);
        g.drawString("default", screenWidth - 80, 92);
        g.drawString("speed", screenWidth - 80, 102);
        // g.setColor(Color.ORANGE);
        for (int i = 0; i < 3; i++) {
            g.drawLine(screenWidth - 60 - i, 75, screenWidth - 60 - i, 80);
        }
    }

    static int peekHeight() {
        return screenHeight;
    }

    static int peekWidth() {
        return screenWidth;
    }


    public void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();
        //System.out.println("You pressed down: " + c);
        keyChanges(c);
    }

    public void keyReleased(KeyEvent e) {
        char c = e.getKeyChar();
    }


    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
    }

    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    private void keyChanges(char c) {
        switch (c) {
            case 'w', 'W':
                keyW();
                break;
            case 's', 'S':
                keyS();
                break;
            case 'a', 'A':
                keyA();
                break;
            case 'd', 'D':
                keyD();
                break;
            default: break; //if any other key pressed, change nothing
        }
    }

    private void keyW() { //if w pressed, change wing direction, player jumps up away up toward beginning of frame
        flapWing = !flapWing;
        gameWorld.setPlayerYVelocity(-1 * GameScreen.defaultYspeed);
    }

    private void keyS() {//start game timer, set acceleration to default, start pipe movements, stops horizontal velocity
        started = true;
        gameWorld.setPlayerAcceleration(defaultAcceleration);
        gameWorld.setPlayerXVelocity(0);
        Pipes.changePipeSpeed(Pipes.getDefaultPipeSpeed());
    }

    private void keyA() {//negative player velocity and slow down pipes
        gameWorld.setPlayerXVelocity(-2.4 * GameScreen.defaultXspeed);
        Pipes.changePipeSpeed((int) (0.85 * Pipes.getDefaultPipeSpeed()));
    }

    private void keyD() {//speed up pipes and increase player horizontal velocity
        gameWorld.setPlayerXVelocity(GameScreen.defaultXspeed);
        Pipes.changePipeSpeed((int) (1.4 * Pipes.getDefaultPipeSpeed()));
    }




    public int getGameTime() {
        return gameTime;
    }

    public void stopGameOvertime() { //stop alarm if game not won yet by overtime
        if (gameTimer.getElapsedTime() >= (overTime) || screenTimer.getElapsedTime() >= safetyOff) { // overtime reached when game time is reaches/exceeds 60 seconds per hammer level OR if game screen has been open by autoShutOff time
            gameOvertime = true;
        }
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void endGraphic(Graphics g) {
        if (endGraphicTimer == null) { //start timer for end credits
            endGraphicTimer = new Timer();
        }
        g.drawImage(loadResources.background1, 0, 0, 1244, 700, null); //create background for end credits
        for (int i = 0; i < 5; i++) {
            g.drawImage(loadResources.grass, 0 + (i * 220), screenHeight - 150, 220, 150, null);
        }
        if (endCredits && win) {
            winCredits(g);
            if(endGraphicTimer.getElapsedTime() <= 5) {
                hand.draw(g);
            }
        }
        if (endCredits && !win) {
            loseCredits(g);
        }
    }

    public void winCredits(Graphics g) {
        g.drawImage(loadResources.morning, (screenWidth / 2) - 250, (screenHeight / 2) - 41, 500, 82, null);
        if(hand == null) {
            hand = new Hand();
        }
        if (endGraphicTimer.getElapsedTime() <= 2) {
            if (endGraphicTimer.getElapsedDecimalSeconds() - (double) endGraphicTimer.getElapsedTime() < 0.3 || (endGraphicTimer.getElapsedDecimalSeconds() - (double) endGraphicTimer.getElapsedTime() > 0.7 && endGraphicTimer.getElapsedDecimalSeconds() - (double) endGraphicTimer.getElapsedTime() < 0.8)) {
                g.drawImage(loadResources.clockL, (screenWidth / 2) - 10 + endGraphicTimer.getElapsedTime(), screenHeight - 150, 40, 55, null);
            } else {
                g.drawImage(loadResources.clockR, (screenWidth / 2) - 15 + endGraphicTimer.getElapsedTime(), screenHeight - 150, 40, 55, null);
            }
        } //first five seconds clock bounces left to right

    if(endGraphicTimer.getElapsedTime() > 2 && endGraphicTimer.getElapsedTime() <= 7) {
        if (endGraphicTimer.getElapsedTime() > 2 && endGraphicTimer.getElapsedTime() <= 5) {
            if (endGraphicTimer.getElapsedDecimalSeconds() - (double) endGraphicTimer.getElapsedTime() > 0.5) {
                g.drawImage(loadResources.clockResting, (screenWidth / 2) - 20, screenHeight - 150, 40, 55, null);
            } else {
                g.drawImage(loadResources.clockBlink, (screenWidth / 2) - 20, screenHeight - 150, 40, 55, null);
            }
        } //next 5 seconds hand turns off alarm

        if (endGraphicTimer.getElapsedTime() > 5 && endGraphicTimer.getElapsedTime() <= 7) {
            g.drawImage(loadResources.clockFlat, (screenWidth / 2) - 20, (screenHeight) - 145, 40, 55, null);
            g.drawImage(loadResources.dust, (screenWidth / 2) - 28, 498, 56, 52, null);
            g.drawImage(loadResources.poof, (screenWidth / 2) - 13, 525, 25, 10, null);
            g.drawImage(loadResources.smoke, (screenWidth / 2) - 270, 300, 500, 500, null);
        } //next 3 seconds alarm is pressed down
    }
        if (endGraphicTimer.getElapsedDecimalSeconds() > 8.5) { //finish end credits
            endCredits = false;
        }
        if(endCredits == false) {
            gameAlarm.stopAlarm();
        }
    }

    public void loseCredits(Graphics g) {
        g.drawImage(loadResources.getUp, (screenWidth / 2) - 300, (screenHeight / 2) - 57, 600, 114, null);
        if (endGraphicTimer.getElapsedTime() <= 3) { //first three seconds tired clock blinks
            g.drawImage(loadResources.getUp, (screenWidth / 2) - 300, (screenHeight / 2) - 57, 600, 114, null);
            if (endGraphicTimer.getElapsedDecimalSeconds() - (double) endGraphicTimer.getElapsedTime() > 0.5) {
                g.drawImage(loadResources.tiredClock, (screenWidth / 2) - 50, screenHeight - 220, 100, 216, null);
            } else {
                g.drawImage(loadResources.tiredClock2, (screenWidth / 2) - 50, screenHeight - 216, 100, 216, null);
            }
        } else if (endGraphicTimer.getElapsedTime() > 3 && endGraphicTimer.getElapsedTime() < 5) { //next second clock falls sideways
            g.drawImage(loadResources.deadClock, (screenWidth / 2) - 50, screenHeight - 150, 215, 100, null);
            System.out.println("reached");
        } else if (endGraphicTimer.getElapsedTime() > 5) { //finish end credits
            endCredits = false;
        }
        if(endCredits == false) {
            gameAlarm.stopAlarm();
        }
    }

    public boolean ifStarted() {
        return started;
    }

    public boolean ifFlap() {
        return flapWing;
    }

}
