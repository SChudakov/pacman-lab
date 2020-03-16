package pacman.state;

import pacman.game.GameConfiguration;
import pacman.game.GameContainer;
import pacman.game.State;
import pacman.graphics.Renderer;
import pacman.object.GameImage;
import pacman.object.GameObject;
import pacman.object.Ghost;
import pacman.object.ObjectManager;
import pacman.object.PacMan;
import pacman.object.PurplePoint;
import pacman.object.Wall;

import java.util.ArrayList;
import java.util.List;


public class MazeState extends State {
    private static final String FLOOR_IMAGE_PATH = "images/floor.png";
    private static final String WALL_TAG = "wall";
    private static final String POINT_TAG = "point_%d_%d";

    public MazeState(GameConfiguration configuration) {
        manager.addObject(new GameImage(FLOOR_IMAGE_PATH, configuration));

        List<GameObject> targets = buildMaze(manager, configuration);

        manager.addObject(new PacMan(1, 1, configuration, targets));
        manager.addObject(new Ghost(9, 18, configuration, 100));
    }


    public List<GameObject> buildMaze(ObjectManager manager, GameConfiguration configuration) {
        List<GameObject> result = new ArrayList<>();
        for (int i = 0; i < configuration.getRowNum(); i++) {
            for (int j = 0; j < configuration.getColumnNum(); j++) {
                if (configuration.isWall(i, j)) {
                    manager.addObject(new Wall(i, j, configuration, WALL_TAG));
                } else if (i != 1 || j != 1) {
                    PurplePoint point = new PurplePoint(i, j, configuration, String.format(POINT_TAG, j, i));
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
}
