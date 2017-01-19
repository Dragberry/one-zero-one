package org.dragberry.ozo.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameArea extends AbstractGameObject {
	
	public static class LevelSettings {
		public static final LevelSettings DEFAULT = new LevelSettings();
		public int width = 8;
		public int height = 6;
	}
	
	private final LevelSettings settings;
	
	private Unit[][] units;
	
	public GameArea(LevelSettings settings) {
		this.settings = settings;
		init();
	}
	
	protected void init() {
		units = new Unit[settings.height][settings.width];
		for (int row = 0; row < settings.height; row++) {
			for (int column = 0; column < settings.width; column++) {
				units[row][column] = new Unit(column, row);
			}
		}
	}
	
	public void render(SpriteBatch batch) {
		for (int row = 0; row < settings.height; row++) {
			for (int column = 0; column < settings.width; column++) {
				units[row][column].render(batch);
			}
		}
	}

}
