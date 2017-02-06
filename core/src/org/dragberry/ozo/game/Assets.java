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
		fonts = AssetFonts.create(Gdx.graphics.getWidth());
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
		public final AtlasRegion ball;
		public final AtlasRegion infoBall;
		
		public AssetUnit(TextureAtlas atlas) {
			ball = atlas.findRegion("ball");
			infoBall = atlas.findRegion("ball_info");
		}
	}

	public static class AssetFonts implements Disposable {

		public final BitmapFont gui_xs;
		public final BitmapFont gui_s;
		public final BitmapFont gui_m;
		public final BitmapFont gui_l;
		public final BitmapFont menu_m;
		public final BitmapFont game_m;
		public final BitmapFont game_l;

		private AssetFonts(int guiXS, int guiS, int guiM, int guiL, int menuM, int gameM, int gameL) {
			gui_xs = createFont(guiXS, true);
			gui_s = createFont(guiS, true);
			gui_m = createFont(guiM, true);
			gui_l = createFont(guiL, true);
			menu_m = createFont(menuM, false);
			game_m = createFont(gameM, false);
			game_l = createFont(gameL, false);
		}

		public static AssetFonts create(int screenWidth) {
			switch (screenWidth) {
				case 720:
					return new AssetFonts(24, 28, 36, 40, 30, 28, 34);
				case 480:
					return new AssetFonts(16, 17, 28, 30, 20, 32, 38);
				case 1080:
				case 1440:
					return new AssetFonts(17, 18, 25, 27, 44, 26, 30);
				default:
					float factor = screenWidth /  Constants.VIEWPORT_WIDTH;
					float factorGui = screenWidth /  Constants.VIEWPORT_GUI_WIDTH;
					return new AssetFonts(
							(int)(24 * factorGui),
							(int)(28 * factorGui),
							(int)(36 * factorGui),
							(int)(40 * factorGui),
							(int)(30 * factor),
							(int)(58 * factor),
							(int)(68 * factor));
			}
		}

		@Override
		public void dispose() {
			gui_xs.dispose();
			gui_s.dispose();
			gui_m.dispose();
			gui_l.dispose();
			game_m.dispose();
			game_l.dispose();
		}
	}

	private static BitmapFont createFont(int size, boolean flip) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("types/UbuntuMono-BI.ttf"));
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
