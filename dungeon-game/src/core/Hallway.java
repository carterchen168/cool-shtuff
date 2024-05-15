package core;

import java.awt.*;
import java.util.ArrayList;

public class Hallway {
    private ArrayList<Point> path;

    public Hallway() {
        this.path = new ArrayList<>();
    }

    // Method to add a straight horizontal or vertical segment to the hallway
    public void addStraightSegment(Point start, Point end) {
        if (start.x == end.x) {
            // Vertical segment
            int minY = Math.min(start.y, end.y);
            int maxY = Math.max(start.y, end.y);
            for (int y = minY; y <= maxY; y++) {
                path.add(new Point(start.x, y));
            }
        } else if (start.y == end.y) {
            // Horizontal segment
            int minX = Math.min(start.x, end.x);
            int maxX = Math.max(start.x, end.x);
            for (int x = minX; x <= maxX; x++) {
                path.add(new Point(x, start.y));
            }
        }
    }

    // Method to add an L-shaped segment to connect two points
    public void addLSegment(Point start, Point end) {
        // Determine the turning point
        Point turn = new Point(end.x, start.y);
        // Add the first segment from start to turn
        addStraightSegment(start, turn);
        // Add the second segment from turn to end
        addStraightSegment(turn, end);
    }

    // Accessor for path
    public ArrayList<Point> getPath() {
        return path;
    }
}


