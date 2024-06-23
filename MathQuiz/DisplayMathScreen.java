package MathQuiz;
import AlarmClock.Alarm;

import javax.swing.*;

public class DisplayMathScreen extends JPanel {
    public MathScreen mathInstance;
    public DisplayMathScreen(int initWidth, int initHeight, int initFPS, JFrame frame, Alarm alarm, int autoAlarmOff) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //need different exit condition??
        mathInstance = new MathScreen(2, initWidth, initHeight, initFPS, alarm, autoAlarmOff);
        frame.setContentPane(mathInstance); //after game is done change content pane???
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        // frame.add(gameInstance);
        frame.setLocationRelativeTo(null);
    }
}
