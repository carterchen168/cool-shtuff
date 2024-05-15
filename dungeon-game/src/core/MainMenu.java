package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import utils.FileUtils;

import java.awt.*;

public class MainMenu {
    private int width;
    private int height;
    private long seed;
    private String currScreen;
    private boolean startedGame;
    private double xPos;
    private double yPos;
    private String typed = "";
    private TETile selectedAvatar = Tileset.AVATAR;


    private MP3AudioPlayer audioPlayer = new MP3AudioPlayer();

    public MainMenu(int width, int height) {
        this.width = width;
        this.height = height;
        this.currScreen = "main";
        this.startedGame = false;

        audioPlayer.play("fantasy.mp3");
        TERenderer ter = new TERenderer();
        ter.initialize(width, height);
        this.xPos = (double) this.width;
        this.yPos = (double) this.height;

        determineScreen("main");
    }

    private void determineScreen(String currentScreen) {
        currScreen = currentScreen;
        if (currScreen.equals("selectCharacter")) {
            StdDraw.clear(StdDraw.BLACK);
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.setFont(new Font("Arial", Font.BOLD, 30));

            String[] avatars = {"Dude", "Oski", "Peyrin", "Rebeel"};
            TETile[] avatarTiles = {Tileset.AVATAR, Tileset.AVATAR_BEAR, Tileset.AVATAR_PEYRIN, Tileset.AVATAR_REBEEL};
            String[] avatarImages = {"dude.png", "bear.png", "peyrin.png", "rebeel.png"};

            for (int i = 0; i < avatars.length; i++) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.text(xPos * 0.5, yPos * (0.8 - i * 0.2), avatars[i] + " (Press " + (i + 1) + ")");
                StdDraw.picture(xPos * 0.3, yPos * (0.8 - i * 0.2), "images/" + avatarImages[i], 5, 5);
            }

            StdDraw.setPenColor(StdDraw.YELLOW);
            StdDraw.setFont(new Font("Arial", Font.PLAIN, 20));
            StdDraw.text(xPos * 0.5, yPos * 0.75, "Hes chill ig");
            StdDraw.text(xPos * 0.5, yPos * 0.55, "Greatest Mascot of All Time");
            StdDraw.text(xPos * 0.5, yPos * 0.35, "Bro teaches 61B");
            StdDraw.text(xPos * 0.5, yPos * 0.15, "He will fight his bros to stop Yokota");


            StdDraw.show();

            while (currScreen.equals("selectCharacter")) {
                if (StdDraw.hasNextKeyTyped()) {
                    char keyTyped = StdDraw.nextKeyTyped();
                    int choice = Character.getNumericValue(keyTyped);
                    if (keyTyped == '1') {
                        selectedAvatar = Tileset.AVATAR;
                    }
                    if (keyTyped == '2') {
                        selectedAvatar = Tileset.AVATAR_BEAR;
                    }
                    if (keyTyped == '3') {
                        selectedAvatar = Tileset.AVATAR_PEYRIN;
                    }
                    if (keyTyped == '4') {
                        selectedAvatar = Tileset.AVATAR_REBEEL;
                    }
//                    System.out.println("Selected Character: " + avatars[choice+1]);
                    audioPlayer.play("select.mp3");
                    determineScreen("main");
                }
            }
        }
        if (currScreen.equals("main")) {
            StdDraw.clear(StdDraw.BLACK);
            StdDraw.setPenColor(StdDraw.RED);
            Font font = new Font("Times New Roman", Font.BOLD, 60);
            StdDraw.setFont(font);
            StdDraw.text(xPos * 0.5, yPos * 0.8, "Yokota's Labyrinth");

            StdDraw.picture(xPos * 0.5, yPos * 0.55, "elmo_burning.png", 50, 15);

            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.setFont(new Font("Georgia", Font.PLAIN, 30));
            StdDraw.text(xPos * 0.5, yPos * 0.325, "Select Character (S)");
            StdDraw.text(xPos * 0.5, yPos * 0.25, "New Game (N)");
            StdDraw.text(xPos * 0.5, yPos * 0.175, "Load Game (L)");
            StdDraw.text(xPos * 0.5, yPos * 0.1, "Quit (Q)");
            StdDraw.show();

            while (currScreen.equals("main")) {
                if (StdDraw.hasNextKeyTyped()) {
                    char keyTyped = StdDraw.nextKeyTyped();
                    handleInputMain(Character.toUpperCase(keyTyped));
                }
            }
        }
        if (currScreen.equals("newGame")) {
            setUpScreen();
            while (currScreen.equals("newGame")) {
                if (StdDraw.hasNextKeyTyped()) {
                    char keyTyped = StdDraw.nextKeyTyped();

                    if (Character.isDigit(keyTyped)) {
                        setUpScreen();
                        typed += keyTyped;
                        StdDraw.text(xPos * 0.5, yPos * 0.5, typed);
                        StdDraw.show();
                    }
                    if (keyTyped == 'S' || keyTyped == 's') {
                        startedGame = true;
                        currScreen = "world";
                        World world = new World(width, height, Long.parseLong(typed), selectedAvatar);
                        audioPlayer.play("select.mp3");
                        world.renderGame();
                    }
                }
            }
        }
        if (currScreen.equals("loadGame")) {
            String filename = "src/world_save.txt";
            if (FileUtils.fileExists(filename)) {
                System.out.println("Save exists!");
                String[] lines = FileUtils.readFile(filename).split("\n");
                String[] playerData = lines[0].split(" "); // first line is the player position
                String[] worldData = lines[1].split(" "); // second line is the world size

                int x = Integer.parseInt(worldData[0]);
                int y = Integer.parseInt(worldData[1]);
                long newSeed = Long.parseLong(lines[2]); // third line is the world seed
                System.out.println(x);
                System.out.println(y);
                System.out.println(newSeed);
                World world = new World(x, y, newSeed, selectedAvatar);

                int playerX = Integer.parseInt(playerData[0]);
                int playerY = Integer.parseInt(playerData[1]);
                System.out.print(playerX);
                System.out.println(playerY);

                world.setPlayerPosition(playerX, playerY);
                world.renderGame();

            } else {
                System.out.println("No save found. Exiting...");
                System.exit(0);
            }
        }
    }


    private void setUpScreen() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(new Font("Georgia", Font.PLAIN, 30));
        StdDraw.text(xPos * 0.5, yPos * 0.7, "Enter a seed number of your choice below:");

        StdDraw.text(xPos * 0.5, yPos * 0.3, "Press the (S) key when you have finished.");
        StdDraw.show();
    }

    private void handleInputMain(char input) {
        if (input == 'N') {
            audioPlayer.play("select.mp3");
            audioPlayer.play("happynew.mp3");
            audioPlayer.play("select.mp3");
            determineScreen("newGame");
        }
        if (input == 'L') {
            audioPlayer.play("select.mp3");
            determineScreen("loadGame");
        }
        if (input == 'S') {
            audioPlayer.play("select.mp3");
            determineScreen("selectCharacter");
        }
        if (input == 'Q') {
            audioPlayer.play("select.mp3");
            System.exit(0);
        }
    }

}
