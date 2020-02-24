package pacman.object;

import pacman.game.GameContainer;
import pacman.graphics.Renderer;

public interface Component {

	void update(GameContainer gc, double dt);

	void render(GameContainer gc, Renderer r);

	void componentEvent(String name, GameObject object);

	void dispose();
}
