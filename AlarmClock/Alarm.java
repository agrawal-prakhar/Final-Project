package AlarmClock;//package AlarmClock;
import MathQuiz.DisplayMathScreen;
import ClockGame.DisplayGameScreen;
import Wordle.DisplayWordleScreen;
import Timers.Timer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.sound.sampled.*;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Alarm {
    public static Font buttonFont = new Font(Font.MONOSPACED, Font.BOLD, 13);
    private static Alarm currentAlarm = null; //holds location for alarm in progress - static bc class should only have one alarm go off at once so should change for all instances of alarm
    private File soundFile;
    private JPanel panel;
    private JButton onOffButton;
    private JButton deleteButton;
    private JButton alarmTimeButton;
    private JTextField alarmName = new JTextField(1);;
    private JButton alarmCountDownButton;
    private JButton gameButton;
    private boolean alarmOn = false;
    private static boolean gameActive = false; //if game is active and another alarm goes off, ignore repeat alarm
    public LocalTime alarmTime; //able to be referenced by instance outside of class
    private LocalTime currentTime;
    public int notifiedCount = 0;
    private Clip clip;
    private String gameSelection;
    private DisplayGameScreen flappyGame;
    private DisplayMathScreen mathGame;
    private DisplayWordleScreen wordleGame;
    private JComboBox<String> gameLevels;
    private JFrame frame;
    private int FPS;
    private int WIDTH;
    private int HEIGHT;
    private int hammerNum;
    private int defaultHammerNum;
    private int autoAlarmOff = 600; //alarm will automatically shut off after 10 minutes
    private Timer safetyOff;
    private String difficultyLevel = null;
    private boolean clickedTurnOff = false;
    private int hoursBetween;
    private int minutesBetween;
    private int secondsBetween;
    private String countDown;
    private String name;
    private int mouseX;
    private int mouseY;
    public boolean countDownStarted = false;
    private int alarmSpacing;

    public Alarm(int initWidth, int initHeight, int initFPS, JFrame f, JPanel p, int hammers, String gameType, LocalTime setTime) {
        System.out.println("new alarm reached");
        frame = f;
        FPS = initFPS;
        WIDTH = initWidth;
        HEIGHT = initHeight;
        defaultHammerNum = hammers;
        hammerNum = defaultHammerNum; //set hammer number to default so if alarm goes off at the same exact time as set was clicked, the game has this loaded ahead of time if user does not have a chance to select
        panel = p;
        alarmTime = setTime;
        gameSelection = gameType;
    }

   // public Alarm (String time, String )

    public void makeButtons(){
        createAlarmIcons();
        createOnOffButton();
        createGameButton();
        manageAlarm(); //when alarm first created, make sure alarm starts as on when time first set
        createDeleteButton();
        createGameLevels();


        onOffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manageAlarm();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delete();
            }
        });

        alarmName.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                nameFocusGained(e);
            }

            @Override
            public void focusLost(FocusEvent e) {
                nameFocusLost(e);
            }
        });

        alarmName.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                //char c = e.getKeyChar();
                nameKeyPressed(e);
            }
            @Override
            public void keyReleased(KeyEvent e) {
                char c = e.getKeyChar();
            }

            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
            }

        });
//????????????????????????????????????????????
        alarmName.addMouseListener(new java.awt.event.MouseAdapter() { //is user clicks on screen, pointer sets //HOW TO GET FOCUS TO RECOGNIZE IT IS LOST IF CLICKED ANYWHERE OUTSIDE OF BOX, NOT JUT IF DIFFERENT JLABEL/BUTTON PRESSED
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY(); {
                    if(e.getX()<230|| e.getY()>340) { //if outside of jtext x bounds
                        if(e.getY()< (290 + ( (DisplayAlarm.getAlarmList().indexOf(Alarm.this)*30) + ( 35*(DisplayAlarm.getAlarmList().indexOf(Alarm.this))))) || e.getY()> (290 + ( (DisplayAlarm.getAlarmList().indexOf(Alarm.this)*30) + ( 35*(DisplayAlarm.getAlarmList().indexOf(Alarm.this)) ) )+30)) { //if outside of jtext y bounds
                            //alarmName.setFocusable(false);
                        }
                    }
                }
            }
        });



        setOnOffBounds();
        setDeleteBounds();
        setAlarmIconsBounds();
        setGameLevelsBounds();
        setGameButton();
        panel.add(alarmCountDownButton);
        panel.add(alarmName);
        panel.add(alarmTimeButton);
        panel.add(onOffButton);
        panel.add(deleteButton);
        panel.add(gameButton);
//        frame.setSize(900, 700);          //XXXXXXXXXXXX            THIS GIVES ERROR ABOUT ADDING PARENT TO ITSELF
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        frame.add(panel);
//        frame.setVisible(true);
//        frame.repaint();
    }

    private int alarmSpacing() { //vertical spacing between the alarm objects
        alarmSpacing = ( (DisplayAlarm.getAlarmList().indexOf(Alarm.this)*30) + ( 30*(DisplayAlarm.getAlarmList().indexOf(Alarm.this)) ) );
        return alarmSpacing;
    }
    public void setAlarmSpacing(Alarm alarm, List<Alarm> alarmList) { //set saved alarm spacing for alarms created in Display Alarm
        alarmSpacing = ( (alarmList.indexOf(alarm)*30) + ( 30*(alarmList.indexOf(alarm)) ) );
    }

    private void nameFocusGained(java.awt.event.FocusEvent e) { //if focus went to alarmName jtext box
        name = alarmName.getText();
        if(name.equals("Alarm Name")) {
            alarmName.setCaretPosition(0);
        }
    }

    private void nameFocusLost(java.awt.event.FocusEvent e) { //if focus was lost from jtext box (happens if user clocks another jbutton or jlabel)
        name = alarmName.getText();
        if(name.equals("")||name.equals("Alarm Name")) {
            alarmName.setBackground(new JButton().getBackground());
            alarmName.setForeground(new JButton().getForeground());
            alarmName.setText("Alarm Name");
        }
        else {
            alarmName.setBackground(DisplayAlarm.lime);
            alarmName.setForeground(DisplayAlarm.darkestBlue);
        }
            alarmName.setCaretPosition(0);
    }

    private void nameKeyPressed(java.awt.event.KeyEvent e) { //if key typed
        name = alarmName.getText();
        if(name.equals("Alarm Name")) {
            alarmName.setForeground(DisplayAlarm.darkestBlue);
            alarmName.setText(null);
            alarmName.setCaretPosition(0);
        }
    }
    private void createAlarmIcons() {
        createName(); // create jtext for alarm name

        createAlarmTimeButton(); //create button for set alarm time

        getCountDown(); //get count down to alarm time
        createCountDownButton(); //create button for count down
    }

    private void createName() {
        alarmName.setText("Alarm Name");
        alarmName.setBackground(new JButton().getBackground());
        alarmName.setForeground(DisplayAlarm.lime);
        alarmName.setFont(buttonFont);
    }


    private void createAlarmTimeButton() {
        alarmTimeButton = new JButton(""+alarmTime.format(DateTimeFormatter.ofPattern("HH:mm")));
        alarmTimeButton.setBackground(DisplayAlarm.dustBlue);
        alarmTimeButton.setForeground(DisplayAlarm.darkestBlue);
        alarmTimeButton.setFont(buttonFont);
        alarmTimeButton.setFocusPainted(false);
    }

    private void getCountDown() { //get count down
        if(LocalTime.now().isAfter(alarmTime)) { //if current time is after alarm time in military time
            hoursBetween = 24 - (int) (ChronoUnit.HOURS.between(alarmTime,LocalTime.now()) % 24);
            minutesBetween = 60 - (int)(ChronoUnit.MINUTES.between(alarmTime,LocalTime.now()) % 60);
            secondsBetween = 60 - (int)(ChronoUnit.SECONDS.between(alarmTime,LocalTime.now()) % 60);
        }
        else { //if alarm time is after the current time, switch elements to make positive
            hoursBetween = (int) (ChronoUnit.HOURS.between(LocalTime.now(),alarmTime) % 24);
            minutesBetween = (int)(ChronoUnit.MINUTES.between(LocalTime.now(),alarmTime) % 60);
            secondsBetween = (int)(ChronoUnit.SECONDS.between(LocalTime.now(),alarmTime) % 60);
        }
        countDown = "" + hoursBetween + " : " + minutesBetween + " : " + secondsBetween;
    }

    private void createCountDownButton() {
        alarmCountDownButton = new JButton(countDown);
        alarmCountDownButton.setOpaque(false);
        alarmCountDownButton.setContentAreaFilled(false);
        alarmCountDownButton.setBorderPainted(false);
        alarmCountDownButton.setForeground(DisplayAlarm.lime);
        alarmCountDownButton.setFont(buttonFont);
        alarmCountDownButton.setFocusPainted(false);
        countDownStarted = true;
    }

    public void resetCountDown() { //update count down
        getCountDown();
        alarmCountDownButton.setText(countDown);
        panel.repaint();
    }

    public boolean didCountDownStart() {
        return countDownStarted;
    }

    private void createOnOffButton() { //create on/off button
        onOffButton = new JButton("TURN ON");
        onOffButton.setFont(buttonFont);
        onOffButton.setFocusPainted(false);
    }

    private void createDeleteButton() { //create delete button
        deleteButton = new JButton("Delete");
        deleteButton.setFont(buttonFont);
        deleteButton.setForeground(new Color(255, 173, 214));
        deleteButton.setBackground(new Color(136, 0, 6));
        deleteButton.setFocusPainted(false);
    }

    private void createGameButton() { //create button for type of game
        gameButton = new JButton(gameSelection);
        gameButton.setFont(buttonFont);
        gameButton.setBackground(DisplayAlarm.dustBlue);
        gameButton.setForeground(DisplayAlarm.darkestBlue);
        gameButton.setFocusPainted(false);
    }

    public void setAlarmIconsBounds() { //set format for alarm name, alarm time, and count-down
        setNameBounds();
        setAlarmTimeBounds();
        setAlarmCountDownBounds();
    }

    private void setNameBounds() { //set format of name button
        alarmName.setBounds(95, 290 + alarmSpacing(), 100, 30);
        alarmName.setFont(buttonFont);
    }
    private void setAlarmTimeBounds() {
        alarmTimeButton.setBounds(205, 290 + alarmSpacing(), 100, 30);
        alarmTimeButton.setFont(buttonFont);
    }

    private void setAlarmCountDownBounds() {
            alarmCountDownButton.setBounds(178, 315 + alarmSpacing(), 150, 30);
            alarmCountDownButton.setFont(buttonFont);
    }
    public void setOnOffBounds() { //set format for on/off buttons
        onOffButton.setBounds(425, 290 + alarmSpacing(), 100, 30);
        onOffButton.setFont(buttonFont);
    }

    public void setDeleteBounds() { //set format for delete buttons
        deleteButton.setBounds(535, 290 + alarmSpacing(), 100, 30);
        deleteButton.setFont(buttonFont);
    }

    public void setGameButton() { //set format for chosen game button
        gameButton.setBounds(315, 290 + alarmSpacing(), 100, 30);
        gameButton.setFont(buttonFont);
    }

    private void delete() { //remove buttons from panel
        panel.remove(deleteButton); //delete jbuttons for this alarm
        panel.remove(onOffButton);
        panel.remove(alarmTimeButton);
        panel.remove(alarmCountDownButton);
        panel.remove(alarmName);
        panel.remove(gameButton);
        if(gameLevels!=null) { //if alarm had a game with levels,
            panel.remove(gameLevels); //remove gamelevel dropdown
        }
        panel.repaint();
        panel.revalidate();
        DisplayAlarm.deleteAlarm(DisplayAlarm.getAlarmList().indexOf(Alarm.this)); //delete alarm from arrayList and redraw non-deleted alarms at correct index height
        System.out.println("alarm deleted");
    }

    private void createGameLevels() { //create drop down for difficulty levels
        switch (gameSelection) {
            case "Wordle", "Math":
                String [] setOption = {"Difficulty Fixed"};
                gameLevels = new JComboBox<>(setOption);
                panel.add(gameLevels);
                break;
            case "Flappy":
                String[] hammerOptions = {"Default Difficulty", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
                gameLevels = new JComboBox<>(hammerOptions);
                panel.add(gameLevels);
                break;
        }
        gameLevels.setBackground(DisplayAlarm.lime);
        gameLevels.setForeground(DisplayAlarm.darkestBlue);
    }

    public void setGameLevelsBounds() { //set format for difficulty level drop down
        if(gameLevels!=null) {
            gameLevels.setBounds(645, 290 + alarmSpacing(), 150, 30);
        }
    }

    private void checkGameLevels() { //check game options repeatedly to see what is selected for game level and if it changes
        if(gameLevels!=null) {
            switch (gameSelection) {
                case "Wordle", "Math": difficultyLevel = "Difficulty Fixed";
                    break;
                case "Flappy":
                    difficultyLevel = String.valueOf(gameLevels.getSelectedItem());
                    if (difficultyLevel.equals("Default Difficulty")||difficultyLevel.equals("")||difficultyLevel.equals(null)) { //if flappy game has been selected and there is default selected, empty selected, or null for difficulty string (in case alarm starts at same time as it is being created)
                        hammerNum = defaultHammerNum;//if not selected, use default
                    } else {
                        hammerNum = Integer.valueOf((String) gameLevels.getSelectedItem());
                    }
                    break;
            }
        }
    }

    private void notifyOptionsNonSelect() {//remind to select hammer difficulty if not yet selected by time alarm is turned on
        if(DisplayAlarm.NotifyAlarms) { //if ability to notify alarms is true (don't want to notify when DisplayAlarm is loading saved list)
            if (gameSelection.equals("Flappy")) {
                if(alarmOn) {
                    if (!clickedTurnOff) { //if never clicked off before,
                        JOptionPane.showMessageDialog(frame, "Alarm added! Choose difficulty level for game");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Are you sure about this difficulty?");
                    }
                }
            }
        }
    }

    public void selectGame(int w, int h, int fps, int hammers, JFrame frame) { //select game to play from when alarm was set
        System.out.println("game reached");
        switch (gameSelection) {
            case "Wordle": wordleGame = new DisplayWordleScreen(w, h, fps, frame, Alarm.this, autoAlarmOff);
                break;//Wordle = new Wordle object
            case "Flappy":
                    flappyGame = new DisplayGameScreen(w, h, fps, hammers, frame, Alarm.this, autoAlarmOff);
                break;
            case "Math":
                mathGame = new DisplayMathScreen(w, h, fps, frame, Alarm.this, autoAlarmOff);
                break; //MathGame = new math object
        }

    }

    private void manageAlarm() { //change alarm to on or off
        if (alarmTime != null) {
            alarmOn = !alarmOn;
            if (alarmOn) {
                checkGameLevels();
                notifyOptionsNonSelect();
                onOffButton.setForeground(DisplayAlarm.lime);
                onOffButton.setBackground(DisplayAlarm.forest); //alarm button turns dark purple if on

                if(!clickedTurnOff) { //if the alarm has never been shut off
                    onOffButton.setText("TURN OFF"); //display text in caps if never clicked yet
                    startAlarm();
                }
                else { //else if this user is turning the alarm that was previously turned off back on
                    onOffButton.setText("turn off"); //if alarm has been clicked before, display in lowercase
                    startAlarm();
                }
            } else {
                clickedTurnOff = true; //change to true the first time the alarm is set to off
                onOffButton.setForeground(new JButton().getForeground()); //return to default color scheme
                onOffButton.setBackground(new JButton().getBackground());
                onOffButton.setText("turn on"); //displays option to turn alarm on
            }
        } else {
            JOptionPane.showMessageDialog(panel, "Please set the alarm first."); //if no time has been entered for the alarm, notify user
        }
    }

    private void chooseSound() {
        soundFile = new File("AlarmClock/sound/alarm.wav");
    } //load alarm sound

    private void startAlarm() {
        new Thread(new Runnable() {

            public void run() {
                    try {
                        chooseSound();
                        AudioInputStream inputStream = AudioSystem.getAudioInputStream((soundFile));
                        clip = AudioSystem.getClip();
                        while (alarmOn) {
                            if (safetyOff == null) {
                                safetyOff = new Timer();
                            }
                            if (notifiedCount < 1) { //if alarm has not yet notified user during alarm notification time
                                LocalTime currentTime = LocalTime.now();
                                checkGameLevels(); //check what level was selected for game
                                if (currentTime.getHour() == alarmTime.getHour() && currentTime.getMinute() == alarmTime.getMinute() && alarmOn) {
                                    if (currentAlarm == null) { //if no active game, current alarm is this alarm
                                        currentAlarm = Alarm.this;
                                    }
                                    if (currentAlarm == Alarm.this) { //if current alarm going off is the first to reach currentAlarm
                                        if (safetyOff.getElapsedTime() >= autoAlarmOff) { //if timer reached auto shut off time
                                            stopAlarm();
                                        }
                                        notifiedCount += 1;
                                        clip.open(inputStream);
                                        clip.loop(Clip.LOOP_CONTINUOUSLY);
                                        SwingUtilities.invokeLater(new Runnable() {
                                            public void run() {
                                                int option = JOptionPane.showOptionDialog(
                                                        panel,
                                                        "Wake Up!",
                                                        gameSelection,
                                                        JOptionPane.DEFAULT_OPTION,
                                                        JOptionPane.INFORMATION_MESSAGE,
                                                        null,
                                                        new Object[]{"Play to Stop!"},
                                                        "Play to Stop!");
                                                if (option == 0) {
                                                    selectGame(WIDTH, HEIGHT, FPS, hammerNum, frame);
                                                } else {
                                                    selectGame(WIDTH, HEIGHT, FPS, hammerNum, frame);
                                                }
                                            }
                                        }); //Runnable
                                    }//if another alarm is active
                                } //if alarm is on and time is reached

//                            Thread.sleep(10000); // looping as long as this thread is alive

                            }//notified
                        } //while alarm on is true
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }//run()
        }).start();
        notifiedCount = 0; //reset notified count after alarm is done
    }

    public void stopAlarm() {
        System.out.println("stoppp");
        clip.stop(); //stop sound
        clip.close(); //close sound
        alarmOn = false; //alarm is turned off
        currentAlarm = null; //current alarm in progress returned to null

        switch (gameSelection) { //for each game, set the previous content pane false and reset content pane to the panel in the instance of Display Alarm in AlarmApp
            case "Wordle":
                wordleGame.wordleInstance.setVisible(false);frame.setContentPane(DisplayAlarm.panel); DisplayAlarm.repaintHomePage();
                break;
            case "Flappy":
                flappyGame.gameInstance.setVisible(false); frame.setContentPane(DisplayAlarm.panel); DisplayAlarm.repaintHomePage();
                break;
            case "Math":
                mathGame.mathInstance.setVisible(false); frame.setContentPane(DisplayAlarm.panel); DisplayAlarm.repaintHomePage();
                break; //MathGame = new math object
        }
        DisplayAlarm.repaintHomePage();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //unlock screen to allow exit

    }
    public void stopSoundOnly() {
        clip.stop(); //stop sound
        clip.close(); //close sound
    }


    public LocalTime getAlarmTime() {
        return alarmTime;
    }


    public String saveAlarmNameString() { //return string of alarm name that user set
        if(name!=null) {
            return name;
        }
        else return "Alarm Name";
    }

    public void setAlarmName(String nameString) {
        if(!nameString.equals("Alarm Name") && !nameString.equals("")) {
            name = nameString; //make sure alarm's current String name changes to saved
            alarmName.setBackground(DisplayAlarm.lime);
            alarmName.setForeground(DisplayAlarm.darkestBlue);
            alarmName.setText(nameString); //set alarm name text in box to saved name
        } else {
            alarmName.setBackground(new JButton().getBackground());
            alarmName.setForeground(new JButton().getForeground());
            alarmName.setText("Alarm Name"); //set alarm name text in box to saved name
        }
        alarmName.setCaretPosition(0);
    }

    public String saveAlarmTimeString() { //return string of alarm time that user set
        String alarmTimeString = alarmTime.toString(); //get string version of alarm time (from local time)
        return alarmTimeString;
    }

    public static LocalTime setAlarmTime(String stringAlarmTime) {
        LocalTime alarmTime = LocalTime.parse(stringAlarmTime); //return local time of string
        return alarmTime;
    }

    public String saveGameString() { //return string of game that user set
        String gameString = gameSelection;
        return gameString;
    }

    public String saveLevelString() {
        if(gameLevels!=null) {
            difficultyLevel = String.valueOf(gameLevels.getSelectedItem());
            return difficultyLevel;
        }
        else return "NONE";
    }

    public static int setLevels(String level) {
        if(level.equals("NONE")||level.equals("Default Difficulty")||level.equals("Difficulty Fixed")) { //if level was nothing when saved, change nothing
            return 3; //if no level selected, return default value of 3
        } else { //else if there is level present, set level in game
            return Integer.parseInt(level); //return int from string difficulty level to be used as arg for new alarm
        }
    }

    public void resetScreenLevels(String level) {
        if(gameLevels!=null) { //if the game has game levels, reset
            gameLevels.setSelectedItem(level);
            difficultyLevel = level;
        }
    }

    public String saveOnOffString() { //return string of on if on, or off if alarm is off
        resetClickedOffBoolean(); //reset boolean that checks whether off button has been clicked before to false
        if(alarmOn) {
            return "ON";
        }
        else return "OFF";
    }

    public void setOnOff(String onOffString) {
        if(onOffString.equals("OFF")) { //if alarm was off, manage the alarm to turn off (alarm starts as on by default when first created, managing switches on/off to opposite)
            manageAlarm();
        }
        else { } //else if alarm says on, start alarm as on by doing no changes - alarm is on by default when created
    }

    public void resetClickedOffBoolean() {
        clickedTurnOff = false;
    }




}
