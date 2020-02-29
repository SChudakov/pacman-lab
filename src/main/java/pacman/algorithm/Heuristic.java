package pacman.algorithm;

@FunctionalInterface
public interface Heuristic {
    double estimateDistance(Position p1, Position p2);
}
