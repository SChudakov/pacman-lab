package pacman.object;

import pacman.game.GameContainer;
import pacman.graphics.Renderer;

public abstract class GameObject implements Component {
	protected float x, y;
	protected float width, height;
	protected String tag;
	protected Collider collider;

	public GameObject(float x, float y, float width, float height, String tag) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.tag = tag;
		this.collider = new Collider(this);
	}

	public void update(GameContainer gc, float dt){}

	public void render(GameContainer gc, Renderer r){}

	public void componentEvent(String name, GameObject object){}

	public void dispose(){}

	public void collideWith(GameObject obj){

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

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
}
