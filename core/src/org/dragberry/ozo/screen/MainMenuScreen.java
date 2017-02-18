package org.dragberry.ozo.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.level.settings.ReachTheGoalLevelSettings;
import org.dragberry.ozo.game.util.Constants;
import org.dragberry.ozo.screen.transitions.ScreenTransitionFade;

public class MainMenuScreen extends AbstractGameScreen {

	private static final String TAG = MainMenuScreen.class.getName();
	public static final float BTN_HEIGHT = 100.0f;

	private Stage stage;

	private float buttonWidth;
	private float firstBtnY;

	public MainMenuScreen(DirectedGame game) {
		super(game);
	}

	@Override
	public InputProcessor getInputProcessor() {
		return stage;
	}

	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(Constants.BACKGROUND.r, Constants.BACKGROUND.g, Constants.BACKGROUND.b, Constants.BACKGROUND.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.getBatch().begin();
		TextureRegion logo = Assets.instance.skin.logo;
		float zoom = 0.8f;
		stage.getBatch().draw(logo,
				(stage.getWidth() - logo.getRegionWidth() * zoom) / 2,
				firstBtnY + logo.getRegionHeight() * 0.5f,
				0, 0,
				logo.getRegionWidth(), logo.getRegionHeight(),
				zoom, zoom, 0f);
		stage.getBatch().end();
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		stage = new CustomStage(new ScalingViewport(Scaling.stretch,
				Constants.VIEWPORT_GUI_WIDTH,
				Gdx.graphics.getHeight() * Constants.VIEWPORT_GUI_WIDTH / Gdx.graphics.getWidth()),
				new ActionExecutor() {

					@Override
					public void execute() {
						Gdx.app.debug(TAG, "Exit from the application");
						Gdx.app.exit();
					}
				});

		buttonWidth = stage.getWidth() * 0.75f;
		firstBtnY = stage.getHeight() / 2;

		stage.addActor(createNewGameBtn());
		stage.addActor(createRandomGameBtn());
		stage.addActor(createResultsBtn());
		stage.addActor(createExitBtn());

		game.adsController.showBannerAdNew();
	}

	@Override
	public void hide() {
		stage.dispose();
	}

	@Override
	public void pause() {

	}
	
	private TextButton createNewGameBtn() {
		TextButton btn = new TextButton(Assets.instance.translation.format("ozo.start"), Assets.instance.skin.skin);
		btn.setWidth(buttonWidth);
		btn.setHeight(BTN_HEIGHT);
		btn.setPosition(
				stage.getWidth() / 2 - btn.getWidth() / 2, firstBtnY) ;
		btn.addListener(new ClickListener() {
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new SelectLevelMenuScreen(game), ScreenTransitionFade.init(), MainMenuScreen.this.getClass());
			}
		});
		return btn;
	}

	private TextButton createRandomGameBtn() {
		final String freeplay = "ozo.freeplay";
		TextButton btn = new TextButton(Assets.instance.translation.format(freeplay), Assets.instance.skin.skin);
		btn.setWidth(buttonWidth);
		btn.setHeight(BTN_HEIGHT);
		btn.setPosition(
				stage.getWidth() / 2 - btn.getWidth() / 2, firstBtnY - BTN_HEIGHT * 1.5f);
		btn.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.playLevel(new ReachTheGoalLevelSettings(
						freeplay, -999, 9999), MainMenuScreen.this.getClass());
			}
		});
		return btn;
	}
	
	private TextButton createResultsBtn() {
		final String freeplay = "ozo.bestResults";
		TextButton btn = new TextButton(Assets.instance.translation.format(freeplay), Assets.instance.skin.skin);
		btn.setWidth(buttonWidth);
		btn.setHeight(BTN_HEIGHT);
		btn.setPosition(
				stage.getWidth() / 2 - btn.getWidth() / 2, firstBtnY - BTN_HEIGHT * 3f);
		btn.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new BestResultScreen(game), ScreenTransitionFade.init(), MainMenuScreen.this.getClass());
			}
		});
		return btn;
	}
	
	private TextButton createExitBtn() {
		TextButton btn = new TextButton(Assets.instance.translation.format("ozo.exit"), Assets.instance.skin.skin);
		btn.setWidth(buttonWidth);
		btn.setHeight(BTN_HEIGHT);
		btn.setPosition(
				stage.getWidth() / 2 - btn.getWidth() / 2, firstBtnY - BTN_HEIGHT * 4.5f);
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
