package org.dragberry.ozo;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.screen.DirectedGame;
import org.dragberry.ozo.screen.MainMenuScreen;
import org.dragberry.ozo.screen.MenuSkin;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

public class OneZeroOneGame extends DirectedGame {
	
	private static final  String TAG = OneZeroOneGame.class.getName();
	
	@Override
	public void create() {
		super.create();
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Assets.instance.init(new AssetManager());
		setScreen(new MainMenuScreen(this));
		Gdx.app.debug(TAG, "OneZeroOneGame has been created");
	}

	@Override
	public void dispose() {
		super.dispose();
		MenuSkin.dispose();
	}
}
