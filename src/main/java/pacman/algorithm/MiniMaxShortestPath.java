package pacman.algorithm;

import pacman.game.GameConfiguration;
import pacman.object.GameObject;
import pacman.object.Ghost;
import pacman.object.PacMan;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MiniMaxShortestPath {
    private static final long SEED = 19L;
    private static final double RANDOM_STEP_PROBABILITY = 0.2;

    private final PacMan pacMan;
    private final Ghost ghost;
    private final List<GameObject> targets;

    private Random random;

    public MiniMaxShortestPath(PacMan pacMan, Ghost ghost, List<GameObject> targets) {
        this.pacMan = Objects.requireNonNull(pacMan, "pacMan should not be null!");
        this.ghost = Objects.requireNonNull(ghost, "ghost should not be null!");
        this.targets = Objects.requireNonNull(targets, "targets should not be null!");

        this.random = new Random(SEED);
    }

    public Direction getPacmanNextDirection(GameConfiguration configuration) {
        targets.removeIf(GameObject::isDead);
        return null;
    }

    public Direction getGhostNextDirection(GameConfiguration configuration) {
        if (randomStep()) {
            return getRandomDirection();
        }
        return null;
    }

    private boolean randomStep() {
        return random.nextDouble() < RANDOM_STEP_PROBABILITY;
    }

    private Direction getRandomDirection() {
        int rnd = random.nextInt(4);
        if (rnd == 0) {
            return Direction.UP;
        } else if (rnd == 1) {
            return Direction.RIGHT;
        } else if (rnd == 2) {
            return Direction.DOWN;
        } else {
            return Direction.LEFT;
        }
    }
}
