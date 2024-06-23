package ClockGame;

import Timers.Timer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePlayer extends Obj {
    private BufferedImage[] wingDirection = new BufferedImage[2];
    private BufferedImage otherClock;
    private BufferedImage screenClock;
    private static int wingWidth = 30;
    private static int wingHeight = 25;
    private static int bubbleWidth = 50;
    private static int bubbleHeight = 25;
    private Timer bubbleEffects;
    private Timer characterEffects;
    private Timer skullDisplay;
    private static double startingPosition = (1.25 * wingWidth);
    private DisplayGameScreen game;
    private boolean died = false;
    private int prevSkullDisplayTime;

    public GamePlayer(DisplayGameScreen initGame) {
        super();
        game = initGame;
        this.width = 40;
        this.height = 55;
        this.position = new Pair(startingPosition, (GameScreen.peekHeight() / 2) - (height / 2)); //bottom left
        this.velocity = new Pair(0, 0); //default 0 velocity
        this.acceleration = new Pair(0, 0);
        bubbleEffects = new Timer(); //timer for bubbles
        characterEffects = new Timer(); //timer for size changes
        skullDisplay = new Timer();
    }

    @Override
    public void draw(Graphics g) { //BufferedImage name
        //ADD DRAW METHOD FOR CHARACTER HERE
        bubbleEffects.frameInterval(10);
        characterEffects.frameInterval(25);
        drawClock(g);
        drawWings(g);
        resizePlayer();
        drawBubbles(g);
        if (died) {
            drawSkull(g);
        }
    }

    @Override
    public void verticalTopBounce() {
        if (position.y <= 0) {
            died = true;
            resetPlayer(); //reset all characteristics of player (speed, position)
            game.gameInstance.gameWorld.resetPipes(); //reset pipes to last position before player died and reset pipe speed to default
            game.gameInstance.gameWorld.resetHammer(); //reset hammer to outside of visible screen
            game.gameInstance.gameWorld.setDamageTotal(0); //player loses all damage progress (represented by the hammers collected)
            game.gameInstance.gameWorld.pipesPassed = 0; //consecutive number count for # pipes passed reset to 0
        }
    }

    @Override
    public void verticalBottomBounce() {
        if (position.y + height >= GameScreen.peekHeight()) {
            died = true;
            resetPlayer();
            game.gameInstance.gameWorld.resetPipes();
            game.gameInstance.gameWorld.resetHammer();
            game.gameInstance.gameWorld.setDamageTotal(0);
            game.gameInstance.gameWorld.pipesPassed = 0; //consecutive number count for # pipes passed reset to 0
        }
    }

    @Override
    public void horizontalLBounce() { //player is not affected if they touch the horizontal bounds. They won't die, but they cannot go offscreen
        if (position.x <= 0) {
            setPosition(new Pair(width, peekPosition().y));
        }
    }

    @Override
    public void horizontalRBounce() {
        if (position.x >= GameScreen.peekWidth() - width) {
            setPosition(new Pair(GameScreen.peekWidth() - width, peekPosition().y));//if hit right of screen, fall back in screen
        }
    }

    void resetPlayer() { //resets player position and velocity to starting
        setPosition(new Pair(startingPosition, (GameScreen.peekHeight() / 2) - (height / 2)));
        setVelocity(new Pair(0, -200));
    }

    private void drawClock(Graphics g) { //draw clock graphic
        if (velocity.x < 0) { // if moving left
            otherClock = GameScreen.loadResources.clockL; //display clock leaning left
        }
        if (velocity.x > 0) { //if moving right
            otherClock = GameScreen.loadResources.clockR; //display clock leaning right
        }
        if (velocity.x == 0) { //if moving at a steady pace (appears to stay in the same position on screen)
            otherClock = GameScreen.loadResources.clockResting; //display clock in resting position
            if (characterEffects.getIntervalTime() % 3 == 0) { //blink
                otherClock = GameScreen.loadResources.clockBlink;
            }
        }
        if (characterEffects.getIntervalTime() % 2 == 0) { ///every other second switch blink clock that blinks and doesn't blink
            screenClock = GameScreen.loadResources.clock;
        } else {
            screenClock = otherClock;
        }
        g.drawImage(screenClock, (int) position.x, (int) position.y, (int) width, (int) height, null);
    }

    private void resizePlayer() { //resize player a little every other second so that they look like they are bouncing
        if (characterEffects.getIntervalTime() % 2 == 0) {
            this.width += 0.3;
            this.position.x -= 0.15;

            this.height += 0.3;
            this.position.y -= 0.15;
        } else {
            this.width -= 0.3;
            this.position.x += 0.15;
            this.height -= 0.3;
            this.position.y += 0.15;
        }
    }

    private void drawWings(Graphics g) { //draw player wings
        wingDirection(); //get the direction of the wings
        g.drawImage(wingDirection[0], (int) position.x - wingWidth, (int) (position.y + (height / 2) - wingHeight), wingWidth, wingHeight, null);
        g.drawImage(wingDirection[1], (int) (position.x + width), (int) (position.y + (height / 2) - wingHeight), wingWidth, wingHeight, null);
    }

    private BufferedImage[] wingDirection() { //change direction of wings according to key clicks
        if (game.gameInstance.ifFlap()) {
            wingDirection[0] = GameScreen.loadResources.wingLup;
            wingDirection[1] = GameScreen.loadResources.wingRup;
        } else {
            wingDirection[0] = GameScreen.loadResources.wingLdown;
            wingDirection[1] = GameScreen.loadResources.wingRdown;
        }
        return wingDirection;
    }

    private void drawBubbles(Graphics g) { //draw bubbles behind the player
        rightBubbles(g); //bubbles appear opposite to direction player is moving in
        leftBubbles(g);
        centerBubbles(g); //if player remains centered, bubbles appear on both sides
    }

    private void rightBubbles(Graphics g) {
        if (velocity.x < 0) { // if moving left, bubbles to the right
            if (bubbleEffects.getIntervalTime() % 2 == 0) {
                g.drawImage(GameScreen.loadResources.bubbleUp, (int) (position.x + width + 3), (int) (position.y + (height / 2)), bubbleWidth, bubbleHeight, null);
                g.drawImage(GameScreen.loadResources.bubbleDown, (int) (position.x + width + 3 + 3 + bubbleWidth), (int) (position.y + height), bubbleWidth, bubbleHeight, null);
                g.drawImage(GameScreen.loadResources.bubbleUp, (int) (position.x + width + 3 + 3 + bubbleWidth + 3 + bubbleWidth), (int) (position.y + (height / 2)), bubbleWidth, bubbleHeight, null);
            } else {
                g.drawImage(GameScreen.loadResources.bubbleDown, (int) (position.x + width + 3), (int) (position.y + height), bubbleWidth, bubbleHeight, null);
                g.drawImage(GameScreen.loadResources.bubbleUp, (int) (position.x + width + 3 + 3 + bubbleWidth), (int) (position.y + (height / 2)), bubbleWidth, bubbleHeight, null);
                g.drawImage(GameScreen.loadResources.bubbleDown, (int) (position.x + width + 3 + 3 + bubbleWidth + 3 + bubbleWidth), (int) (position.y + height), bubbleWidth, bubbleHeight, null);
            }
        }
    }

    private void leftBubbles(Graphics g) {
        if (velocity.x > 0) { //moving left
            if (bubbleEffects.getIntervalTime() % 2 == 0) {
                g.drawImage(GameScreen.loadResources.bubbleUp, (int) (position.x - bubbleWidth - 3), (int) (position.y + (height / 2)), bubbleWidth, bubbleHeight, null);
                g.drawImage(GameScreen.loadResources.bubbleDown, (int) (position.x - bubbleWidth - 3 - bubbleWidth - 3), (int) (position.y + height), bubbleWidth, bubbleHeight, null);
                g.drawImage(GameScreen.loadResources.bubbleUp, (int) (position.x - bubbleWidth - 3 - bubbleWidth - 3 - bubbleWidth - 3), (int) (position.y + (height / 2)), bubbleWidth, bubbleHeight, null);
            } else {
                g.drawImage(GameScreen.loadResources.bubbleDown, (int) (position.x - bubbleWidth - 3), (int) (position.y + height), bubbleWidth, 25, null);
                g.drawImage(GameScreen.loadResources.bubbleUp, (int) (position.x - bubbleWidth - 3 - bubbleWidth - 3), (int) (position.y + (height / 2)), bubbleWidth, bubbleHeight, null);
                g.drawImage(GameScreen.loadResources.bubbleDown, (int) (position.x - bubbleWidth - 3 - bubbleWidth - 3 - bubbleWidth - 3), (int) (position.y + height), bubbleWidth, bubbleHeight, null);
            }
        }
    }

    private void centerBubbles(Graphics g) {
        if (velocity.x == 0) {
            if (bubbleEffects.getIntervalTime() % 2 == 0) {
                g.drawImage(GameScreen.loadResources.bubbleUp, (int) (position.x - bubbleWidth - 3), (int) (position.y + (height / 2)), bubbleWidth, bubbleHeight, null);
                g.drawImage(GameScreen.loadResources.bubbleDown, (int) (position.x + width + 3), (int) (position.y + height), bubbleWidth, bubbleHeight, null);
            } else {
                g.drawImage(GameScreen.loadResources.bubbleDown, (int) (position.x - bubbleWidth - 3), (int) (position.y + height), bubbleWidth, 25, null);
                g.drawImage(GameScreen.loadResources.bubbleUp, (int) (position.x + width + 3), (int) (position.y + (height / 2)), bubbleWidth, bubbleHeight, null);
            }
        }
    }

    private void drawSkull(Graphics g) { //draw a skull that appears if player dies by hitting the top or bottom of the screen
        if (died && prevSkullDisplayTime == skullDisplay.getIntervalTime()) {
            g.drawImage(GameScreen.loadResources.skull, (GameScreen.peekWidth() / 2) - 30, (GameScreen.peekHeight() / 2) - 65, 60, 130, null);
            skullDisplay.frameInterval(40);
        }
        if (prevSkullDisplayTime != skullDisplay.getIntervalTime()) { //stop drawing when it gets to a new second
            died = false;
        }
        prevSkullDisplayTime = skullDisplay.getIntervalTime();
    }

}
