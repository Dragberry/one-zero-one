package org.dragberry.ozo.game.render;

import org.dragberry.ozo.game.GameController;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Renderer {
	
	void render(SpriteBatch batch);
	
	void init();
	
	GameController getGameContoller();
	
	OrthographicCamera getCamera();
	
	void resize(int width, int height);

}
