package pacman.object;

import java.util.ArrayList;
import java.util.List;

import pacman.game.GameContainer;
import pacman.graphics.Renderer;

public class ObjectManager {
	private static final double EPS = 1e-6;
	private List<GameObject> objects;

	public ObjectManager(){
		objects = new ArrayList<>();
	}

	public void addObject(GameObject object) {
		objects.add(object);
	}

	public void updateObjects(GameContainer gc, double dt) {
		for (GameObject object : objects) {
			object.update(gc, dt);
		}
		update();
		objects.removeIf(GameObject::isDead);
	}

	public void renderObjects(GameContainer gc, Renderer r) {
		for (GameObject object : objects) {
			object.render(gc, r);
		}
	}

	public void update() {
		for (int i = 0; i < objects.size(); i++) {
			for (int j = i + 1; j < objects.size(); j++) {
				Collider c0 = objects.get(i).getCollider();
				Collider c1 = objects.get(j).getCollider();

				if (Math.abs(c0.getCenterX() - c1.getCenterX()) < c0.getWidth() + c1.getWidth() - EPS) {
					if (Math.abs(c0.getCenterY() - c1.getCenterY()) < c0.getHeight() + c1.getHeight() - EPS) {
						objects.get(i).collideWith(objects.get(j));
						objects.get(j).collideWith(objects.get(i));
					}
				}
			}
		}
	}
}
