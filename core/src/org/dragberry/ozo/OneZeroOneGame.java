package org.dragberry.ozo;

import org.dragberry.ozo.platform.Platform;
import org.dragberry.ozo.platform.PlatformAdapter;
import org.dragberry.ozo.game.DirectedGame;
import org.dragberry.ozo.screen.MainMenuScreen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

public class OneZeroOneGame extends DirectedGame {
	
	public OneZeroOneGame(Platform platform, boolean auditEnabled) {
		super(platform, auditEnabled);
	}

	public OneZeroOneGame() {
		this(new PlatformAdapter(), false);
	}

	@Override
	public void create() {
		super.create();
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		setScreen(new MainMenuScreen(this));
	}

}
