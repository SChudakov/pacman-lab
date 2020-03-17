package pacman.algorithm;

import org.apache.commons.lang3.tuple.Triple;
import pacman.game.GameConfiguration;
import pacman.object.GameObject;
import pacman.object.Ghost;
import pacman.object.PacMan;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static pacman.algorithm.Direction.NONE;

public class MiniMaxShortestPath extends AbstractShortestPath {
    private static final long SEED = 19L;
    private static final double RANDOM_STEP_PROBABILITY = 0.0;

    private PacMan pacman;
    private Ghost ghost;

    private List<GameObject> pacmanTargets;
    private GameObject pacmanTarget;

    private Random random;
    private int miniMaxDepth;

    private static int cnt = 0;

    public GameObject getPacmanTarget() {
        return pacmanTarget;
    }

    public void setPacmanTarget(GameObject pacmanTarget) {
        this.pacmanTarget = pacmanTarget;
    }


    public MiniMaxShortestPath(GameConfiguration configuration, PacMan pacman, Ghost ghost, List<GameObject> pacmanTargets) {
        super(configuration);
        this.pacman = Objects.requireNonNull(pacman, "pacMan should not be null!");
        this.ghost = Objects.requireNonNull(ghost, "ghost should not be null!");
        this.pacmanTargets = Objects.requireNonNull(pacmanTargets, "targets should not be null!");
        this.random = new Random(SEED);
        this.miniMaxDepth = 2;
    }


    public Direction getPacmanNextDirection() {
        cnt++;
        pacmanTargets.removeIf(GameObject::isDead);
        if (pacman.isDead() || pacmanTargets.size() == 0) {
            return Direction.NONE;
        }

        if (pacmanTarget == null) {
            System.out.println("Set pacman target to a point: " + configuration.getPosition(pacmanTargets.get(0)));
            pacmanTarget = pacmanTargets.get(0);
        }
        return nextDirectionImpl(true).getLeft();
    }

    public Direction getGhostNextDirection() {
        if (pacman.isDead() || pacmanTargets.size() == 0) {
            return Direction.NONE;
        }

        if (randomStep()) {
            System.out.println("Ghost random step");
            return getRandomDirection(configuration.getPosition(ghost));
        }
        return nextDirectionImpl(false).getMiddle();
    }

    // pacman direction, ghost direction, distance
    private Triple<Direction, Direction, Integer> nextDirectionImpl(boolean pacmanStep) {
        Position pacmanPosition = configuration.getPosition(pacman);
        Position ghostPosition = configuration.getPosition(ghost);

        // 0 = not visited, 1 = visited, 2 = target, 3 = avoid
        int[][] pacmanVisited = new int[configuration.getRowNum()][configuration.getColumnNum()];
        int[][] ghostVisited = new int[configuration.getRowNum()][configuration.getColumnNum()];

        return nextDirectionImpl(pacmanVisited, ghostVisited,
                pacmanPosition, ghostPosition, pacmanStep, 0);
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

        if (depth == miniMaxDepth) {
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
        return Math.abs(p1.row - p2.row) + Math.abs(p1.col - p2.col);
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
