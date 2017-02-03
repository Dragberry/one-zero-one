package org.dragberry.ozo.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import org.dragberry.ozo.game.level.settings.ReachTheGoalLevelSettings;
import org.dragberry.ozo.screen.transitions.ScreenTransitionFade;

public class MainMenuScreen extends AbstractGameScreen {
	
	private static final String TAG = MainMenuScreen.class.getName();
	private static final String EXIT_LBL = "Exit";
	private static final String START_GAME_LBL = "Start";
	private static final String FREEPLAY_LBL = "Freeplay";

	private Stage stage;

	private float buttonWidth;

	public MainMenuScreen(DirectedGame game) {
		super(game);
	}

	@Override
	public InputProcessor getScreenInputProcessor() {
		return stage;
	}

	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(MenuSkin.BACKGROUND_COLOR.r, MenuSkin.BACKGROUND_COLOR.g, MenuSkin.BACKGROUND_COLOR.b, MenuSkin.BACKGROUND_COLOR.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		stage = new Stage();

		buttonWidth = stage.getWidth() * 0.75f;

		stage.addActor(createNewGameBtn());
		stage.addActor(createRandomGameBtn());
		stage.addActor(createExitBtn());
	}

	@Override
	public void hide() {
		stage.dispose();
	}

	@Override
	public void pause() {

	}
	
	private TextButton createNewGameBtn() {
		TextButton btn = new TextButton(START_GAME_LBL, MenuSkin.getSkin());
		btn.setWidth(buttonWidth);
		btn.setPosition(
				Gdx.graphics.getWidth() / 2 - btn.getWidth() / 2,
				Gdx.graphics.getHeight() / 2 - btn.getHeight() / 2 + btn.getHeight() * 1.5f) ;
		btn.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new SelectLevelMenuScreen(game), ScreenTransitionFade.init(), MainMenuScreen.this.getClass());
			}
		});
		return btn;
	}

	private TextButton createRandomGameBtn() {
		TextButton btn = new TextButton(FREEPLAY_LBL, MenuSkin.getSkin());
		btn.setWidth(buttonWidth);
		btn.setPosition(
				Gdx.graphics.getWidth() / 2 - btn.getWidth() / 2,
				Gdx.graphics.getHeight() / 2 - btn.getHeight() / 2);
		btn.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.playLevel(new ReachTheGoalLevelSettings("Freeplay", 25, 99), MainMenuScreen.this.getClass());
			}
		});
		return btn;
	}
	
	private TextButton createExitBtn() {
		TextButton btn = new TextButton(EXIT_LBL, MenuSkin.getSkin());
		btn.setWidth(buttonWidth);
		btn.setPosition(
				Gdx.graphics.getWidth() / 2 - btn.getWidth() / 2,
				Gdx.graphics.getHeight() / 2 - btn.getHeight() / 2 - btn.getHeight() * 1.5f);
		btn.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				switch (Gdx.app.getType()) {
				case iOS:
					// TODO for IOS
					break;
				default:
					Gdx.app.debug(TAG, "Exit from the application");
					Gdx.app.exit();
					break;
					
				}
			}
		});
		return btn;
	}
	
}
