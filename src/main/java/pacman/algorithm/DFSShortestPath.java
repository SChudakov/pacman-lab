package pacman.algorithm;

import org.apache.commons.lang3.tuple.Pair;
import pacman.game.GameConfiguration;
import pacman.object.GameObject;

import java.util.List;

import static pacman.algorithm.Direction.NONE;
import static pacman.algorithm.Direction.SAME;

public class DFSShortestPath extends AbstractShortestPath {
    public DFSShortestPath(GameConfiguration conf) {
        super(conf);
    }

    public Direction getNextDirection(GameConfiguration conf, GameObject obj, List<GameObject> targets) {
        invokeAlgorithm();
        Position position = conf.getPosition(obj);
        // 0 = not visited, 1 = visited, 2 = target
        int[][] visited = new int[conf.getRowNum()][conf.getColumnNum()];
        initVisited(targets, conf, visited);
        if (visited[position.row][position.col] == 2) {
            return SAME;
        }
        return dfs(visited, position).getKey();
    }

    private Pair<Direction, Integer> dfs(int[][] visited, Position position) {
        if (!validPosition(position) || visited[position.row][position.col] == 1) {
            return Pair.of(NONE, Integer.MAX_VALUE);
        } else if (visited[position.row][position.col] == 2) {
            return Pair.of(NONE, 0);
        }
        visited[position.row][position.col] = 1;

        List<Position> steps = getSteps(position);

        Direction resultDirection = NONE;
        int minimumSteps = Integer.MAX_VALUE;

        for (Position next : steps) {
            Pair<Direction, Integer> p = dfs(visited, next);
            if (p.getValue() < minimumSteps) {
                resultDirection = position.directionTo(next);
                minimumSteps = p.getValue();
            }
        }
        if (minimumSteps != Integer.MAX_VALUE) {
            minimumSteps += 1;
        }
        return Pair.of(resultDirection, minimumSteps);
    }
}
