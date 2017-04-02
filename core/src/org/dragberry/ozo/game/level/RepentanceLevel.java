package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.generator.ConstGenerator;
import org.dragberry.ozo.game.level.generator.Generator;
import org.dragberry.ozo.game.level.generator.SequenceOf2Generator;
import org.dragberry.ozo.game.level.generator.ZeroMinusOneGenerator;
import org.dragberry.ozo.game.level.settings.ReachTheGoalLevelSettings;
import org.dragberry.ozo.game.objects.Unit;

import java.util.HashMap;

public class RepentanceLevel extends ReachTheGoalLevel {

	private transient ZeroMinusOneGenerator.ThirdValueState thirdValueState;

	public RepentanceLevel() {}

	public RepentanceLevel(ReachTheGoalLevelSettings settings) {
		super(settings);
	}
	
	@Override
	protected void createGenerators() {
		thirdValueState = new SequenceOf2Generator.ThirdValueState();

		generators = new HashMap<String, Generator>((width - 2) * (height - 2));
		int index;
		Generator gen;
		for (index = 0; index < width; index++) {
			gen = new ZeroMinusOneGenerator(index % 2 == 0 ? 0 : -1, index, 0, thirdValueState);
			generators.put(gen.id, gen);
			gen = new ZeroMinusOneGenerator((index + (height - 1)) % 2 == 0 ? 0 : -1, index, height - 1, thirdValueState);
			generators.put(gen.id, gen);
		}
		for (index = 0; index < height; index++) {
			gen = new ZeroMinusOneGenerator(index % 2 == 0 ? 0 : -1, 0, index, thirdValueState);
			generators.put(gen.id, gen);
			gen = new ZeroMinusOneGenerator((index  + (width - 1)) % 2 == 0 ? 0 : -1, width - 1, index, thirdValueState);
			generators.put(gen.id, gen);
		}
	}
	
	@Override
	protected Generator getDefaultGenerator(int x, int y) {
		if (x == 1 && y == 2) {
			return  ConstGenerator.NEG_ONE;
		}
		if (x == 4 && y == 4) {
			return ConstGenerator.ZERO;
		}
		return x % 2 == y % 2 ? ConstGenerator.NEG_ONE : ConstGenerator.ZERO;
	}

	private boolean isCross() {
		if (neighbors.size < 4) {
			return false;
		}
		float previousSign = Math.signum(selectedUnit.getValue());
		for (Unit neighbor : neighbors) {
			float sign = Math.signum(neighbor.getValue());
			if (previousSign != sign) {
				return false;
			}
			previousSign = sign;
		}
		return true;
	}

	@Override
	protected void updateGeneratorsBeforeStepCalculation() {
		if (isCross()) {
			thirdValueState.updatePosition();
		}
	}

	@Override
	public void reset(boolean restore) {
		super.reset(restore);
		if (thirdValueState == null) {
			thirdValueState = new SequenceOf2Generator.ThirdValueState();
		}
		for (Generator generator : generators.values()) {
			if (generator instanceof SequenceOf2Generator) {
				((SequenceOf2Generator) generator).setThirdValueState(thirdValueState);
			}
		}
	}
}
