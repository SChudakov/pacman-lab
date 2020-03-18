package pacman.algorithm;

import org.apache.commons.lang3.tuple.Triple;
import pacman.game.GameConfiguration;
import pacman.object.GameObject;
import pacman.object.Ghost;
import pacman.object.PacMan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static pacman.algorithm.Direction.DOWN;
import static pacman.algorithm.Direction.LEFT;
import static pacman.algorithm.Direction.NONE;
import static pacman.algorithm.Direction.RIGHT;
import static pacman.algorithm.Direction.UP;

public class MiniMaxShortestPath extends AbstractShortestPath {
    private static final long SEED = 17L;
    private static final double RANDOM_STEP_PROBABILITY = 0.2;

    private PacMan pacman;
    private Ghost ghost;

    private List<GameObject> pacmanTargets;
    private GameObject pacmanTarget;

    private Random random;
    private int maximumDepth;

    private Map<Position, Map<Position, Integer>> distanceMap;

    private int numOfGhostPositions = 5;
    private List<Position> ghostPositions;
    private Map<Position, Integer> positionToIndex;

    private List<Position> pacmanPath;
    private List<Position> ghostPath;


    public GameObject getPacmanTarget() {
        return pacmanTarget;
    }

    public void setPacmanTarget(GameObject pacmanTarget) {
        this.pacmanTarget = pacmanTarget;
    }


    public MiniMaxShortestPath(GameConfiguration configuration, PacMan pacman, Ghost ghost,
                               List<GameObject> pacmanTargets, Map<Position, Map<Position, Integer>> distanceMap) {
        super(configuration);
        this.pacman = Objects.requireNonNull(pacman, "pacMan should not be null!");
        this.ghost = Objects.requireNonNull(ghost, "ghost should not be null!");
        this.pacmanTargets = Objects.requireNonNull(pacmanTargets, "targets should not be null!");
        this.random = new Random(SEED);
        this.maximumDepth = 2;

        this.pacmanTarget = pacmanTargets.get(0);

        this.distanceMap = distanceMap;

        this.ghostPositions = new ArrayList<>(numOfGhostPositions);
        this.positionToIndex = new HashMap<>(numOfGhostPositions);

        this.pacmanPath = new ArrayList<>();
        this.ghostPath = new ArrayList<>();
    }


    public Direction getPacmanNextDirection() {
        pacmanTargets.removeIf(GameObject::isDead);
        if (pacman.isDead() || pacmanTargets.size() == 0) {
            return Direction.NONE;
        }
        lazySetPacmanTarget();
        if (pacmanPath.size() == 0 || !pacmanPath.get(pacmanPath.size() - 1).equals(configuration.getPosition(pacman))) {
            pacmanPath.add(configuration.getPosition(pacman));
        }

        return nextDirectionImpl(configuration.getPosition(pacman),
                configuration.getPosition(ghost), true).getLeft();
    }

    public Direction getGhostNextDirection() {
        if (pacman.isDead() || pacmanTargets.size() == 0) {
            return Direction.NONE;
        }
        lazySetPacmanTarget();
        if (ghostPath.size() == 0 || !ghostPath.get(ghostPath.size() - 1).equals(configuration.getPosition(ghost))) {
            ghostPath.add(configuration.getPosition(ghost));
        }

        Position pacmanPosition = configuration.getPosition(pacman);
        Position ghostPosition = configuration.getPosition(ghost);
        if (ghostPositions.isEmpty() || positionToIndex.get(ghostPosition) == ghostPositions.size() - 1) {
            ghostPositions.clear();
            positionToIndex.clear();

            ghostPositions.add(ghostPosition);
            if (randomStep()) {
                System.out.println("Ghost random step");
                for (int i = 0; i < numOfGhostPositions - 1; ++i) {
                    Position lastPosition = ghostPositions.get(ghostPositions.size() - 1);
                    Position randomNextPositions = lastPosition.withDirection(getRandomDirection(lastPosition));
                    while (ghostPositions.contains(randomNextPositions)) {
                        randomNextPositions = lastPosition.withDirection(getRandomDirection(lastPosition));
                    }
                    ghostPositions.add(randomNextPositions);
                }
            } else {
                for (int i = 0; i < numOfGhostPositions - 1; ++i) {
                    Position lastPosition = ghostPositions.get(ghostPositions.size() - 1);
                    Direction direction = nextDirectionImpl(pacmanPosition, lastPosition, false).getMiddle();
                    Position nextPosition = lastPosition.withDirection(direction);

                    while (ghostPositions.contains(nextPosition)) {
                        nextPosition = lastPosition.withDirection(getRandomDirection(lastPosition));
                    }

                    ghostPositions.add(nextPosition);
                }
            }
            System.out.println("Selected positions: " + ghostPositions);
            for (int i = 0; i < ghostPositions.size(); ++i) {
                positionToIndex.put(ghostPositions.get(i), i);
            }
        }

        int index = positionToIndex.get(ghostPosition);
        return ghostPositions.get(index).directionTo(ghostPositions.get(index + 1));
    }

    private void lazySetPacmanTarget() {
        if (pacmanTarget == null) {
            System.out.println("Set pacman target to point: " + configuration.getPosition(pacmanTargets.get(0)));
            pacmanTarget = pacmanTargets.get(0);
        }
    }

    // pacman direction, ghost direction, distance
    private Triple<Direction, Direction, Integer> nextDirectionImpl(Position pacmanPosition,
                                                                    Position ghostPosition,
                                                                    boolean pacmanStep) {
        // 0 = not visited, 1 = visited, 2 = target, 3 = avoid
        int[][] pacmanVisited = new int[configuration.getRowNum()][configuration.getColumnNum()];
        int[][] ghostVisited = new int[configuration.getRowNum()][configuration.getColumnNum()];
        if (isCrossRoad(configuration.getPosition(pacman))) {
            excludeSecondLastPosition(pacmanPath, pacmanVisited);
        }
        if (isCrossRoad(configuration.getPosition(ghost))) {
            excludeSecondLastPosition(ghostPath, ghostVisited);
        }

        return nextDirectionImpl(pacmanVisited, ghostVisited,
                pacmanPosition, ghostPosition, pacmanStep, 0);
    }

    private boolean isCrossRoad(Position position) {
        List<Position> steps = getSteps(position);
        steps.removeIf(step -> !validPosition(step));
        if (steps.size() > 2) {
            return true;
        }
        if (steps.size() < 2) {
            return false;
        }
        Set<Direction> directions = steps.stream().map(position::directionTo).collect(Collectors.toSet());
        if (directions.equals(new HashSet<>(Arrays.asList(DOWN, UP)))) {
            return false;
        }
        if (directions.equals(new HashSet<>(Arrays.asList(LEFT, RIGHT)))) {
            return false;
        }
        return true;
    }

    private void excludeSecondLastPosition(List<Position> path, int[][] visited) {
        if (path.size() >= 2) {
            Position secondLastPosition = path.get(path.size() - 2);
            visited[secondLastPosition.row][secondLastPosition.col] = 1;
        }
    }

    // pacman direction, ghost direction, distance
    private Triple<Direction, Direction, Integer> nextDirectionImpl(
            int[][] pacmanVisited, int[][] ghostVisited,
            Position pacmanPosition, Position ghostPosition,
            boolean pacmanStep, int depth) {
        if (!validPosition(pacmanPosition) || pacmanVisited[pacmanPosition.row][pacmanPosition.col] == 1 ||
                !validPosition(ghostPosition) || ghostVisited[ghostPosition.row][ghostPosition.col] == 1) {
            return Triple.of(NONE, NONE, Integer.MIN_VALUE);
        }

        if (depth == maximumDepth) {
            int pacman2ghostDistance = distance(pacmanPosition, ghostPosition);
            return Triple.of(NONE, NONE, pacman2ghostDistance);
        }

        if (pacmanStep) {
            pacmanVisited[pacmanPosition.row][pacmanPosition.col] = 1;

            List<Position> maxScorePositions = new ArrayList<>();
            int maxScore = Integer.MIN_VALUE;

            List<Position> steps = getSteps(pacmanPosition);
            for (Position pacmanNext : steps) {
                Triple<Direction, Direction, Integer> p = nextDirectionImpl(
                        pacmanVisited, ghostVisited,
                        pacmanNext, ghostPosition,
                        !pacmanStep, depth + 1);
                if (p.getRight().equals(Integer.MIN_VALUE)) {
                    continue;
                }
                if (p.getRight() > maxScore) {
                    maxScore = p.getRight();
                    maxScorePositions.clear();
                    maxScorePositions.add(pacmanNext);
                } else if (p.getRight() == maxScore) {
                    maxScorePositions.add(pacmanNext);
                }
            }

            int minDistanceToTarget = Integer.MAX_VALUE;
            Position resultPosition = null;
            for (Position position : maxScorePositions) {
                int currentDistance = distance(position, configuration.getPosition(pacmanTarget));
                if (currentDistance < minDistanceToTarget) {
                    minDistanceToTarget = currentDistance;
                    resultPosition = position;
                }
            }

            pacmanVisited[pacmanPosition.row][pacmanPosition.col] = 0;
            return Triple.of(pacmanPosition.directionTo(resultPosition), NONE, maxScore);
        } else {
            ghostVisited[ghostPosition.row][ghostPosition.col] = 1;

            Position minScorePosition = null;
            int minScore = Integer.MAX_VALUE;

            List<Position> steps = getSteps(ghostPosition);
            for (Position ghostNext : steps) {
                Triple<Direction, Direction, Integer> p = nextDirectionImpl(
                        pacmanVisited, ghostVisited,
                        pacmanPosition, ghostNext,
                        !pacmanStep, depth + 1);
                if (p.getRight().equals(Integer.MIN_VALUE)) {
                    continue;
                }
                if (p.getRight() < minScore) {
                    minScore = p.getRight();
                    minScorePosition = ghostNext;
                }
            }

            ghostVisited[ghostPosition.row][ghostPosition.col] = 0;
            return Triple.of(NONE, ghostPosition.directionTo(minScorePosition), minScore);
        }
    }

    private int distance(Position p1, Position p2) {
        assert !configuration.isWall(p1.row, p1.col);
        assert !configuration.isWall(p2.row, p2.col);
        return distanceMap.get(p1).get(p2);
    }

    private boolean randomStep() {
        return random.nextDouble() < RANDOM_STEP_PROBABILITY;
    }

    private Direction getRandomDirection(Position position) {
        List<Position> steps = getSteps(position);
        steps.removeIf(p -> !validPosition(p));
        int rnd = random.nextInt(steps.size());
        return position.directionTo(steps.get(rnd));
    }

    @Override
    public Direction getNextDirection(GameConfiguration conf, GameObject obj, List<GameObject> targets) {
        throw new UnsupportedOperationException("getNextDirection");
    }
}
