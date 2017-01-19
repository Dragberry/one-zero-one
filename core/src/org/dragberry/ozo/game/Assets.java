package org.dragberry.ozo.game;

import org.dragberry.ozo.game.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {

	private static final String TAG = Assets.class.getName();
	
	public static final Assets instance = new Assets();
	
	private AssetManager assetManager;
	
	public AssetUnit unit;
	
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
}
