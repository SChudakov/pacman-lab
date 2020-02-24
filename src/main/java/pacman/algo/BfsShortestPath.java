package pacman.algo;

import javafx.geometry.Pos;
import pacman.game.GameConfiguration;
import pacman.object.GameObject;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import static pacman.algo.Direction.*;

public class BfsShortestPath extends AbstractShortestPath {
    private static int count = 0;

    public BfsShortestPath(GameConfiguration conf) {
        super(conf);
    }

    @Override
    public Direction getNextDirection(GameConfiguration conf, GameObject obj, List<GameObject> targets) {
        invokeAlgorithm();
        Position pos = conf.getPosition(obj);
        int[][] visited = new int[conf.getRowNum()][conf.getColumnNum()];

        Direction[][] dirs = new Direction[conf.getRowNum()][conf.getColumnNum()];

        initVisited(targets, conf, visited);
        return bfs(visited, dirs, pos);
    }

    private Direction bfs(int[][] visited, Direction[][] dirs, Position start) {
        Queue<Position> q = new ArrayDeque<>();
        q.add(start);
        while (!q.isEmpty()) {
            Position pos = q.poll();
            int i = pos.row, j = pos.col;
            if (visited[i][j] == 2) {
                return getDir(dirs, i, j);
            } else if (visited[i][j] == 0) {
                visited[i][j] = 1;
                List<Position> steps = getSteps(pos);
                for (Position next : steps) {
                    int row = next.row;
                    int col = next.col;
                    if (validPosition(next) && visited[row][col] != 1) {
                        dirs[row][col] = getDirection(i - row, j - col);
                        q.add(next);
                    }
                }
            }
        }
        return NONE;
    }

    private Direction getDir(Direction[][] dirs, int i, int j) {
        Direction direction = null;
        while (dirs[i][j] != null) {
            direction = dirs[i][j];
            i = getRow(direction, i);
            j = getColumn(direction, j);
        }
        if (direction == null) {
            return SAME;
        }
        return direction.getOpposite();
    }
}
