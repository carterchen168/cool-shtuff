package core;

import javazoom.jl.player.Player;

import java.io.BufferedInputStream;

public class MP3AudioPlayer {
    private Player mp3Player;
    private Thread playerThread;

    public void play(String filename) {
        if (mp3Player != null) {
            stop(); // Stop currently playing music (if any)
        }
        playerThread = new Thread(() -> {
            try {
                BufferedInputStream buffer = new BufferedInputStream(
                        getClass().getResourceAsStream("/" + filename)
                );
                mp3Player = new Player(buffer);
                mp3Player.play();
            } catch (Exception e) {
                System.err.println("Could not play the audio file: " + e.getMessage());
                e.printStackTrace();
            }
        });
        playerThread.start(); // Start the playback in a new thread
    }

    public void stop() {
            playerThread.interrupt(); // Interrupt the thread
            mp3Player.close(); // Stop the player if it's running
    }
}
