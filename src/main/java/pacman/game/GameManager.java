package pacman.game;

import pacman.graphics.Renderer;
import pacman.state.MazeState;

public class GameManager {

	private State state;

	public GameManager(GraphicsConfiguration conf) {
		state = new MazeState(conf);
	}

	public void setState(State state) {
		this.state = state;
	}

	public void update(GameContainer gc, float dt) {
		state.update(gc, dt);
	}

	public void render(GameContainer gc, Renderer r) {
		state.render(gc, r);
	}

}
