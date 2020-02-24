package pacman.state;

import pacman.game.GameConfiguration;
import pacman.object.ObjectManager;
import pacman.game.State;
import pacman.game.GameContainer;
import pacman.graphics.Renderer;
import pacman.object.*;

import java.util.ArrayList;
import java.util.List;


public class MazeState extends State {

    public MazeState(GameConfiguration conf) {
        manager.addObject(new GameImage("images/floor.png", conf));

        List<GameObject> targets = buildMaze(manager, conf);

        manager.addObject(new PacMan(1, 1, conf, targets));
    }

    public List<GameObject> buildMaze(ObjectManager manager, GameConfiguration conf) {
        List<GameObject> result = new ArrayList<>();
        for (int i = 0; i < conf.getRowNum(); i++) {
            for (int j = 0; j < conf.getColumnNum(); j++) {
                if (conf.isWall(i, j)) {
                    manager.addObject(new Wall(i, j, conf, "wall"));
                } else if (i != 1 || j != 1) {
                    GameObject point = new PurplePoint(i, j, conf, String.format("point_%d_%d", j, i));
                    manager.addObject(point);
                    result.add(point);
                }
            }
        }
        return result;
    }

    @Override
    public void update(GameContainer gc, double dt) {
        manager.updateObjects(gc, dt);
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        manager.renderObjects(gc, r);
    }

    public ObjectManager getManager() {
        return manager;
    }

    public void setManager(ObjectManager manager) {
        this.manager = manager;
    }

}
