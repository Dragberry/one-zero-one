package org.dragberry.ozo.game;

import org.dragberry.ozo.game.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
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
	}
	
	public class AssetUnit {
		public final AtlasRegion ball;
		
		public AssetUnit(TextureAtlas atlas) {
			ball = atlas.findRegion("ball");
		}
	}
	
	public class AssetFonts implements Disposable {
		public BitmapFont normal;
		
		public AssetFonts() {
			FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("types/hemi_head_bd_ it.ttf"));
			FreeTypeFontParameter parameter = new FreeTypeFontParameter();
			parameter.size = 29;
			parameter.characters = "0123456789+-";
			normal = generator.generateFont(parameter);
			generator.dispose();
			
			normal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
		}

		@Override
		public void dispose() {
			normal.dispose();
		}
		
	}
}
