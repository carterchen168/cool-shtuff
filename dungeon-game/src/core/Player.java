package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import utils.FileUtils;
import utils.RandomUtils;

import java.awt.*;
import java.util.ArrayList;

public class Player {
    public TETile currentAvatar;
    public int numHearts;
    public int positionX;
    public int positionY;
    public TETile[][] game;
    public Point position;
    public MP3AudioPlayer audioSound = new MP3AudioPlayer();

    public Item equipped;
    public Inventory playerInventory;
    public boolean swordEquipped;
    public boolean onCooldown;
    public int cooldownTimer;
    public int totalCoins;
    public int damageTimer;
    public boolean canBeDamaged;
    public boolean keyEquipped;
    public boolean openedGate;

    public Player(int x, int y, int health, TETile avatar, TETile[][] universe, Inventory inv) {
        this.currentAvatar = avatar;
        this.numHearts = health;
        this.positionX = x;
        this.positionY = y;
        this.game = universe;

        this.position = new Point(x, y);
        this.playerInventory = inv;

        this.equipped = null;
        this.swordEquipped = false;
        this.keyEquipped = false;
        this.onCooldown = false;
        this.cooldownTimer = 0;
        this.totalCoins = 0;
        this.damageTimer = 0;
        this.canBeDamaged = true;
        this.openedGate = false;

    }

    public void useItem(int index) {
        if (playerInventory.inventoryList.get(index - 1) != null) {
            if (playerInventory.inventoryList.get(index - 1) == ItemList.HEALTH_POTION) {
                audioSound.play("heal.mp3");
                numHearts = 5;
                playerInventory.inventoryList.remove(index - 1);
            }
            if (playerInventory.inventoryList.get(index - 1) == ItemList.SWORD) {
                Item sword = (Item) playerInventory.inventoryList.get(index - 1);
                if (sword.isEquipped) {
                    sword.isEquipped = false;
                    this.swordEquipped = false;
                } else {
                    sword.isEquipped = true;
                    this.swordEquipped = true;
                }
                //sword.isEquipped = !sword.isEquipped;
                //this.swordEquipped = !this.swordEquipped;
            }
            if (playerInventory.inventoryList.get(index - 1) == ItemList.KEY) {
                openedGate = true;
                playerInventory.inventoryList.remove(index - 1);

            }
            /**
            if (playerInventory.inventoryList.get(index - 1) == ItemList.SWORD) {
                Item sword = (Item) playerInventory.inventoryList.get(index - 1);
                sword.isEquipped = !sword.isEquipped;
                this.swordEquipped = !this.swordEquipped;
            }
             */
        }
    }

}
