package org.dragberry.ozo.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class AbstractGameObject {
	
	public Rectangle bounds;
	public Vector2 position;
	public Vector2 dimension;
	public Vector2 origin;
	public Vector2 scale;
	public float rotation;
	
	
	public AbstractGameObject() {
		position = new Vector2();
		dimension = new Vector2(1, 1);
		origin = new Vector2();
		scale = new Vector2(1, 1);
		rotation = 0;
		bounds = new Rectangle();
	}
	
	protected void init() {}
	
	public void update (float deltaTime) {
		// do nothing by default
	}
	
	public abstract void render(SpriteBatch batch);

}
