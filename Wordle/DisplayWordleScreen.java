package Wordle;
import AlarmClock.Alarm;
import javax.swing.*;

public class DisplayWordleScreen {
    public WordleScreen wordleInstance;
    JFrame frame;
    public DisplayWordleScreen (int initWidth, int initHeight, int initFPS, JFrame frame, Alarm alarm, int autoAlarmOff){
        this.frame = frame;
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        //creates a wordle instance
        wordleInstance = new WordleScreen(initWidth, initHeight, initFPS, alarm, autoAlarmOff, frame);
        frame.setContentPane(wordleInstance);
        frame.pack();
        frame.setVisible(true);
        //frame.repaint();
    }


}
