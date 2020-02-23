package pacman.object;

public class Collider {
	private final float width, height;
	private GameObject obj;

	public Collider(GameObject obj) {
		this.width = obj.getWidth()/2;
		this.height = obj.getHeight()/2;
		this.obj = obj;
	}

	public float getCenterX() {
		return obj.getX() + width;
	}

	public float getCenterY() {
		return obj.getY() + height;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
}
