package org.dragberry.ozo.game.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import org.dragberry.ozo.common.levelresult.LevelResultName;
import org.dragberry.ozo.common.levelresult.NewLevelResultRequest;
import org.dragberry.ozo.common.levelresult.NewLevelResultsRequest;
import org.dragberry.ozo.game.GameController;
import org.dragberry.ozo.game.level.generator.Generator;
import org.dragberry.ozo.game.level.generator.RandomGenerator;
import org.dragberry.ozo.game.level.goal.AbstractGoal;
import org.dragberry.ozo.game.level.goal.Goal;
import org.dragberry.ozo.game.level.settings.LevelSettings;
import org.dragberry.ozo.game.objects.Unit;
import org.dragberry.ozo.game.util.CameraHelper;
import org.dragberry.ozo.game.util.Constants;
import org.dragberry.ozo.game.util.DigitUtil;
import org.dragberry.ozo.screen.popup.AbstractGameFinishedPopup;
import org.dragberry.ozo.screen.popup.AbstractPopup;
import org.dragberry.ozo.screen.popup.VictoryPopup;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * Created by maksim on 30.01.17.
 */

public abstract class Level<LS extends LevelSettings> implements Serializable {

    private final static String TAG = Level.class.getName();

    private final static int DEFAULT_WIDTH = 6;
    private final static int DEFAULT_HEIGHT = 8;

    public final transient Array<AbstractGoal> goalsToWin = new Array<AbstractGoal>();
    public final transient Array<AbstractGoal> goalsToLose = new Array<AbstractGoal>();

    public transient Array<String> rules;

    protected Map<String, Generator> generators = Collections.emptyMap();
    
    public transient LS settings;
    public int width;
    public int height;

    public Unit[][] units;

    public float time = 0;
    public int steps = 0;
    public int lostNumbers = 0;
    public boolean savedState = false;

	public transient boolean started = false;

    private enum State {
        FIXED, IN_MOTION
    }

    private transient State state;
    public transient Unit selectedUnit;
    protected transient Array<Unit> neighbors;

    private transient float motionTime ;

    private transient int negCount;
    private transient int negSum;
    private transient int posCount;
    private transient int posSum;
    private transient int zeroCount;

    public transient Array<TextureRegion> posCountDigits;
    public transient Array<TextureRegion> posSumDigits;
    public transient Array<TextureRegion> zeroCountDigits;
    public transient Array<TextureRegion> lostNumbersDigits;
    public transient Array<TextureRegion> negCountDigits;
    public transient Array<TextureRegion> negSumDigits;

    public Level() {
        initTransientFields();
    }

    /**
     * Call this method when level has been restored
     * @param settings
     */
    public void setSettings(LS settings) {
        this.settings = settings;
        addRules(settings);
        addGoals(settings);
    }

    public Level(LS settings) {
        this(settings, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public Level(LS settings, int width, int height) {
        this.width = width;
        this.height = height;
        this.settings = settings;
        addGoals(settings);
        addRules(settings);
        units = new Unit[width][height];
        initTransientFields();
        createGenerators();
    }

    private void initTransientFields() {
        neighbors = new Array<Unit>(4);

        posCountDigits = new Array<TextureRegion>(4);
        posSumDigits = new Array<TextureRegion>(4);
        zeroCountDigits = new Array<TextureRegion>(4);
        lostNumbersDigits = new Array<TextureRegion>(4);
        negCountDigits = new Array<TextureRegion>(4);
        negSumDigits = new Array<TextureRegion>(4);
    }

    public void reset(boolean restore) {
        if (!restore) {
            time = 0;
            steps = 0;
            lostNumbers = 0;
        }
        started = false;

        selectedUnit = null;
        neighbors.clear();
        state = State.FIXED;
        motionTime = 0;

        negCount = 0;
        negSum = 0;
        posCount = 0;
        posSum = 0;
        zeroCount = 0;

        for (Generator generator : generators.values()) {
            generator.reset();
        }

        resetUnits(restore);

        updateStateForUnit();
        resolveStateDigits();

        for (Goal goal : goalsToWin) {
            goal.reset(restore);
        }
        for (Goal goal : goalsToLose) {
            goal.reset(restore);
        }

    }
    
    protected void createGenerators() {
    	generators = Collections.emptyMap();
    }

    protected Unit generateUnit(int x, int y, Unit unit) {
    	Generator gen = null;
    	if (!generators.isEmpty()) {
    		gen = generators.get(Generator.getId(x, y));
    	}
    	if (gen == null) {
    		gen = getDefaultGenerator(x, y);
    	}
        if (unit == null) {
            unit = new Unit();
        }
        return unit.init(gen.next(steps,
                selectedUnit == null ? unit.x : selectedUnit.x,
                selectedUnit == null ? unit.y : selectedUnit.y), x, y);
    }

    protected Generator getDefaultGenerator(int x, int y) {
    	 return RandomGenerator.DEFAUTL.adjust(getTotal());
    }

    private int getTotal() {
        int total = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (units[x][y] != null) {
                    total += units[x][y].getValue();
                }
            }
        }
        return total;
    }

    protected void addGoalToWin(AbstractGoal goalToWin) {
        this.goalsToWin.add(goalToWin);
    }

    protected void addGoalToLose(AbstractGoal goalToLose) {
        this.goalsToLose.add(goalToLose);
    }

    protected abstract void addGoals(LS settings);

    protected void addRules(LS settings) {
        rules = settings.rules;
    }

    public Class<? extends AbstractGameFinishedPopup> getGameFinishedPopup() {
        return VictoryPopup.class;
    }

    protected boolean isLost() {
        for (AbstractGoal goal : goalsToLose) {
            if (goal.isReached(units, selectedUnit, neighbors)) {
                return true;
            }
        }
        return false;
    }

    private boolean isWon() {
        boolean reached = true;
        for (AbstractGoal goal : goalsToWin) {
            if (!goal.isReached(units, selectedUnit, neighbors)) {
                reached = false;
            }
        }
        return reached;
    }

    public void renderGoals(SpriteBatch batch, float goalPositionX, float goalPositionY ) {
        float goalPosX = goalPositionX;
		for (AbstractGoal goal : goalsToWin) {
			goal.render(batch, goalPosX, goalPositionY);
            goalPosX += (goal.dimension.x);
		}
        goalPosX = CameraHelper.INSTANCE.cameraGui.viewportWidth - goalPositionX;
		for (AbstractGoal goal : goalsToLose) {
            goalPosX -= (goal.dimension.x);
			goal.render(batch, goalPosX, goalPositionY);
		}
    }

    public void update(float deltaTime) {
        if (started) {
            time += deltaTime;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    units[x][y].update(deltaTime);
                }
            }
        }
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

        for (AbstractGoal goal : goalsToWin) {
            goal.update(deltaTime);
        }
        for (AbstractGoal goal : goalsToLose) {
            goal.update(deltaTime);
        }
    }

    public NewLevelResultsRequest formNewResults() {
        NewLevelResultsRequest results = new NewLevelResultsRequest();
        results.getResults().put(LevelResultName.TIME,
                new NewLevelResultRequest<Integer>((int) time));
        results.getResults().put(LevelResultName.STEPS,
                new NewLevelResultRequest<Integer>(steps));
        results.getResults().put(LevelResultName.LOST_UNITS,
                new NewLevelResultRequest<Integer>(lostNumbers));
        Gdx.app.debug(TAG, "Form new results");
        return results;
    }

    private void resetUnits(boolean restore) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                units[x][y] = restore ? units[x][y].init() : generateUnit(x, y, units[x][y]);
            }
        }
    }

    @Override
    public String toString() {
        return settings == null ? "Unknown level" : "Level: " + settings.levelId;
    }

    public boolean inStepMotion() {
        return state == State.IN_MOTION;
    }

    public Unit selectUnit(float xCoord, float yCoord) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (units[x][y].bounds.contains(xCoord, yCoord)) {
                    return units[x][y];
                }
            }
        }
        return null;
    }

    public void deselectAllUnits() {
        selectedUnit = null;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                units[x][y].unselect();
                units[x][y].unselectNeighbor();
            }
        }
    }

    public boolean isUnitSelectedAgain(Unit currentSelectedUnit) {
        return selectedUnit == currentSelectedUnit;
    }

    public void startStepMotion() {
        selectedUnit.triggerSelectionEffect();
        for (Unit neighbor : neighbors) {
            neighbor.triggerSelectionEffect();
        }
        // step execution is started
        state = State.IN_MOTION;
    }

    public void processFirstSection(Unit currentSelectedUnit) {
        // unit first selection
        selectedUnit = currentSelectedUnit;
        selectedUnit.select();
        selectedUnit.triggerSelectionEffect();
        getNeighbors(selectedUnit);
        for (Unit neighbor : neighbors) {
            neighbor.selectNeighbor();
            neighbor.triggerSelectionEffect();
        }
    }

    private void getNeighbors(Unit unit) {
        neighbors.clear();
        if (unit.y != 0) {
            neighbors.add(units[unit.x][unit.y - 1]);
        }
        if (unit.x != width - 1) {
            neighbors.add(units[unit.x + 1][unit.y]);
        }
        if (unit.y != height - 1) {
            neighbors.add(units[unit.x][unit.y + 1]);
        }
        if (unit.x != 0) {
            neighbors.add(units[unit.x - 1][unit.y]);
        }
    }

    private void updateMotion(float deltaTime) {
        float step = deltaTime * Constants.UNIT_SPEED;
        shiftTopUnits(step);
        shiftRightUnits(step);
        shiftBottomUnits(step);
        shiftLeftUnits(step);
    }

    private void shiftTopUnits(float step) {
        for (int y = selectedUnit.y + 1; y < height; y++) {
            Unit unitToMove = units[selectedUnit.x][y];
            unitToMove.moveTo(Unit.Direction.SOUTH, step);
        }
    }

    private void shiftRightUnits(float step) {
        for (int x = selectedUnit.x + 1; x < width; x++) {
            Unit unitToMove = units[x][selectedUnit.y];
            unitToMove.moveTo(Unit.Direction.WEST, step);
        }
    }

    private void shiftBottomUnits(float step) {
        for (int y = selectedUnit.y - 1; y >= 0; y--) {
            Unit unitToMove = units[selectedUnit.x][y];
            unitToMove.moveTo(Unit.Direction.NORTH, step);
        }
    }

    private void shiftLeftUnits(float step) {
        for (int x = selectedUnit.x - 1; x >= 0; x--) {
            Unit unitToMove = units[x][selectedUnit.y];
            unitToMove.moveTo(Unit.Direction.EAST, step);
        }
    }

    private void finishStepExecution() {
        updateGeneratorsBeforeStepCalculation();

        // sum neighbors
        selectedUnit.previousValue = selectedUnit.getValue();
        int valueToAdd = 0;
        for (Unit neighbor : neighbors) {
            valueToAdd +=neighbor.getValue();
            neighbor.previousValue = neighbor.getValue();
        }
        selectedUnit.addValue(valueToAdd);

        updateGeneratorsAfterStepCalculation();

        // logical shift all units
        // fix and recalculate position
        shiftTopUnits(selectedUnit);
        shiftRightUnits(selectedUnit);
        shiftBottomUnits(selectedUnit);
        shiftLeftUnits(selectedUnit);

        updateGeneratorsAfterStep();

        refreshState();
        steps++;
        if (isGameFinished()) {
            settings.saveState(this, false);
            savedState = false;
            return;
        }
        selectedUnit.unselect();
        selectedUnit = null;
    }

    protected void updateGeneratorsAfterStep() {}

    protected void updateGeneratorsBeforeStepCalculation() {}

    protected void updateGeneratorsAfterStepCalculation() {}

    protected boolean isGameFinished() {
        if (isLost()) {
            GameController.instance.onGameLost(this);
            return true;
        }
        if (isWon()) {
            GameController.instance.onGameWon(this);
            return true;
        }
        return false;
    }

    private void shiftBottomUnits(Unit selectedUnit) {
        Unit neighbor = null;
        if (selectedUnit.y != 0) {
            neighbor = units[selectedUnit.x][selectedUnit.y - 1];
        }
        if (neighbor != null) {
            for (int y = selectedUnit.y - 1; y > 0; y--) {
                Unit unitToMove = units[selectedUnit.x][y - 1];
                units[selectedUnit.x][y] = unitToMove;
                unitToMove.moveTo(selectedUnit.x, y);
            }
            units[selectedUnit.x][0] = generateUnit(selectedUnit.x, 0, neighbor);
        }
    }

    private void shiftTopUnits(Unit selectedUnit) {
        Unit neighbor = null;
        if (selectedUnit.y != height - 1) {
            neighbor = units[selectedUnit.x][selectedUnit.y + 1];
        }
        if (neighbor != null) {
            for (int y = selectedUnit.y + 1; y < height - 1; y++) {
                Unit unitToMove = units[selectedUnit.x][y + 1];
                units[selectedUnit.x][y] = unitToMove;
                unitToMove.moveTo(selectedUnit.x, y);
            }
            units[selectedUnit.x][height - 1] = generateUnit(selectedUnit.x, height - 1, neighbor);
        }
    }

    private void shiftRightUnits(Unit selectedUnit) {
        Unit neighbor = null;
        if (selectedUnit.x != width - 1) {
            neighbor = units[selectedUnit.x + 1][selectedUnit.y];
        }
        if (neighbor != null) {
            for (int x = selectedUnit.x + 1; x < width - 1; x++) {
                Unit unitToMove = units[x + 1][selectedUnit.y];
                units[x][selectedUnit.y] = unitToMove;
                unitToMove.moveTo(x, selectedUnit.y);
            }
            units[width - 1][selectedUnit.y] = generateUnit(width - 1, selectedUnit.y, neighbor);
        }
    }

    private void shiftLeftUnits(Unit selectedUnit) {
        Unit neighbor = null;
        if (selectedUnit.x != 0) {
            neighbor = units[selectedUnit.x - 1][selectedUnit.y];
        }
        if (neighbor != null) {
            for (int x = selectedUnit.x - 1; x > 0; x--) {
                Unit unitToMove = units[x - 1][selectedUnit.y];
                units[x][selectedUnit.y] = unitToMove;
                unitToMove.moveTo(x, selectedUnit.y);
            }
            units[0][selectedUnit.y] = generateUnit(0, selectedUnit.y, neighbor);
        }
    }

    private void refreshState() {
        updateLostNumbers();
        negCount = 0;
        negSum = 0;
        posCount = 0;
        posSum = 0;
        zeroCount = 0;
        updateStateForUnit();
        resolveStateDigits();
    }

    private void updateLostNumbers() {
        int pos = 0;
        int neg = 0;
        if (selectedUnit.previousValue > 0) {
            pos += selectedUnit.previousValue;
        } else if (selectedUnit.previousValue < 0) {
            neg += -selectedUnit.previousValue;
        }
        int value;
        for (Unit unit : neighbors) {
            value = unit.previousValue;
            if (value > 0) {
                pos += value;
            } else if (value < 0) {
                neg += -value;
            }
        }
        if (neg != 0 && pos != 0) {
            lostNumbers += pos < neg ? pos : neg;
        }
    }

    private void updateStateForUnit() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int value = units[x][y].getValue();
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
        }
    }

    private void resolveStateDigits() {
        DigitUtil.resolveDigits(posCount, posCountDigits, false);
        DigitUtil.resolveDigits(posSum, posSumDigits);
        DigitUtil.resolveDigits(zeroCount, zeroCountDigits, false);
        DigitUtil.resolveDigits(lostNumbers, lostNumbersDigits, false);
        DigitUtil.resolveDigits(negCount, negCountDigits, false);
        DigitUtil.resolveDigits(negSum, negSumDigits);
    }
}
