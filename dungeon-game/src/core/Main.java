package core;

import core.MainMenu;

public class Main {

    public static void main(String[] args) {

        // build your own world!
        /** RoomTest thing = new RoomTest(20, 30, 12345678);
         thing.renderBoard(); */
        int width = 100;
        int height = 50;
        MainMenu menu = new MainMenu(width, height);

    }
}
