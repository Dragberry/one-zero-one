package org.dragberry.ozo.game;

import org.dragberry.ozo.game.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Locale;

public class Assets implements Disposable, AssetErrorListener {

	private static final String TAG = Assets.class.getName();
	
	private static final String FONT_CHARS;
	static {
		StringBuilder sb = new StringBuilder();
		for( int i = 32; i < 127; i++ ) {
			sb.append((char)i);
		}
		for( int i = 1024; i < 1104; i++ ) {
			sb.append((char)i); 
		}
		FONT_CHARS = sb.toString();
	}
	
	public static final Assets instance = new Assets();
	
	private AssetManager assetManager;
	
	public AssetUnit unit;
	public AssetDigits digits;
	public AssetFonts fonts;
	public I18NBundle translation;
	public AssetSkin skin;
	
	private Assets() {}
	
	public void init(AssetManager assetManager) {
		this.assetManager = assetManager;
		assetManager.setErrorListener(this);
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		assetManager.load(Constants.TRANSLATION, I18NBundle.class);
		assetManager.load(Constants.TEXTURE_ATLAS_SKIN, TextureAtlas.class);
		assetManager.finishLoading();
		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
		for (String asset : assetManager.getAssetNames()) {
			Gdx.app.debug(TAG, "asset: " + asset);
		}
		
		TextureAtlas atlas = getTextureAtlas(Constants.TEXTURE_ATLAS_OBJECTS);
		unit = new AssetUnit(atlas);
		digits = new AssetDigits(atlas);
		fonts = AssetFonts.create(Gdx.graphics.getWidth());
		skin = new AssetSkin(assetManager, getTextureAtlas(Constants.TEXTURE_ATLAS_SKIN));
		translation = assetManager.get(Constants.TRANSLATION, I18NBundle.class);
	}
	
	private TextureAtlas getTextureAtlas(String id) {
		TextureAtlas atlas = assetManager.get(id);
		for (Texture txt : atlas.getTextures()) {
			txt.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		return atlas;
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
		public final AtlasRegion ballRed;
		public final AtlasRegion ballBlue;
		public final AtlasRegion ballGreen;

		public final AtlasRegion infoBall;
		
		public AssetUnit(TextureAtlas atlas) {
			ballRed = atlas.findRegion("ball_red");
			ballBlue = atlas.findRegion("ball_blue");
			ballGreen = atlas.findRegion("ball_green");
			infoBall = atlas.findRegion("info_ball");
		}
	}
	
	public class AssetDigits {
		public final AtlasRegion[] digits = new AtlasRegion[10];
		public final AtlasRegion plus;
		public final AtlasRegion minus;
		
		public AssetDigits(TextureAtlas atlas) {
			for (int i = 0; i < 10; i++) {
				digits[i] = atlas.findRegion("n" + i);
			}
			plus = atlas.findRegion("n+");
			minus = atlas.findRegion("n-");
		}
	}

	public static class AssetFonts implements Disposable {

		public final BitmapFont gui_xs;
		public final BitmapFont gui_s;
		public final BitmapFont gui_m;
		public final BitmapFont gui_l;
		public final BitmapFont menu_m;
		public final BitmapFont menu_l;

		private AssetFonts(int guiXS, int guiS, int guiM, int guiL, int menuM, int menuL) {
			gui_xs = createFont(guiXS, true, Color.BLACK);
			gui_s = createFont(guiS, true, Color.BLACK);
			gui_m = createFont(guiM, true, Color.BLACK);
			gui_l = createFont(guiL, true, Color.BLACK);
			menu_m = createFont(menuM, false, Color.WHITE);
			menu_l = createFont(menuL, false, Color.WHITE);
		}

		public static AssetFonts create(int screenWidth) {
			switch (screenWidth) {
				case 720:
					return new AssetFonts(24, 28, 36, 40, 30, 36);
				case 480:
					return new AssetFonts(16, 17, 26, 28, 28, 34);
				case 1080:
				case 1200:
				case 1440:
				case 1600:
					return new AssetFonts(17, 18, 25, 27, 50, 54);
				default:
					float factorGui = screenWidth /  Constants.VIEWPORT_GUI_WIDTH;
					return new AssetFonts(
							(int)(24 * factorGui),
							(int)(28 * factorGui),
							(int)(36 * factorGui),
							(int)(40 * factorGui),
							(int)(40 * factorGui),
							(int)(46 * factorGui));
			}
		}

		@Override
		public void dispose() {
			gui_xs.dispose();
			gui_s.dispose();
			gui_m.dispose();
			gui_l.dispose();
			menu_m.dispose();
			menu_l.dispose();
		}
	}

	private static BitmapFont createFont(int size, boolean flip, Color color) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(Constants.FONTS));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.characters = FONT_CHARS;
		parameter.color = color;
		parameter.size = size;
		parameter.flip = flip;
		parameter.magFilter = TextureFilter.Linear;
		parameter.minFilter = TextureFilter.Linear;
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();
		return font;
	}
	
	public class AssetSkin {
		public final Skin skin;
		public final TextureRegion logo;
		
		public AssetSkin(AssetManager manager, TextureAtlas atlas) {
			logo = atlas.findRegion("logo");
			ObjectMap<String, Object> resources = new ObjectMap<String, Object>();
			resources.put("menu-font-medium", fonts.menu_m);
			resources.put("menu-font-large", fonts.menu_l);
			manager.load(Constants.SKIN, Skin.class, new SkinLoader.SkinParameter(resources));
			manager.finishLoading();
			skin = manager.get(Constants.SKIN);
		}
	}
}
