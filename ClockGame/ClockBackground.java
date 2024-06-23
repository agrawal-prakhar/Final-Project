package ClockGame;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ClockBackground extends Obj{
    private BufferedImage background;
    private static double defaultWidth = 1244;

    public ClockBackground(double x, BufferedImage backgroundName) {
        super();
        background = backgroundName;
        this.width = defaultWidth;
        this.height = 700;
        this.position = new Pair(x, 0); //bottom left
        this.velocity = new Pair(0, 0); //default 0 velocity
        this.acceleration = new Pair(0,0);
    }
    @Override
    public void draw(Graphics g) { //BufferedImage name
        g.drawImage(background, (int)position.x, (int)position.y, (int)width, (int)height, null); //make width slightly larger to make sure there are no cracks between background images
    }

    @Override
    public void verticalTopBounce() {
    }

    @Override
    public void verticalBottomBounce() {
    }

    @Override
    public void horizontalLBounce() {
        if(position.x+width<=0) { //set to back of background reel
            setPosition(new Pair(width*3, 0));
        }
    }

    @Override
    public void horizontalRBounce() {
    }

    public static double peekDefaultWidth() {
        return defaultWidth;
    }


}
