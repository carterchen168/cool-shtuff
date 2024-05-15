package core;
import java.awt.*;
import java.util.ArrayList;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;


public class Inventory {
    public ArrayList inventoryList;
    int maxCapacity;
    public Inventory(int capacity) {
        this.maxCapacity = capacity;
        this.inventoryList = new ArrayList<Item>(maxCapacity);
    }

    public void addInventory() {
        inventoryList.clear();
        for (int i = 0; i < maxCapacity; i++) {
            inventoryList.add(i, null);
        }
//        inventory.add(0, ItemList.SWORD);
//        inventory.add(1, ItemList.BOW);
//        inventory.add(2, ItemList.HEALTH_POTION);
//        inventory.add(3, ItemList.KEY);
//        inventory.add(4, ItemList.FINAL_EXAM_PAPERS);

    }
    public void displayInventory() {

        for (int i = 0; i < maxCapacity; i++) {
            Item current = (Item) inventoryList.get(i);
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.textLeft((i * 3), 3.4, "" + (i + 1));
            StdDraw.square((i * 3) + 1.5, 1.5, 1.4);
            if (current != null) {
                if (current.isEquipped) {
                    StdDraw.setPenColor(Color.RED);
                    StdDraw.square((i * 3) + 1.5, 1.5, 1.4);
                }
                else {
                    StdDraw.setPenColor(Color.WHITE);
                    StdDraw.square((i * 3) + 1.5, 1.5, 1.4);
                }
                StdDraw.picture((i * 3) + 1.5, 1.5, current.imageSource, current.width, current.height);
            }
        }
        if (numSlotsUsed() == maxCapacity) {
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.textLeft(0, 4.5, "Inventory Full!");
        }
    }

    public Item itemEquipped(int index) {
        Item current = (Item) inventoryList.get(index);
        if (current != null) {
            if (current.isEquipped) {
                return current;
            }
        }
        return null;
    }

    public int inventorySize() {
        return maxCapacity;
    }

    public int numSlotsUsed() {
        int counter = 0;
        for (Object thing : inventoryList) {
            Item thingItem = (Item) thing;
            if (thingItem != null) {
                counter += 1;
            }
        }
        return counter;
    }





}
