package pacman.object;

import javafx.scene.image.Image;
import pacman.game.GameConfiguration;
import pacman.game.GameContainer;
import pacman.graphics.Renderer;

public class PurplePoint extends GameObject {
    private static final String imagePath = "images/purple_point.png";
    private Image img;

    public PurplePoint(int row, int column, GameConfiguration conf, String tag) {
        super(conf.getTileX(column), conf.getTileY(row), conf.getTileWidth(), conf.getTileHeight(), tag);

        this.img = new Image(imagePath);
        this.x += (conf.getTileWidth() - img.getWidth()) / 2;
        this.y += (conf.getTileHeight() - img.getHeight()) / 2;
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        r.drawImage(img, x, y);
    }
}
