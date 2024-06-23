package AlarmClock;
import AlarmClock.DisplayAlarm;

import javax.swing.*;

public class AlarmApp {
    private static final int WIDTH = 900;
    private static final int HEIGHT = 700;
    private static final int FPS = 60;
    private static int hammerGoal = 3; //default hammerGoal
    private volatile static boolean gameOver = false; //optimize visibility in memory
    private static int highScore = 0;
    private int numAlarms = 0;
    private static DisplayAlarm alarmApp;

    public AlarmApp(JFrame frame) {
        alarmHomePage(frame);
    }

    public static void alarmHomePage(JFrame frame) {
        alarmApp = new DisplayAlarm(WIDTH, HEIGHT, FPS, frame, hammerGoal);
    }

}
