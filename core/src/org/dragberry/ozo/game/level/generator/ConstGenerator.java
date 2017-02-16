package org.dragberry.ozo.game.level.generator;

public class ConstGenerator extends Generator {
	
	public static final ConstGenerator NEG_ONE = new ConstGenerator(-1, -1, -1);
	public static final ConstGenerator ZERO = new ConstGenerator(0, -1, -1);
	public static final ConstGenerator POS_ONE = new ConstGenerator(1, -1, -1);
	
	public ConstGenerator(int initialValue, int x, int y) {
		super(x, y);
		this.value = initialValue;
	}

	private final int value;

	@Override
	public int next(int step, int selectedX, int selectedY) {
		return value;
	}

}
