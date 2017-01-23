package org.dragberry.ozo.game;

import org.dragberry.ozo.game.objects.Unit;
import org.dragberry.ozo.game.util.CameraHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;

public class GameController extends InputAdapter {

	private static final String TAG = GameController.class.getName();
	
	public int gameWidth;
	public int gameHeight;
	
	public Unit[][] units;
	
	public CameraHelper cameraHelper;
	
	public GameController(int gameWidth, int gameHeight) {
		this.gameWidth = gameWidth;
		this.gameHeight = gameHeight;
		init();
	}
	
	public void init() {
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		units = new Unit[gameWidth][gameHeight];
		for (int x = 0; x < gameWidth; x++) {
			for (int y = 0; y < gameHeight; y++) {
				units[x][y] = new Unit(getRandomValue(), x, y, gameWidth, gameHeight);
			}
		}
	}

	public static int getRandomValue() {
		return MathUtils.random(-1, 1);
	}
	
    public void update(float deltaTime) {
    	handleDebugInput(deltaTime);
        cameraHelper.update(deltaTime);
    }

    private void handleDebugInput(float deltaTime) {
		if (Gdx.app.getType() != ApplicationType.Desktop) {
			return;
		}
		
		float camMoveSpeed = 5 * deltaTime;
		float camMoveSpeedAccelerationFactor = 5;
		
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
			cameraHelper.setPosition(0, 0);
		}
		
		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAccelerationfactor = 5;
		
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
			camZoomSpeed *= camZoomSpeedAccelerationfactor;
		}
		if (Gdx.input.isKeyPressed(Keys.COMMA)) {
			cameraHelper.addZoom(camZoomSpeed);
		}
		if (Gdx.input.isKeyPressed(Keys.PERIOD)) {
			cameraHelper.addZoom(-camZoomSpeed);
		}
		if (Gdx.input.isKeyPressed(Keys.SLASH)) {
			cameraHelper.setZoom(1);
		}
		
	}
    
    private void moveCamera(float x, float y) {
    	x += cameraHelper.getPosition().x;
    	y += cameraHelper.getPosition().y;
    	cameraHelper.setPosition(x, y);
    }
    
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    	Gdx.app.log(TAG, "x=" + screenX + " y=" + screenY);
    	return false;
    }
	
	
}
