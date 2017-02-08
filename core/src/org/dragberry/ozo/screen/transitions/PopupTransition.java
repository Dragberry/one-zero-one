package org.dragberry.ozo.screen.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;

public class PopupTransition implements ScreenTransition {

	 private static final float DEFAULT_TIME = 0.25f;
	    private static final PopupTransition INSTANCE = new PopupTransition();

	    private float duration;
	    private ShaderProgram shader;

	    public static PopupTransition init(float duration, ShaderProgram shader) {
	        INSTANCE.duration = duration;
	        INSTANCE.shader = shader;
	        return INSTANCE;
	    }

	    public static PopupTransition init(ShaderProgram shader) {
	        return init(DEFAULT_TIME, shader);
	    }

	    @Override
	    public float getDuration() {
	        return duration;
	    }

	    @Override
	    public void render(SpriteBatch batch, Texture currScreen, Texture nextScreen, float progress) {
	        float width = currScreen.getWidth();
	        float height = currScreen.getHeight();
	        progress = Interpolation.fade.apply(progress);

	        Gdx.gl.glClearColor(0, 0, 0, 0);
	        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	        batch.begin();
	        batch.setShader(shader);
	        shader.setUniformf("progress", progress);
	        batch.setColor(1, 1, 1, 1);
	        batch.draw(currScreen, 0, 0, 0, 0, width, height, 1, 1, 0, 0, 0,
	                currScreen.getWidth(), currScreen.getHeight(),
	                false, true);
	        batch.setShader(null);
	        batch.setColor(1, 1, 1, progress);
	        batch.draw(nextScreen, 0, 0, 0, 0, width, height, 1, 1, 0, 0, 0,
	                nextScreen.getWidth(), nextScreen.getHeight(),
	                false, true);
	        batch.end();
	    }

}
