package Wordle;

import Timers.Timer;
import AlarmClock.Alarm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class WordleScreen extends JPanel implements KeyListener, MouseListener {
    public static int WIDTH;
    public static int HEIGHT;
    public static int FPS;

    private boolean everWin = false;
    JFrame frame;
    World world;
    String newWord;
    int y = 0;
    int numGuesses = 0;
    int onWhichWord;
    boolean win = false;
    boolean lose = false;

    char[][] userWordsArray = new char[8][5];
    String[][] colorsArray = new String[8][5];

    char[] keyboardFirstRow = {'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P'};
    VirtualKeyboardKey[] keyboardRow1 = new VirtualKeyboardKey[10];
    String[] keyboardFirstRowColor = new String[10];

    char[] keyboardSecondRow = {'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L'};
    VirtualKeyboardKey[] keyboardRow2 = new VirtualKeyboardKey[9];
    String[] keyboardSecondRowColor = new String[9];

    char[] keyboardThirdRow = {'Z', 'X', 'C', 'V', 'B', 'N', 'M'};
    VirtualKeyboardKey[] keyboardRow3 = new VirtualKeyboardKey[7];
    String[] keyboardThirdRowColor = new String[7];

    int coins = 0;
    private int countDown = 5;
    private Alarm wordleAlarm;
    private Timer wordleTimer;
    private Timer endGraphicsTimer;
    private int autoShutOff;

    //Char[] letterArray = new Char[5];

    public WordleScreen(int initWidth, int initHeight, int initFPS, Alarm alarm, int autoAlarmOff, JFrame frame) {
        this.frame = frame;
        WIDTH = initWidth;
        HEIGHT = initHeight;
        FPS = initFPS;
        wordleAlarm = alarm;
        autoShutOff = autoAlarmOff; //sets int for the time the alarm should shut off in (and return to screen)
        wordleTimer = new Timer();

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        //initiating the mouse and keyboard listener for use in the program
        addKeyListener(this);
        addMouseListener(this);
        world = new World(WIDTH, HEIGHT);

        Thread mainThread = new Thread(new Runner());
        mainThread.start();

        initialize();

        //userWord = world.takeInput();

    }

    //function for starting/restarting the game to generate a newly random word and empty everything else
    public void initialize() {

        win = false;
        lose = false;

        //newWord stores the new word from the file that the user has to guess
        newWord = world.readFile();
        onWhichWord = 0;


        //clears all the guesses
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 5; j++) {

                colorsArray[i][j] = "white";
                userWordsArray[i][j] = ' ';
            }

        }

        //sets foundation display of virtual keyboard
        for (int i = 0; i < 10; i++) {
            keyboardFirstRowColor[i] = "white";
            keyboardRow1[i] = new VirtualKeyboardKey();
            if (i < 9) {
                keyboardSecondRowColor[i] = "white";
                keyboardRow2[i] = new VirtualKeyboardKey();
            }
            if (i < 7) {
                keyboardThirdRowColor[i] = "white";
                keyboardRow3[i] = new VirtualKeyboardKey();
            }
        }

    }


    class Runner implements Runnable {
        public Runner() {

        }

        public void run() {
            while (true) {

                if(wordleTimer.getElapsedTime()>=autoShutOff) {
                    if(endGraphicsTimer==null) {
                        endGraphicsTimer = new Timer();
                    }
                    checkEndGraphicsTimer();
                }
                repaint();
                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {
                }
            }
        }

    }

    //pressing the mouse
    public void mousePressed(MouseEvent e) {

        char c = ' ';
        // show the point where the user pressed the mouse

        // getting the appropriate char values for the guess, when user presses in the first row of virtual keyboard
        for (int i = 0; i < 10; i++) {
            if (((keyboardRow1[i].startX <= e.getX())) && (keyboardRow1[i].startX + keyboardRow1[i].width >= e.getX()) &&
                    ((keyboardRow1[i].startY <= e.getY())) && (keyboardRow1[i].startY + keyboardRow1[i].height >= e.getY())) {
                c = keyboardRow1[i].letter;
                break;
            }

            // getting the appropriate char values for the guess, when user presses in the second row of virtual keyboard
            if (i < 9) {
                if (((keyboardRow2[i].startX <= e.getX())) && (keyboardRow2[i].startX + keyboardRow2[i].width >= e.getX()) &&
                        ((keyboardRow2[i].startY <= e.getY())) && (keyboardRow2[i].startY + keyboardRow2[i].height >= e.getY())) {
                    c = keyboardRow2[i].letter;
                    break;
                }
            }

            // getting the appropriate char values for the guess, when user presses in the third row of virtual keyboard
            if (i < 7) {
                if (((keyboardRow3[i].startX <= e.getX())) && (keyboardRow3[i].startX + keyboardRow3[i].width >= e.getX()) &&
                        ((keyboardRow3[i].startY <= e.getY())) && (keyboardRow3[i].startY + keyboardRow3[i].height >= e.getY())) {
                    c = keyboardRow3[i].letter;
                    break;
                }
            }
        }

        //printing out the char value obtained in the guesses above
        if (c != ' ') {
            for (int i = 0; i < 5; i++) {
                if (userWordsArray[onWhichWord][i] == ' ') {
                    userWordsArray[onWhichWord][i] = c;
                    break;
                }

            }
        }

        //pressed enter by mouse
        if ((e.getX() >= 743 && e.getX() <= 873) && (e.getY() >= 540 && e.getY() <= 590)) {
            pressedEnter();
        }

        //pressed the delete button by mouse
        else if ((e.getX() >= 128 && e.getX() <= 258) && (e.getY() >= 540 && e.getY() <= 590)) {
            pressedDelete();
        }

    }


    public void mouseReleased(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    // user input through the keyboard
    public void keyTyped(KeyEvent e) {

        //get the character that the user pressed
        char c = e.getKeyChar();

        //checks if a game has been won or lost and 'space' is entered, then restarts a new game
        if ((lose || win) && ((int) c == 32)) {
            initialize();
        } else if (((lose || win) && everWin) && (c == 'H') || (c == 'h')) {
            wordleAlarm.stopAlarm(); //go back to the main menu
        }

        //pressing the 'esc' key for hint while checking that user has enough coins for the hint
        if ((int) c == 27 && coins >= 10) {
            for (int i = 0; i < 5; i++) {

                //variable checks if there is this letter has already been correctly guessed or 'greened' by the user or not
                boolean green = false;
                for (int j = 0; j < onWhichWord + 1; j++) {
                    if (colorsArray[j][i].equals("green")) {
                        green = true;
                        break;
                    }
                }
                if ((onWhichWord == 0 && !colorsArray[onWhichWord][i].equals("green")) || (!green)) {

                    //if the condition is met, it displays the hint of a correct (or green) letter
                    userWordsArray[onWhichWord][i] = newWord.toUpperCase().charAt(i);
                    colorsArray[onWhichWord][i] = "green";
                    coins -= 10;
                    break;
                }
            }
        }

        //pressing the Enter key
        else if ((int) c == 10) {

            pressedEnter();

        }


        //pressing the delete key
        else if ((int) c == 8) {
            pressedDelete();

        }

        // will only accept character input of upper and lower case alphabets
        else if ((((int) c >= 97) && ((int) c <= 122)) || (((int) c >= 65) && ((int) c <= 90))) {
            c = Character.toString(c).toUpperCase().charAt(0);

            //displays the character in the next empty position
            for (int i = 0; i < 5; i++) {
                if (userWordsArray[onWhichWord][i] == ' ') {
                    userWordsArray[onWhichWord][i] = c;
                    break;
                }

            }
        }
    }

    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    // displaying things on the frame
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setFont(new Font("Roboto", Font.PLAIN, 25));

        //nested loops to go into the arrays of all the guesses and color them according to the values in colorArray

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 5; j++) {

                if (colorsArray[i][j].equals("white")) {
                    g.setColor(Color.WHITE);
                } else if (colorsArray[i][j].equals("grey")) {
                    g.setColor(Color.GRAY);
                } else if (colorsArray[i][j].equals("green")) {
                    g.setColor(Color.GREEN);
                } else if (colorsArray[i][j].equals("yellow")) {
                    g.setColor(Color.YELLOW);
                }

                //the boxes for the guesses are created here
                g.fillRect(350 + 60 * j, 75 + 40 * i, 50, 35);
                g.setColor(Color.BLACK);

                //the guessed letters are printed here from the values in userWordsArray
                g.drawString(Character.toString(userWordsArray[i][j]), 365 + 60 * j, 100 + 40 * i);
            }
        }

        g.setFont(new Font("Roboto", Font.PLAIN, 28));

        //printing keyboard
        for (int i = 0; i < 10; i++) {

            //first row

            if (keyboardFirstRowColor[i].equals("white")) {
                g.setColor(Color.WHITE);
            } else if (keyboardFirstRowColor[i].equals("grey")) {
                g.setColor(Color.GRAY);
            } else if (keyboardFirstRowColor[i].equals("green")) {
                g.setColor(Color.GREEN);
            } else if (keyboardFirstRowColor[i].equals("yellow")) {
                g.setColor(Color.YELLOW);
            }


            //prints out the first line of the virtual keyboard here
            g.fillRect(180 + 65 * i, 410, 50, 50);
            g.setColor(Color.BLACK);
            g.drawString(Character.toString(keyboardFirstRow[i]), 195 + 65 * i, 440);
            keyboardRow1[i].startX = (180 + 65 * i);
            keyboardRow1[i].startY = 410;
            keyboardRow1[i].letter = keyboardFirstRow[i];

            //second row
            if (i < 9) {


                if (keyboardSecondRowColor[i].equals("white")) {
                    g.setColor(Color.WHITE);
                } else if (keyboardSecondRowColor[i].equals("grey")) {
                    g.setColor(Color.GRAY);
                } else if (keyboardSecondRowColor[i].equals("green")) {
                    g.setColor(Color.GREEN);
                } else if (keyboardSecondRowColor[i].equals("yellow")) {
                    g.setColor(Color.YELLOW);
                }

                //prints out the second line of the virtual keyboard here
                g.fillRect(208 + 65 * i, 475, 50, 50);
                g.setColor(Color.BLACK);
                g.drawString(Character.toString(keyboardSecondRow[i]), 223 + 65 * i, 505);

                keyboardRow2[i].startX = (208 + 65 * i);
                keyboardRow2[i].startY = 475;
                keyboardRow2[i].letter = keyboardSecondRow[i];

            }

            //third row
            if (i < 7) {


                if (keyboardThirdRowColor[i].equals("white")) {
                    g.setColor(Color.WHITE);
                } else if (keyboardThirdRowColor[i].equals("grey")) {
                    g.setColor(Color.GRAY);
                } else if (keyboardThirdRowColor[i].equals("green")) {
                    g.setColor(Color.GREEN);
                } else if (keyboardThirdRowColor[i].equals("yellow")) {
                    g.setColor(Color.YELLOW);
                }

                //prints out the third line of the virtual keyboard here

                g.fillRect(273 + 65 * i, 540, 50, 50);
                g.setColor(Color.BLACK);
                g.drawString(Character.toString(keyboardThirdRow[i]), 288 + 65 * i, 570);

                keyboardRow3[i].startX = (273 + 65 * i);
                keyboardRow3[i].startY = 540;
                keyboardRow3[i].letter = keyboardThirdRow[i];
            }
        }

        //Makes the enter and delete keys on the virtual keyboard
        g.setColor(Color.WHITE);
        g.fillRect(128, 540, 130, 50);
        g.fillRect(743, 540, 130, 50);
        g.setColor(Color.BLACK);
        g.drawString("DELETE", 140, 570);
        g.drawString("ENTER", 755, 570);

        //checks if the game has been won or lost
        drawWinLose(g);

        g.setFont(new Font("TimesRoman", Font.BOLD, 25));
        g.setColor(Color.WHITE);
        g.drawString("You have " + Integer.toString(coins) + " coins!", 380, 30);
        g.drawString("In 10 coins you can ask for a letter hint by pressing esc", 180, 60);

        drawAutoShutOff(g);

    }

    private void checkEndGraphicsTimer() { //return to home if auto shut off timer created
        if(endGraphicsTimer != null && endGraphicsTimer.getElapsedTime()>5) { //create new timer for end graphics
            wordleAlarm.stopAlarm();
        }
    }

    private void drawAutoShutOff(Graphics g) {
        if(endGraphicsTimer != null) { //create new timer for end graphics
            countDown = (5-endGraphicsTimer.getElapsedTime());
            g.setFont(new Font("TimesRoman", Font.BOLD, 30));
            g.setColor(Color.WHITE);
            g.drawString("Out of time! alarm shuts off in "+countDown+"...", 150, 635);
        }
    }
    void drawWinLose(Graphics g) {

        // if the game has been won, displays text and stops the alarm
        if (win) {
                g.setFont(new Font("TimesRoman", Font.BOLD, 30));
                g.setColor(Color.WHITE);
                g.drawString("Congratulations! You WON!!! Your alarm is stopped", 150, 635);
                drawEverWin(g);
        }

        // if the game has been lost, displays text
        if (lose) {
            g.setFont(new Font("TimesRoman", Font.BOLD, 30));
            g.setColor(Color.WHITE);
            g.drawString("Sorry, you lost :(, the word was " + newWord, 200, 635);
            drawEverWin(g);
        }

    }

    public void drawEverWin(Graphics g) {
        if (everWin) {
            g.drawString("Click space to play again or click H to return home", 150, 675);
        } else {
            g.drawString("You have to press space to try again", 150, 675);
        }
    }


    //works when enter is pressed either on the actual or vitual keyboard
    public void pressedEnter() {
        String userWord = new String(userWordsArray[onWhichWord]);

        //checks if the word even exists
        if (!world.doesWordExist(userWord)) {

            //shows error dialogue box if it doesn't exist
            JOptionPane.showMessageDialog(frame, "Please enter a valid 5-letter word");

            //empties the guess in this case
            for (int i = 0; i < 5; i++) {
                userWordsArray[onWhichWord][i] = ' ';
            }

        } else {

            //otherwise sends the appropriate colors of each character and stores it in colorsArray
            colorsArray[onWhichWord] = world.colorWord(newWord, userWord.toLowerCase());

            //colors the boxes in the virtual keyboard appropriately
            colorVirtualKeyboard();

            //increase the counter of the guess in question
            onWhichWord++;

            //if the the correct word is guessed, then makes the boolean win true, and allocates coins according to the guesses they took to guess
            if (newWord.equals(userWord.toLowerCase())) {
                win = true;
                for (int i = 0; i < 8; i++) {
                    if (numGuesses == i) {
                        coins += (8 - onWhichWord) * 5;
                    }
                }
                wordleAlarm.stopSoundOnly(); //only stop the alarm sound here
                everWin = true; //if user gets word correct, everWin is true so they can choose to quit any time later
            }

            //if they couldn't guess in 8 guesses, make the boolean lose true
            else if (onWhichWord > 7) {
                lose = true;
                onWhichWord--;
            }
        }
    }

    //appropriately colors the virtual keyboard line by line according to the notes in ReadMe file
    public void colorVirtualKeyboard() {
        for (int i = 0; i < 5; i++) {

            //first row
            for (int j = 0; j < 10; j++) {
                if (userWordsArray[onWhichWord][i] == keyboardFirstRow[j]) {
                    if ((keyboardFirstRowColor[j].equals("white")) || (keyboardFirstRowColor[j].equals("grey") && colorsArray[onWhichWord][i].equals("yellow")) || (colorsArray[onWhichWord][i].equals("green"))) {
                        keyboardFirstRowColor[j] = colorsArray[onWhichWord][i];
                    }

                }

                //second row
                if (j < 9) {
                    if (userWordsArray[onWhichWord][i] == keyboardSecondRow[j]) {
                        if ((keyboardSecondRowColor[j].equals("white")) || (keyboardSecondRowColor[j].equals("grey") && colorsArray[onWhichWord][i].equals("yellow")) || (colorsArray[onWhichWord][i].equals("green"))) {
                            keyboardSecondRowColor[j] = colorsArray[onWhichWord][i];
                        }
                    }
                }

                //third row
                if (j < 7) {
                    if (userWordsArray[onWhichWord][i] == keyboardThirdRow[j]) {
                        if ((keyboardThirdRowColor[j].equals("white")) || (keyboardThirdRowColor[j].equals("grey") && colorsArray[onWhichWord][i].equals("yellow")) || (colorsArray[onWhichWord][i].equals("green"))) {
                            keyboardThirdRowColor[j] = colorsArray[onWhichWord][i];
                        }
                    }
                }
            }
        }
    }

    //does this when delete is pressed either on the virtual or physical keyboard
    public void pressedDelete() {
        boolean done = false;
        if (userWordsArray[onWhichWord][0] != ' ') {
            for (int i = 0; i < 5; i++) {
                if (userWordsArray[onWhichWord][i] == ' ') {
                    userWordsArray[onWhichWord][i - 1] = ' ';
                    done = true;
                }
            }
            if (!done) {
                userWordsArray[onWhichWord][4] = ' ';
            }
        }
    }

    public boolean ifWordleComplete() {
        return win;
    }
}

