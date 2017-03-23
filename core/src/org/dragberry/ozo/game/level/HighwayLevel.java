package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.generator.ConstGenerator;
import org.dragberry.ozo.game.level.generator.Generator;
import org.dragberry.ozo.game.level.generator.HighwayGenerator;
import org.dragberry.ozo.game.level.settings.ReachTheGoalLevelSettings;

import java.util.HashMap;

public class HighwayLevel extends ReachTheGoalLevel {

	private transient HighwayGenerator.PositiveState positiveState;

	public HighwayLevel() {}

	public HighwayLevel(ReachTheGoalLevelSettings settings) {
		super(settings);
	}
	
	@Override
	protected void createGenerators() {
	positiveState = new HighwayGenerator.PositiveState(width, height);

		generators = new HashMap<String, Generator>((width - 2) * (height - 2));
		int index;
		Generator gen;
		for (index = 0; index < width; index++) {
			gen = new HighwayGenerator(index % 2 == 0 ? 0 : -1, index, 0, positiveState);
			generators.put(gen.id, gen);
			gen = new HighwayGenerator((index + (height - 1)) % 2 == 0 ? 0 : -1, index, height - 1, positiveState);
			generators.put(gen.id, gen);
		}
		for (index = 0; index < height; index++) {
			gen = new HighwayGenerator(index % 2 == 0 ? 0 : -1, 0, index, positiveState);
			generators.put(gen.id, gen);
			gen = new HighwayGenerator((index  + (width - 1)) % 2 == 0 ? 0 : -1, width - 1, index, positiveState);
			generators.put(gen.id, gen);
		}
	}
	
	@Override
	protected Generator getDefaultGenerator(int x, int y) {
		return x % 2 == y % 2 ? ConstGenerator.NEG_ONE : ConstGenerator.ZERO;
	}

	private boolean isVertical() {
		float previousSign = Math.signum(units[selectedUnit.x][0].getValue());
		for (int y = 1; y < height; y++) {
			float sign = Math.signum(units[selectedUnit.x][y].getValue());
			if (previousSign != sign) {
				return false;
			}
			previousSign = sign;
		}
		return true;
	}

	private boolean isHorizontal() {
		float previousSign = Math.signum(units[0][selectedUnit.y].getValue());
		for (int x = 1; x < width; x++) {
			float sign = Math.signum(units[x][selectedUnit.y].getValue());
			if (previousSign != sign) {
				return false;
			}
			previousSign = sign;
		}
		return true;
	}


	@Override
	protected void updateGeneratorsStateBeforeStep() {
		positiveState.updateState(isVertical(), isHorizontal());
	}

	@Override
	protected void updateGeneratorsStateAfterStep() {
		positiveState.updateState(false, false);
	}

	@Override
	public void reset(boolean restore) {
		super.reset(restore);
		if (positiveState == null) {
			positiveState = new HighwayGenerator.PositiveState(width, height);
		}
		for (Generator generator : generators.values()) {
			if (generator instanceof HighwayGenerator) {
				((HighwayGenerator) generator).setPositiveState(positiveState);
			}
		}
	}
}
