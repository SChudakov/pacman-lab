package pacman.object;

public class Collider {
    private final double width, height;
    private GameObject obj;

    public Collider(GameObject obj) {
        this.width = obj.getWidth() / 3;
        this.height = obj.getHeight() / 3;
        this.obj = obj;
        System.out.printf("Width = %f, height = %f, x = %f, y = %f\n", width, height, getCenterX(), getCenterY());
    }

    public double getX() {
        return getCenterX() - width;
    }

    public double getY() {
        return getCenterY() - height;
    }

    public double getColliderWidth() {
        return 2 * width;
    }

    public double getColliderHeight() {
        return 2 * height;
    }

    public double getCenterX() {
        return obj.getCenterX();
    }

    public double getCenterY() {
        return obj.getCenterY();
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
