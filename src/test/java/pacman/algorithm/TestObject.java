package pacman.algorithm;

import pacman.game.GameConfiguration;
import pacman.object.GameObject;

public class TestObject extends GameObject {
    public TestObject(int row, int col, GameConfiguration conf) {
        super(conf.getTileX(col), conf.getTileY(row), conf.getTileWidth(), conf.getTileHeight(), "");
    }
}
