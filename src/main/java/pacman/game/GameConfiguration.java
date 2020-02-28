package pacman.game;

import pacman.algo.Position;
import pacman.object.GameObject;

public class GameConfiguration {
    static final int[][] DEFAULT_MAZE = {{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0},
            {0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0},
            {0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0},
            {1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1},
            {0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0},
            {0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0},
            {0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};

    public static class GameConfigurationBuilder {
        private int windowWidth;
        private int windowHeight;
        private int[][] maze;
        private boolean drawCollider;

        public GameConfiguration build() {
            if (windowWidth <= 0) {
                windowWidth = 720;
            }
            if (windowHeight <= 0) {
                windowHeight = 396;
            }
            if (maze == null) {
                maze = DEFAULT_MAZE;
            }
            return new GameConfiguration(windowWidth, windowHeight, maze, drawCollider);
        }

        public GameConfigurationBuilder setWindowWidth(int windowWidth) {
            this.windowWidth = windowWidth;
            return this;
        }

        public GameConfigurationBuilder setWindowHeight(int windowHeight) {
            this.windowHeight = windowHeight;
            return this;
        }

        public GameConfigurationBuilder setMaze(int[][] maze) {
            this.maze = maze;
            return this;
        }

        public GameConfigurationBuilder setDrawCollider(boolean drawCollider) {
            this.drawCollider = drawCollider;
            return this;
        }
    }


    private int windowWidth;
    private int windowHeight;
    private int[][] maze;
    private int rowNum;
    private int columnNum;
    private boolean drawCollider;

    private GameConfiguration(int windowWidth, int windowHeight, int[][] maze, boolean drawCollider) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.maze = maze;
        this.rowNum = maze.length;
        this.columnNum = maze[0].length;
        this.drawCollider = drawCollider;
    }

    public boolean isWall(int row, int col) {
        return maze[row][col] == 0;
    }

    public boolean isDrawCollider() {
        return drawCollider;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public int getRowNum() {
        return rowNum;
    }

    public int getColumnNum() {
        return columnNum;
    }

    public double getTileWidth() {
        return (double) windowWidth / columnNum;
    }

    public double getTileHeight() {
        return (double) windowHeight / rowNum;
    }

    public double getTileX(int columnNum) {
        return getTileWidth() * columnNum;
    }

    public double getTileCenterX(int columnNum) {
        return getTileX(columnNum) + getTileWidth() / 2;
    }

    public double getTileCenterY(int rowNum) {
        return getTileY(rowNum) + getTileHeight() / 2;
    }

    public int getColumn(GameObject obj) {
        return (int) Math.floor(obj.getCenterX() / getTileWidth());
    }

    public int getRow(GameObject obj) {
        return (int) Math.floor(obj.getCenterY() / getTileHeight());
    }

    public Position getPosition(GameObject obj) {
        return Position.of(getRow(obj), getColumn(obj));
    }

    public double getTileY(int rowNum) {
        return getTileHeight() * rowNum;
    }
}
