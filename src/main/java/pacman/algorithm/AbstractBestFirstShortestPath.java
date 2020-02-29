package pacman.algorithm;

import org.apache.commons.lang3.tuple.Pair;
import pacman.game.GameConfiguration;
import pacman.object.GameObject;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public abstract class AbstractBestFirstShortestPath extends AbstractShortestPath {

    private Heuristic heuristic;

    private Queue<Pair<Double, Position>> queue;
    private Set<Position> openList;
    private Set<Position> closedList;
    private Map<Position, Double> gScoreMap;

    public AbstractBestFirstShortestPath(GameConfiguration configuration, Heuristic heuristic) {
        super(configuration);
        this.heuristic = heuristic;
    }

    @Override
    public Direction getNextDirection(GameConfiguration configuration, GameObject object, List<GameObject> targets) {
        if (targets.isEmpty()) {
            return Direction.NONE;
        }
        invokeAlgorithm();
        queue = new PriorityQueue<>(Comparator.comparingDouble(Pair::getKey));
        openList = new HashSet<>();
        closedList = new HashSet<>();
        gScoreMap = new HashMap<>();

        Direction[][] directions = new Direction[configuration.getRowNum()][configuration.getColumnNum()];
        Position startPosition = configuration.getPosition(object);
        Position targetPosition = conf.getPosition(targets.get(0));
        return aStartImpl(directions, startPosition, targetPosition);
    }

    private Direction aStartImpl(Direction[][] directions, Position start, Position target) {
        gScoreMap.put(start, 0.0);
        queue.add(Pair.of(0.0, start));
        openList.add(start);

        while (!queue.isEmpty()) {
            Pair<Double, Position> p = queue.remove();
            double distance = p.getKey();
            Position currentPosition = p.getRight();

            if (currentPosition.equals(target)) { // finished search
                return getDirection(directions, currentPosition.row, currentPosition.col);
            }

            expandPosition(currentPosition, distance, directions, target);

            closedList.add(currentPosition);
        }

        return Direction.NONE;
    }

    private void expandPosition(Position position, double distance,
                                Direction[][] directions, Position target) {
        List<Position> successors = getSteps(position);

        for (Position successor : successors) {
            int row = successor.row;
            int col = successor.col;
            if (validPosition(successor)) {
                double tentativeGScore = gScoreMap.get(position) + 1.0; // add one step
                double fScore = tentativeGScore + heuristic.estimateDistance(successor, target);

                if (openList.contains(successor) || closedList.contains(successor)) { // visited vertex
                    if (tentativeGScore >= gScoreMap.get(successor)) { // non-improving path
                        continue;
                    }

                    directions[row][col] = getDirection(position.row - row, position.col - col);
                    gScoreMap.put(successor, tentativeGScore);

                    if (closedList.contains(successor)) { // it's in the closed list => move to open list
                        closedList.remove(successor);
                        openList.add(successor);
                    }
                    // no decrease key operation => just add to the queue
                    queue.add(Pair.of(fScore, successor));
                } else { // encountered a new vertex.
                    directions[row][col] = getDirection(position.row - row, position.col - col);
                    gScoreMap.put(successor, tentativeGScore);
                    queue.add(Pair.of(fScore, successor)); // put on the queue
                }
            }
        }
    }

    abstract double getTentativeScore(double gScore, double fScore);
}
