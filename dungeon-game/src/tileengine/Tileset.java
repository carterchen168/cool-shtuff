package tileengine;


import java.awt.Color;

public class Tileset {
    public static final TETile AVATAR = new TETile('㋡', new Color(255, 215, 0), Color.black, "you", "images/dude.png", 0); // gold
    public static final TETile AVATAR_BEAR = new TETile('㋡', new Color(255, 215, 0), Color.black, "you", "images/bear.png", 0);
    public static final TETile AVATAR_PEYRIN = new TETile('㋡', new Color(255, 215, 0), Color.black, "you", "images/peyrin.png", 0);
    public static final TETile AVATAR_REBEEL = new TETile('㋡', new Color(255, 215, 0), Color.black, "you", "images/rebeel.png", 0);
    public static final TETile WALL = new TETile('▢', new Color(220, 40, 80), new Color(50, 50, 50), "wall", 1); // dark grey walls
    public static final TETile FLOOR = new TETile('-', new Color(110, 110, 110), Color.black, "floor", 2); // lighter grey floor
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing", 3); // void
    public static final TETile GRASS = new TETile('"', new Color(34, 139, 34), Color.black, "moss", 4); // dark green moss
    public static final TETile WATER = new TETile('≈', new Color(0, 0, 139), Color.black, "water", 5); // deep blue water
    public static final TETile FLOWER = new TETile('❀', new Color(139, 0, 139), new Color(255, 20, 147), "fungus", 6); // magenta fungus
    public static final TETile LOCKED_DOOR = new TETile('█', new Color(139, 69, 19), Color.black, "Locked door. You need a key.", 7); // brown door
    public static final TETile UNLOCKED_DOOR = new TETile('▢', new Color(205, 133, 63), Color.black, "unlocked door", 8); // lighter brown door
    public static final TETile SAND = new TETile('▒', new Color(210, 180, 140), Color.black, "dust", 9); // sandy dust
    public static final TETile MOUNTAIN = new TETile('▲', new Color(105, 105, 105), Color.black, "rock", 10); // dark gray rock
    public static final TETile TREE = new TETile('♠', new Color(0, 100, 0), Color.black, "old tree", 11); // dark green old tree

    public static final TETile CELL = new TETile('█', new Color(255, 255, 255), Color.black, "cell", 12); // white cells, maybe for special areas

    public static final TETile KILLBRICK = new TETile(' ', new Color(178, 34, 34), Color.black, "bee", "images/bee.png", 0 ); // dark red danger

    public static final TETile HEALTH_POTION = new TETile(' ', new Color(0, 0, 0), Color.black, "potion", "images/16bitpotion.png", -1);
    public static final TETile SKULL = new TETile('☠', Color.WHITE, Color.BLACK, "dead", 13);
    public static final TETile COIN = new TETile('⛁', Color.YELLOW, Color.BLACK, "coin", 14);

    public static final TETile SWORD = new TETile(' ', new Color(0, 0, 0), Color.black, "sword", "images/16bitsword.png", -1);

    public static final TETile FINAL_EXAMS = new TETile(' ', new Color(255,255,255), Color.black, "exams", "images/16bitexams.png", -1);
    public static final TETile KEY = new TETile(' ', new Color(255,255,255), Color.black, "key", "images/16bitkey.png", -1);


}
