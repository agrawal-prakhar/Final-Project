package ClockGame;

import java.awt.*;

public abstract class Obj {
    Pair position;
    Pair velocity;
    Pair acceleration;
    double height;
    double width;

    public Obj() {
    }

    public abstract void draw(Graphics g);

    public abstract void verticalTopBounce(); //different bounds for every object

    public abstract void verticalBottomBounce();

    public abstract void horizontalLBounce();

    public abstract void horizontalRBounce();

    public void bounce() {
        verticalTopBounce();
        verticalBottomBounce();
        horizontalLBounce();
        horizontalRBounce();
    }

    public void update(double time) {
        bounce();
        position = position.add(velocity.times(time));
        velocity = velocity.add(acceleration.times(time));
    }


    public Pair peekPosition() {
        return position;
    }

    public void setPosition(Pair xyz) {
        position = xyz;
    }

    public void setVelocity(Pair xyz) {
        velocity = xyz;
    }

}
