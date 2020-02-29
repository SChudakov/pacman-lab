package pacman.algorithm;

import pacman.game.GameConfiguration;
import pacman.object.GameObject;

import java.util.Arrays;
import java.util.List;

import static pacman.algorithm.Direction.DOWN;
import static pacman.algorithm.Direction.LEFT;
import static pacman.algorithm.Direction.RIGHT;
import static pacman.algorithm.Direction.SAME;
import static pacman.algorithm.Direction.UP;

public abstract class AbstractShortestPath implements ShortestPath {
    protected final GameConfiguration conf;
    protected int count = 0;

    public AbstractShortestPath(GameConfiguration conf) {
        this.conf = conf;
    }

    protected void invokeAlgorithm() {
        count++;
    }

    protected boolean validPosition(int i, int j) {
        return i >= 0 && j >= 0 && i < conf.getRowNum() && j < conf.getColumnNum() && !conf.isWall(i, j);
    }

    protected boolean validPosition(Position pos) {
        return validPosition(pos.row, pos.col);
    }

    protected List<Position> getSteps(Position pos) {
        int row = pos.row, col = pos.col;
        return Arrays.asList(Position.of(row + 1, col), Position.of(row, col + 1), Position.of(row - 1, col), Position.of(row, col - 1));
    }

    @Override
    public int getCount() {
        return count;
    }

    protected Direction getDirection(int i, int j) {
        if (i == 1) {
            return DOWN;
        } else if (i == -1) {
            return UP;
        } else if (j == 1) {
            return RIGHT;
        } else {
            return LEFT;
        }
    }

    protected int getColumn(Direction dir, int col) {
        switch (dir) {
            case LEFT:
                return col - 1;
            case RIGHT:
                return col + 1;
            default:
                return col;
        }
    }

    protected int getRow(Direction dir, int row) {
        switch (dir) {
            case UP:
                return row - 1;
            case DOWN:
                return row + 1;
            default:
                return row;
        }
    }


    protected void initVisited(List<GameObject> targets, GameConfiguration conf, int[][] visited) {
        for (GameObject obj : targets) {
            visited[conf.getRow(obj)][conf.getColumn(obj)] = 2;
        }
    }

    public Direction getDirection(Direction[][] directions, int i, int j) {
        Direction direction = null;
        while (directions[i][j] != null) {
            direction = directions[i][j];
            i = getRow(direction, i);
            j = getColumn(direction, j);
        }
        if (direction == null) {
            return SAME;
        }
        return direction.getOpposite();
    }
}
