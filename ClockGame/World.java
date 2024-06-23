package ClockGame;

import Timers.Timer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class World {
    private static int height;
    private static int width;
    private ClockBackground[] backgrounds = new ClockBackground[4];
    BufferedImage[] backgroundImages = new BufferedImage[4];
    private GamePlayer player1;
    private Pipes pipe1;
    private Pipes pipe2;
    private Pipes pipe3;
    private static Pair pipe1default;
    private static Pair pipe2default;
    private static Pair pipe3default;
    private Hammer hammer;
    private static Pair hammerResetPosition = new Pair(-100, -100);
    private static Pair hammerResetVelocity = new Pair(0, 0);
    private CollisionDetection detectPipe1Passed;
    private CollisionDetection detectPipe2Passed;
    private CollisionDetection detectPipe3Passed;
    private CollisionDetection playerTopPipe1;
    private CollisionDetection playerBottomPipe1;
    private CollisionDetection playerTopPipe2;
    private CollisionDetection playerBottomPipe2;
    private CollisionDetection playerTopPipe3;
    private CollisionDetection playerBottomPipe3;
    private CollisionDetection playerHammer;
    private boolean takeDamage = true;
    private boolean drawDamage = false;
    private double damageTotal;
    private Timer hammerTimer;
    private Timer damageTimer;
    private Timer loseProgressTimer;
    private int prevHammerTime = -1;
    private int prevDamageTime = -1;
    private int prevloseProgressTime = -1;
    public boolean win;
    private double winCondition;
    private int wordWidth;
    private String winningMessage;
    private String highScoreString;
    private String currentScoreString;
    private int record;
    public int pipesPassed;
    private int scoreBoardWidth;
    private boolean loseThree = false;
    private boolean loseTwo = false;
    private boolean loseOne = false;
    private String numHammersLost;
    DisplayGameScreen game;
    private static int hammerFrequency = 120; //one hammer created every hammerFrequency frames
    private boolean canAddPipesPassed = true;
    private int framesPassingSamePipes = 0; //tracks amt of frames that a player takes to pass pipe so that int for pipes passed only increases once when a pair of pipes are passed

    public World(int initWidth, int initHeight, int hammerGoal, DisplayGameScreen initGame) {
        pipesPassed = 0; //record number of pipes player passes consecutively
        createBackground();
        game = initGame;
        damageTotal = 0;
        record = GameScreen.getCurrentHighScore();
        winCondition = hammerGoal;
        hammerTimer = new Timer();
        damageTimer = new Timer();
        loseProgressTimer = new Timer();
        width = initWidth;
        height = initHeight;
        player1 = new GamePlayer(game);
        hammer = new Hammer(-100, -100, 0); //start offscreen
        createPipes();
        detectorPipesPassed();
        collisionPipePlayer();
        collisionPlayerHammer();
    }

    private void createPipes() {
        pipe1 = new Pipes(GameScreen.loadResources.greenTopPipe, GameScreen.loadResources.greenBottomPipe);
        pipe1default = pipe1.peekPosition(); //defaults to most current pipe position when resetting pipes
        pipe2 = new Pipes((width / 3) + pipe1.width + pipe1.getPipeSpacing(), GameScreen.loadResources.yellowTopPipe, GameScreen.loadResources.yellowBottomPipe);
        pipe2default = pipe2.peekPosition();
        pipe3 = new Pipes((width / 3) + (2 * pipe2.width) + (2 * pipe2.getPipeSpacing()), GameScreen.loadResources.redTopPipe, GameScreen.loadResources.redBottomPipe);
        pipe3default = pipe3.peekPosition();
    }

    private void createBackground() {
        backgroundImages[0] = GameScreen.loadResources.background1;
        backgroundImages[1] = GameScreen.loadResources.background2;
        backgroundImages[2] = GameScreen.loadResources.background3;
        backgroundImages[3] = GameScreen.loadResources.background4;

        for (int i = 0; i < 4; i++) {
            backgrounds[i] = new ClockBackground((i * ClockBackground.peekDefaultWidth()), backgroundImages[i]);
        }
    }


    void drawObjects(Graphics g) {
        drawBackground(g);
        player1.draw(g);
        drawCrack(g);
        drawHammer(g);
        drawPipes(g);
        drawHammerScore(g);
    }

    private void drawBackground(Graphics g) {
        for (int i = 0; i < 4; i++) {
            if (backgrounds[i].position.x < width) {
                g.drawImage(backgroundImages[i], (int) backgrounds[i].position.x, (int) backgrounds[i].position.y, (int) backgrounds[i].width, (int) backgrounds[i].height, null);
            }
        }
    }

    private void drawCrack(Graphics g) {
        if ((damageTotal / winCondition) >= (1.0 / 5.0) && (damageTotal / winCondition) < (2.0 / 5.0)) {
            g.drawImage(GameScreen.loadResources.crack1, (int) player1.position.x, (int) player1.position.y, (int) player1.width - 5, (int) player1.height - 5, null);
        }
        if ((damageTotal / winCondition) >= (2.0 / 5.0) && (damageTotal / winCondition) < (3.0 / 5.0)) {
            g.drawImage(GameScreen.loadResources.crack2, (int) player1.position.x, (int) player1.position.y, (int) player1.width - 5, (int) player1.height - 5, null);
        }
        if ((damageTotal / winCondition) >= (3.0 / 5.0) && (damageTotal / winCondition) < (4.0 / 5.0)) {
            g.drawImage(GameScreen.loadResources.crack3, (int) player1.position.x, (int) player1.position.y, (int) player1.width - 5, (int) player1.height - 5, null);
        }
        if ((damageTotal / winCondition) >= (4.0 / 5.0) && (damageTotal / winCondition) < (5.0 / 5.0)) {
            g.drawImage(GameScreen.loadResources.crack4, (int) player1.position.x, (int) player1.position.y, (int) player1.width - 5, (int) player1.height - 5, null);
        }
        if ((damageTotal / winCondition) >= (1.0)) {
            g.drawImage(GameScreen.loadResources.crack5, (int) player1.position.x, (int) player1.position.y, (int) player1.width - 5, (int) player1.height - 5, null);
        }
    }

    private void drawPipes(Graphics g) {
        pipe1.draw(g);
        pipe2.draw(g);
        pipe3.draw(g);
    }

    private void drawHammer(Graphics g) {
        hammer.draw(g);
        drawHammerDamage(g);
    }

    private void drawHammerDamage(Graphics g) {
        drawHammerExplosion(g);
        drawHammerPopUp(g);
    }

    private void drawHammerPopUp(Graphics g) {
        loseProgressTimer.frameInterval(100);
        int popUpPosition = (height / 2) - (int) (player1.height / 2);

        if ((loseOne || loseTwo || loseThree) && prevloseProgressTime == loseProgressTimer.getIntervalTime()) {
            g.drawImage(GameScreen.loadResources.loseHammer, (width / 2) + (int) (player1.width / 2), popUpPosition, (int) (hammer.width), (int) (hammer.height), null);
            if (loseOne) {
                numHammersLost = "" + 1;
            }
            if (loseTwo) {
                numHammersLost = "" + 2;
            }
            if (loseThree) {
                numHammersLost = "" + 3;
            }
            g.setFont(GameScreen.loadResources.MonospacedBold);
            g.setColor(Color.BLACK);
            g.drawString("-" + numHammersLost + " ", (5 + (width / 2) - (int) (1.5 * player1.width) + (int) (hammer.width) + 10), popUpPosition + (int) (hammer.height / 2));
            g.setColor(GameScreen.loadResources.lightRed);
            g.drawString("-" + numHammersLost + " ", ((width / 2) - (int) (1.5 * player1.width) + (int) (hammer.width) + 10), popUpPosition + (int) (hammer.height / 2));
        }

        if (prevloseProgressTime != loseProgressTimer.getIntervalTime()) { //stop drawing when it gets to a new second
            loseOne = false;
            loseTwo = false;
            loseThree = false;
        }
        prevloseProgressTime = loseProgressTimer.getIntervalTime();

    }

    private void drawHammerExplosion(Graphics g) {
        damageTimer.frameInterval(35);
        if (drawDamage && prevDamageTime == damageTimer.getIntervalTime()) {
            g.drawImage(GameScreen.loadResources.damage, (int) player1.position.x, (int) player1.position.y, (int) player1.width, (int) player1.height, null);
        }
        if (prevDamageTime != damageTimer.getIntervalTime()) { //stop drawing when it gets to a new second
            drawDamage = false;
        }
        prevDamageTime = damageTimer.getIntervalTime();
    }

    private void drawHammerScore(Graphics g) {
        drawScoreBoard(g);
        drawScoreBoardHammer(g);

        g.setColor(GameScreen.loadResources.lightBlue);
        g.setFont(GameScreen.loadResources.Monospaced);
        currentScoreString = ("current score : " + pipesPassed);
        wordWidth = g.getFontMetrics().stringWidth(currentScoreString);
        g.drawString(currentScoreString, (scoreBoardWidth - wordWidth) / 2, 10 + (int) (2 * (hammer.height / 3)));

        highScoreString = ("high score : " + newRecordCheck());
        wordWidth = g.getFontMetrics().stringWidth(highScoreString);
        g.drawString(highScoreString, (scoreBoardWidth - wordWidth) / 2, 10 + (int) (2.5 * (hammer.height / 3)));

    }

    private void drawScoreBoardHammer(Graphics g) {
        for (int i = 0; i < damageTotal; i++) {
            g.drawImage(GameScreen.loadResources.collectHammer, 12 + (i * (int) (10 + (hammer.width / 3))), 20, (int) (hammer.width / 3), (int) (hammer.height / 3), null);
        }
        for (int i = 0; i < (winCondition - damageTotal); i++) {
            g.drawImage(GameScreen.loadResources.greyHammer, 12 + ((i + (int) damageTotal) * (int) (10 + (hammer.width / 3))), 20, (int) (hammer.width / 3), (int) (hammer.height / 3), null);
        }
    }

    private void drawScoreBoard(Graphics g) {
        g.setColor(Color.BLACK);
        if (winCondition <= 3) {
            scoreBoardWidth = (int) (8 * (hammer.width / 3));
        } else if (winCondition > 2) {
            scoreBoardWidth = 40 + (((int) winCondition - 1) * (int) (10 + (hammer.width / 3)));
        }
        g.fillRect(0, 10, scoreBoardWidth, 50 + (int) (hammer.height / 3));
        g.setColor(Color.WHITE);
        g.drawRect(0, 10, scoreBoardWidth, 50 + (int) (hammer.height / 3));
    }

    void updateObjects(double time) {
        updateBackgrounds(time); //update backgrounds according to velocity to move
        updatePlayer(time); //update player, pipes, hammer over time according to velocity
        updatePipes(time);
        updateHammer(time);
        updatePipePlayerCollision(); //update pipe collision detection between pipe and player
        updatePlayerHammerCollision(); //update hammer collision detection between hammer and player
        updatePipesPassedDetector(); //update current score of consecutive pipes passed
    }

    public int newRecordCheck() { //check if user has achieved new record by passing greatest number of pipes without dying
        if(pipesPassed > record) {
            record = pipesPassed;
        }
        return record;
    }

    private void updateBackgrounds(double time) {
        for (int i = 0; i < 4; i++) {
            backgrounds[i].velocity.x = (0.12 * Pipes.getPipeSpeed());
            backgrounds[i].update(time);
        }
    }

    private void updatePlayer(double time) {
        player1.update(time);
    }

    private void updatePipes(double time) {
        if (game.gameInstance.ifStarted()) { //update velocity according to pipespeed variable
            pipe1.updatePipeSpeed();
            pipe2.updatePipeSpeed();
            pipe3.updatePipeSpeed();
        }
        pipe1.update(time); //update position according to velocity
        pipe2.update(time);
        pipe3.update(time);
        pipe1.updateRanPipePosition(); //update position to match random Y
        pipe2.updateRanPipePosition();
        pipe3.updateRanPipePosition();
    }

    private void updateHammer(double time) {
        hammerTimer.frameInterval(hammerFrequency); //set how often hammers appear
        if (prevHammerTime != hammerTimer.getIntervalTime()) {
            takeDamage = true; //after an interval of time, the hammer is able to take damage again
            reCreateHammer(); //move hammer to next position if player is hit
        }
        prevHammerTime = hammerTimer.getIntervalTime();
        hammer.velocity.x = Pipes.getPipeSpeed(); //update hammer velocity if pipespeed changes
        hammer.update(time);
    }

    private void reCreateHammer() { //recreate hammer object to appear from different pipes at random
        switch (ranPipeNum()) {
            case 1: //if 1, emerge from topPipe1
                hammer.position.x = pipe1.position.x + (pipe1.width / 2) - (hammer.width / 2);
                hammer.position.y = pipe1.position.y + pipe1.height - hammer.height;
                hammer.velocity.y = Math.abs(hammer.getDefaultYVelocity());
                break;
            case 2: //If 2, emerge from topPipe2
                hammer.position.x = pipe2.position.x + (pipe2.width / 2) - (hammer.width / 2);
                hammer.position.y = pipe2.position.y + pipe1.height - hammer.height;
                hammer.velocity.y = Math.abs(hammer.getDefaultYVelocity());
                break;
            case 3: //If 3, emerge from topPipe3
                hammer.position.x = pipe3.position.x + (pipe3.width / 2) - (hammer.width / 2);
                hammer.position.y = pipe3.position.y + pipe1.height - hammer.height;
                hammer.velocity.y = Math.abs(hammer.getDefaultYVelocity());
                break;
            case 4://if 4, emerge from bottomPipe1
                hammer.position.x = pipe1.position.x + (pipe1.width / 2) - (hammer.width / 2);
                hammer.position.y = pipe1.getBottomPosition().y + hammer.height;
                hammer.velocity.y = (-1 * (Math.abs(hammer.getDefaultYVelocity())));
                break;
            case 5://if 5, emerge from bottomPipe2
                hammer.position.x = pipe2.position.x + (pipe2.width / 2) - (hammer.width / 2);
                hammer.position.y = pipe2.getBottomPosition().y + hammer.height;
                hammer.velocity.y = (-1 * (Math.abs(hammer.getDefaultYVelocity())));
                break;
            case 6://if 6, emerge from bottomPipe3
                hammer.position.x = pipe3.position.x + (pipe3.width / 2) - (hammer.width / 2);
                hammer.position.y = pipe3.getBottomPosition().y + hammer.height;
                hammer.velocity.y = (-1 * (Math.abs(hammer.getDefaultYVelocity())));
                break;
        }
    }

    private void updatePipesPassedDetector() {
        detectPipe1Passed.updateDetection((int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe1.position.x, 0, (int) (pipe1.position.x + pipe1.width), height);
        detectPipe2Passed.updateDetection((int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe2.position.x, 0, (int) (pipe2.position.x + pipe2.width), height);
        detectPipe3Passed.updateDetection((int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe3.position.x, 0, (int) (pipe3.position.x + pipe3.width), height);
        checkIfPipesPassed();
    }
    private void updatePipePlayerCollision() { //update collision detection between player and pipe
        playerTopPipe1.updateDetection((int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe1.position.x, (int) pipe1.position.y, (int) (pipe1.position.x + pipe1.width), (int) (pipe1.position.y + pipe1.height));
        playerBottomPipe1.updateDetection((int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe1.getBottomPosition().x, (int) pipe1.getBottomPosition().y, (int) (pipe1.getBottomPosition().x + pipe1.width), (int) (pipe1.getBottomPosition().y + pipe1.height));
        playerTopPipe2.updateDetection((int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe2.position.x, (int) pipe2.position.y, (int) (pipe2.position.x + pipe2.width), (int) (pipe2.position.y + pipe2.height));
        playerBottomPipe2.updateDetection((int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe2.getBottomPosition().x, (int) pipe2.getBottomPosition().y, (int) (pipe2.getBottomPosition().x + pipe2.width), (int) (pipe2.getBottomPosition().y + pipe2.height));
        playerTopPipe3.updateDetection((int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe3.position.x, (int) pipe3.position.y, (int) (pipe3.position.x + pipe3.width), (int) (pipe3.position.y + pipe3.height));
        playerBottomPipe3.updateDetection((int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe3.getBottomPosition().x, (int) pipe3.getBottomPosition().y, (int) (pipe3.getBottomPosition().x + pipe3.width), (int) (pipe3.getBottomPosition().y + pipe3.height));
        pipePlayerOverlap();
    }

    private void updatePlayerHammerCollision() { //update collision detection between player and hammer
        playerHammer.updateDetection((int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) (hammer.position.x), (int) (hammer.position.y), (int) (hammer.position.x + hammer.width), (int) (hammer.position.y + hammer.height));
        playerHammerOverlap();
    }


    private void checkIfPipesPassed() { //check if player passes any of the 6 pipes (6 counting both top and bottom)
        if((detectPipe1Passed.checkOverlap()||detectPipe2Passed.checkOverlap()||detectPipe3Passed.checkOverlap())) {
            if(canAddPipesPassed) {
                pipesPassed += 1;
                canAddPipesPassed = false; //set false after one added for count
            }
        } else { //if player is not overlapping with a pipe anymore, turn canAddPipesPassed back to true
            canAddPipesPassed = true;
        }
    }
    private void pipePlayerOverlap() { //check overlap for pipe/player collision detection
        if (playerTopPipe1.checkOverlap() || playerTopPipe2.checkOverlap() || playerTopPipe3.checkOverlap() || playerBottomPipe1.checkOverlap() || playerBottomPipe2.checkOverlap() || playerBottomPipe3.checkOverlap()) { //if player overlaps with any of the 6 pipes
            resetHammer(); //reset hammer position, velocity
            resetPipes(); //reset pipes to most recent position, reset velocity to default
            player1.resetPlayer(); //reset player to starting position, with default velocity
            drawDamage = false; //don't draw explosion for when player hits pipe (only when player hits hammer)
            pipesPassed = 0; //reset count for number of pipes passed to 0
        }

        if ((playerTopPipe1.checkOverlap() || playerBottomPipe1.checkOverlap()) && (damageTotal > 0)) { //if player overlaps with first set of pipes (green)
            damageTotal -= 1;
            loseOne = true;
        }
        if ((playerTopPipe2.checkOverlap() || playerBottomPipe2.checkOverlap()) && (damageTotal > 0)) { //if player overlaps with second set of pipes (yellow)
            damageTotal -= 2;
            loseTwo = true;
        }
        if ((playerTopPipe3.checkOverlap() || playerBottomPipe3.checkOverlap()) && (damageTotal > 0)) { //if player overlaps with third set of pipes (red)
            damageTotal -= 3;
            loseThree = true;
        }
        if (damageTotal < 0) {
            damageTotal = 0;
        }
    }

    private void playerHammerOverlap() { //check overlap for hammer/player collision detection
        if (playerHammer.checkOverlap() && takeDamage) { //if true, damage+=1 //explode hammer, add crack to clock face
            takeDamage = false; //turn take damage false (until it is turned true a few moments later) so if hammer is on player for multiple frames it only registers as one hit
            drawDamage = true;
            damageTotal++; //increase damage total which determines how crack on clock is drawn
            resetHammer(); //reset hammer position, velocitu
            damageTimer.clearIntervalTimer(); //reset drawing timer for hammer to 0 so the drawing interval is accurate
        }
    }

    private void detectorPipesPassed() { //create detector that checks if player crosses anywhere within the x bounds of a pipe
        detectPipe1Passed = new CollisionDetection((int) player1.width / 2, (int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe1.position.x, 0, (int) (pipe1.position.x + pipe1.width), height);
        detectPipe2Passed = new CollisionDetection((int) player1.width / 2, (int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe2.position.x, 0, (int) (pipe2.position.x + pipe2.width), height);
        detectPipe3Passed = new CollisionDetection((int) player1.width / 2, (int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe3.position.x, 0, (int) (pipe3.position.x + pipe3.width), height);
    }
    private void collisionPipePlayer() { //initialize collision detection for pipe and player
        playerTopPipe1 = new CollisionDetection((int) player1.width / 2, (int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe1.position.x, (int) pipe1.position.y, (int) (pipe1.position.x + pipe1.width), (int) (pipe1.position.y + pipe1.height));
        playerBottomPipe1 = new CollisionDetection((int) player1.width / 2, (int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe1.getBottomPosition().x, (int) pipe1.getBottomPosition().y, (int) (pipe1.getBottomPosition().x + pipe1.width), (int) (pipe1.getBottomPosition().y + pipe1.height));

        playerTopPipe2 = new CollisionDetection((int) player1.width / 2, (int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe2.position.x, (int) pipe2.position.y, (int) (pipe2.position.x + pipe2.width), (int) (pipe2.position.y + pipe2.height));
        playerBottomPipe2 = new CollisionDetection((int) player1.width / 2, (int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe2.getBottomPosition().x, (int) pipe2.getBottomPosition().y, (int) (pipe2.getBottomPosition().x + pipe2.width), (int) (pipe2.getBottomPosition().y + pipe2.height));

        playerTopPipe3 = new CollisionDetection((int) player1.width / 2, (int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe3.position.x, (int) pipe3.position.y, (int) (pipe3.position.x + pipe3.width), (int) (pipe3.position.y + pipe3.height));
        playerBottomPipe3 = new CollisionDetection((int) player1.width / 2, (int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) pipe3.getBottomPosition().x, (int) pipe3.getBottomPosition().y, (int) (pipe3.getBottomPosition().x + pipe3.width), (int) (pipe3.getBottomPosition().y + pipe3.height));
    }

    private void collisionPlayerHammer() { //initalize collision detection between player and hammer
        playerHammer = new CollisionDetection((int) (player1.width / 2), (int) (player1.position.x + (player1.width / 2)), (int) (player1.position.y + (player1.height / 2)), (int) (hammer.position.x), (int) (hammer.position.y), (int) (hammer.position.x + hammer.width), (int) (hammer.position.y + hammer.height));
    }

    private void resetPipePosition() { //reset pipe position to last saved pipe position if user dies (so they have a chance to predictably get back into the game without dying right away)
        pipe1.setPosition(pipe1default);
        pipe2.setPosition(pipe2default);
        pipe3.setPosition(pipe3default);
    }

    private static void resetPipeSpeeds() { //reset pipe speeds to default
        Pipes.changePipeSpeed(Pipes.getDefaultPipeSpeed());
    }

    void resetPipes() { //reset pipe speeds and positions
        resetPipeSpeeds();
        resetPipePosition();
    }

    void resetHammer() { //reset hammer position and velocity to default
        hammer.setPosition(hammerResetPosition);
        hammer.setVelocity(hammerResetVelocity);
    }

    private int ranPipeNum() { //random number for pipes to decide which pipe the hammer emerges from
        int pipeNum = (int) (Math.random() * 6 + 1);
        return pipeNum;
    }

    public void setPlayerXVelocity(double x) {
        player1.velocity.x = x;
    }

    public void setPlayerYVelocity(double y) {
        player1.velocity.y = y;
    }

    public void setPlayerAcceleration(Pair xyz) {
        player1.acceleration = xyz;
    }

    public void setDamageTotal(int damageNum) {
        damageTotal = damageNum;
    }

    public boolean checkIfWon() { //check if game has been won yet
        if (damageTotal == winCondition) {
            win = true;
        }
        return win;
    }

}


