package pacman.object;

import javafx.geometry.Rectangle2D;
import pacman.algorithm.Direction;
import pacman.algorithm.MiniMaxShortestPath;
import pacman.game.GameConfiguration;
import pacman.game.GameContainer;
import pacman.graphics.AnimatedImage;
import pacman.graphics.Renderer;

import static pacman.algorithm.Direction.DOWN;
import static pacman.algorithm.Direction.LEFT;
import static pacman.algorithm.Direction.NONE;
import static pacman.algorithm.Direction.RIGHT;
import static pacman.algorithm.Direction.SAME;
import static pacman.algorithm.Direction.UP;

public class PacMan extends GameObject {
    private static final String IMAGE_PATH = "images/pacman_sprites.png";

    private AnimatedImage pacman;
    private MiniMaxShortestPath searchAlgorithm;

    private Direction currentDirection;
    private int speed;

    private int score;

    public void setSearchAlgorithm(MiniMaxShortestPath searchAlgorithm) {
        this.searchAlgorithm = searchAlgorithm;
    }


    public PacMan(int row, int column, GameConfiguration configuration) {
        super(configuration.getTileX(column), configuration.getTileY(row), configuration.getTileWidth(), configuration.getTileHeight(), "Pacman");

        this.pacman = new AnimatedImage(IMAGE_PATH, 400, 4, 4, 0, 0, 36, 36);
        this.currentDirection = NONE;
        this.speed = 100;
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
            pacman.setOffSetY(0);
            this.x += posChange;
        } else if (currentDirection.equals(DOWN)) {
            pacman.setOffSetY(36);
            this.y += posChange;
        } else if (currentDirection.equals(LEFT)) {
            pacman.setOffSetY(72);
            this.x -= posChange;
        } else if (currentDirection.equals(UP)) {
            pacman.setOffSetY(108);
            this.y -= posChange;
        }
    }

    @Override
    public void update(GameContainer gameContainer, double dt) {
        GameConfiguration configuration = gameContainer.getConfiguration();
        Direction nextDirection = searchAlgorithm.getPacmanNextDirection();
        System.out.println("Pacman direction: " + nextDirection);

        go(nextDirection, dt, configuration);
        pacman.nextFrame(dt);
    }

    @Override
    public void render(GameContainer gameContainer, Renderer renderer) {
        GameConfiguration configuration = gameContainer.getConfiguration();
        Rectangle2D rectangle2D = pacman.getCurrentFramePosition();
        renderer.drawImage(pacman.getImage(),
                rectangle2D.getMinX(), rectangle2D.getMinY(),
                rectangle2D.getWidth(), rectangle2D.getHeight(),
                x, y,
                rectangle2D.getWidth(), rectangle2D.getHeight());

        if (configuration.isDrawCollider()) {
            renderer.drawRectangle(collider.getX(), collider.getY(), collider.getColliderWidth(), collider.getColliderHeight());
        }
    }


    @Override
    public void collideWith(GameObject obj) {
        if (obj instanceof PurplePoint) {
            System.out.println("PackMan has eaten a point, current score: " + ++score);
            if (obj.equals(searchAlgorithm.getPacmanTarget())) {
                searchAlgorithm.setPacmanTarget(null);
            }
            obj.makeDead();
        }
    }
}