package pacman.algorithm;

@FunctionalInterface
public interface AStartHeuristic {
    double estimateDistance(Position p1, Position p2);
}
