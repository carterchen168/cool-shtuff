package core;

import java.awt.*;

public class Room {
    int x, y; // Top-left corner of the room
    int width, height;
    boolean lockedRoom;

    public Room(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.lockedRoom = false;
    }

    // checks if two rooms overlap
    public boolean overlaps(Room other) {
        return x < other.x + other.width && x + width > other.x && y < other.y + other.height && y + height > other.y;
    }

    // returns the center point of the room for connecting hallways
    public Point center() {
        return new Point(x + width / 2, y + height / 2);
    }
}
