package pacman.algorithm;

import pacman.game.GameConfiguration;

public class GreedyShortestPath extends AbstractBestFirstShortestPath {
    public GreedyShortestPath(GameConfiguration configuration, Heuristic heuristic) {
        super(configuration, heuristic);
    }

    @Override
    double getTentativeScore(double gScore, double fScore) {
        return fScore;
    }
}
