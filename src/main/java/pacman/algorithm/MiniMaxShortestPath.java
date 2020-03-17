package pacman.algorithm;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import pacman.game.GameConfiguration;
import pacman.object.GameObject;
import pacman.object.Ghost;
import pacman.object.PacMan;

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
        this.miniMaxDepth = 6;
    }


    public Direction getPacmanNextDirection() {
        pacmanTargets.removeIf(GameObject::isDead);
        if (pacman.isDead() || pacmanTargets.size() == 0) {
            return Direction.NONE;
        }

        if (pacmanTarget == null) {
            pacmanTarget = getRandomPacmanTarget();
        }
        return nextDirectionImp(true).getLeft().getLeft();
    }

    public Direction getGhostNextDirection() {
        if (pacman.isDead() || pacmanTargets.size() == 0) {
            return Direction.NONE;
        }

        if (randomStep()) {
            System.out.println("Ghost random step");
            return getRandomDirection(configuration.getPosition(ghost));
        }
        return nextDirectionImp(false).getRight().getLeft();
    }

    // pacman : direction, dist to target, dist to ghost,
    // ghost  : direction, dist to pacman
    private Pair<Triple<Direction, Integer, Integer>, Pair<Direction, Integer>> nextDirectionImp(boolean pacmanStep) {
        Position pacmanPosition = configuration.getPosition(pacman);
        Position ghostPosition = configuration.getPosition(ghost);

        // 0 = not visited, 1 = visited, 2 = target, 3 = avoid
        int[][] pacmanVisited = new int[configuration.getRowNum()][configuration.getColumnNum()];
        pacmanVisited[configuration.getRow(pacmanTarget)][configuration.getColumn(pacmanTarget)] = 2;
        pacmanVisited[configuration.getRow(ghost)][configuration.getColumn(ghost)] = 3;

        int[][] ghostVisited = new int[configuration.getRowNum()][configuration.getColumnNum()];
        ghostVisited[configuration.getRow(pacman)][configuration.getColumn(pacman)] = 2;

        return nextDirectionImp(pacmanVisited, ghostVisited,
                pacmanPosition, ghostPosition, pacmanStep, 0);
    }

    private Pair<Triple<Direction, Integer, Integer>, Pair<Direction, Integer>> nextDirectionImp(
            int[][] pacmanVisited, int[][] ghostVisited,
            Position pacmanPosition, Position ghostPosition,
            boolean pacmanStep, int depth) {
        if (!validPosition(pacmanPosition) || pacmanVisited[pacmanPosition.row][pacmanPosition.col] == 1 ||
                !validPosition(ghostPosition) || ghostVisited[ghostPosition.row][ghostPosition.col] == 1) {
            return Pair.of(Triple.of(NONE, Integer.MAX_VALUE, Integer.MIN_VALUE), Pair.of(NONE, Integer.MAX_VALUE));
        }

        if (pacmanVisited[pacmanPosition.row][pacmanPosition.col] == 2) {
            return Pair.of(Triple.of(NONE, 0, Integer.MAX_VALUE), Pair.of(NONE, Integer.MAX_VALUE));
        }
        if (pacmanVisited[pacmanPosition.row][pacmanPosition.col] == 3) {
            System.out.println("HERE");
            return Pair.of(Triple.of(NONE, Integer.MAX_VALUE, 0), Pair.of(NONE, 0));
        }
        if (ghostVisited[ghostPosition.row][ghostPosition.col] == 2) {
            return Pair.of(Triple.of(NONE, Integer.MAX_VALUE, 0), Pair.of(NONE, 0));
        }

        if (depth == miniMaxDepth) {
            int pacman2targetDistance = distance(pacmanPosition, ghostPosition);
            int pacman2ghostDistance = distance(pacmanPosition, configuration.getPosition(pacmanTarget));
            int ghost2pacmanDistance = distance(pacmanPosition, ghostPosition);
            return Pair.of(Triple.of(NONE, pacman2targetDistance, pacman2ghostDistance), Pair.of(NONE, ghost2pacmanDistance));
        }

        if (pacmanStep) {
            pacmanVisited[pacmanPosition.row][pacmanPosition.col] = 1;

            MutablePair<MutableTriple<Direction, Integer, Integer>,
                    MutablePair<Direction, Integer>> result = MutablePair.of(
                    MutableTriple.of(NONE, Integer.MAX_VALUE, Integer.MIN_VALUE),
                    MutablePair.of(NONE, Integer.MAX_VALUE)
            );

            List<Position> steps = getSteps(pacmanPosition);
            for (Position pacmanNext : steps) {
                Pair<Triple<Direction, Integer, Integer>, Pair<Direction, Integer>> p = nextDirectionImp(
                        pacmanVisited, ghostVisited,
                        pacmanNext, ghostPosition,
                        !pacmanStep, depth + 1);
                if (p.getLeft().getRight() > 0) {                                 // ghost is to close
                    if (p.getLeft().getMiddle() < result.getLeft().getMiddle()) { // go only if closer
                        result.getLeft().setLeft(pacmanPosition.directionTo(pacmanNext));
                        result.getLeft().setMiddle(p.getLeft().getMiddle());
                        result.getLeft().setRight(p.getLeft().getRight());
                        result.getRight().setLeft(p.getRight().getLeft());
                        result.getRight().setRight(p.getRight().getRight());
                    }
                }
            }

            pacmanVisited[pacmanPosition.row][pacmanPosition.col] = 0;
            return Pair.of(result.getLeft(), result.getRight());
        } else {
            ghostVisited[ghostPosition.row][ghostPosition.col] = 1;

            MutablePair<MutableTriple<Direction, Integer, Integer>,
                    MutablePair<Direction, Integer>> result = MutablePair.of(
                    MutableTriple.of(NONE, Integer.MAX_VALUE, Integer.MIN_VALUE),
                    MutablePair.of(NONE, Integer.MAX_VALUE));

            List<Position> steps = getSteps(ghostPosition);
            for (Position ghostNext : steps) {
                Pair<Triple<Direction, Integer, Integer>, Pair<Direction, Integer>> p = nextDirectionImp(
                        pacmanVisited, ghostVisited,
                        pacmanPosition, ghostNext,
                        !pacmanStep, depth + 1);
                if (p.getRight().getRight() < result.getRight().getRight()) {
                    result.getLeft().setLeft(p.getLeft().getLeft());
                    result.getLeft().setMiddle(p.getLeft().getMiddle());
                    result.getLeft().setRight(p.getLeft().getRight());
                    result.getRight().setLeft(ghostPosition.directionTo(ghostNext));
                    result.getRight().setRight(p.getRight().getRight());
                }
            }

            ghostVisited[ghostPosition.row][ghostPosition.col] = 0;
            return Pair.of(result.getLeft(), result.getRight());
        }
    }

    private int distance(Position p1, Position p2) {
        return Math.abs(p1.row - p2.row) + Math.abs(p1.col - p2.col);
    }

    private GameObject getRandomPacmanTarget() {
        return pacmanTargets.get(random.nextInt(pacmanTargets.size()));
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
