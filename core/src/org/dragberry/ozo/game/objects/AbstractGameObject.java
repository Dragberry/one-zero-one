package org.dragberry.ozo.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class AbstractGameObject {
	
	public transient Vector2 position = new Vector2();
	public transient Vector2 dimension = new Vector2(1, 1);
	public transient Vector2 origin = new Vector2();
	public transient Vector2 scale = new Vector2(1, 1);
	public transient Rectangle bounds = new Rectangle();
	public transient float rotation;
	
	public void update (float deltaTime) {
		// do nothing by default
	}
	
	public abstract void render(SpriteBatch batch);

}
