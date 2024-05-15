package core;

public class WinScreenTest {
    public static void main(String[] args) {

        // build your own world!
        /** RoomTest thing = new RoomTest(20, 30, 12345678);
         thing.renderBoard(); */
        int width = 100;
        int height = 50;
        long seed = 12345;
        WinScreen screen = new WinScreen(width, height, seed);

    }
}
