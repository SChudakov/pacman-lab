package pacman.game;

import javafx.stage.Stage;
import pacman.graphics.Renderer;

public class GameContainer implements Runnable {
    private Thread thread;
    private GameManager gManager;
    private Renderer renderer;
    private GraphicsConfiguration configuration;

    private boolean isRunning = false;

    public GameContainer(GraphicsConfiguration conf, Stage mainStage) {
    	this.configuration = conf;

		this.gManager = new GameManager(conf);
		this.renderer = new Renderer(conf, mainStage);
		this.thread = new Thread(this);
    }

    public void start() {
        if (isRunning) {
            return;
        }
        thread.start();
    }

    public void stop() {
        if (!isRunning)
            return;

        isRunning = false;

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

        double getTimePassed(){
        	return timePassed;
		}
    }

    public void run() {
        isRunning = true;

        TimeTracker tracker = new TimeTracker(30);

        while (isRunning) {
            if (tracker.newFrame()) {
                gManager.update(this, (float) tracker.getTimePassed());
				if (configuration.isClearScreen())
					renderer.clear();

				gManager.render(this, renderer);
            }
        }

        cleanUp();
    }

    private void cleanUp() {
        renderer.cleanUp();
    }

    public GameManager getgManager() {
        return gManager;
    }
}
