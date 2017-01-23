package org.dragberry.ozo.game.objects;

import org.dragberry.ozo.game.Assets;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Unit extends AbstractGameObject {

	private TextureRegion regBall;
	
	private int value;
	
	public Unit() {
		this(0, 0);
	}
	
	public Unit(float x, float y) {
		this.position.x = x;
		this.position.y = y;
		init();
	}
	
	@Override
	protected void init() {
		regBall = Assets.instance.unit.ball;
		origin.x = dimension.x / 2;
		origin.y = dimension.y / 2;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		batch.draw(regBall.getTexture(),
				position.x, position.y,
				origin.x, origin.y,
				dimension.x, dimension.y,
				scale.x, scale.y,
				rotation,
				regBall.getRegionX(), regBall.getRegionY(),
				regBall.getRegionWidth(), regBall.getRegionHeight(),
				false, false);
	}

}
