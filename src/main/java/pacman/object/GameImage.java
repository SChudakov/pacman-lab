package pacman.object;

import pacman.game.GameContainer;
import pacman.game.GameConfiguration;
import pacman.graphics.Renderer;
import javafx.scene.image.Image;

public class GameImage extends GameObject{
	private Image background_image;
	
	
	public GameImage(String background_path, GameConfiguration conf) {
		super(0, 0, conf.getWindowWidth(), conf.getWindowHeight(), "background");
		this.background_image = new Image(background_path);
	}
	
	@Override
	public void render(GameContainer gc, Renderer r) {
		r.drawImage(this.background_image, this.x, this.y);
	}

}
