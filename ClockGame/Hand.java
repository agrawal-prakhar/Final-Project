package ClockGame;

import java.awt.*;

public class Hand extends Obj {

    public Hand() {

        this.position = new Pair((GameScreen.peekWidth()/2-20), 0);
        this.velocity = new Pair(0, 100);
        this.acceleration = new Pair(0, 0);
        this.width = 40;
        this.height = 55;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(GameScreen.loadResources.hand, (int) position.x, (int) position.y, (int) width, (int) height, null);
    }

    @Override
    public void verticalTopBounce() {
    }

    @Override
    public void verticalBottomBounce() {
        if (position.y >= 500) {
            velocity.y = 0; //stop if hand reaches height of 490
        }
    }

    @Override
    public void horizontalLBounce() {
    }

    @Override
    public void horizontalRBounce() {
    }

}
