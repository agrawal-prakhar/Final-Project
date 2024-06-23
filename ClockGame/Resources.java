package ClockGame;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Font;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

public class Resources {
    BufferedImage clock;
    BufferedImage clockL;
    BufferedImage clockR;
    BufferedImage clockResting;
    BufferedImage clockFlat;
    BufferedImage clockBlink;
    BufferedImage deadClock;
    BufferedImage tiredClock;
    BufferedImage tiredClock2;
    BufferedImage wingLup;
    BufferedImage wingRup;
    BufferedImage wingLdown;
    BufferedImage wingRdown;
    BufferedImage defaultBackground;
    BufferedImage background1;
    BufferedImage background2;
    BufferedImage background3;
    BufferedImage background4;
    BufferedImage greenTopPipe;
    BufferedImage yellowTopPipe;
    BufferedImage redTopPipe;
    BufferedImage greenBottomPipe;
    BufferedImage yellowBottomPipe;
    BufferedImage redBottomPipe;
    BufferedImage bubbleUp;
    BufferedImage bubbleDown;
    BufferedImage hammer;
    BufferedImage damage;
    BufferedImage collectHammer;
    BufferedImage loseHammer;
    BufferedImage pointsHammer;
    BufferedImage greyHammer;
    BufferedImage skull;
    BufferedImage crack1;
    BufferedImage crack2;
    BufferedImage crack3;
    BufferedImage crack4;
    BufferedImage crack5;
    BufferedImage grass;
    BufferedImage hand;
    BufferedImage getUp;
    BufferedImage morning;
    BufferedImage dust;
    BufferedImage poof;
    BufferedImage smoke;
    BufferedImage wasd;
    Font MonospacedBold;
    Font Monospaced;
    Font SansSerifBoldItalic;
    Font SansSerifBold;
    Color lightBlue;
    Color lightRed;
    Color turquoise;


    public Resources() {
        createImages();
        createFonts();
        lightBlue = new Color(51, 204, 255);
        turquoise = new Color(51, 255, 255);
        lightRed = new Color(255, 70, 100);
    }

    public void createImages() {
        clock();
        background();
        pipes();
        hammer();
        endScreen();
        createStartScreen();
    }

    void createStartScreen() {
        try {
            wasd = ImageIO.read(new File("Res/wasd.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void createFonts() {
        MonospacedBold = new Font("Monospaced", Font.BOLD, 30);
        SansSerifBoldItalic = new Font("SansSerif", Font.BOLD + Font.ITALIC, 30);
        SansSerifBold = new Font("SansSerifBold", Font.PLAIN, 30);
        Monospaced = new Font("Monospaced", Font.PLAIN, 12);
    }

    public void clock() {
        clockImages();
        clockWings();
        clockBubbles();
        clockCracks();
    }

    public void clockCracks() {
        try {
            crack1 = ImageIO.read(new File("Res/crack1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            crack2 = ImageIO.read(new File("Res/crack2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            crack3 = ImageIO.read(new File("Res/crack3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            crack4 = ImageIO.read(new File("Res/crack4.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            crack5 = ImageIO.read(new File("Res/crack5.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            skull = ImageIO.read(new File("Res/skull.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clockImages() {
        try {
            clock = ImageIO.read(new File("Res/clock.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            clockL = ImageIO.read(new File("Res/clockL.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            clockR = ImageIO.read(new File("Res/clockR.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            clockBlink = ImageIO.read(new File("Res/clockBlink.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            tiredClock = ImageIO.read(new File("Res/tiredClock.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            tiredClock2 = ImageIO.read(new File("Res/tiredClock2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            deadClock = ImageIO.read(new File("Res/deadClock.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clockWings() {
        try {
            wingLup = ImageIO.read(new File("Res/wingLup.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            wingRup = ImageIO.read(new File("Res/wingRup.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            wingLdown = ImageIO.read(new File("Res/wingLdown.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            wingRdown = ImageIO.read(new File("Res/wingRdown.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            clockResting = ImageIO.read(new File("Res/clockResting.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            clockFlat = ImageIO.read(new File("Res/clockFlat.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clockBubbles() {
        try {
            bubbleUp = ImageIO.read(new File("Res/bubbleUp.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            bubbleDown = ImageIO.read(new File("Res/bubbleDown.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void background() {
        try {
            defaultBackground = ImageIO.read(new File("Res/defaultBackground.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            background1 = ImageIO.read(new File("Res/background1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            background2 = ImageIO.read(new File("Res/background2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            background3 = ImageIO.read(new File("Res/background3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            background4 = ImageIO.read(new File("Res/background4.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void endScreen() {
        try {
            grass = ImageIO.read(new File("Res/grass.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            hand = ImageIO.read(new File("Res/hand.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            getUp = ImageIO.read(new File("Res/getUp.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            morning = ImageIO.read(new File("Res/morning.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dust = ImageIO.read(new File("Res/dust.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            poof = ImageIO.read(new File("Res/poof.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            smoke = ImageIO.read(new File("Res/smoke.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pipes() {
        upPipes();
        downPipes();
    }

    public void upPipes() {
        try {
            greenTopPipe = ImageIO.read(new File("Res/greenTopPipe.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            yellowTopPipe = ImageIO.read(new File("Res/yellowTopPipe.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            redTopPipe = ImageIO.read(new File("Res/redTopPipe.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downPipes() {
        try {
            greenBottomPipe = ImageIO.read(new File("Res/greenBottomPipe.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            yellowBottomPipe = ImageIO.read(new File("Res/yellowBottomPipe.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            redBottomPipe = ImageIO.read(new File("Res/redBottomPipe.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void hammer() {
        try {
            hammer = ImageIO.read(new File("Res/hammer.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        damage();
        hammerPointIcon();
    }

    public void hammerPointIcon() {
        try {
            collectHammer = ImageIO.read(new File("Res/collectHammer.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            loseHammer = ImageIO.read(new File("Res/loseHammer.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            greyHammer = ImageIO.read(new File("Res/greyHammer.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void damage() {
        try {
            damage = ImageIO.read(new File("Res/damage.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
