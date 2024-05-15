import core.AutograderBuddy;
import edu.princeton.cs.algs4.StdDraw;
import org.junit.jupiter.api.Test;
import tileengine.TERenderer;
import tileengine.TETile;

import static com.google.common.truth.Truth.assertThat;

public class AutograderBuddyTest {
    @Test
    public void multipleInputTest() {
        TETile[][] tiles = AutograderBuddy.getWorldFromInput("n1392967723524655428sddsaawwsaddw");
        TETile[][] tilesTwo = AutograderBuddy.getWorldFromInput("n1392967723524655428sddsaawws:q" + "\n" + "laddw");
        assertThat(tiles).isEqualTo(tilesTwo);
    }

    @Test
    public void multipleInputTestTwo() {
        AutograderBuddy.getWorldFromInput("n1392967723524655428sddsaawwsaddw");
        AutograderBuddy.getWorldFromInput("n1392967723524655428sddsaawws:q" + "\n" + "laddw");
    }

    @Test
    public void newGameSave() {
        AutograderBuddy.getWorldFromInput("n1392967723524655428sddsaawwsaddw");
    }
}
