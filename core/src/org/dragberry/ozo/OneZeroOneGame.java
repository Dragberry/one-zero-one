package org.dragberry.ozo;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.GameController;
import org.dragberry.ozo.game.GameRenderer;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;

public class OneZeroOneGame extends ApplicationAdapter {
	
	private static final  String TAG = OneZeroOneGame.class.getName();
	
	private GameController gameController;
	private GameRenderer gameRenderer;
	
	private boolean paused;
	
	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		Assets.instance.init(new AssetManager());
		
		gameController = new GameController();
		gameRenderer = new GameRenderer(gameController);
		paused = false;
	}

	@Override
	public void render () {
		if (!paused) {
            gameController.update(Gdx.graphics.getDeltaTime());
        }
        Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xed/255.0f, 0xff/255.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameRenderer.render();
	}
	
	@Override
	public void dispose () {
		gameRenderer.dispose();
		Assets.instance.dispose();
	}
	
	@Override
	public void pause() {
		paused = true;
	}
	
	@Override
	public void resume() {
		Assets.instance.init(new AssetManager());
		paused = false;
	}
}
