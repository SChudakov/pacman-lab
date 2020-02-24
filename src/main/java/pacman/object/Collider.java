package pacman.object;

public class Collider {
	private final double width, height;
	private GameObject obj;

	public Collider(GameObject obj) {
		this.width = obj.getWidth()/3;
		this.height = obj.getHeight()/3;
		System.out.printf("Width = %f, height = %f\n", width, height);
		this.obj = obj;
	}

	public double getCenterX() {
		return obj.getX() + width;
	}

	public double getCenterY() {
		return obj.getY() + height;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}
}
