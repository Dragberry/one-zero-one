package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.generator.ConstGenerator;
import org.dragberry.ozo.game.level.generator.Generator;
import org.dragberry.ozo.game.level.generator.SequenceOf2Generator;
import org.dragberry.ozo.game.level.generator.ZeroMinusOneGenerator;
import org.dragberry.ozo.game.level.settings.ReachTheGoalLevelSettings;

import java.util.HashMap;

public abstract class SequenceOf2Level extends ReachTheGoalLevel {

	protected int sequenceValue = -1;

	private transient SequenceOf2Generator.ThirdValueState thirdValueState;

	public SequenceOf2Level() {}

	public SequenceOf2Level(ReachTheGoalLevelSettings settings) {
		super(settings);
	}
	
	@Override
	protected void createGenerators() {
		thirdValueState = new SequenceOf2Generator.ThirdValueState();

		generators = new HashMap<String, Generator>((width - 2) * (height - 2));
		int index;
		Generator gen;
		for (index = 0; index < width; index++) {
			gen = new ZeroMinusOneGenerator(index % 2 == 0 ? 0 : 1, index, 0, thirdValueState);
			generators.put(gen.id, gen);
			gen = new ZeroMinusOneGenerator((index + (height - 1)) % 2 == 0 ? 0 : 1, index, height - 1, thirdValueState);
			generators.put(gen.id, gen);
		}
		for (index = 0; index < height; index++) {
			gen = new ZeroMinusOneGenerator(index % 2 == 0 ? 0 : 1, 0, index, thirdValueState);
			generators.put(gen.id, gen);
			gen = new ZeroMinusOneGenerator((index  + (width - 1)) % 2 == 0 ? 0 : 1, width - 1, index, thirdValueState);
			generators.put(gen.id, gen);
		}
	}
	
	@Override
	protected Generator getDefaultGenerator(int x, int y) {
		return x % 2 == y % 2 ? ConstGenerator.NEG_ONE : ConstGenerator.ZERO;
	}

	private boolean isStepResultMatchedSequnceValue() {
		return selectedUnit.getValue() == sequenceValue;
	}

	@Override
	protected void updateGeneratorsAfterStepCalculation() {
		if (isStepResultMatchedSequnceValue()) {
			thirdValueState.updatePosition(neighbors.size);
			updateSequence();
		}
	}

	protected abstract void updateSequence();

	@Override
	public void reset(boolean restore) {
		super.reset(restore);
		if (thirdValueState == null) {
			thirdValueState = new SequenceOf2Generator.ThirdValueState();
		}
		for (Generator generator : generators.values()) {
			if (generator instanceof ZeroMinusOneGenerator) {
				((ZeroMinusOneGenerator) generator).setThirdValueState(thirdValueState);
			}
		}
		if (!restore) {
			sequenceValue = -1;
		}
	}
}
