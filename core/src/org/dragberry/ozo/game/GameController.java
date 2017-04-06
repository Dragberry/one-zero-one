package org.dragberry.ozo.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;

import org.dragberry.ozo.common.audit.AuditEventType;
import org.dragberry.ozo.common.audit.LevelAttemptAuditEventRequest;
import org.dragberry.ozo.common.audit.LevelAttemptStatus;
import org.dragberry.ozo.common.levelresult.NewLevelResultsRequest;
import org.dragberry.ozo.common.levelresult.NewLevelResultsResponse;
import org.dragberry.ozo.game.level.Level;
import org.dragberry.ozo.game.objects.Unit;
import org.dragberry.ozo.game.util.CameraHelper;
import org.dragberry.ozo.http.HttpClient;
import org.dragberry.ozo.http.PostHttpTask;
import org.dragberry.ozo.screen.popup.AbstractPopup;
import org.dragberry.ozo.screen.popup.DefeatPopup;
import org.dragberry.ozo.screen.popup.PausePopup;
import org.dragberry.ozo.screen.popup.VictoryPopup;

import java.text.MessageFormat;

public class GameController extends InputAdapter {

	private static final String TAG = GameController.class.getName();

	public Level<?> level;

	public static GameController instance = new GameController();

	public GameController init(Level<?> level, boolean restore) {
		this.level = level;
		this.level.reset(restore);
		return this;
	}

	public void update(float deltaTime) {
		level.update(deltaTime);
    	handleDebugInput(deltaTime);
		CameraHelper.INSTANCE.update(deltaTime);
    }

	public void logLevelAttempt(LevelAttemptStatus status) {
		DirectedGame.game.logAuditEvent(populateLevelAttempt(status));
	}

	private LevelAttemptAuditEventRequest populateLevelAttempt(LevelAttemptStatus status) {
		LevelAttemptAuditEventRequest attempt = new LevelAttemptAuditEventRequest();
		attempt.setUserId(DirectedGame.game.platform.getUser().getId());
		attempt.setType(AuditEventType.LEVEL_ATTEMPT);
		attempt.setLevelId(level.settings.levelId);
		attempt.setStatus(status);
		switch (status) {
			case FAILED:
			case INTERRUPTED:
			case PAUSED:
			case SUCCESS:
				attempt.setLostUnits(level.lostNumbers);
				attempt.setTime((int) level.time);
				attempt.setSteps(level.steps);
				break;
			default:
				attempt.setLostUnits(null);
				attempt.setTime(null);
				attempt.setSteps(null);
		}
		return attempt;
	}

	public void onGameLost(Level<?> level) {
		level.started = false;
		DirectedGame.game.setPopup(DirectedGame.game.getScreen(DefeatPopup.class));
		logLevelAttempt(LevelAttemptStatus.FAILED);
	}

	public void onGameWon(final Level<?> level) {
		level.started = false;

		NewLevelResultsRequest newResults = level.formNewResults();
		newResults.setLevelId(level.settings.levelId);
		newResults.setUserId(DirectedGame.game.platform.getUser().getId());
		if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
			Gdx.app.debug(TAG, "New results have formed:\n" + newResults);
		}

		NewLevelResultsResponse response = level.settings.checkLocalResults(newResults);
		level.settings.completed = true;
		level.settings.updateResults(response);

		if (!DirectedGame.game.platform.getUser().isDefault()) {
			DirectedGame.game.platform.getHttpClient().executeTask(
					new PostHttpTask<NewLevelResultsRequest, NewLevelResultsResponse>(
							newResults, NewLevelResultsResponse.class, HttpClient.URL.NEW_RESULT) {

						@Override
						public void onComplete(NewLevelResultsResponse result) {
							level.settings.updateResults(result);
						}
					});
		}

		DirectedGame.game.setPopup(DirectedGame.game.getScreen(level.getGameFinishedPopup()).init(response));

		logLevelAttempt(LevelAttemptStatus.SUCCESS);
	}

    private void onScreenTouch(float xCoord, float yCoord) {
		if (level.inStepMotion()) {
			// level is in motion
			return;
		}
		Unit currentSelectedUnit = level.selectUnit(xCoord, yCoord);
		if (currentSelectedUnit == null) {
			// unit is border unit
			level.deselectAllUnits();
			return;
		}
		if (level.isUnitSelectedAgain(currentSelectedUnit)) {
			level.startStepMotion();
			return;
		}
		if (level.selectedUnit != null) {
			level.deselectAllUnits();
		}
		if (level.selectedUnit == null) {
			level.processFirstSection(currentSelectedUnit);
		}
    }

	private boolean isBorderUnit(Unit selectedUnit) {
		return selectedUnit.x == 0 || selectedUnit.x == level.width - 1
				|| selectedUnit.y == 0 || selectedUnit.y == level.height - 1;
	}
	
	private void handleDebugInput(float deltaTime) {
		if (Gdx.app.getType() != ApplicationType.Desktop) {
			return;
		}

		float camMoveSpeed = 100 * deltaTime;
		float camMoveSpeedAccelerationFactor = 100;

		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
			camMoveSpeed *= camMoveSpeedAccelerationFactor;
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			moveCamera(-camMoveSpeed, 0);
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			moveCamera(camMoveSpeed, 0);
		}
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			moveCamera(0, camMoveSpeed);
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			moveCamera(0, -camMoveSpeed);
		}
		if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) {
			CameraHelper.INSTANCE.setPosition(0, 0);
		}

		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAccelerationfactor = 5;

		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
			camZoomSpeed *= camZoomSpeedAccelerationfactor;
		}
		if (Gdx.input.isKeyPressed(Keys.COMMA)) {
			CameraHelper.INSTANCE.addZoom(camZoomSpeed);
			Gdx.app.log(TAG, MessageFormat.format("Zoom={0}", CameraHelper.INSTANCE.getZoom()));
		}
		if (Gdx.input.isKeyPressed(Keys.PERIOD)) {
			CameraHelper.INSTANCE.addZoom(-camZoomSpeed);
			Gdx.app.log(TAG, MessageFormat.format("Zoom={0}", CameraHelper.INSTANCE.getZoom()));
		}
		if (Gdx.input.isKeyPressed(Keys.SLASH)) {
			CameraHelper.INSTANCE.setZoom(1);
		}

	}

    private void moveCamera(float x, float y) {
    	x += CameraHelper.INSTANCE.getPosition().x;
    	y += CameraHelper.INSTANCE.getPosition().y;
		CameraHelper.INSTANCE.setPosition(x, y);
    }

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Vector3 touchCoord = CameraHelper.INSTANCE.camera.unproject(new Vector3(screenX, screenY, 0));
		onScreenTouch(touchCoord.x, touchCoord.y);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
			case Input.Keys.BACK:
			case Input.Keys.ESCAPE:
				DirectedGame.game.setPopup(DirectedGame.game.getScreen(PausePopup.class).init(level));
				break;
		}
		return false;
	}
}
