package pacman.object;

import pacman.game.GameContainer;
import pacman.game.GameConfiguration;
import pacman.graphics.Renderer;
import javafx.scene.image.Image;

public class Wall extends GameObject{
	private Image point;

	public Wall(int row, int column, GameConfiguration conf, String tag) {
		super(conf.getTileX(column), conf.getTileY(row), conf.getTileWidth(), conf.getTileHeight(),tag);
		setTag(tag);

		this.point = new Image("images/wall.png");

		this.x = this.width * column;
		this.y = this.height * row;
		
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		r.drawImage(this.point, this.x, this.y);
	}
}
