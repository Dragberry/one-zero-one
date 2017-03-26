package org.dragberry.ozo.game.level;

import org.dragberry.ozo.game.level.generator.ConstGenerator;
import org.dragberry.ozo.game.level.generator.FibonacciGenerator;
import org.dragberry.ozo.game.level.generator.Generator;
import org.dragberry.ozo.game.level.settings.ReachTheGoalLevelSettings;

import java.util.HashMap;

public class FibonacciLevel extends ReachTheGoalLevel {

	private int previousNumber = 0;
	private int currentNumber = -1;

	private FibonacciGenerator.ThirdValueState thirdValueState;

	public FibonacciLevel() {}

	public FibonacciLevel(ReachTheGoalLevelSettings settings) {
		super(settings);
	}
	
	@Override
	protected void createGenerators() {
		thirdValueState = new FibonacciGenerator.ThirdValueState();

		generators = new HashMap<String, Generator>((width - 2) * (height - 2));
		int index;
		Generator gen;
		for (index = 0; index < width; index++) {
			gen = new FibonacciGenerator(index % 2 == 0 ? 0 : 1, index, 0, thirdValueState);
			generators.put(gen.id, gen);
			gen = new FibonacciGenerator((index + (height - 1)) % 2 == 0 ? 0 : 1, index, height - 1, thirdValueState);
			generators.put(gen.id, gen);
		}
		for (index = 0; index < height; index++) {
			gen = new FibonacciGenerator(index % 2 == 0 ? 0 : 1, 0, index, thirdValueState);
			generators.put(gen.id, gen);
			gen = new FibonacciGenerator((index  + (width - 1)) % 2 == 0 ? 0 : 1, width - 1, index, thirdValueState);
			generators.put(gen.id, gen);
		}
	}
	
	@Override
	protected Generator getDefaultGenerator(int x, int y) {
		return x % 2 == y % 2 ? ConstGenerator.NEG_ONE : ConstGenerator.ZERO;
	}

	private boolean isStepResultMatchedToFibonacci() {
		return selectedUnit.getValue() == currentNumber;
	}

	@Override
	protected void updateGeneratorsAfterStepCalculation() {
		if (isStepResultMatchedToFibonacci()) {
			thirdValueState.updatePosition();
			updateFibonacciSeq();
		}
	}

	private void updateFibonacciSeq() {
		int prev = previousNumber;
		previousNumber = currentNumber;
		currentNumber += prev;
	}

	@Override
	public void reset(boolean restore) {
		super.reset(restore);
		if (thirdValueState == null) {
			thirdValueState = new FibonacciGenerator.ThirdValueState();
		}
		for (Generator generator : generators.values()) {
			if (generator instanceof FibonacciGenerator) {
				((FibonacciGenerator) generator).setThirdValueState(thirdValueState);
			}
		}
		if (!restore) {
			previousNumber = 0;
			currentNumber = -1;
		}
	}
}
