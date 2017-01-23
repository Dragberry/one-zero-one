package org.dragberry.ozo;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.screen.MenuScreen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

public class OneZeroOneGame extends Game {
	
	private static final  String TAG = OneZeroOneGame.class.getName();
	
	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Assets.instance.init(new AssetManager());
		setScreen(new MenuScreen(this));
		Gdx.app.debug(TAG, "OneZeroOneGame has been created");
	}
	
}
