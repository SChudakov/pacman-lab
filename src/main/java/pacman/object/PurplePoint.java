package pacman.object;

import javafx.scene.image.Image;
import pacman.game.GameConfiguration;
import pacman.game.GameContainer;
import pacman.graphics.Renderer;

public class PurplePoint extends GameObject {
    private static final String imagePath = "images/purple_point.png";
    private Image img;

    public PurplePoint(int row, int column, GameConfiguration conf, String tag) {
        super(tag);
        this.img = new Image(imagePath);

        double centerX = conf.getTileCenterX(column);
        double centerY = conf.getTileCenterY(row);

        this.x = centerX - img.getWidth() / 2;
        this.y = centerY - img.getHeight() / 2;
        this.width = img.getWidth();
        this.height = img.getHeight();
        this.collider = new Collider(this);
    }

    @Override
    public void render(GameContainer gc, Renderer r) {
        GameConfiguration conf = gc.getConfiguration();
        r.drawImage(img, x, y);

        if (conf.isDrawCollider()) {
            r.drawRectangle(collider.getX(),
                    collider.getY(),
                    collider.getColliderWidth(),
                    collider.getColliderHeight());
        }
    }
}
