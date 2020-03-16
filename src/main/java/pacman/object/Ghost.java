package pacman.object;

import javafx.scene.image.Image;
import pacman.algorithm.Direction;
import pacman.game.GameConfiguration;
import pacman.game.GameContainer;
import pacman.graphics.Renderer;

import static pacman.algorithm.Direction.NONE;

public class Ghost extends GameObject {
    private static final String IMAGE_PATH = "images/ghost.png";

    private Image image;

    private Direction currentDirection;
    private int speed;

    public Ghost(int row, int column, GameConfiguration configuration, int speed) {
        super(configuration.getTileX(column), configuration.getTileY(row),
                configuration.getTileWidth(), configuration.getTileHeight(), "Ghost");
        if (speed < 0) {
            throw new IllegalArgumentException("Speed must be positive");
        }

        this.image = new Image(IMAGE_PATH);
        this.currentDirection = NONE;
        this.speed = speed;
    }

    protected void go(Direction nextDir, double dt, GameConfiguration configuration) {

    }

    @Override
    public void update(GameContainer gameContainer, double dt) {

    }

    @Override
    public void render(GameContainer gameContainer, Renderer renderer) {
        renderer.drawImage(image, 0, 0, 36, 36, x, y, 36, 36);
    }

    @Override
    public void collideWith(GameObject obj) {

    }
}
