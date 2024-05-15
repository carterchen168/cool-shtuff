package core;

import tileengine.TETile;
import tileengine.Tileset;
import utils.FileUtils;

public class AutograderBuddy {

    private static final String FILENAME = "saveGame.txt";
    private static TETile selectedAvatar = Tileset.AVATAR;

    public static TETile[][] getWorldFromInput(String input) {
        int width = 50;
        int height = 50;
        long saveSeed;
        String movements;

        String[] lines = input.split("\n");
        for (String line : lines) {
            System.out.println(line);
        }
        World placeholder = null;
        for (String line : lines) {
            line = line.toUpperCase();

            // Extracting the seed and movements, handle loading if necessary
            if (line.startsWith("N")) {
                int seedEnd = line.indexOf('S');
                if (seedEnd == -1) {
                    throw new IllegalArgumentException("'N' must be followed by a seed ending with 'S'.");
                }
                saveSeed = Long.parseLong(line.substring(1, seedEnd));
                movements = line.substring(seedEnd + 1);

                World world = new World(width, height, saveSeed, selectedAvatar);


                for (char move : movements.toCharArray()) {
                    world.moveAvatar(move);
                }
                if (line.endsWith(":Q")) {
                    saveWorldState(world);
                }
                placeholder = world;
            } else if (line.startsWith("L")) {
                String savedData = FileUtils.readFile(FILENAME);
                String[] savedParts = savedData.split("\n");
                saveSeed = Long.parseLong(savedParts[0]); // Extract seed from saved data
                String[] savePlayerPosition = savedParts[1].split(" ");

                int savePlayerX = Integer.parseInt(savePlayerPosition[0]);
                System.out.println(savePlayerX);
                int savePlayerY = Integer.parseInt(savePlayerPosition[1]);
                System.out.println(savePlayerY);
                movements = line.substring(1); // Exclude 'L' from movements


                World world = new World(width, height, saveSeed, selectedAvatar);
                world.setPlayerPosition(savePlayerX, savePlayerY);


                for (char move : movements.toCharArray()) {
                    world.moveAvatar(move);
                }
                if (line.endsWith(":Q")) {
                    saveWorldState(world);
                }
                placeholder = world;
            } else {
                throw new IllegalArgumentException("Input must start with 'N' or 'L'.");

            }

            /**
            //  create or load the world based on the seed
            World world = new World(width, height, saveSeed);

            for (char move : allMovements.toCharArray()) {
                world.moveAvatar(move);
            }

            // save the world state if input ends with ":Q"
            if (line.endsWith(":Q")) {
                saveWorldState(world);
            }

            placeholder = world;
             */
        }
        return placeholder.getWorld();
    }
    /**
    input = input.toUpperCase();

    // Extracting the seed and movements, handle loading if necessary
        if (input.startsWith("N")) {
        int seedEnd = input.indexOf('S');
        if (seedEnd == -1) {
            throw new IllegalArgumentException("'N' must be followed by a seed ending with 'S'.");
        }
        saveSeed = Long.parseLong(input.substring(1, seedEnd));
        System.out.println(saveSeed);
        movements = input.substring(seedEnd + 1);
    } else if (input.startsWith("L")) {
        String savedData = FileUtils.readFile(FILENAME);
        String[] savedParts = savedData.split("\\s+");
        saveSeed = Long.parseLong(savedParts[2]); // Extract seed from saved data
        movements = input.substring(1); // Exclude 'L' from movements
    } else {
        throw new IllegalArgumentException("Input must start with 'N' or 'L'.");
    }

    //  create or load the world based on the seed
    World world = new World(width, height, saveSeed);

        for (char move : movements.replaceAll(":Q", "").toCharArray()) {
        world.moveAvatar(move);
    }

    // save the world state if input ends with ":Q"
        if (input.endsWith(":Q")) {
        saveWorldState(world);
    }
        */
    private static void saveWorldState(World world) {
        FileUtils.writeFile(FILENAME, world.getWorldSeed()
                + "\n"
                + world.getPlayerPosition().x
                + " "
                + world.getPlayerPosition().y);
    }

    /**
     * Used to tell the autograder which tiles are the floor/ground (including
     * any lights/items resting on the ground). Change this
     * method if you add additional tiles.
     */
    public static boolean isGroundTile(TETile t) {
        return t.character() == Tileset.FLOOR.character()
                || t.character() == Tileset.AVATAR.character()
                || t.character() == Tileset.FLOWER.character();
    }

    /**
     * Used to tell the autograder while tiles are the walls/boundaries. Change
     * this method if you add additional tiles.
     */
    public static boolean isBoundaryTile(TETile t) {
        return t.character() == Tileset.WALL.character()
                || t.character() == Tileset.LOCKED_DOOR.character()
                || t.character() == Tileset.UNLOCKED_DOOR.character();
    }
}
