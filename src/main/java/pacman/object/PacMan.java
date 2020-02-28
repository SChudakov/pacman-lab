package pacman.object;

import pacman.algo.BfsShortestPath;
import pacman.algo.DfsShortestPath;
import pacman.algo.Direction;
import pacman.algo.ShortestPath;
import pacman.game.GameContainer;
import pacman.game.GameConfiguration;
import pacman.graphics.Renderer;
import pacman.graphics.AnimatedImage;
import javafx.geometry.Rectangle2D;

import static pacman.algo.Direction.*;

import java.util.List;

public class PacMan extends GameObject {
    private AnimatedImage pacman;
    private int speed;
    private ShortestPath searchAlg;
    private List<GameObject> targets;
    private Direction currentDir = NONE;

    public PacMan(int row, int column, GameConfiguration conf, List<GameObject> targets) {
        super(conf.getTileX(column), conf.getTileY(row), conf.getTileWidth(), conf.getTileHeight(), "Pacman");

        this.speed = 400;
        this.searchAlg = new DfsShortestPath(conf);
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
    public void update(GameContainer gc, double dt) {
        targets.removeIf(GameObject::isDead);
        GameConfiguration conf = gc.getConfiguration();
        Direction nextDir = searchAlg.getNextDirection(conf, this, targets);

        go(nextDir, dt, conf);
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
