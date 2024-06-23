package MathQuiz;
import AlarmClock.Alarm;
import Timers.Timer;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.Math;

public class MathScreen extends JPanel implements KeyListener {
    private Alarm mathAlarm;
    static MathWorld mathWorld;
    Thread mathThread;
    int width;
    int height;
    int fps;
    String mathSign;
    String userAnswer = "";
    boolean guessed = false;
    int correctCount = 0;  // Counter for correct answers
    public boolean complete = false;
    int argNum;
    int[] storeProblem;
    int random = (int)(Math.random()*3) + 1;
    JLabel submitLabel;  // Change from JButton to JLabel
    private int autoShutOff;
    private Timer mathTimer;
    private Timer endGraphicsTimer;
    private int countDown;

    public MathScreen(int initArg, int initWidth, int initHeight, int initFPS, Alarm alarm, int autoAlarmOff) {
        mathTimer = new Timer();
        mathAlarm = alarm;
        autoShutOff = autoAlarmOff;
        argNum = initArg;
        width = initWidth;
        height = initHeight;
        storeProblem = new int[argNum];
        fps = initFPS;
        this.setPreferredSize(new Dimension(width, height));
        this.setDoubleBuffered(true);
        this.addKeyListener(this);
        this.setFocusable(true);
        mathWorld = new MathWorld(argNum);
        mathThread = new Thread(new Runner());
        mathThread.start();

        submitLabel = new JLabel("SUBMIT");
        submitLabel.setForeground(Color.WHITE);
        submitLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 50));
        submitLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!userAnswer.equals("")) {
                    mathWorld.checkAnswer(Integer.parseInt(userAnswer), mathSign);
                    guessed = true;
                    if (mathWorld.correct) {
                        mathAlarm.stopAlarm();
                        correctCount++;
                        if (correctCount >= 3) {
                            complete = true;
                            System.out.println("Alarm stopped!");
                        } else {
                            // Reset state and generate a new problem
                            mathWorld = new MathWorld(argNum);
                            guessed = false;
                            userAnswer = "";
                        }
                    }
                }
            }
        });

        submitLabel.setBounds(360, 400, 200, 70);
        this.setLayout(null);
        this.add(submitLabel);
        System.out.println(random);

        mathSign = "X";
    }


    class Runner implements Runnable {
        @Override
        public void run() {
            while (mathThread != null && !complete) {

                if(mathTimer.getElapsedTime()>= autoShutOff) {
                    //complete = true;
                    if(endGraphicsTimer==null) {
                        endGraphicsTimer = new Timer();
                    }
                    checkEndGraphicsTimer();
                }
                repaint();
                try {
                    Thread.sleep(1000 / fps);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void checkEndGraphicsTimer() {
        if(endGraphicsTimer != null && endGraphicsTimer.getElapsedTime()>5) { //create new timer for end graphics
            mathAlarm.stopAlarm();
        }
    }



    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintBackground(g);
        mathWorld.displayMathProblem(g, mathSign);
        g.fillRect(345, 265, 220, 80);
        g.setColor(Color.BLACK);
        g.drawString(userAnswer, 420, 320);

        if (guessed) {
            if (mathWorld.correct) {
                g.setColor(Color.WHITE);
                g.drawString("CORRECT!", 345, 100);

                if (correctCount >= 3) {
                    g.drawString("Press 'ESC' to Turn Off Alarm", 20, 600);
                } else {
                    // If the correctCount is less than 3, display a message indicating the count
                    g.drawString("Correct Answers: " + correctCount + " out of 3", 20, 600);
                }
            } else {
                g.setColor(Color.WHITE);
                g.drawString("WRONG!", 370, 100);
                g.drawString("Press 'N' for a New Problem", 50, 550);
                g.drawString("Or Press 'C' to Try Again", 80, 620);
            }
        }
        drawAutoShutOff(g);
    }

    private void drawAutoShutOff(Graphics g) {
        if(endGraphicsTimer != null) { //create new timer for end graphics
            countDown = (5-endGraphicsTimer.getElapsedTime());
            g.setFont(new Font("TimesRoman", Font.BOLD, 30));
            g.setColor(Color.WHITE);
            g.drawString("Out of time! alarm shuts off in "+countDown+"...", 150, 635);
        }
    }


    public void paintBackground(Graphics g) {
        g.setColor(java.awt.Color.BLACK);
        g.fillRect(0, 0, width, height);
        g.setColor(java.awt.Color.WHITE);
    }

    public void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();

        if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getSource() == submitLabel) {
            if (!userAnswer.equals("")) {
                mathWorld.checkAnswer(Integer.parseInt(userAnswer), mathSign);
                guessed = true;
                if (mathWorld.correct) {

                    correctCount++;

                    if (correctCount >= 3) {
                        complete = true;
                        System.out.println("Alarm stopped!");
                    } else {
                        // Reset state and generate a new problem


                        random = (int)(Math.random()*2) + 1;
                        switch (random) {
                            case 1:
                                mathSign = "+";
                                break;

                            case 2:
                                mathSign = "-";
                                break;
                        }

                        if (correctCount == 1){
                            argNum = 3;

                        }
                        else if (correctCount == 2){
                            argNum = 4;
                        }

                        mathWorld = new MathWorld(argNum);
                        guessed = false;
                        userAnswer = "";

                    }
                }
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && userAnswer.length() > 0) {
            userAnswer = userAnswer.substring(0, userAnswer.length() - 1);
        }
        if ((Character.getNumericValue(c) >= 0 && Character.getNumericValue(c) <= 9)|| (c == '-')) {
            userAnswer += c;
        }

        if (c == 'n' && guessed && !mathWorld.correct) {
            mathWorld = new MathWorld(argNum);
            guessed = false;
            userAnswer = "";
        }
        if (c == 'c' && guessed && !mathWorld.correct) {
            storeProblem = mathWorld.getRanNumsArray();
            mathWorld = new MathWorld(argNum);
            mathWorld.setNumArray(storeProblem);
            guessed = false;
            userAnswer = "";
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && guessed && mathWorld.correct) {
            System.exit(0);
        }
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
}


