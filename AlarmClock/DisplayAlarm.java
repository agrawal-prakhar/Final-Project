package AlarmClock;//package AlarmClock;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.JComboBox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.IOException;
import java.time.*;
import java.time.format.TextStyle;



public class DisplayAlarm extends JPanel {
    public static Color periwinkle = new Color(166, 130, 255);
    public static Color amherstPurple = new Color(113, 90, 255);
    public static Color dustBlue = new Color(60, 165, 255);
    public static Color mediumBlue = new Color(5, 88, 214);
    public static Color darkBlue = new Color(5, 88, 158);
    public static Color darkestBlue = new Color(16, 46, 74);
    public static Color mintGreen = new Color(6, 167, 125);
    public static Color lime = new Color(122, 199, 79);
    public static Color grass = new Color(35, 108, 69);
    public static Color forest = new Color(20, 62, 39);

    private static int WIDTH;
    private static int HEIGHT;
    private static int FPS;
    private static int hammers;
    private static int minute;
    private static int hour;
    private static String digitalHour;
    private static String digitalMinute;
    private static JFrame frame;
    public static JPanel panel;

    private static JButton saveButton;
    private static JLabel currentDay;
    private static JLabel militaryTime;
    private static JLabel digitalClock;
    private static JLabel digitalShadow;
    private static JLabel setLabel;
    private static JLabel setShadow;
    private static JButton setButton;
    private static JTextField hourField;
    private static JTextField minuteField;
    private static LocalTime alarmTime;
    private static String selectGame;
    public static int alarmNumber; //alarm array length starts at 1
    public static List<Alarm> alarmList = Collections.synchronizedList(new ArrayList<>()); //
    private static JComboBox<String> selectComboBox;
    private static final int maxAlarmsPossible = 6;
    public static boolean NotifyAlarms = true;
    private static LocalDate date;
    private static DayOfWeek day;
    private static String dayName;



    public DisplayAlarm(int initWidth, int initHeight, int initFPS, JFrame f, int hammerGoal) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } //standardizes format
        catch (Exception e) {
            e.printStackTrace();
        }
        hammers = hammerGoal;
        WIDTH = initWidth;
        HEIGHT = initHeight;
        FPS = initFPS;
        frame = f;
        panel = new JPanel();
        drawDigitalClock();
        drawSaveButton();
        runClock();
        setButton();
        hourField = new JTextField(2);
        minuteField = new JTextField(2);

        setButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //create new alarm instance
                setAlarm(); //set time and game option for alarm
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAlarms(); //save alarms on save
                System.out.println("save");
            }
        });

        backgroundColors();
        timeField();

        addSetButton();
        gameSelectionBox();
        setFileAlarms(); //set alarms from saved file on screen if applicable

        frame.setContentPane(panel); //add panel from DisplayGameScreen onto JFrame
        frame.setSize(900, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public static void repaintHomePage() {
        panel.repaint();
    }

    private static void backgroundColors() {
        panel.setBackground(Color.BLACK);
        setButton.setForeground(darkestBlue);
    }

    private static void setButton() {
        setButton = new JButton("SET ALARM");
        setButton.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
        panel.add(setButton);
    }

    private static void addSetButton() { //add set button to set alarm
        setButton.setBounds(480, 250, 130, 30);
        setButton.setFont(new Font(Font.MONOSPACED, Font.BOLD, 13));
        panel.add(setButton);
    }

    private static void timeField() { //create text and label to take in time
        setLabel = new JLabel("SET ALARM (HH:MM)"); //setting time on screen
        setLabel.setForeground(lime);
        setLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
        setLabel.setBounds(230, 100, 500, 40);

        setShadow = new JLabel("SET ALARM (HH:MM)"); //setting time on screen
        setShadow.setForeground(darkestBlue);
        setShadow.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
        setShadow.setBounds(234, 100, 500, 40);


        panel.setLayout(null);
        panel.add(setLabel);
        panel.add(setShadow);

        hourField.setBounds(330, 150, 90, 70);
        hourField.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
        panel.add(hourField);

        JLabel colonLabel = new JLabel(":");
        colonLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
        colonLabel.setForeground(Color.WHITE);
        colonLabel.setBounds(425, 160, 20, 40);
        panel.add(colonLabel);

        minuteField.setBounds(450, 150, 90, 70);
        minuteField.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
        panel.add(minuteField);
    }

    public static void getDigitalTime() {
        digitalHour = String.format("%02d",LocalTime.now().getHour());
        digitalMinute = String.format("%02d",LocalTime.now().getMinute());

        date = LocalDate.now();
        day = date.getDayOfWeek();
        dayName = day.getDisplayName(TextStyle.FULL_STANDALONE, Locale.ENGLISH);
    }
    private static void drawDigitalClock() {
        getDigitalTime();
        currentDay = new JLabel(dayName); //setting time on screen
        currentDay.setForeground(dustBlue);
        currentDay.setBounds(760, 2, 500, 30);
        currentDay.setFont(new Font(Font.MONOSPACED, Font.BOLD, 13));

        digitalShadow = new JLabel(digitalHour + ":" + digitalMinute); //setting time on screen
        digitalShadow.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
        digitalShadow.setForeground(darkestBlue);
        digitalShadow.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 30));
        digitalShadow.setBounds(754, 25, 300, 40);

        digitalClock = new JLabel(digitalHour + ":" + digitalMinute); //setting time on screen
        digitalClock.setForeground(dustBlue);
        digitalClock.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 30));
        digitalClock.setBounds(750, 25, 300, 40);

        militaryTime = new JLabel("military time");
        militaryTime.setForeground(dustBlue);
        militaryTime.setBounds(757, 50, 500, 30);
        militaryTime.setFont(new Font(Font.MONOSPACED, Font.BOLD, 10));

        panel.add(currentDay); //add current time labels to screen
        panel.add(digitalClock);
        panel.add(digitalShadow);
        panel.add(militaryTime);
    }

    private static void updateDigitalClock() {
        getDigitalTime();
        currentDay.setText(dayName);
        digitalShadow.setText(digitalHour + ":" + digitalMinute); //current setting time on screen
        digitalClock.setText(digitalHour + ":" + digitalMinute); //current setting time on screen
        panel.repaint();
    }

    private static void drawSaveButton() {
        saveButton = new JButton("SAVE"); //create save button
        saveButton.setFont(Alarm.buttonFont);
        saveButton.setFocusPainted(false);
        saveButton.setBounds(30, 15, 100, 30);
        saveButton.setBackground(dustBlue);
        saveButton.setForeground(darkestBlue);
        panel.add(saveButton);
    }


    public static void runClock() {
        new Thread(new Runnable() { //create runner that starts when class starts to update time and count down
            public void run() {

                    while (true) {
                        try {
                            if (alarmList.size() > 0) {
                                updateAlarmCountDowns(); //update count-downs for every alarm if list has at least 1 alarm
                            }
                            updateDigitalClock();//update time on digital clock
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } //while program is open, try

            } //run

        }).start();
    }

    private static void gameSelectionBox() { //create game selection drop down menu
        String[] gameOptions = {"Choose Method", "Math", "Flappy", "Wordle"};
        selectComboBox = new JComboBox<>(gameOptions);
        selectComboBox.setBounds(295, 250, 180, 30);
        panel.add(selectComboBox);
    }

    private void setAlarm() { //starts when alarm is set
        try {
                hour = Integer.parseInt(hourField.getText());
                minute = Integer.parseInt(minuteField.getText());
                if (hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59) { //if selected time is within possible times and an int
                    selectGame = (String) selectComboBox.getSelectedItem();
                    if (!selectGame.contains("Choose Method")) {
                        alarmTime = LocalTime.of(hour, minute);
                        if (!isAlarmAlreadySet(alarmTime)) {
                            if (alarmList.size() < maxAlarmsPossible) { //if the size is less than the maximum number of alarms that can fit on page
                                System.out.println("Alarm set for " + alarmTime.format(DateTimeFormatter.ofPattern("HH:mm")));
                                Alarm alarm = new Alarm(WIDTH, HEIGHT, FPS, frame, panel, hammers, selectGame, alarmTime); //create new alarm
                                alarmList.add(alarm); //add new alarm to linked list
                                alarm.makeButtons(); //make buttons for new alarms
                                System.out.println(alarmList.size());
                            } else {
                                JOptionPane.showMessageDialog(frame, "Maximum alarms reached. Delete an alarm to create a new alarm.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "An alarm is already set for this time.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Select a valid game.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid input. Please enter valid time values.");
                }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please enter numeric values.");
        }
    }

    public static void deleteAlarm(int alarmIndex) { //synchronized to make sure there is no interference between redrawing of variables related to the alarms and the deletion method of the alarms in thread
        synchronized(alarmList) {
            if (alarmList.size() > 0) {
                alarmList.remove(alarmIndex); //remove alarm from list according to its index
                System.out.println("alarm removed");
                redrawAlarmList();
            }
        }
    }

    public static int getAlarmListIndex(Alarm alarm) { //gets index of alarm in list
        alarmNumber = alarmList.indexOf(alarm);
        return alarmNumber;
    }

    private static void redrawAlarmList() { //goes through alarm list once an alarm is deleted and redraws remaining alarms
        synchronized(alarmList) { //synchronized to make sure there is no interference between redrawing of alarms and deletion of alarms in thread
            for (Alarm a : alarmList) { //iterate through list to redraw
                a.setDeleteBounds();
                a.setAlarmIconsBounds();
                a.setOnOffBounds();
                a.setGameLevelsBounds();
                a.setGameButton();
            }
        }
    }

    private boolean isAlarmAlreadySet(LocalTime newAlarmTime) { //????????? ERROR: 0 out of bounds for length 0 ????
        for(Alarm a: alarmList) { //iterate through list to redraw count down for each alarm
            if(a.getAlarmTime().getHour() == newAlarmTime.getHour() && a.getAlarmTime().getMinute() == newAlarmTime.getMinute()) {
                return true;
            }
        }
        return false;
    }

    private static void updateAlarmCountDowns() { //updaye from beginning of array list to one less than array list size
        synchronized(alarmList) { //synchronized to make sure there is no interference between redrawing of count down times and deletion in thread
            for (Alarm a : alarmList) { //iterate through list to redraw count down for each alarm
                if (a.didCountDownStart()) { //if count down button has been created
                    a.resetCountDown();
                }
            }
        }
    }


    public static List<Alarm> getAlarmList() {
        return alarmList;
    }

    private void setFileAlarms() { //set alarms from saved file
        NotifyAlarms = false;
        synchronized(alarmList) { //synchronized to make sure there is no interference between creation of alarms and next updates
            try {
                File savedFile = new File("res/savedAlarms.txt");
                Scanner readSaved = new Scanner(savedFile);
                while (readSaved.hasNext()) { //while there are still lines for new alarms to be created
                    int level = Alarm.setLevels(readSaved.nextLine().trim()); // 1. a) get correct level int according to saved string input
                    String gameType = readSaved.nextLine().trim(); // 2. get game type string from next line
                    LocalTime savedAlarmTime = Alarm.setAlarmTime(readSaved.nextLine().trim()); // 3. get local time from saved alarm time string (saved alarm time string found by next line)

                    Alarm prevSavedAlarm = new Alarm(WIDTH, HEIGHT, FPS, frame, panel, level, gameType, savedAlarmTime); //create alarm with saved values
                    alarmList.add(prevSavedAlarm); //add saved alarms to linked list
                    prevSavedAlarm.setAlarmSpacing(prevSavedAlarm, alarmList);
                    prevSavedAlarm.makeButtons(); //make buttons for previously saved alarms before editing game level drop down

                    prevSavedAlarm.resetScreenLevels(Integer.toString(level));// 1. b) reset level on screen
                    prevSavedAlarm.setAlarmName(readSaved.nextLine().trim());//4. set alarm name from next line
                    prevSavedAlarm.setOnOff(readSaved.nextLine().trim()); //5. set on/off according to next line

                } //until end of file, read
                readSaved.close();
            } catch (FileNotFoundException e) {
                System.out.println("No alarms saved in file");
                e.printStackTrace();
            }
        }
        NotifyAlarms = true;
    }

    private void saveAlarms() { //save currently existing alarms and their elements to a file
        File savedFile = new File("res/savedAlarms.txt");
        checkTXT(savedFile);
        try{ //clear existing file so saved alarms are adding to clear file
            FileWriter clearAlarms = new FileWriter(savedFile, false);
            clearAlarms.write("");
            clearAlarms.close();
        } catch(IOException i) {
            i.printStackTrace();
        }

        try(FileWriter saveAlarms = new FileWriter(savedFile, true); //for alarmlist, append list with string of alarm values
            BufferedWriter buffer = new BufferedWriter(saveAlarms); PrintWriter p = new PrintWriter(buffer);) { //buffered writer closes file after wrapped completes
            for (Alarm a : alarmList) { //go down list and save all the alarm values as a string in the same order alarms will be edited in
                p.println(a.saveLevelString()); // 1. save level from every alarm on list
                p.println(a.saveGameString()); // 2. save game selection from every alarm on list
                p.println(a.saveAlarmTimeString()); // 3. save alarm time from every alarm on list
                p.println(a.saveAlarmNameString()); // 4. save alarm name from every alarm on list
                p.println(a.saveOnOffString()); //5. save on/off position from every alarm on list
            }
            //saveAlarms.close();
        } catch(IOException i) {
            i.printStackTrace();
            System.out.println("invalid file, please try again");
        }
    }

    public boolean checkTXT(File file) { //throw exception of file is not a .txt file
        String fileName = file.getName();
        boolean extension = fileName.endsWith(".txt");
        if (!extension) {
            System.out.println("Incorrect file type. Must submit.txt file");
            return false;
        }
        return extension;
    } //throw exception if not supplied .txt file for file

}
