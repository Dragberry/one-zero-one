package org.dragberry.ozo;

import org.dragberry.ozo.admob.AdsController;
import org.dragberry.ozo.admob.AdsControllerAdapter;
import org.dragberry.ozo.screen.DirectedGame;
import org.dragberry.ozo.screen.MainMenuScreen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

public class OneZeroOneGame extends DirectedGame {
	
	private static final  String TAG = OneZeroOneGame.class.getName();

	public OneZeroOneGame(AdsController adsController) {
		super(adsController);
	}

	public OneZeroOneGame() {
		this(new AdsControllerAdapter());
	}

	@Override
	public void create() {
		super.create();
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		setScreen(new MainMenuScreen(this));
		Gdx.app.debug(TAG, "OneZeroOneGame has been created");
	}

}
