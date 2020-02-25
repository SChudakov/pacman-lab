package pacman.state;

import org.apache.commons.lang3.tuple.Pair;
import pacman.game.GameConfiguration;
import pacman.game.GameContainer;
import pacman.game.State;
import pacman.graphics.Renderer;
import pacman.object.GameImage;
import pacman.object.GameObject;
import pacman.object.ObjectManager;
import pacman.object.PacMan;
import pacman.object.PurplePoint;
import pacman.object.Wall;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MazeState extends State {
    public MazeState(GameConfiguration conf) {
        manager.addObject(new GameImage("images/floor.png", conf));

        List<GameObject> targets = buildMaze(manager, conf);
        manager.addObject(new PacMan(1, 1, conf, targets));
    }

    public List<GameObject> buildMaze(ObjectManager manager, GameConfiguration conf) {
        List<Pair<Integer, Integer>> notFilledPoints = new ArrayList<>();
        for (int i = 0; i < conf.getRowNum(); i++) {
            for (int j = 0; j < conf.getColumnNum(); j++) {
                if (conf.isWall(i, j)) {
                    manager.addObject(new Wall(i, j, conf, "wall"));
                } else if (i != 1 || j != 1) {
                    notFilledPoints.add(Pair.of(i, j));
                }
            }
        }

        List<GameObject> result = new ArrayList<>();
        Pair<Integer, Integer> randomPoint = getRandom(notFilledPoints);
        int i = randomPoint.getKey();
        int j = randomPoint.getValue();

        GameObject point = new PurplePoint(i, j, conf, String.format("point_%d_%d", j, i));
        manager.addObject(point);
        result.add(point);

        return result;
    }

    private Pair<Integer, Integer> getRandom(List<Pair<Integer, Integer>> objects) {
        Random random = new Random();
        int randomPosition = random.nextInt(objects.size());
        return objects.get(randomPosition);
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
