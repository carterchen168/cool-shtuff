package core;
import java.awt.*;
import java.util.Set;


public class Space {
    Point lowerLeftCorner;
    Point upperLeftCorner;
    Point lowerRightCorner;
    Point upperRightCorner;

    Set points;


    public Space(Point ll, Point ul, Point ur, Point lr) {
        this.lowerLeftCorner = ll;
        this.upperLeftCorner = ul;
        this.lowerRightCorner = lr;
        this.upperRightCorner = ur;
    }

    public int getLeftX() {
        return (int) lowerLeftCorner.getX();
    }
    public int getRightX() {
        return (int) upperRightCorner.getX();
    }
    public int getUpY() {
        return (int) upperLeftCorner.getY();
    }
    public int getDownY() {
        return (int) lowerRightCorner.getY();
    }

    public String printPoints() {
        return "(" + getLeftX() + "," + getDownY() + "),"
                + "(" + getLeftX() + "," + getUpY() + "),"
                + "(" + getRightX() + "," + getUpY() + "),"
                + "(" + getRightX() + "," + getDownY() + ")";
    }

}
