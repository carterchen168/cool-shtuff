package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import utils.FileUtils;
import utils.RandomUtils;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;


public class World {

    // build your own world!
    /**
     * public GameOfLife(long seed) {
     * width = DEFAULT_WIDTH;
     * height = DEFAULT_HEIGHT;sa
     * ter = new TERenderer();
     * ter.initialize(width, height);
     * random = new Random(seed);
     * TETile[][] randomTiles = new TETile[width][height];
     * fillWithRandomTiles(randomTiles);
     * currentState = randomTiles;
     * }
     */
    TETile[][] world;
    ArrayList<NPC> npcs = new ArrayList<>();
    private MP3AudioPlayer audioMusic = new MP3AudioPlayer();
    private MP3AudioPlayer audioSound = new MP3AudioPlayer();
    private ArrayList<Room> rooms;
    private ArrayList<Hallway> hallways;
    private Random random;
    private int width;
    private int height;
    private int minRoomSize = 4;
    private int maxRoomSize = 8;
    private int minPossibleWidth = 6;
    private int minPossibleHeight = 4;

    private double roomDensity = 0.15;

    private Point playerPosition;
    private boolean isGameOver;
    private String movements = "";
    private char lastKeyTyped;
    private ArrayList<Space> dividers;
    private String filename = "src/world_save.txt";
    Space origin;
    private long worldSeed;
    private boolean saveExists;
    private Player currentPlayer;
    private Inventory inventory;

    private Room topUI = new Room(0, 99, 20, 12);
    private Room bottomUI = new Room(0, 5, 20, 6);
    private TERenderer ter;
    private HashMap<TETile, Integer> skullCoinsMap;

    private TETile selectedAvatar;
    private int maxCoins;
    private boolean keyGotten;


    public World(int width, int height, long seed, TETile avatar) {
        this.world = new TETile[width][height];
        this.width = width;
        this.height = height;
        this.random = new Random(seed);
        this.rooms = new ArrayList<>();
        this.hallways = new ArrayList<>();

        this.dividers = new ArrayList<>();
        this.origin = new Space(new Point(0, 0), new Point(0, height - 1),
                new Point(width - 1, height - 1), new Point(width - 1, 0));
        this.isGameOver = false;
        this.worldSeed = seed;
        int defaultHealth = 5;
        this.inventory = new Inventory(5);
        this.skullCoinsMap = new HashMap<>();
        this.keyGotten = false;


        partitionWorld(origin, random);
        createEmptyWorld();
        generateRooms();
        createLockedRoom();
        //fillLockedRoom();
        // connect rooms
        connectRooms();
        // add tiles
        fillWorld();
        // add walls
        addWalls();
        //createLockedWalls();


        Room startingRoom = rooms.get(0);
        int playerX = startingRoom.x + startingRoom.width / 2;
        int playerY = startingRoom.y + startingRoom.height / 2;


        this.currentPlayer = new Player(playerX, playerY, defaultHealth, avatar, this.world, inventory);
        this.world[playerX][playerY] = avatar;
        this.playerPosition = new Point(playerX, playerY);
        selectedAvatar = avatar;

        int numNPCs = 20; // or any other number based on game difficulty level
        while (npcs.size() < numNPCs) {
            int roomNumber = RandomUtils.uniform(random, 1, rooms.size() - 1);
            Room room = rooms.get(roomNumber);

            int npcX = RandomUtils.uniform(random, room.x, room.x + room.width - 1);
            int npcY = RandomUtils.uniform(random, room.y, room.y + room.height - 1);
            if (world[npcX][npcY].equals(Tileset.FLOOR)) {
                NPC npc = new NPC(new Point(npcX, npcY), world, 4);
                npcs.add(npc);
                world[npcX][npcY] = Tileset.KILLBRICK; // Initialize NPC in the world
            }
            /**
            int npcX = RandomUtils.uniform(random, 1, width - 1);
            int npcY = RandomUtils.uniform(random, 1, height - 1);
            if (Math.abs(npcX - playerX) > 3 && Math.abs(npcY - playerY) > 3) { // prevents bees spawn-killing
                if (world[npcX][npcY].equals(Tileset.FLOOR)) {
                    NPC npc = new NPC(new Point(npcX, npcY), world, 4);
                    npcs.add(npc);
                    world[npcX][npcY] = Tileset.KILLBRICK; // Initialize NPC in the world
                }
            }
             */
        }
        int maxPotions = 10;
        int potions = 0;
        while (potions < maxPotions) {
            int roomNumber = RandomUtils.uniform(random, 1, rooms.size() - 1);
            Room room = rooms.get(roomNumber);

            int potionX = RandomUtils.uniform(random, room.x, room.x + room.width - 1);
            int potionY = RandomUtils.uniform(random, room.y, room.y + room.height - 1);
            if (world[potionX][potionY].equals(Tileset.FLOOR)) {
                world[potionX][potionY] = Tileset.HEALTH_POTION;
                potions += 1;
            }
        }

        int maxSword = 1;
        int sword = 0;
        while (sword < maxSword) {
            int weaponX = RandomUtils.uniform(random, playerX - 1, playerX + 2);
            int weaponY = RandomUtils.uniform(random, playerY - 1, playerY + 2);
            if (world[weaponX][weaponY].equals(Tileset.FLOOR)) {
                world[weaponX][weaponY] = Tileset.SWORD;
                sword += 1;
            }
        }
        Room endRoom = rooms.getLast();
        int examX = RandomUtils.uniform(random, endRoom.x, endRoom.x + endRoom.width);
        int examY = RandomUtils.uniform(random, endRoom.y, endRoom.y + endRoom.height);
        world[examX][examY] = Tileset.FINAL_EXAMS;

        audioMusic.play("8bitdungeon.mp3");

        inventory.addInventory();
        //renderGame();
    }

    public void checkEnemiesKilled() {
        while (!keyGotten) {
            Room randomRoom = rooms.get(RandomUtils.uniform(random, 0, rooms.size()));
            if (randomRoom != rooms.getLast()) {
                int keyX = RandomUtils.uniform(random, randomRoom.x, randomRoom.x + randomRoom.width);
                int keyY = RandomUtils.uniform(random, randomRoom.y, randomRoom.y + randomRoom.height);

                if (world[keyX][keyY] == Tileset.FLOOR) {
                    world[keyX][keyY] = Tileset.KEY;
                    keyGotten = true;
                }
            }
        }
    }

    public void removeLockedDoors() {
        if (currentPlayer.openedGate) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (world[i][j].equals(Tileset.LOCKED_DOOR)) {
                        world[i][j] = Tileset.FLOOR;
                    }
                }
            }
        }
    }
    public void renderGame() {
        ter = new TERenderer();
        ter.initialize(width, height);

        while (!isGameOver) {
            for (NPC npc : npcs) {
                npc.move(); // Move each NPC
                Point npcPosition = npc.position;
                if (npcPosition.x == playerPosition.x && npcPosition.y == playerPosition.y) {
                    takeDamage(1);
                }
            }

            StdDraw.clear(Color.BLACK);
            if (npcs.isEmpty() && !keyGotten) {
                checkEnemiesKilled();
            }
            if (StdDraw.isMousePressed() && currentPlayer.swordEquipped) {
                swordAttack();
            }
            cooldownTimer();
            damageTimer();
            ter.drawTiles(getWorld());
            currentTile();
            currentHealth();
            checkIfGameOver();
            inventory.displayInventory();
            maintainAvatar();
            StdDraw.show();
            StdDraw.pause(60);
            turnToCoins();
            removeLockedDoors();


            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                handleInput(Character.toUpperCase(key));
            }
        }
    }
    private void checkIfGameOver() {
        if (currentPlayer.numHearts <= 0) {
            isGameOver = true;
            audioMusic.play("DIEDNEW.mp3");
            GameOver gameOver = new GameOver(width, height, worldSeed);
        }
    }

    private void turnToCoins() {
        for (TETile tile : skullCoinsMap.keySet()) {
            int timeRemaining = skullCoinsMap.get(tile);
            if (timeRemaining <= 0) {
                skullCoinsMap.remove(tile);
            } else {
                skullCoinsMap.put(tile, timeRemaining - 1);
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (skullCoinsMap.get(world[i][j]) == null && world[i][j] == Tileset.SKULL) {
                    world[i][j] = Tileset.COIN;
                }
            }
        }
    }
    private void cooldownTimer() {
        if (currentPlayer.onCooldown && currentPlayer.cooldownTimer > 0) {
            currentPlayer.cooldownTimer -= 1;
        }
        if (currentPlayer.cooldownTimer == 0) {
            currentPlayer.onCooldown = false;
        }
    }

    private void damageTimer() {
        if (currentPlayer.damageTimer > 0) {
            currentPlayer.damageTimer -= 1;
        }
        if (currentPlayer.damageTimer == 0) {
            currentPlayer.canBeDamaged = true;
        }
    }
    private void handleInput(char input) {
        if (Character.isDigit(input)) {
            System.out.println(input);
            int inputInteger = Integer.parseInt(String.valueOf(input));
            if (inputInteger - 1 < inventory.inventorySize()) {
                System.out.println("yes");
                currentPlayer.useItem(inputInteger);
                inventory = currentPlayer.playerInventory;
            }
        }

        if (lastKeyTyped == ':' && (input == 'Q')) {
            saveGame();
        }
        lastKeyTyped = input;

        if (input == 'W') {
            moveAvatar('W');
        }
        if (input == 'A') {
            moveAvatar('A');
        }
        if (input == 'S') {
            moveAvatar('S');
        }
        if (input == 'D') {
            moveAvatar('D');
        }
        /** if (input == 'L'){
         loadGame();
         } */


        movements = movements + input;
        System.out.println(movements);
    }

    private void maintainAvatar() {
        world[playerPosition.x][playerPosition.y] = selectedAvatar;
    }
    private void saveGame() {
        System.out.println("Saved game");
        FileUtils.writeFile(filename, playerPosition.x + " "
                + playerPosition.y + "\n" + width + " "
                + height + "\n" + worldSeed);
        System.exit(0);
    }

    private String saveWorld() {
        StringBuilder builder = new StringBuilder();
        builder.append(movements);
        return builder.toString();
    }

    /** void loadGame(int x, int y){
     Point oldPosition = playerPosition;
     this.playerPosition = new Point(x, y);
     world[(int) oldPosition.getX()][(int) oldPosition.getY()] = Tileset.FLOOR;
     world[x][y] = Tileset.AVATAR;
     //        processMovements();
     } */

    /**
     * UL             UR
     * ---------------
     * |              |
     * |              |
     * |              |
     * |              |
     * ----------------
     * LL             LR
     * Space(LL, UL, UR, LR)
     */
    int count = 1;
    int maxCount = 9;

    public void partitionWorld(Space space, Random randomInstance) {
        if (!dividers.contains(space)) {
            dividers.add(space);
        }
        int xSize = Math.abs(space.getRightX() - space.getLeftX()) + 1;
        int ySize = Math.abs(space.getUpY() - space.getDownY()) + 1;
        int direction = RandomUtils.uniform(randomInstance, 0, 2); // 0 for horizontal, 1 for vertical
        if (direction == 0) {
            if (ySize / 2 < minPossibleHeight || count >= maxCount) {
                return;
            }
            dividers.remove(space);
            int coordinate = RandomUtils.uniform(randomInstance, space.getDownY(), space.getUpY());
            Point up1 = new Point(space.getLeftX(), coordinate + 1);
            Point up2 = new Point(space.getLeftX(), space.getUpY());
            Point up3 = new Point(space.getRightX(), space.getUpY());
            Point up4 = new Point(space.getRightX(), coordinate + 1);

            Point down1 = new Point(space.getLeftX(), space.getDownY());
            Point down2 = new Point(space.getLeftX(), coordinate);
            Point down3 = new Point(space.getRightX(), coordinate);
            Point down4 = new Point(space.getRightX(), space.getDownY());

            Space up = new Space(up1, up2, up3, up4);
            Space down = new Space(down1, down2, down3, down4);
            count -= 1;
            count += 2;

            dividers.add(up);
            dividers.add(down);
            partitionWorld(up, randomInstance);
            partitionWorld(down, randomInstance);
        } else {
            if (xSize / 2 < minPossibleWidth || count >= maxCount) {
                return;
            }
            dividers.remove(space);
            int coordinate = RandomUtils.uniform(randomInstance, space.getLeftX(), space.getRightX());
            Point left1 = new Point(space.getLeftX(), space.getDownY());
            Point left2 = new Point(space.getLeftX(), space.getUpY());
            Point left3 = new Point(coordinate, space.getUpY());
            Point left4 = new Point(coordinate, space.getDownY());

            Point right1 = new Point(coordinate + 1, space.getDownY());
            Point right2 = new Point(coordinate + 1, space.getUpY());
            Point right3 = new Point(space.getRightX(), space.getUpY());
            Point right4 = new Point(space.getRightX(), space.getDownY());

            Space left = new Space(left1, left2, left3, left4);
            Space right = new Space(right1, right2, right3, right4);

            count -= 1;
            count += 2;

            dividers.add(left);
            dividers.add(right);
            partitionWorld(left, randomInstance);
            partitionWorld(right, randomInstance);
        }
    }

    private void createEmptyWorld() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

    private void generateRooms() {
        int targetArea = (int) (width * height * roomDensity);
        int currentArea = 0;

        while (currentArea < targetArea) {
            int roomWidth = RandomUtils.uniform(random, minRoomSize, maxRoomSize);
            int roomHeight = RandomUtils.uniform(random, minRoomSize, maxRoomSize);
            // make sure the room does not go off the edges by adjusting the potential position range
            int x = RandomUtils.uniform(random, 1, width - roomWidth - 1); // leave a space for the wall
            int y = RandomUtils.uniform(random, 1, height - roomHeight - 1); // Leave a space for the wall

            Room room = new Room(x, y, roomWidth, roomHeight);

            /**
            if (roomOverlaps(topUI) || roomOverlaps(bottomUI)) {
                roomWidth = RandomUtils.uniform(random, minRoomSize, maxRoomSize);
                roomHeight = RandomUtils.uniform(random, minRoomSize, maxRoomSize);

                x = RandomUtils.uniform(random, 1, width - roomWidth - 1); // leave a space for the wall
                y = RandomUtils.uniform(random, 1, height - roomHeight - 1); // Leave a space for the wall

                room = new Room(x, y, roomWidth, roomHeight);
            } */
            if (!roomOverlaps(room) && !room.overlaps(topUI) && !room.overlaps(bottomUI)) {
                rooms.add(room);
                currentArea += roomWidth * roomHeight;
            }
        }
    }

    private void generateRooms(int i) { // generate specific number of rooms
        int targetArea = (int) (width * height * roomDensity);
        int currentArea = 0;
        int numRooms = 0;
        while (numRooms < i) {
            while (currentArea < targetArea) {
                int roomWidth = RandomUtils.uniform(random, minRoomSize, maxRoomSize);
                int roomHeight = RandomUtils.uniform(random, minRoomSize, maxRoomSize);
                // make sure the room does not go off the edges by adjusting the potential position range
                int x = RandomUtils.uniform(random, 1, width - roomWidth - 1); // leave a space for the wall
                int y = RandomUtils.uniform(random, 1, height - roomHeight - 1); // Leave a space for the wall

                Room room = new Room(x, y, roomWidth, roomHeight);

                /**
                 if (roomOverlaps(topUI) || roomOverlaps(bottomUI)) {
                 roomWidth = RandomUtils.uniform(random, minRoomSize, maxRoomSize);
                 roomHeight = RandomUtils.uniform(random, minRoomSize, maxRoomSize);

                 x = RandomUtils.uniform(random, 1, width - roomWidth - 1); // leave a space for the wall
                 y = RandomUtils.uniform(random, 1, height - roomHeight - 1); // Leave a space for the wall

                 room = new Room(x, y, roomWidth, roomHeight);
                 } */
                if (!roomOverlaps(room) && !room.overlaps(topUI) && !room.overlaps(bottomUI)) {
                    rooms.add(room);
                    currentArea += roomWidth * roomHeight;
                }
            }
            numRooms += 1;
        }

    }

    private boolean roomOverlaps(Room room) {
        for (Room other : rooms) {
            if (room.overlaps(other)) {
                return true;
            }
        }
        return false;
    }

    private void connectRooms() {
        for (int i = 0; i < rooms.size() - 1; i++) {
            Room currentRoom = rooms.get(i);
            Room closestRoom = null;
            Room secondClosestRoom = null;
            double closestDistance = Double.MAX_VALUE;

            for (int j = i + 1; j < rooms.size(); j++) {
                Room potentialRoom = rooms.get(j);
                double distance = currentRoom.center().distanceSq(potentialRoom.center());
                if (distance < closestDistance) {
                    closestDistance = distance;
                    secondClosestRoom = closestRoom;
                    closestRoom = potentialRoom;

                }
            }

            if (closestRoom != null) {
                if (closestRoom.lockedRoom && secondClosestRoom != null) {
                    connectTwoRooms(currentRoom, secondClosestRoom);
                }
                connectTwoRooms(currentRoom, closestRoom);
            }
        }
    }

    private void connectRooms(Room room) {
        Room currentRoom = room;
        Room closestRoom = null;
        double closestDistance = Double.MAX_VALUE;

        for (int j = 0; j < rooms.size(); j++) {
            Room potentialRoom = rooms.get(j);
            double distance = currentRoom.center().distanceSq(potentialRoom.center());
            if (distance < closestDistance) {
                closestDistance = distance;
                closestRoom = potentialRoom;
            }
        }

        if (closestRoom != null) {
            connectTwoRooms(currentRoom, closestRoom);
        }
    }

    private void connectTwoRooms(Room a, Room b) {
        Point start = a.center();
        Point end = b.center();
        Hallway hallway = new Hallway();
        // randomly decide whether to start with horizontal or vertical segment
        if (random.nextBoolean()) {
            hallway.addLSegment(start, end);
        } else {
            // add L-Segment with first segment vertical
            hallway.addLSegment(new Point(start.x, end.y), start);
            hallway.addStraightSegment(new Point(start.x, end.y), end);
        }
        hallways.add(hallway);
    }
    /**
    private void selectLockedRoom() {
        int totalRooms = rooms.size();
        int lockedRoomIndex = RandomUtils.uniform(random, 1, totalRooms); //does not include starting room obviously
        Room lockedRoom = rooms.get(lockedRoomIndex);
        lockedRoom.lockedRoom = true;
    } */

    private void createLockedRoom() {
        boolean lockedRoomCreated = false;
        while (!lockedRoomCreated) {
            int roomConnectedTo = RandomUtils.uniform(random, 1, rooms.size());
            generateRooms(1);
            //connectTwoRooms(rooms.getLast(), rooms.get(roomConnectedTo));
            //connectRooms(rooms.getLast());
            lockedRoomCreated = true;
            /**
            for (Room other : rooms) {
                if (other != rooms.getLast()) {
                    if (rooms.getLast().overlaps(other)) {
                        lockedRoomCreated = false;
                        rooms.removeLast();
                        break;
                    }
                }
            }
            */
        }
        rooms.getLast().lockedRoom = true;
    }

    private void fillLockedRoom() {
        Room lockedRoom = rooms.getLast();
        for (int x = lockedRoom.x; x < lockedRoom.x + lockedRoom.width; x++) {
            for (int y = lockedRoom.y; y < lockedRoom.y + lockedRoom.height; y++) {
                world[x][y] = Tileset.GRASS;
            }
        }
    }

    private void createLockedWalls() {
        Room lockedRoom = rooms.getLast();
        boolean lockedDoorMade = false;
        for (int x = lockedRoom.x - 1; x < lockedRoom.x + lockedRoom.width + 1; x++) {
            for (int y = lockedRoom.y + 1; y > lockedRoom.y + 1 - lockedRoom.height; y--) {
                if (x < lockedRoom.x || x > lockedRoom.x + lockedRoom.width - 1) {
                    if (world[x][y] == Tileset.FLOOR) {
                        world[x][y] = Tileset.LOCKED_DOOR;
                    }
                }
            }
        }
    }

    private void fillWorld() {
        for (Room room : rooms) {
            if (!room.lockedRoom) {
                for (int x = room.x; x < room.x + room.width; x++) {
                    for (int y = room.y; y < room.y + room.height; y++) {
                        world[x][y] = Tileset.FLOOR;
                    }
                }
            }

        }
        for (Hallway hallway : hallways) {
            for (Point p : hallway.getPath()) {
                world[p.x][p.y] = Tileset.FLOOR;
            }
        }
        Room lastRoom = rooms.getLast();
        for (int x = lastRoom.x; x < lastRoom.x + lastRoom.width; x++) {
            for (int y = lastRoom.y; y < lastRoom.y + lastRoom.height; y++) {
                world[x][y] = Tileset.FLOOR;
            }
        }
        System.out.println(lastRoom.width);
        System.out.println(lastRoom.height);
        System.out.println(lastRoom);
    }

    private void addWalls() {
        for (Room room : rooms) {
            for (int x = room.x - 1; x <= room.x + room.width; x++) {
                for (int y = room.y - 1; y <= room.y + room.height; y++) {
                    if (room.lockedRoom && (x < room.x || x == room.x + room.width || y < room.y || y == room.y + room.height) && world[x][y] == Tileset.FLOOR) {
                        world[x][y] = Tileset.LOCKED_DOOR;
                    }
                    if (isInBounds(x, y) && world[x][y] == Tileset.NOTHING) {
                        world[x][y] = Tileset.WALL;
                    }
                }
            }
        }

        // remove walls on the border if they are not next to a floor
        removeInvalidBorderWalls();

        for (Hallway hallway : hallways) {
            for (Point p : hallway.getPath()) {
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        int offsetX = p.x + dx;
                        int offsetY = p.y + dy;
                        if (offsetX >= 0 && offsetY >= 0 && offsetX < width) {
                            if (offsetY < height && world[offsetX][offsetY] == Tileset.NOTHING) {
                                world[offsetX][offsetY] = Tileset.WALL;
                            }
                        }
                    }
                }
            }
        }

    }

    private void removeInvalidBorderWalls() {
        for (int x = 0; x < width; x++) {
            if (!isNextToFloor(x, 0)) {
                world[x][0] = Tileset.NOTHING;
            }
            if (!isNextToFloor(x, height - 1)) {
                world[x][height - 1] = Tileset.NOTHING;
            }
        }
        for (int y = 0; y < height; y++) {
            if (!isNextToFloor(0, y)) {
                world[0][y] = Tileset.NOTHING;
            }
            if (!isNextToFloor(width - 1, y)) {
                world[width - 1][y] = Tileset.NOTHING;
            }
        }
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    private boolean isNextToFloor(int x, int y) {
        int[][] directions = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1}
        };
        for (int[] dir : directions) {
            int offsetX = x + dir[0];
            int offsetY = y + dir[1];
            if (isInBounds(offsetX, offsetY) && world[offsetX][offsetY] == Tileset.FLOOR) {
                return true;
            }
        }
        return false;
    }

    private void currentTile() {
        TETile currTile = world[(int) StdDraw.mouseX()][(int) StdDraw.mouseY()];
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.textLeft(3, 48, "Tile: " + currTile.description());
        StdDraw.textLeft(3, 45, "Coins: " + currentPlayer.totalCoins);

    }
    private void swordAttack() {
        int xCoord = (int) StdDraw.mouseX();
        int yCoord = (int) StdDraw.mouseY();
        ArrayList<TETile> testingTiles = new ArrayList<>();
        ArrayList<NPC> deadBees = new ArrayList<>();
        if (xCoord > playerPosition.x && !currentPlayer.onCooldown) { // attacking right
            for (int i = playerPosition.x + 1; i <= playerPosition.x + 2; i++) {
                for (int j = playerPosition.y - 1; j <= playerPosition.y + 1; j++) {
                    if (world[i][j] == Tileset.KILLBRICK) {
                        Point currPoint = new Point(i, j);
                        for (NPC npc : npcs) {
                            if (npc.getBeePosition().equals(currPoint)) {
                                npc.health -= 2;
                                System.out.println("Direct hit!");
                            }
                            if (npc.health <= 0) {
                                System.out.println("Dead");
                                world[npc.getBeeX()][npc.getBeeY()] = Tileset.SKULL;
                                skullCoinsMap.put(world[npc.getBeeX()][npc.getBeeY()], 20);
                                deadBees.add(npc);
                            }
                        }

                    }
                    TETile thing = world[i][j];
                    testingTiles.add(thing);
                    world[i][j] = Tileset.CELL;
                    audioSound.play("swordhit.mp3");
                    currentPlayer.onCooldown = true;
                    currentPlayer.cooldownTimer = 7;
                }
            }

            ter.drawTiles(getWorld());
            StdDraw.show();
            StdDraw.pause(10);



            int dummy = 0;

            for (int i = playerPosition.x + 1; i <= playerPosition.x + 2; i++) {
                for (int j = playerPosition.y - 1; j <= playerPosition.y + 1; j++) {
                    world[i][j] = testingTiles.get(dummy);
                    dummy += 1;
                }
            }
        }
        else if (xCoord < playerPosition.x && !currentPlayer.onCooldown) {
            for (int i = playerPosition.x - 1; i >= playerPosition.x - 2; i--) {
                for (int j = playerPosition.y - 1; j <= playerPosition.y + 1; j++) {
                    if (world[i][j] == Tileset.KILLBRICK) {
                        Point currPoint = new Point(i, j);
                        for (NPC npc : npcs) {
                            if (npc.getBeePosition().equals(currPoint)) {
                                npc.health -= 2;
                                System.out.println("Direct hit!");
                            }
                            if (npc.health <= 0) {
                                System.out.println("Dead");
                                world[npc.getBeeX()][npc.getBeeY()] = Tileset.SKULL;
                                deadBees.add(npc);
                                skullCoinsMap.put(world[npc.getBeeX()][npc.getBeeY()], 20);
                            }
                        }
                    }
                    TETile thing = world[i][j];
                    testingTiles.add(thing);
                    world[i][j] = Tileset.CELL;
                    audioSound.play("swordhit.mp3");
                    currentPlayer.onCooldown = true;
                    currentPlayer.cooldownTimer = 7;
                }
            }

            ter.drawTiles(getWorld());
            StdDraw.show();
              StdDraw.pause(10);



            int dummy = 0;
            for (int i = playerPosition.x - 1; i >= playerPosition.x - 2; i--) {
                for (int j = playerPosition.y - 1; j <= playerPosition.y + 1; j++)  {
                    world[i][j] = testingTiles.get(dummy);
                    dummy += 1;
                }
            }
        }
        else if (yCoord > playerPosition.y && !currentPlayer.onCooldown) {
            for (int i = playerPosition.x - 1; i <= playerPosition.x + 1; i++) {
                for (int j = playerPosition.y + 1; j <= playerPosition.y + 2; j++) {
                    if (world[i][j] == Tileset.KILLBRICK) {
                        Point currPoint = new Point(i, j);
                        for (NPC npc : npcs) {
                            if (npc.getBeePosition().equals(currPoint)) {
                                npc.health -= 2;
                                System.out.println("Direct hit!");
                            }
                            if (npc.health <= 0) {
                                System.out.println("Dead");
                                world[npc.getBeeX()][npc.getBeeY()] = Tileset.SKULL;
                                deadBees.add(npc);
                                skullCoinsMap.put(world[npc.getBeeX()][npc.getBeeY()], 20);
                            }
                        }
                    }
                    TETile thing = world[i][j];
                    testingTiles.add(thing);
                    world[i][j] = Tileset.CELL;
                    audioSound.play("swordhit.mp3");
                    currentPlayer.onCooldown = true;
                    currentPlayer.cooldownTimer = 7;
                }
            }

            ter.drawTiles(getWorld());
            StdDraw.show();
            StdDraw.pause(10);



            int dummy = 0;
            for (int i = playerPosition.x - 1; i <= playerPosition.x + 1; i++) {
                for (int j = playerPosition.y + 1; j <= playerPosition.y + 2; j++) {
                    world[i][j] = testingTiles.get(dummy);
                    dummy += 1;
                }
            }
        }
        else if (yCoord < playerPosition.y && !currentPlayer.onCooldown) {
            for (int i = playerPosition.x - 1; i <= playerPosition.x + 1; i++) {
                for (int j = playerPosition.y - 1; j >= playerPosition.y - 2; j--) {
                    if (world[i][j] == Tileset.KILLBRICK) {
                        Point currPoint = new Point(i, j);
                        for (NPC npc : npcs) {
                            if (npc.getBeePosition().equals(currPoint)) {
                                npc.health -= 2;
                                System.out.println("Direct hit!");
                            }
                            if (npc.health <= 0) {
                                System.out.println("Dead");
                                world[npc.getBeeX()][npc.getBeeY()] = Tileset.SKULL;
                                deadBees.add(npc);
                                skullCoinsMap.put(world[npc.getBeeX()][npc.getBeeY()], 20);
                            }
                        }
                    }
                    TETile thing = world[i][j];
                    testingTiles.add(thing);
                    world[i][j] = Tileset.CELL;
                    audioSound.play("swordhit.mp3");
                    currentPlayer.onCooldown = true;
                    currentPlayer.cooldownTimer = 7;
                }
            }

            ter.drawTiles(getWorld());
            StdDraw.show();
            StdDraw.pause(10);



            int dummy = 0;
            for (int i = playerPosition.x - 1; i <= playerPosition.x + 1; i++) {
                for (int j = playerPosition.y - 1; j >= playerPosition.y - 2; j--) {
                    world[i][j] = testingTiles.get(dummy);
                    dummy += 1;
                }
            }
        }
        for (NPC bee : deadBees) {
            npcs.remove(bee);
        }
    }
    private void resetTiles() {
        /** for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                TETile compare = world[playerPosition.x + i][playerPosition.y + j];
                if (compare == Tileset.CELL) {
                    world[playerPosition.x + i][playerPosition.y + j] = Tileset.FLOOR;
                }
            }
        } */
    }

    private void currentHealth() {
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.textLeft(3, 46.5, "♥ ".repeat(currentPlayer.numHearts));
    }

    public void moveAvatar(char direction) {
        int x = 0;
        int y = 0;
        if (direction == 'W') {
            y += 1;
        }
        if (direction == 'A') {
            x -= 1;
        }
        if (direction == 'S') {
            y -= 1;
        }
        if (direction == 'D') {
            x += 1;
        }

        int newX = playerPosition.x + x;
        int newY = playerPosition.y + y;

        if (canMove(newX, newY)) {
            if (world[newX][newY] == Tileset.FINAL_EXAMS) {
                isGameOver = true;
                audioMusic.play("victory.mp3");
                WinScreen thing = new WinScreen(width, height, worldSeed);
            }
            if (world[newX][newY] == Tileset.KILLBRICK) {
                takeDamage(1);
            }
            if (world[newX][newY] == Tileset.COIN) {
                audioSound.play("coin.mp3");
                currentPlayer.totalCoins += 1;
            }
            if (world[newX][newY].id() == -1) {
                String description = world[newX][newY].description();
                ArrayList temp = inventory.inventoryList;
                temp.add(temp.indexOf(null), ItemList.getItem(description));
            }
            world[playerPosition.x][playerPosition.y] = Tileset.FLOOR;
            playerPosition.move(newX, newY);
            world[newX][newY] = selectedAvatar;
        }
    }

    private void takeDamage(int amount) {
        if (currentPlayer.canBeDamaged) {
            audioSound.play("hit.mp3");
            currentPlayer.damageTimer = 10;
            currentPlayer.canBeDamaged = false;

            currentPlayer.numHearts -= amount;
            if (currentPlayer.numHearts == 2) {
                audioSound.play("needheal.mp3");
            }
            StdDraw.clear(Color.RED);
            StdDraw.show();
            StdDraw.pause(20);
        }
    }
    private boolean canMove(int x, int y) {
        if (world[x][y].equals(Tileset.LOCKED_DOOR) && !ItemList.KEY.isEquipped) {
            return false;
        }
        int invSize = currentPlayer.playerInventory.numSlotsUsed();
        int maxSize = currentPlayer.playerInventory.maxCapacity;
        if (invSize == maxSize && (world[x][y].id() == -1)) {
            return false;
        }
        return isInBounds(x, y) && (!world[x][y].equals(Tileset.WALL) && !world[x][y].equals(Tileset.NOTHING) && !world[x][y].equals(Tileset.SKULL));
    }

    public TETile[][] getWorld() {
        return world;
    }

    public long getWorldSeed() {
        return this.worldSeed;
    }

    public Point getPlayerPosition() {
        return this.playerPosition;
    }

    public void setPlayerPosition(int x, int y) {
        world[playerPosition.x][playerPosition.y] = Tileset.FLOOR;
        playerPosition = new Point(x, y);
        world[x][y] = Tileset.AVATAR;
    }
}