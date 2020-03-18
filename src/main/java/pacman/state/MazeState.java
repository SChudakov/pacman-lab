package pacman.state;

import pacman.algorithm.AStartShortestPath;
import pacman.algorithm.AbstractBestFirstShortestPath;
import pacman.algorithm.MiniMaxShortestPath;
import pacman.algorithm.Position;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MazeState extends State {
    private static final String FLOOR_IMAGE_PATH = "images/floor.png";
    private static final String WALL_TAG = "wall";
    private static final String POINT_TAG = "point_%d_%d";

    public MazeState(GameConfiguration configuration) {
        manager.addObject(new GameImage(FLOOR_IMAGE_PATH, configuration));

        List<GameObject> targets = buildMaze(manager, configuration);

        PacMan pacMan = new PacMan(1, 1, configuration, 200);
        Ghost ghost = new Ghost(5, 14, configuration, 100);

        Map<Position, Map<Position, Integer>> distanceMap = getDistanceMap(configuration, targets);
        MiniMaxShortestPath searchAlgorithm = new MiniMaxShortestPath(configuration, pacMan, ghost, targets, distanceMap);
        pacMan.setSearchAlgorithm(searchAlgorithm);
        ghost.setSearchAlgorithm(searchAlgorithm);

        manager.addObject(pacMan);
        manager.addObject(ghost);
    }

    private Map<Position, Map<Position, Integer>> getDistanceMap(GameConfiguration configuration, List<GameObject> targets) {
        Map<Position, Map<Position, Integer>> result = new HashMap<>(targets.size());

        AStartShortestPath shortestPath = new AStartShortestPath(configuration, AbstractBestFirstShortestPath.ZERO_HEURISTIC);
        for (int i = 0; i < targets.size(); ++i) {
            for (int j = i; j < targets.size(); ++j) {
                GameObject source = targets.get(i);
                GameObject target = targets.get(j);
                Position sourcePosition = configuration.getPosition(source);
                Position targetPosition = configuration.getPosition(target);
                shortestPath.getNextDirection(configuration, source, Collections.singletonList(target));
                int distance = shortestPath.getDistance(targetPosition);

                result.compute(sourcePosition, (key, value) -> {
                    if (value == null) {
                        return new HashMap<>(targets.size());
                    }
                    return value;
                });
                result.compute(targetPosition, (key, value) -> {
                    if (value == null) {
                        return new HashMap<>(targets.size());
                    }
                    return value;
                });
                result.get(sourcePosition).put(targetPosition, distance);
                result.get(targetPosition).put(sourcePosition, distance);
            }
        }
        return result;
    }


    public List<GameObject> buildMaze(ObjectManager manager, GameConfiguration configuration) {
        List<GameObject> result = new ArrayList<>();
        for (int i = 0; i < configuration.getRowNum(); i++) {
            for (int j = 0; j < configuration.getColumnNum(); j++) {
                if (configuration.isWall(i, j)) {
                    manager.addObject(new Wall(i, j, configuration, WALL_TAG));
                } else {
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
