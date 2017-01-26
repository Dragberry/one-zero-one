package org.dragberry.ozo.game;

import java.text.MessageFormat;

import org.dragberry.ozo.game.objects.Unit;
import org.dragberry.ozo.game.util.CameraHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class GameController {

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
		cameraHelper = new CameraHelper();
		units = new Unit[gameWidth][gameHeight];
		for (int x = 0; x < gameWidth; x++) {
			for (int y = 0; y < gameHeight; y++) {
				units[x][y] = new Unit(getRandomValue(), x, y);
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
			cameraHelper.setPosition(0, 0);
		}
		
		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAccelerationfactor = 5;
		
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
			camZoomSpeed *= camZoomSpeedAccelerationfactor;
		}
		if (Gdx.input.isKeyPressed(Keys.COMMA)) {
			cameraHelper.addZoom(camZoomSpeed);
			Gdx.app.log(TAG, MessageFormat.format("Zoom={0}", cameraHelper.getZoom()));
		}
		if (Gdx.input.isKeyPressed(Keys.PERIOD)) {
			cameraHelper.addZoom(-camZoomSpeed);
			Gdx.app.log(TAG, MessageFormat.format("Zoom={0}", cameraHelper.getZoom()));
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
    
    public void onScreenTouch(float xCoord, float yCoord) {
    	Unit selectedUnit = null;
    	for (int x = 0; x < gameWidth; x++) {
			for (int y = 0; y < gameHeight; y++) {
				if (units[x][y].bounds.contains(xCoord, yCoord)) {
					selectedUnit = units[x][y];
					Gdx.app.debug(TAG, "unitX=" + x + " unitY=" + y);
					if (isUnitOnBorder(selectedUnit)) {
						selectedUnit = null;
					}
				} else {
					units[x][y].selected = false;
				}
			}
		}
    	if (selectedUnit != null) {
    		Array<Unit> neighbors = new Array<Unit>(4);
    		neighbors.add(units[selectedUnit.gameX][selectedUnit.gameY - 1]);
    		neighbors.add(units[selectedUnit.gameX + 1][selectedUnit.gameY]);
    		neighbors.add(units[selectedUnit.gameX][selectedUnit.gameY + 1]);
    		neighbors.add(units[selectedUnit.gameX - 1][selectedUnit.gameY]);
    		
    		if (selectedUnit.selected) {
    			for (Unit unit : neighbors) {
    				selectedUnit.value += unit.value;
    			}
    			shiftRightUnits(selectedUnit);
    			shiftLeftUnits(selectedUnit);
    			shiftTopUnits(selectedUnit);
    			shiftBottomUnits(selectedUnit);
    		}
    		
    		selectedUnit.selected = !selectedUnit.selected;
    		for (Unit unit : neighbors) {
    			unit.selected = selectedUnit.selected;
    		}
    		
	    }
    }
    
    private void shiftBottomUnits(Unit selectedUnit) {
		for (int y = selectedUnit.gameY - 1; y > 0; y--) {
			Unit unitToMove = units[selectedUnit.gameX][y - 1];
			units[selectedUnit.gameX][y] = unitToMove;
			unitToMove.moveTo(selectedUnit.gameX, y);
		}
		units[selectedUnit.gameX][0] = new Unit(getRandomValue(), selectedUnit.gameX, 0);
	}
    
    private void shiftTopUnits(Unit selectedUnit) {
		for (int y = selectedUnit.gameY + 1; y < gameHeight - 1; y++) {
			Unit unitToMove = units[selectedUnit.gameX][y + 1];
			units[selectedUnit.gameX][y] = unitToMove;
			unitToMove.moveTo(selectedUnit.gameX, y);
		}
		units[selectedUnit.gameX][gameHeight - 1] = new Unit(getRandomValue(), selectedUnit.gameX, gameHeight - 1);
	}

	private void shiftRightUnits(Unit selectedUnit) {
		for (int x = selectedUnit.gameX + 1; x < gameWidth - 1; x++) {
			Unit unitToMove = units[x + 1][selectedUnit.gameY];
			units[x][selectedUnit.gameY] = unitToMove;
			unitToMove.moveTo(x, selectedUnit.gameY);
		}
		units[gameWidth - 1][selectedUnit.gameY] = new Unit(getRandomValue(), gameWidth - 1, selectedUnit.gameY);
	}

	private void shiftLeftUnits(Unit selectedUnit) {
		for (int x = selectedUnit.gameX - 1; x > 0; x--) {
			Unit unitToMove = units[x - 1][selectedUnit.gameY];
			units[x][selectedUnit.gameY] = unitToMove;
			unitToMove.moveTo(x, selectedUnit.gameY);
		}
		units[0][selectedUnit.gameY] = new Unit(getRandomValue(), 0, selectedUnit.gameY);
	}

	private boolean isUnitOnBorder(Unit selectedUnit) {
		return selectedUnit.gameX == 0 || selectedUnit.gameX == gameWidth - 1
				|| selectedUnit.gameY == 0 || selectedUnit.gameY == gameHeight - 1;
	}
    
	
}
