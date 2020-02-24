package pacman.algo;

import pacman.game.GameConfiguration;
import pacman.object.GameObject;

import java.util.List;

public interface ShortestPath {

    Direction getNextDirection(GameConfiguration conf, GameObject obj, List<GameObject> targets);
}
