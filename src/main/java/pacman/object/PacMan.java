package pacman.object;

import javafx.geometry.Rectangle2D;
import pacman.algorithm.AStartShortestPath;
import pacman.algorithm.Direction;
import pacman.algorithm.ShortestPath;
import pacman.game.GameConfiguration;
import pacman.game.GameContainer;
import pacman.graphics.AnimatedImage;
import pacman.graphics.Renderer;

import java.util.List;
import java.util.Objects;

import static pacman.algorithm.Direction.DOWN;
import static pacman.algorithm.Direction.LEFT;
import static pacman.algorithm.Direction.NONE;
import static pacman.algorithm.Direction.RIGHT;
import static pacman.algorithm.Direction.SAME;
import static pacman.algorithm.Direction.UP;

public class PacMan extends GameObject {
    private AnimatedImage pacman;
    private int speed;
    private ShortestPath searchAlg;
    private List<GameObject> targets;
    private Direction currentDir = NONE;

    public PacMan(int row, int column, GameConfiguration configuration, List<GameObject> targets) {
        super(configuration.getTileX(column), configuration.getTileY(row), configuration.getTileWidth(), configuration.getTileHeight(), "Pacman");
        Objects.requireNonNull(configuration);
        Objects.requireNonNull(targets);

        this.speed = 100;
        this.searchAlg = new AStartShortestPath(configuration, (p1, p2) -> 0.0);
        this.pacman = new AnimatedImage("images/pacman_sprites.png", 400, 4, 4, 0, 0, 36, 36);

        this.targets = targets;
    }

    private void go(Direction nextDir, double dt, GameConfiguration conf) {
        if (nextDir == NONE) {
            System.out.println("Count = " + searchAlg.getCount());
            return;
        }
        if (nextDir != SAME && nextDir != currentDir) {
            if ((currentDir == LEFT || currentDir == RIGHT) && (nextDir == DOWN || nextDir == UP)) {
                int col = conf.getColumn(this);
                setCenterX(conf.getTileCenterX(col));
            } else if ((currentDir == DOWN || currentDir == UP) && (nextDir == RIGHT || nextDir == LEFT)) {
                int row = conf.getRow(this);
                setCenterY(conf.getTileCenterY(row));
            }
            currentDir = nextDir;
        }
        double posChange = dt * speed;
        switch (currentDir) {
            case RIGHT:
                pacman.setOffSetY(0);
                this.x += posChange;
                break;
            case DOWN:
                pacman.setOffSetY(36);
                this.y += posChange;
                break;
            case LEFT:
                pacman.setOffSetY(72);
                this.x -= posChange;
                break;
            case UP:
                pacman.setOffSetY(108);
                this.y -= posChange;
                break;
            default:
        }
    }

    @Override
    public void update(GameContainer gameContainer, double dt) {
        targets.removeIf(GameObject::isDead);
        GameConfiguration configuration = gameContainer.getConfiguration();
        Direction nextDir = searchAlg.getNextDirection(configuration, this, targets);

        go(nextDir, dt, configuration);
        pacman.nextFrame(dt);
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        GameConfiguration conf = gc.getConfiguration();
        Rectangle2D pos = pacman.getCurrentFramePosition();
        r.drawImage(pacman.getImage(), pos.getMinX(), pos.getMinY(),
                pos.getWidth(), pos.getHeight(), x, y,
                pos.getWidth(), pos.getHeight());

        if (conf.isDrawCollider()) {
            r.drawRectangle(collider.getX(), collider.getY(), collider.getColliderWidth(), collider.getColliderHeight());
        }
    }


    @Override
    public void collideWith(GameObject obj) {
        if (obj instanceof PurplePoint) {
            obj.makeDead();
        }
    }

}
