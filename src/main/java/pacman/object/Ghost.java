package pacman.object;

import javafx.scene.image.Image;
import pacman.algorithm.Direction;
import pacman.algorithm.MiniMaxShortestPath;
import pacman.game.GameConfiguration;
import pacman.game.GameContainer;
import pacman.graphics.Renderer;

import static pacman.algorithm.Direction.DOWN;
import static pacman.algorithm.Direction.LEFT;
import static pacman.algorithm.Direction.NONE;
import static pacman.algorithm.Direction.RIGHT;
import static pacman.algorithm.Direction.SAME;
import static pacman.algorithm.Direction.UP;

public class Ghost extends GameObject {
    private static final String IMAGE_PATH = "images/ghost.png";

    private Image image;
    private MiniMaxShortestPath searchAlgorithm;

    private Direction currentDirection;
    private int speed;


    public void setSearchAlgorithm(MiniMaxShortestPath searchAlgorithm) {
        this.searchAlgorithm = searchAlgorithm;
    }


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
        if (nextDir == NONE) {
            return;
        }

        if (nextDir != SAME && nextDir != currentDirection) {
            if ((currentDirection == LEFT || currentDirection == RIGHT) && (nextDir == DOWN || nextDir == UP)) {
                int col = configuration.getColumn(this);
                setCenterX(configuration.getTileCenterX(col));
            } else if ((currentDirection == DOWN || currentDirection == UP) && (nextDir == RIGHT || nextDir == LEFT)) {
                int row = configuration.getRow(this);
                setCenterY(configuration.getTileCenterY(row));
            }
            currentDirection = nextDir;
        }

        double posChange = dt * speed;
        if (currentDirection.equals(RIGHT)) {
            this.x += posChange;
        } else if (currentDirection.equals(DOWN)) {
            this.y += posChange;
        } else if (currentDirection.equals(LEFT)) {
            this.x -= posChange;
        } else if (currentDirection.equals(UP)) {
            this.y -= posChange;
        }
    }

    @Override
    public void update(GameContainer gameContainer, double dt) {
        GameConfiguration configuration = gameContainer.getConfiguration();
        Direction nextDirection = searchAlgorithm.getGhostNextDirection();
        System.out.println("Ghost direction: " + nextDirection);

        go(nextDirection, dt, configuration);
    }

    @Override
    public void render(GameContainer gameContainer, Renderer renderer) {
        renderer.drawImage(image, 0, 0, 36, 36, x, y, 36, 36);
    }

    @Override
    public void collideWith(GameObject obj) {
        if (obj instanceof PacMan) {
            System.out.println("Ghost has eaten the pacman, game over");
            obj.makeDead();
        }
    }
}
