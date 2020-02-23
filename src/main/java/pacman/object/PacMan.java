package pacman.object;

import pacman.game.GameContainer;
import pacman.game.GraphicsConfiguration;
import pacman.graphics.Renderer;
import pacman.graphics.AnimatedImage;
import javafx.geometry.Rectangle2D;

import java.lang.Math;

public class PacMan extends GameObject {
    private AnimatedImage pacman;
    private final int[][] maze;
    private int speed;

    public PacMan(int column, int row, GraphicsConfiguration conf, int[][] maze) {
        super(conf.getTileX(column), conf.getTileY(row), conf.getTileWidth(), conf.getTileHeight(), "Pacman");

        this.speed = 120;

        this.pacman = new AnimatedImage("images/pacman_sprites.png", 400, 4, 4, 0, 0, 36, 36);

        this.maze = maze;
    }

    private boolean isAnAvailableTile(int row, int column) {
        return maze[row][column] == 1;
    }


    private boolean canGoToTheRight() {
        int row = (int) Math.floor(y / height);
        int column = (int) Math.floor(x / width) + 1;

        return isAnAvailableTile(row, column);
    }


    private boolean canGoDown() {
        int row = (int) Math.floor(y / height) + 1;
        int column = (int) Math.floor(x / width);

        return isAnAvailableTile(row, column);
    }


    private boolean canGoToTheLeft() {
        int row = (int) Math.floor(y / height);
        int column = (int) Math.ceil(x / width) - 1;

        return isAnAvailableTile(row, column);
    }


    private boolean canGoUp() {
        int row = (int) Math.ceil(y / height) - 1;
        int column = (int) Math.floor(x / width);

        return isAnAvailableTile(row, column);
    }

    @Override
    public void update(GameContainer gc, float dt) {
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        Rectangle2D pos = pacman.getCurrentFramePosition();
        r.drawImage(pacman.getImage(), pos.getMinX(), pos.getMinY(),
                pos.getWidth(), pos.getHeight(), x, y,
                pos.getWidth(), pos.getHeight());
    }

}
