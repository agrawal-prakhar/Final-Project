package ClockGame;

import java.awt.*;

public class Hammer extends Obj {
    private int defaultYVelocity = 300;

    public Hammer(double x, double y, double velocityX) {

        this.position = new Pair(x, y);
        this.velocity = new Pair(0, 0);
        this.acceleration = new Pair(0, 0);
        this.width = 50;
        this.height = 80;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(GameScreen.loadResources.hammer, (int) position.x, (int) position.y, (int) width, (int) height, null);
    }

    @Override
    public void verticalTopBounce() {
        if (position.y + height <= 0) { //put off screen
            position.x = -100;
            position.y = -100;
            velocity.x = 0;
            velocity.y = 0;
        }
    }

    @Override
    public void verticalBottomBounce() {
        if (position.y >= GameScreen.peekHeight()) {
            position.x = -100;
            position.y = -100;
            velocity.x = 0;
            velocity.y = 0;
        }
    }

    @Override
    public void horizontalLBounce() {
        if (position.x + width <= 0) { //put offscreen
            position.x = -100;
            position.y = -100;
            velocity.x = 0;
            velocity.y = 0;
        }
    }

    @Override
    public void horizontalRBounce() {
        if (position.x >= GameScreen.peekWidth()) { //put off screen
            position.x = -100;
            position.y = -100;
            velocity.x = 0;
            velocity.y = 0;
        }
    }

    public int getDefaultYVelocity() {
        return defaultYVelocity;
    }

}
