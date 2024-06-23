package ClockGame;

public class CollisionDetection{ // check if any point overlaps the circle and rectangle (based on circle's center point and radius and any two diagonal points on rectangle)
    private int radius;
    private int cX;
    private int cY;
    private int X1;
    private int Y1;
    private int X2;
    private int Y2;
    public CollisionDetection(int R, int Xc, int Yc, int X1, int Y1, int X2, int Y2) {
        this.radius = R;
        this.cX = Xc;
        this.cY = Yc;
        this.X1 = X1;
        this.Y1 = Y1;
        this.X2 = X2;
        this.Y2 = Y2;
    }

    // check if any point overlaps the circle and rectangle provided
    boolean checkOverlap() { //https://www.geeksforgeeks.org/check-if-any-point-overlaps-the-given-circle-and-rectangle/
        // Finds the nearest point on the rectangle to the circle's center
        int Xn = Math.max(X1, Math.min(cX, X2));
        int Yn = Math.max(Y1, Math.min(cY, Y2));

        // Finds the distance between center of the circle and the nearest point
        int Dx = Xn - cX;
        int Dy = Yn - cY;
        return (Dx * Dx + Dy * Dy) <= radius * radius;
    }

    void updateDetection(int Xc, int Yc, int X1, int Y1, int X2, int Y2) {
        this.cX = Xc;
        this.cY = Yc;
        this.X1 = X1;
        this.Y1 = Y1;
        this.X2 = X2;
        this.Y2 = Y2;
    }

}
