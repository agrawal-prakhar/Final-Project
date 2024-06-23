package ClockGame;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Pipes extends Obj {
    private double maxTopHeight;
    private double ranTopYposition;
    private double ranBottomYposition;
    private double topMinPosition;
    private double topMaxPosition;
    private static double pipeGap = 250;
    private static double minShowing = 50;
    private BufferedImage topName;
    private BufferedImage bottomName;
    private static final int defaultPipeSpeed = -220;
    private static int pipeSpeed = -220;
    private static double pipeSpacing;
    private Pair bottomPosition = new Pair (0,0);

    public Pipes(double x, BufferedImage topPipe, BufferedImage bottomPipe) {
        super();
        topName = topPipe ; //greenTopPipe
        bottomName = bottomPipe; //greenBottomPipe
        pipeSetUp();
        this.position = new Pair(x,ranTopYposition);
    }

    public Pipes(BufferedImage topPipe, BufferedImage bottomPipe) {
        super();
        topName = topPipe ; //greenTopPipe
        bottomName = bottomPipe; //greenBottomPipe
        pipeSetUp();
        this.position = new Pair(GameScreen.peekWidth()/3,ranTopYposition);
    }
    public void pipeSetUp() {
        findMaxTopHeight();
        this.height = maxTopHeight;
        this.width = 140;
        pipeSpacing = (GameScreen.peekWidth()-(2*width))/3; //space between pipes
        findTopMinPosition();
        findTopMaxPosition();
        ranTopYposition = generateRanTopY();
        ranBottomYposition = generateRanBottomY();
        this.velocity = new Pair(0, 0);
        this.acceleration = new Pair(0,0);
    }

    static void changePipeSpeed(int xyz) {
        pipeSpeed = xyz;
    }

    void updatePipeSpeed() {
        this.velocity.x = pipeSpeed;
    }

    private void findMaxTopHeight() {
       maxTopHeight = GameScreen.peekHeight() - pipeGap - minShowing; //calculate max height that would allow max y position to be 0
   } //height + gap + minimum amount of bottom pipe showing

    private void findTopMinPosition() {
       topMinPosition = 0 - ((int)maxTopHeight)+minShowing; //height = maxTopHeight
   }
    private void findTopMaxPosition() {
        topMaxPosition = 0;
    }

    private double generateRanTopY() {
        ranTopYposition = topMinPosition + (int)(Math.random() *(topMaxPosition+1 - topMinPosition));
        if(ranTopYposition<=topMinPosition || ranTopYposition >= topMaxPosition) {
             generateRanTopY();
         } //regenerate if not in range
        return ranTopYposition;
    }

    private double generateRanBottomY() {
        ranBottomYposition = ranTopYposition + maxTopHeight+ pipeGap;
        return ranBottomYposition;
    }

    void updateRanPipePosition() {
        position.y = ranTopYposition;
        bottomPosition.x = position.x;
        bottomPosition.y = ranBottomYposition;
    }

    public Pair peekBottomPosition() {
        return bottomPosition;
    }

    public static int getPipeSpeed() {
        return pipeSpeed;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(topName, (int) position.x, (int)ranTopYposition, (int) width, (int) height, null);
        g.drawImage(bottomName, (int) position.x, (int)ranBottomYposition, (int) width, (int) height, null);
    }

    @Override
    public void verticalTopBounce() {}

    @Override
    public void verticalBottomBounce() {}

    @Override
    public void horizontalLBounce() {
        if(position.x+width < 0) { //if pipes reach end of screen
            ranTopYposition = generateRanTopY(); //generate new random positions
            ranBottomYposition = generateRanBottomY();
            updateRanPipePosition();
            position.x = GameScreen.peekWidth(); //place back on screen
        }
    }

    @Override
    public void horizontalRBounce() {}

    public static int getDefaultPipeSpeed() {
        return defaultPipeSpeed;
    }

    public static double getPipeSpacing() {
        return pipeSpacing;
    }

    public Pair getBottomPosition() {
        return bottomPosition;
    }
}
