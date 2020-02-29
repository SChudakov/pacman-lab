package pacman.algorithm;

import pacman.game.GameConfiguration;
import pacman.object.GameObject;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import static pacman.algorithm.Direction.NONE;

public class BFSShortestPath extends AbstractShortestPath {
    private static int count = 0;

    public BFSShortestPath(GameConfiguration conf) {
        super(conf);
    }

    @Override
    public Direction getNextDirection(GameConfiguration conf, GameObject object, List<GameObject> targets) {
        invokeAlgorithm();
        Position position = conf.getPosition(object);
        int[][] visited = new int[conf.getRowNum()][conf.getColumnNum()];

        Direction[][] directions = new Direction[conf.getRowNum()][conf.getColumnNum()];

        initVisited(targets, conf, visited);
        return bfs(visited, directions, position);
    }

    private Direction bfs(int[][] visited, Direction[][] directions, Position start) {
        Queue<Position> queue = new ArrayDeque<>();
        queue.add(start);
        while (!queue.isEmpty()) {
            Position position = queue.poll();
            int i = position.row, j = position.col;
            if (visited[i][j] == 2) {
                return getDirection(directions, i, j);
            } else if (visited[i][j] == 0) {
                visited[i][j] = 1;
                List<Position> steps = getSteps(position);
                for (Position next : steps) {
                    int row = next.row;
                    int col = next.col;
                    if (validPosition(next) && visited[row][col] != 1) {
                        directions[row][col] = getDirection(i - row, j - col);
                        queue.add(next);
                    }
                }
            }
        }
        return NONE;
    }
}
