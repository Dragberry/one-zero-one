package org.dragberry.ozo.game;

import java.text.MessageFormat;

import org.dragberry.ozo.game.level.Level;
import org.dragberry.ozo.game.objects.Unit;
import org.dragberry.ozo.game.objects.Unit.Direction;
import org.dragberry.ozo.game.util.CameraHelper;
import org.dragberry.ozo.game.util.Constants;
import org.dragberry.ozo.screen.DirectedGame;
import org.dragberry.ozo.screen.MainMenuScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;

public class GameController extends InputAdapter {

	private static final String TAG = GameController.class.getName();
	
	private enum State {
		FIXED, IN_MOTION
	}
	
	private State state = State.FIXED;
	private float motionTime = 0;
	private Unit selectedUnit = null;
	
	private DirectedGame game;
	public Level level;
	
	public Unit[][] units;
	private Unit[] neighbors = new Unit[4];
	
	public GameController(DirectedGame game) {
		this.game = game;
	}
	
	public void init(Level level) {
		this.level = level;
		units = new Unit[level.width][level.height];
		for (int x = 0; x < level.width; x++) {
			for (int y = 0; y < level.height; y++) {
				units[x][y] = level.generateUnit(x, y);
			}
		}
	}

    public void update(float deltaTime) {
    	level.time += deltaTime;
		level.update(deltaTime);
    	if (state == State.IN_MOTION) {
    		motionTime += deltaTime;
    		if (motionTime >= Constants.UNIT_MOTION_TIME + 0.1) {
        		state = State.FIXED;
        		motionTime = 0;
        		finishStepExecution();
        	} else {
        		updateMotion(deltaTime);
        	}
    	}
    	handleDebugInput(deltaTime);
		CameraHelper.INSTANCE.update(deltaTime);
    }

	private boolean isGameFinished() {
		if (level.isLost(units, selectedUnit, neighbors)) {
			Gdx.app.debug(TAG, "Lost!");
			game.back();
			return true;
		}
		if (level.isWon(units, selectedUnit, neighbors)) {
			Gdx.app.debug(TAG, "Won!");
			game.back();
			return true;
		}
		return false;
	}

    private void updateMotion(float deltaTime) {
    	float step = deltaTime * Constants.UNIT_SPEED;
		shiftTopUnits(step);
		shiftRightUnits(step);
		shiftBottomUnits(step);
		shiftLeftUnits(step);
    }
    
    private void shiftTopUnits(float step) {
    	for (int y = selectedUnit.y + 1; y < level.height; y++) {
			Unit unitToMove = units[selectedUnit.x][y];
			unitToMove.moveTo(Direction.SOUTH, step);
		}
    }
    
    private void shiftRightUnits(float step) {
    	for (int x = selectedUnit.x + 1; x < level.width; x++) {
			Unit unitToMove = units[x][selectedUnit.y];
			unitToMove.moveTo(Direction.WEST, step);
		}
    }
    
    private void shiftBottomUnits(float step) {
    	for (int y = selectedUnit.y - 1; y >= 0; y--) {
			Unit unitToMove = units[selectedUnit.x][y];
			unitToMove.moveTo(Direction.NORTH, step);
		}
    }
    
    private void shiftLeftUnits(float step) {
    	for (int x = selectedUnit.x - 1; x >= 0; x--) {
			Unit unitToMove = units[x][selectedUnit.y];
			unitToMove.moveTo(Direction.EAST, step);
		}
    }
    
    private void finishStepExecution() {
    	// sum neighbors
    	selectedUnit.previousValue = selectedUnit.value;
    	for (Unit neighbor : neighbors) {
    		selectedUnit.value +=neighbor.value;
    	}
    	// logical shift all units
    	// fix and recalculate position
    	shiftTopUnits(selectedUnit);
    	shiftRightUnits(selectedUnit);
    	shiftBottomUnits(selectedUnit);
    	shiftLeftUnits(selectedUnit);
    	level.steps++;
    	if (isGameFinished()) {
			return;
		}
    	selectedUnit.selected = false;
    	selectedUnit = null;
    }
    
    private void shiftBottomUnits(Unit selectedUnit) {
		for (int y = selectedUnit.y - 1; y > 0; y--) {
			Unit unitToMove = units[selectedUnit.x][y - 1];
			units[selectedUnit.x][y] = unitToMove;
			unitToMove.moveTo(selectedUnit.x, y);
		}
		units[selectedUnit.x][0] = level.generateUnit(selectedUnit.x, 0);
	}
    
    private void shiftTopUnits(Unit selectedUnit) {
		for (int y = selectedUnit.y + 1; y < level.height - 1; y++) {
			Unit unitToMove = units[selectedUnit.x][y + 1];
			units[selectedUnit.x][y] = unitToMove;
			unitToMove.moveTo(selectedUnit.x, y);
		}
		units[selectedUnit.x][level.height - 1] = level.generateUnit(selectedUnit.x, level.height - 1);
	}

	private void shiftRightUnits(Unit selectedUnit) {
		for (int x = selectedUnit.x + 1; x < level.width - 1; x++) {
			Unit unitToMove = units[x + 1][selectedUnit.y];
			units[x][selectedUnit.y] = unitToMove;
			unitToMove.moveTo(x, selectedUnit.y);
		}
		units[level.width - 1][selectedUnit.y] = level.generateUnit(level.width - 1, selectedUnit.y);
	}

	private void shiftLeftUnits(Unit selectedUnit) {
		for (int x = selectedUnit.x - 1; x > 0; x--) {
			Unit unitToMove = units[x - 1][selectedUnit.y];
			units[x][selectedUnit.y] = unitToMove;
			unitToMove.moveTo(x, selectedUnit.y);
		}
		units[0][selectedUnit.y] = level.generateUnit(0, selectedUnit.y);
	}
	
    private Unit getSelectedUnit(float xCoord, float yCoord) {
    	for (int x = 0; x < level.width; x++) {
			for (int y = 0; y < level.height; y++) {
				if (units[x][y].bounds.contains(xCoord, yCoord)) {
					Gdx.app.debug(TAG, "unitX=" + x + " unitY=" + y);
					return units[x][y];
				}
			}
    	}
    	return null;
    }
    
    private void unselectAllUnits() {
    	selectedUnit = null;
    	for (int x = 0; x < level.width; x++) {
			for (int y = 0; y < level.height; y++) {
				units[x][y].selected = false;
				units[x][y].selectedNeighbor = false;
			}
    	}
    }
    
    private void getNeighbors(Unit unit) {
		neighbors[0] = units[unit.x][unit.y - 1];
		neighbors[1] = units[unit.x + 1][unit.y];
		neighbors[2] = units[unit.x][unit.y + 1];
		neighbors[3] = units[unit.x - 1][unit.y];
    }
    
    private void onScreenTouch(float xCoord, float yCoord) {
    	if (state == State.IN_MOTION) {
    		// if game in motion, break control
    		return;
    	}
    	Unit currentSelectedUnit = getSelectedUnit(xCoord, yCoord);
    	if (currentSelectedUnit == null || isBorderUnit(currentSelectedUnit)) {
    		// unit is border unit
    		unselectAllUnits();
    		return;
    	}
    	if (selectedUnit == currentSelectedUnit) {
    		// step execution is started
    		Gdx.app.debug(TAG, "Motion has been started");
    		state = State.IN_MOTION;
    		return;
    	}
    	if (selectedUnit != null) {
    		// unit is not selected or another unit is selected
			unselectAllUnits();
		} 
    	if (selectedUnit == null) {
    		// unit first selection
    		selectedUnit = currentSelectedUnit;
    		selectedUnit.selected = true;
    		getNeighbors(selectedUnit);
    		for (Unit neighbor : neighbors) {
    			neighbor.selectedNeighbor = true;
			}
    	}
    }

	private boolean isBorderUnit(Unit selectedUnit) {
		return selectedUnit.x == 0 || selectedUnit.x == level.width - 1
				|| selectedUnit.y == 0 || selectedUnit.y == level.height - 1;
	}
	
	private void backToMenu() {
		game.setScreen(new MainMenuScreen(game));
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
				backToMenu();
				break;
		}
		return false;
	}
}
