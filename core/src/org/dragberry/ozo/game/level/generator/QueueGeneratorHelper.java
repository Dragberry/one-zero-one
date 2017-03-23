package org.dragberry.ozo.game.level.generator;

import java.util.HashMap;
import java.util.Map;

public class QueueGeneratorHelper {

	public static Map<String, Generator> createGenerators(int width, int height) {
		Map<String, Generator> generators = new HashMap<String, Generator>((width - 1) * (height - 1));
		int index;
		Generator gen;
		for (index = 0; index < width; index++) {
			
			gen = new QueueGenerator(getInitialValue(index, 0), index, 0);
			generators.put(gen.id, gen);
			gen = new QueueGenerator(getInitialValue(index, height -1), index, height - 1);
			generators.put(gen.id, gen);
		}
		for (index = 0; index < height; index++) {
			gen = new QueueGenerator(getInitialValue(0, index), 0, index);
			generators.put(gen.id, gen);
			gen = new QueueGenerator(getInitialValue(width - 1, index), width - 1, index);
			generators.put(gen.id, gen);
		}
		return generators;
	}
	
	public static Generator getDefaultGenerator(int x, int y) {
		switch ((x + y) % 3) {
		case 0:
			return ConstGenerator.NEG_ONE;
		case 1:
			return ConstGenerator.ZERO;
		case 2:
			return ConstGenerator.POS_ONE;
		default:
			throw new IllegalArgumentException();
		}
	}
	
	private static int getInitialValue(int x, int y) {
		switch ((x + y) % 3) {
		case 0:
			return -1;
		case 1:
			return 0;
		case 2:
			return 1;
		default:
			throw new IllegalArgumentException();
		}
	}
}
