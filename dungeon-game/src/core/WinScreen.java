package core;
import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import javazoom.jl.player.Player;

import java.awt.*;

public class WinScreen {
    private int width;
    private int height;
    private long seed;
    private double xPos;
    private double yPos;
    private MP3AudioPlayer audioPlayer = new MP3AudioPlayer();
    private TETile selectedAvatar = Tileset.AVATAR;
    private boolean gameOver;



    public WinScreen(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.gameOver = true;

        TERenderer ter = new TERenderer();
        ter.initialize(width, height);
        this.xPos = (double) this.width;
        this.yPos = (double) this.height;

        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(new Font("Arial", Font.PLAIN, 100));
        StdDraw.text(xPos * 0.5, yPos * 0.85, "YOU WIN!!");

        StdDraw.picture(xPos * 0.5, yPos * 0.55, "images/victory.png", 80, 20);

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
