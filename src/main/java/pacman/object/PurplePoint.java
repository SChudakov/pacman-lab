package pacman.object;

import javafx.scene.image.Image;
import pacman.game.GameConfiguration;
import pacman.game.GameContainer;
import pacman.graphics.Renderer;

public class PurplePoint extends GameObject {
    private static final String IMAGE_PATH = "images/purple_point.png";
    private Image image;

    public PurplePoint(int row, int column, GameConfiguration conf, String tag) {
        super(tag);
        this.image = new Image(IMAGE_PATH);

        double centerX = conf.getTileCenterX(column);
        double centerY = conf.getTileCenterY(row);

        this.x = centerX - image.getWidth() / 2;
        this.y = centerY - image.getHeight() / 2;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.collider = new Collider(this);
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        GameConfiguration configuration = gc.getConfiguration();
        r.drawImage(image, x, y);

        if (configuration.isDrawCollider()) {
            r.drawRectangle(collider.getX(),
                    collider.getY(),
                    collider.getColliderWidth(),
                    collider.getColliderHeight());
        }
    }
}
