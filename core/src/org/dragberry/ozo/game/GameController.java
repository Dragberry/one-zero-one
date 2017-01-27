package org.dragberry.ozo.game;

import java.text.MessageFormat;

import org.dragberry.ozo.game.level.Level;
import org.dragberry.ozo.game.objects.Unit;
import org.dragberry.ozo.game.objects.Unit.Direction;
import org.dragberry.ozo.game.util.CameraHelper;
import org.dragberry.ozo.game.util.Constants;
import org.dragberry.ozo.screen.MenuScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class GameController {

	private static final String TAG = GameController.class.getName();
	
	private enum State {
		FIXED, IN_MOTION
	}
	
	public CameraHelper cameraHelper;
	
	private State state = State.FIXED;
	private float motionTime = 0;
	private Unit selectedUnit = null;
	
	private Game game;
	public Level level;
	
	public Unit[][] units;
	
	public GameController(Game game, Level level) {
		this.game = game;
		this.level = level;
		init();
	}
	
	public void init() {
		cameraHelper = new CameraHelper();
		units = new Unit[level.width][level.height];
		for (int x = 0; x < level.width; x++) {
			for (int y = 0; y < level.height; y++) {
				units[x][y] = new Unit(getRandomValue(), x, y);
			}
		}
	}

	public static int getRandomValue() {
		return MathUtils.random(-1, 1);
	}
	
    public void update(float deltaTime) {
    	level.time += deltaTime; 
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
        cameraHelper.update(deltaTime);
    }

    private void updateMotion(float deltaTime) {
    	float step = deltaTime * Constants.UNIT_SPEED;
		shiftTopUnits(step);
		shiftRightUnits(step);
		shiftBottomUnits(step);
		shiftLeftUnits(step);
    }
    
    private void shiftTopUnits(float step) {
    	for (int y = selectedUnit.gameY + 1; y < level.height; y++) {
			Unit unitToMove = units[selectedUnit.gameX][y];
			unitToMove.moveTo(Direction.SOUTH, step);
		}
    }
    
    private void shiftRightUnits(float step) {
    	for (int x = selectedUnit.gameX + 1; x < level.width; x++) {
			Unit unitToMove = units[x][selectedUnit.gameY];
			unitToMove.moveTo(Direction.WEST, step);
		}
    }
    
    private void shiftBottomUnits(float step) {
    	for (int y = selectedUnit.gameY - 1; y >= 0; y--) {
			Unit unitToMove = units[selectedUnit.gameX][y];
			unitToMove.moveTo(Direction.NORTH, step);
		}
    }
    
    private void shiftLeftUnits(float step) {
    	for (int x = selectedUnit.gameX - 1; x >= 0; x--) {
			Unit unitToMove = units[x][selectedUnit.gameY];
			unitToMove.moveTo(Direction.EAST, step);
		}
    }
    
    private void finishStepExecution() {
    	// sum neighbors
    	for (Unit neighbor : getNeighbors(selectedUnit)) {
    		selectedUnit.value +=neighbor.value;
    	}
    	// logical shift all units
    	// fix and recalculate position
    	shiftTopUnits(selectedUnit);
    	shiftRightUnits(selectedUnit);
    	shiftBottomUnits(selectedUnit);
    	shiftLeftUnits(selectedUnit);
    	selectedUnit.selected = false;
    	selectedUnit = null;
    	level.steps++;
    	if (level.isLost(units)) {
			backToMenu();
			Gdx.app.debug(TAG, "Lost!");
			return;
		} 
		if (level.isWon(units)) {
			Gdx.app.debug(TAG, "Won!");
			backToMenu();
			return;
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
		for (int y = selectedUnit.gameY + 1; y < level.height - 1; y++) {
			Unit unitToMove = units[selectedUnit.gameX][y + 1];
			units[selectedUnit.gameX][y] = unitToMove;
			unitToMove.moveTo(selectedUnit.gameX, y);
		}
		units[selectedUnit.gameX][level.height - 1] = new Unit(getRandomValue(), selectedUnit.gameX, level.height - 1);
	}

	private void shiftRightUnits(Unit selectedUnit) {
		for (int x = selectedUnit.gameX + 1; x < level.width - 1; x++) {
			Unit unitToMove = units[x + 1][selectedUnit.gameY];
			units[x][selectedUnit.gameY] = unitToMove;
			unitToMove.moveTo(x, selectedUnit.gameY);
		}
		units[level.width - 1][selectedUnit.gameY] = new Unit(getRandomValue(), level.width - 1, selectedUnit.gameY);
	}

	private void shiftLeftUnits(Unit selectedUnit) {
		for (int x = selectedUnit.gameX - 1; x > 0; x--) {
			Unit unitToMove = units[x - 1][selectedUnit.gameY];
			units[x][selectedUnit.gameY] = unitToMove;
			unitToMove.moveTo(x, selectedUnit.gameY);
		}
		units[0][selectedUnit.gameY] = new Unit(getRandomValue(), 0, selectedUnit.gameY);
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
    
    private Array<Unit> getNeighbors(Unit unit) {
    	Array<Unit> neighbors = new Array<Unit>(4);
		neighbors.add(units[selectedUnit.gameX][selectedUnit.gameY - 1]);
		neighbors.add(units[selectedUnit.gameX + 1][selectedUnit.gameY]);
		neighbors.add(units[selectedUnit.gameX][selectedUnit.gameY + 1]);
		neighbors.add(units[selectedUnit.gameX - 1][selectedUnit.gameY]);
		return neighbors;
    }
    
    public void onScreenTouch(float xCoord, float yCoord) {
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
    	if (selectedUnit != null && currentSelectedUnit != selectedUnit) {
    		// unit is not selected or another unit is selected
			unselectAllUnits();
		} 
    	if (selectedUnit == null) {
    		// unit first selection
    		selectedUnit = currentSelectedUnit;
    		selectedUnit.selected = true;
    		Array<Unit> neighbors = getNeighbors(selectedUnit);
    		for (Unit neighbor : neighbors) {
    			neighbor.selectedNeighbor = true;
			}
    	}
    }

	private boolean isBorderUnit(Unit selectedUnit) {
		return selectedUnit.gameX == 0 || selectedUnit.gameX == level.width - 1
				|| selectedUnit.gameY == 0 || selectedUnit.gameY == level.height - 1;
	}
	
	private void backToMenu() {
		game.setScreen(new MenuScreen(game));
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
    
	
}
