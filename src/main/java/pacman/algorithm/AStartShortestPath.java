package pacman.algorithm;

import pacman.game.GameConfiguration;

public class AStartShortestPath extends AbstractBestFirstShortestPath {

    public AStartShortestPath(GameConfiguration configuration, Heuristic heuristic) {
        super(configuration, heuristic);
    }

    @Override
    double getTentativeScore(double gScore, double fScore) {
        return gScore + fScore;
    }
}
