package pacman.algorithm;

import org.junit.Assert;
import org.junit.Test;
import pacman.game.GameConfiguration;
import pacman.object.GameObject;

import java.util.Collections;
import java.util.List;

import static pacman.algorithm.Direction.*;

public class BFSShortestPathTest {

    private GameConfiguration getConf(int[][] maze) {
        return new GameConfiguration.GameConfigurationBuilder().setMaze(maze).build();
    }

    @Test
    public void test1() {
        int[][] maze = {{0, 0, 0, 0, 0}, {0, 1, 1, 1, 0}, {0, 1, 1, 1, 0}, {0, 0, 0, 0, 0},};
        GameConfiguration conf = getConf(maze);
        BFSShortestPath path = new BFSShortestPath(conf);
        GameObject point = new TestObject(1, 3, conf);
        List<GameObject> targets = Collections.singletonList(point);
        GameObject pacMan = new TestObject(1, 1, conf);

        Assert.assertEquals(RIGHT, path.getNextDirection(conf, pacMan, targets));
    }

    @Test
    public void test2() {
        int[][] maze = {{0, 0, 0, 0, 0}, {0, 1, 0, 1, 0}, {0, 1, 1, 1, 0}, {0, 0, 0, 0, 0},};
        GameConfiguration conf = getConf(maze);
        BFSShortestPath path = new BFSShortestPath(conf);
        GameObject point = new TestObject(1, 3, conf);
        List<GameObject> targets = Collections.singletonList(point);
        GameObject pacMan = new TestObject(1, 1, conf);

        Assert.assertEquals(DOWN, path.getNextDirection(conf, pacMan, targets));
    }

    @Test
    public void test3() {
        int[][] maze = {{0, 0, 0, 0, 0}, {0, 1, 0, 1, 0}, {0, 1, 1, 1, 0}, {0, 0, 0, 0, 0},};
        GameConfiguration conf = getConf(maze);
        BFSShortestPath path = new BFSShortestPath(conf);
        GameObject point = new TestObject(1, 1, conf);
        List<GameObject> targets = Collections.singletonList(point);
        GameObject pacMan = new TestObject(1, 3, conf);

        Assert.assertEquals(DOWN, path.getNextDirection(conf, pacMan, targets));
    }
}
