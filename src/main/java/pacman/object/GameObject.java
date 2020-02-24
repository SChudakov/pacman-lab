package pacman.object;

import pacman.game.GameContainer;
import pacman.graphics.Renderer;

public abstract class GameObject implements Component {
    protected double x, y;
    protected double width, height;
    protected boolean dead;
    protected String tag;
    protected Collider collider;

    public GameObject(double x, double y, double width, double height, String tag) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.tag = tag;
        this.collider = new Collider(this);
    }

    public double getCenterX() {
        return x + width / 2;
    }

    public double getCenterY() {
        return y + height / 2;
    }

    public void setCenterX(double centerX) {
        this.x = centerX - width / 2;
    }

    public void setCenterY(double centerY) {
        this.y = centerY - height / 2;
    }

    public void makeDead(){
        dead = true;
    }

    public boolean isDead(){
        return dead;
    }

    public void update(GameContainer gc, double dt) {
    }

    public void render(GameContainer gc, Renderer r) {
    }

    public void componentEvent(String name, GameObject object) {
    }

    public void dispose() {
    }

    public void collideWith(GameObject obj) {

    }

    public Collider getCollider() {
        return collider;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
