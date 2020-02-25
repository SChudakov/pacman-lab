package pacman.algorithm;

import pacman.game.GameConfiguration;
import pacman.object.GameObject;

import java.util.List;

public class GreedyShortestPath extends AbstractShortestPath {
    public GreedyShortestPath(GameConfiguration conf) {
        super(conf);
    }

    @Override
    public Direction getNextDirection(GameConfiguration conf, GameObject obj, List<GameObject> targets) {
        return null;
    }
}
