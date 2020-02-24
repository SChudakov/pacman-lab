package pacman.game;

import pacman.graphics.Renderer;
import pacman.object.ObjectManager;

public abstract class State {
	protected ObjectManager manager = new ObjectManager();

	public abstract void update(GameContainer gc, double dt);

	public abstract void render(GameContainer gc, Renderer r);

	public ObjectManager getManager() {
		return manager;
	}

	public void setManager(ObjectManager manager) {
		this.manager = manager;
	}
}
