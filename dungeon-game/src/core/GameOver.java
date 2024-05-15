package core;
import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import utils.FileUtils;
import javazoom.jl.player.Player;

import java.awt.*;
import java.io.BufferedInputStream;

import java.awt.*;

public class GameOver {
    private int width;
    private int height;
    private long seed;
    private double xPos;
    private double yPos;
    private boolean gameOver;
    private MP3AudioPlayer audioPlayer = new MP3AudioPlayer();
    private TETile selectedAvatar = Tileset.AVATAR;



    public GameOver(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.gameOver = true;

        TERenderer ter = new TERenderer();
        ter.initialize(width, height);
        this.xPos = (double) this.width;
        this.yPos = (double) this.height;

        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.BOOK_RED);
        StdDraw.setFont(new Font("Georgia", Font.PLAIN, 150));
        StdDraw.text(xPos * 0.5, yPos * 0.8, "YOU DIED");

        StdDraw.picture(xPos * 0.5, yPos * 0.5, "yokover.png", 30, 20);

        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(new Font("Georgia", Font.PLAIN, 30));
        StdDraw.text(xPos * 0.5, yPos * 0.25, "Restart from previous seed (R)");
        StdDraw.text(xPos * 0.5, yPos * 0.2, "Main Menu (M)");
        StdDraw.text(xPos * 0.5, yPos * 0.15, "Quit (Q)");

        StdDraw.show();

        while (gameOver) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (key == 'R' || key == 'r') {
                    audioPlayer.play("select.mp3");
                    gameOver = false;
                    World restart = new World(width, height, seed, selectedAvatar);
                    restart.renderGame();
                }
                if (key == 'M' || key == 'm') {
                    audioPlayer.play("select.mp3");
                    gameOver = false;
                    MainMenu menu = new MainMenu(width, height);
                }
                if (key == 'Q' || key == 'q') {
                    audioPlayer.play("select.mp3");
                    gameOver = false;
                    System.exit(0);
                }
            }
        }

    }
}