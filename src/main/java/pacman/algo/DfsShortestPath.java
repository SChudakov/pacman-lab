package pacman.algo;

import javafx.geometry.Pos;
import org.apache.commons.lang3.tuple.Pair;
import pacman.game.GameConfiguration;
import pacman.object.GameObject;

import static pacman.algo.Direction.*;

import java.util.Arrays;
import java.util.List;

public class DfsShortestPath extends AbstractShortestPath {
    public DfsShortestPath(GameConfiguration conf) {
        super(conf);
    }

    public Direction getNextDirection(GameConfiguration conf, GameObject obj, List<GameObject> targets) {
        invokeAlgorithm();
        Position pos = conf.getPosition(obj);
        // 0 = not visited, 1 = visited, 2 = target
        int[][] visited = new int[conf.getRowNum()][conf.getColumnNum()];
        initVisited(targets, conf, visited);
        if (visited[pos.row][pos.col] == 2) {
            return SAME;
        }
        return dfs(visited, pos).getKey();
    }

    private Pair<Direction, Integer> dfs(int[][] visited, Position pos) {
        if (!validPosition(pos) || visited[pos.row][pos.col] == 1) {
            return Pair.of(NONE, Integer.MAX_VALUE);
        } else if (visited[pos.row][pos.col] == 2) {
            return Pair.of(NONE, 0);
        }
        visited[pos.row][pos.col] = 1;

        List<Position> steps = getSteps(pos);

        Direction resDir = NONE;
        int minSteps = Integer.MAX_VALUE;

        for (Position next : steps) {
            Pair<Direction, Integer> res = dfs(visited, next);
            if (res.getValue() < minSteps) {
                resDir = pos.dirTo(next);
                minSteps = res.getValue();
            }
        }
        if (minSteps != Integer.MAX_VALUE) {
            minSteps += 1;
        }
        return Pair.of(resDir, minSteps);
    }
}
