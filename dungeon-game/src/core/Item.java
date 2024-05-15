package core;

public class Item {
    String name;
    String imageSource;

    String type;
    int width;
    int height;
    boolean isEquipped;

    public Item(String name, String image, String type, int x, int y) {
        this.name = name;
        this.imageSource = image;
        this.type = type;
        this.width = x;
        this.height = y;
        this.isEquipped = false;

    }
}
