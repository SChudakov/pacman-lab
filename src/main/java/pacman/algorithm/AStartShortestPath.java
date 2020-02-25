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

public class AStartShortestPath extends AbstractShortestPath {
    private AStartHeuristic admissibleHeuristic;

    private Queue<Pair<Double, Position>> openList;
    private Set<Position> closedList;
    private Map<Position, Double> gScoreMap;
    private Map<Position, Direction> cameFrom;

    public AStartShortestPath(GameConfiguration conf) {
        super(conf);
    }

    @Override
    public Direction getNextDirection(GameConfiguration conf, GameObject obj, List<GameObject> targets) {
        openList = new PriorityQueue<>(Comparator.comparingDouble(Pair::getKey));
        closedList = new HashSet<>();
        gScoreMap = new HashMap<>();
        cameFrom = new HashMap<>();

        Position startPosition = conf.getPosition(obj);
        int[][] visited = new int[conf.getRowNum()][conf.getColumnNum()];
        initVisited(targets, conf, visited);
        Direction[][] directions = new Direction[conf.getRowNum()][conf.getColumnNum()];
        return aStartImpl(visited, directions, startPosition);
    }

    private Direction aStartImpl(int[][] visited, Direction[][] directions, Position startPosition) {
        gScoreMap.put(startPosition, 0.0);
        openList.add(Pair.of(0.0, startPosition));

        while (!openList.isEmpty()) {
            Pair<Double, Position> p = openList.remove();
            double distance = p.getKey();
            Position currentPosition = p.getRight();

            if (visited[currentPosition.row][currentPosition.col] == 2) { // finished search
                throw new UnsupportedOperationException();
            }

            closedList.add(currentPosition);
        }

        return Direction.NONE;
    }

    private void expandPosition(Position position, double distance, Set<Position> targetsSet,
                                int[][] visited, Direction[][] directions) {
        List<Position> successors = getSteps(position);

        for (Position successor : successors) {
            int row = successor.row;
            int col = successor.col;
            if (validPosition(successor) && visited[row][col] != 1) {
                double tentativeGScore = gScoreMap.get(position) + 1;
//                double fScore = tentativeGScore + admissibleHeuristic.estimateDistance(successor, );
            }

        }
    }
}
