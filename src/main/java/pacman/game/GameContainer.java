package pacman.game;

import javafx.stage.Stage;
import pacman.graphics.Renderer;

public class GameContainer implements Runnable {
    private Thread thread;
    private GameManager gameManager;
    private Renderer renderer;
    private GameConfiguration configuration;

    private boolean isRunning = false;

    public GameManager getGameManager() {
        return gameManager;
    }

    public GameConfiguration getConfiguration() {
        return configuration;
    }

    public GameContainer(GameConfiguration configuration, Stage mainStage) {
        this.configuration = configuration;
        this.gameManager = new GameManager(configuration);
        this.renderer = new Renderer(configuration, mainStage);
        this.thread = new Thread(this);
    }

    public void run() {
        isRunning = true;

        TimeTracker tracker = new TimeTracker(30);

        while (isRunning) {
            if (tracker.newFrame()) {
                gameManager.update(this, tracker.getTimePassed());
                gameManager.render(this, renderer);
            }
        }
        cleanUp();
    }

    public void start() {
        if (isRunning) {
            return;
        }
        thread.start();
    }

    public void stop() {
        if (!isRunning) {
            return;
        }
        isRunning = false;
        renderer.cleanUp();
    }

    private void cleanUp() {
        renderer.cleanUp();
    }


    private static class TimeTracker {
        private int fps;
        private long start;
        private double timePassed;


        TimeTracker(int fps) {
            this.fps = fps;
            this.start = System.nanoTime();
        }

        boolean newFrame() {
            long cur = System.nanoTime();
            long delta = cur - start;
            double frameDelta = 1.0 / fps;
            double curDelta = delta / 1e9;

            if (curDelta >= frameDelta) {
                start = cur;
                timePassed = curDelta;
                return true;
            }
            return false;
        }

        double getTimePassed() {
            return timePassed;
        }
    }
}
