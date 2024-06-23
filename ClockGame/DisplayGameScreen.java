package ClockGame;
import AlarmClock.Alarm;
import javax.swing.*;

public class DisplayGameScreen {
    public GameScreen gameInstance;
    //public JFrame frame;

    public DisplayGameScreen(int initWidth, int initHeight, int initFPS, int hammerGoal, JFrame frame, Alarm alarm, int autoAlarmOff) {
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        gameInstance = new GameScreen(initWidth, initHeight, initFPS, hammerGoal, this, alarm, autoAlarmOff);
        frame.setContentPane(gameInstance); //after game is done change content pane???
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

}
