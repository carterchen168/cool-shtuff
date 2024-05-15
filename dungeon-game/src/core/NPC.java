package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class NPC {
    Point position;
    TETile[][] world;
    Random rand;
    int moveDelay; // Delay between moves in ticks
    int delayCounter; // Current count down to next move
    int health;
    NPC npc;

    public NPC(Point start, TETile[][] world, int moveDelay) {
        this.position = start;
        this.world = world;
        this.rand = new Random();
        this.moveDelay = moveDelay;
        this.delayCounter = moveDelay; // Initialize so it can move immediately

        this.health = 3;
    }

    public void move() {
        // Only move if delay counter has reached 0
        if (delayCounter <= 0) {
            int[] dx = {-1, 1, 0, 0};
            int[] dy = {0, 0, -1, 1};
            ArrayList<Integer> directions = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
            Collections.shuffle(directions); // Randomize direction to avoid patterns

            for (int dirIndex : directions) {
                int newX = position.x + dx[dirIndex];
                int newY = position.y + dy[dirIndex];

                // Move if the new position is within bounds and not a wall
                if (newX >= 0 && newY >= 0 && newX < world.length && newY < world[0].length &&
                        (!world[newX][newY].equals(Tileset.KILLBRICK) && (world[newX][newY].equals(Tileset.FLOOR) || world[newX][newY].equals(Tileset.AVATAR)))) {
                    world[position.x][position.y] = Tileset.FLOOR; // Clear old position
                    position.setLocation(newX, newY); // Update position
                    world[newX][newY] = Tileset.KILLBRICK; // NPC moves to new position
                    delayCounter = moveDelay; // Reset delay counter
                    break; // Exit loop after move
                }
            }
        } else {
            // Decrease delay counter if not ready to move
            delayCounter--;
        }
    }
    public Point getBeePosition() {
        return position;
    }
    public int getBeeX() {return position.x;}
    public int getBeeY() {return position.y;}
}

