import AlarmClock.AlarmApp;
import javax.swing.*;

public class Main {
    static private JFrame frame;
    static private AlarmApp alarmPage;


    public static void main(String[] args) {
            JFrame frame = new JFrame("Alarm Clock");
            alarmPage = new AlarmApp(frame);
    }
}