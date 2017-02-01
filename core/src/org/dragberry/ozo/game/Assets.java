package org.dragberry.ozo.game;

import org.dragberry.ozo.game.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {

	private static final String TAG = Assets.class.getName();
	
	public static final Assets instance = new Assets();
	
	private AssetManager assetManager;
	
	public AssetUnit unit;
	public AssetFonts fonts;
	
	private Assets() {}
	
	public void init(AssetManager assetManager) {
		this.assetManager = assetManager;
		assetManager.setErrorListener(this);
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		assetManager.finishLoading();
		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
		for (String asset : assetManager.getAssetNames()) {
			Gdx.app.debug(TAG, "asset: " + asset);
		}
		
		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
		
		for (Texture txt : atlas.getTextures()) {
			txt.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		
		unit = new AssetUnit(atlas);
		fonts = new AssetFonts();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", throwable);
	}

	@Override
	public void dispose() {
		assetManager.dispose();
		fonts.dispose();
	}
	
	public class AssetUnit {
		public final AtlasRegion redBall;
		public final AtlasRegion greenBall;
		public final AtlasRegion blueBall;
		
		public AssetUnit(TextureAtlas atlas) {
			redBall = atlas.findRegion("red_ball");
			greenBall = atlas.findRegion("green_ball");
			blueBall = atlas.findRegion("blue_ball");
		}
	}
	
	public class AssetFonts implements Disposable {
		public BitmapFont gui_24;
		public BitmapFont gui_28;
		public BitmapFont gui_36;
		public BitmapFont game_58;
		public BitmapFont game_68;
		
		public AssetFonts() {
			float factor = Gdx.graphics.getWidth() /  Constants.VIEWPORT_WIDTH;
			float factorGui = Gdx.graphics.getWidth() /  Constants.VIEWPORT_GUI_WIDTH;
			gui_24 = createFont((int)(24 * factorGui), true);
			gui_28 = createFont((int)(28 * factorGui), true);
			gui_36 = createFont((int)(36 * factorGui), true);
			game_58 = createFont((int)(58 * factor), false);
			game_68 = createFont((int)(68 * factor), false);
		}
		
		@Override
		public void dispose() {
			gui_24.dispose();
			gui_28.dispose();
			gui_36.dispose();
			game_58.dispose();
			game_68.dispose();
		}
	}
	
	private static BitmapFont createFont(int size, boolean flip) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("types/hemi_head_bd_ it.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.color = Color.BLACK;
		parameter.size = size;
		parameter.flip = flip;
		parameter.magFilter = TextureFilter.Linear;
		parameter.minFilter = TextureFilter.Linear;
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();
		return font;
	}
}
