package org.dragberry.ozo.game;

import java.text.MessageFormat;

import org.dragberry.ozo.game.level.Level;
import org.dragberry.ozo.game.objects.Unit;
import org.dragberry.ozo.game.objects.Unit.Direction;
import org.dragberry.ozo.game.util.CameraHelper;
import org.dragberry.ozo.game.util.Constants;
import org.dragberry.ozo.game.util.DigitUtil;
import org.dragberry.ozo.screen.DirectedGame;
import org.dragberry.ozo.screen.popup.ConfirmationPopup;
import org.dragberry.ozo.screen.popup.DefeatScreen;
import org.dragberry.ozo.screen.popup.VictoryPopup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class GameController extends InputAdapter {

	private static final String TAG = GameController.class.getName();
	
	private enum State {
		FIXED, IN_MOTION
	}

	private State state;
	private float motionTime ;
	private Unit selectedUnit;
	
	private DirectedGame game;
	public Level<?> level;
	
	public Unit[][] units;
	private Array<Unit> neighbors;

	private int negCount;
	private int negSum;
	private int posCount;
	private int posSum;
	private int zeroCount;
	
	public Array<TextureRegion> posCountDigits;
	public Array<TextureRegion> posSumDigits;
	public Array<TextureRegion> zeroCountDigits;
	public Array<TextureRegion> negCountDigits;
	public Array<TextureRegion> negSumDigits;

	public static GameController instance;

	public static GameController init(DirectedGame game, Level<?> level) {
		if (instance == null) {
			instance = new GameController();
			instance.game = game;
			instance.units = new Unit[level.width][level.height];
			instance.neighbors = new Array<Unit>(4);

			instance.posCountDigits = new Array<TextureRegion>(4);
			instance.posSumDigits = new Array<TextureRegion>(4);
			instance.zeroCountDigits = new Array<TextureRegion>(4);
			instance.negCountDigits = new Array<TextureRegion>(4);
			instance.negSumDigits = new Array<TextureRegion>(4);
		} else {
			instance.neighbors.clear();
			instance.negCount = 0;
			instance.negSum = 0;
			instance.posCount = 0;
			instance.posSum = 0;
			instance.zeroCount = 0;
		}

		instance.level = level;

		instance.state = State.FIXED;
		instance.motionTime = 0;
		instance.selectedUnit = null;

		for (int x = 0; x < level.width; x++) {
			for (int y = 0; y < level.height; y++) {
				Unit unit = level.generateUnit(x, y, instance.selectedUnit, instance.units[x][y]);
				instance.units[x][y] = unit;
				instance.updateStateForUnit(unit);
			}
		}
		instance.resolveStateDigits();
		return instance;
	}

	private void updateStateForUnit(Unit unit) {
		int value = unit.getValue();
		if (value < 0) {
			negCount++;
			negSum += value;
		} else if (value > 0) {
			posCount++;
			posSum += value;
		} else {
			zeroCount++;
		}
	}

	private void resolveStateDigits() {
		DigitUtil.resolveDigits(posCount, posCountDigits, false);
		DigitUtil.resolveDigits(posSum, posSumDigits);
		DigitUtil.resolveDigits(zeroCount, zeroCountDigits, false);
		DigitUtil.resolveDigits(negCount, negCountDigits, false);
		DigitUtil.resolveDigits(negSum, negSumDigits);
	}

	public void update(float deltaTime) {
    	if (level.started) {
    		level.time += deltaTime;
    		for (int x = 0; x < level.width; x++) {
    			for (int y = 0; y < level.height; y++) {
    				units[x][y].update(deltaTime);
    			}
    		}
    	}
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
			level.started = false;
			game.setPopup(new DefeatScreen(game));
			return true;
		}
		if (level.isWon(units, selectedUnit, neighbors)) {
			level.started = false;
			level.save();
			game.setPopup(new VictoryPopup(game));
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
    	selectedUnit.previousValue = selectedUnit.getValue();
    	int valueToAdd = 0;
    	for (Unit neighbor : neighbors) {
    		valueToAdd +=neighbor.getValue();
    	}
    	selectedUnit.addValue(valueToAdd);
    	// logical shift all units
    	// fix and recalculate position
    	shiftTopUnits(selectedUnit);
    	shiftRightUnits(selectedUnit);
    	shiftBottomUnits(selectedUnit);
    	shiftLeftUnits(selectedUnit);
		refreshState();
    	level.steps++;
    	if (isGameFinished()) {
			return;
		}
    	selectedUnit.unselect();
    	selectedUnit = null;
    }

	private void refreshState() {
		negCount = 0;
		negSum = 0;
		posCount = 0;
		posSum = 0;
		zeroCount = 0;
		for (int x = 0; x < level.width; x++) {
			for (int y = 0; y < level.height; y++) {
				updateStateForUnit(units[x][y]);
			}
		}
		resolveStateDigits();
	}

	private void shiftBottomUnits(Unit selectedUnit) {
		Unit neighbor = null;
		if (selectedUnit.y != 0) {
			neighbor = units[selectedUnit.x][selectedUnit.y - 1];
		}
		for (int y = selectedUnit.y - 1; y > 0; y--) {
			Unit unitToMove = units[selectedUnit.x][y - 1];
			units[selectedUnit.x][y] = unitToMove;
			unitToMove.moveTo(selectedUnit.x, y);
		}
		if (neighbor != null) {
			units[selectedUnit.x][0] = level.generateUnit(selectedUnit.x, 0, selectedUnit, neighbor);
		}
	}
    
    private void shiftTopUnits(Unit selectedUnit) {
		Unit neighbor = null;
		if (selectedUnit.y != level.height - 1) {
			neighbor = units[selectedUnit.x][selectedUnit.y + 1];
		}
		for (int y = selectedUnit.y + 1; y < level.height - 1; y++) {
			Unit unitToMove = units[selectedUnit.x][y + 1];
			units[selectedUnit.x][y] = unitToMove;
			unitToMove.moveTo(selectedUnit.x, y);
		}
		if (neighbor != null) {
			units[selectedUnit.x][level.height - 1] = level.generateUnit(selectedUnit.x, level.height - 1, selectedUnit, neighbor);
		}
	}

	private void shiftRightUnits(Unit selectedUnit) {
		Unit neighbor = null;
		if (selectedUnit.x != level.width - 1) {
			neighbor = units[selectedUnit.x + 1][selectedUnit.y];
		}
		for (int x = selectedUnit.x + 1; x < level.width - 1; x++) {
			Unit unitToMove = units[x + 1][selectedUnit.y];
			units[x][selectedUnit.y] = unitToMove;
			unitToMove.moveTo(x, selectedUnit.y);
		}
		if (neighbor != null) {
			units[level.width - 1][selectedUnit.y] = level.generateUnit(level.width - 1, selectedUnit.y, selectedUnit, neighbor);
		}
	}

	private void shiftLeftUnits(Unit selectedUnit) {
		Unit neighbor = null;
		if (selectedUnit.x != 0) {
			neighbor = units[selectedUnit.x - 1][selectedUnit.y];
		}
		for (int x = selectedUnit.x - 1; x > 0; x--) {
			Unit unitToMove = units[x - 1][selectedUnit.y];
			units[x][selectedUnit.y] = unitToMove;
			unitToMove.moveTo(x, selectedUnit.y);
		}
		if (neighbor != null) {
			units[0][selectedUnit.y] = level.generateUnit(0, selectedUnit.y, selectedUnit, neighbor);
		}
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
    
    private void deselectAllUnits() {
    	selectedUnit = null;
    	for (int x = 0; x < level.width; x++) {
			for (int y = 0; y < level.height; y++) {
				units[x][y].unselect();
				units[x][y].unselectedNeighbor();
			}
    	}
    }
    
    private void getNeighbors(Unit unit) {
    	neighbors.clear();
    	if (unit.y != 0) {
    		neighbors.add(units[unit.x][unit.y - 1]);
    	}
    	if (unit.x != level.width - 1) {
    		neighbors.add(units[unit.x + 1][unit.y]);
    	}
    	if (unit.y != level.height - 1) {
    		neighbors.add(units[unit.x][unit.y + 1]);
    	}
    	if (unit.x != 0) {
    		neighbors.add(units[unit.x - 1][unit.y]);
    	}
    }
    
    private void onScreenTouch(float xCoord, float yCoord) {
    	if (state == State.IN_MOTION) {
    		// if game in motion, break control
    		return;
    	}
    	Unit currentSelectedUnit = getSelectedUnit(xCoord, yCoord);
    	if (currentSelectedUnit == null) {
    		// unit is border unit
    		deselectAllUnits();
    		return;
    	}
    	if (selectedUnit == currentSelectedUnit) {
    		// step execution is started
    		state = State.IN_MOTION;
    		return;
    	}
    	if (selectedUnit != null) {
    		// unit is not selected or another unit is selected
			deselectAllUnits();
		} 
    	if (selectedUnit == null) {
    		// unit first selection
    		selectedUnit = currentSelectedUnit;
    		selectedUnit.select();
    		getNeighbors(selectedUnit);
    		for (Unit neighbor : neighbors) {
    			neighbor.selectedNeighbor();
			}
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
				game.setPopup(new ConfirmationPopup(game));
				break;
		}
		return false;
	}
}
