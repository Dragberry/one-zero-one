package org.dragberry.ozo.screen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import java.lang.reflect.Constructor;

import org.dragberry.ozo.screen.transitions.ScreenTransition;
import org.dragberry.ozo.screen.transitions.ScreenTransitionFade;

/**
 * Created by maksim on 29.01.17.
 */

public abstract class DirectedGame implements ApplicationListener {

    private boolean init;
    private AbstractGameScreen currScreen;
    private AbstractGameScreen nextScreen;
    private FrameBuffer currFbo;
    private FrameBuffer nextFbo;
    private SpriteBatch batch;
    private float time;
    private ScreenTransition screenTransition;
    
    private Class<? extends AbstractGameScreen> callerScreen;
    
    public void setScreen(AbstractGameScreen screen) {
        setScreen(screen, null, null);
    }
    
    public void setScreen(AbstractGameScreen screen, Class<? extends AbstractGameScreen> callerScreen) {
        setScreen(screen, null, callerScreen);
    }

    public void setScreen(AbstractGameScreen screen, ScreenTransition screenTransition, Class<? extends AbstractGameScreen> callerScreen) {
        this.callerScreen = callerScreen;
    	int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        if (!init) {
            currFbo = new FrameBuffer(Pixmap.Format.RGB888, width, height, false);
            nextFbo = new FrameBuffer(Pixmap.Format.RGB888, width, height, false);
            batch = new SpriteBatch();
            init = true;
        }
        // start new transition
        nextScreen = screen;
        nextScreen.show(); // activate next screen
        nextScreen.resize(width, height);
        nextScreen.render(0); // let screen update() once
        if (currScreen != null) {
            currScreen.pause();
        }
        nextScreen.pause();
        Gdx.input.setInputProcessor(null); // disable input
        this.screenTransition = screenTransition;
        time = 0;
    }
    
    public void back() {
    	try {
	    	if (callerScreen != null) {
	    		Constructor<? extends AbstractGameScreen> constructor = callerScreen.getConstructor(DirectedGame.class);
	    		constructor.newInstance(this);
	    		setScreen(constructor.newInstance(this), ScreenTransitionFade.init(), null);
	    		Gdx.app.debug(getClass().getName(), "Navigate to " + callerScreen);
	    	} else {
	    		setScreen(new MainMenuScreen(this), ScreenTransitionFade.init(), null);
	    		Gdx.app.debug(getClass().getName(), "Navigate to " + MainMenuScreen.class);
	    	}
    	} catch (Exception exc) {
    		Gdx.app.error(getClass().getName(), "An error has occured during navigation! Application is terminated!", exc);
    		Gdx.app.exit();
    	}
    }

    @Override
    public void render() {
        float deltaTime = Math.min(Gdx.graphics.getDeltaTime(), 1.0f / 60.0f);
        if (nextScreen == null) {
            // no ongoing transition
            if (currScreen != null) {
                currScreen.render(deltaTime);
            }
        } else {
            //ongoing transition
            float duration = 0;
            if (screenTransition != null) {
                duration = screenTransition.getDuration();
            }
            // update progress of ongoing transition
            time = Math.min(time + deltaTime, duration);
            if (screenTransition == null || time >= duration) {
                // no transition effect set or transition has just finished
                if (currScreen != null) {
                    currScreen.hide();
                }
                nextScreen.resume();
                // enable input  for next screen
                Gdx.input.setInputProcessor(nextScreen.getInputProcessor());
                // switch screens
                currScreen = nextScreen;
                nextScreen = null;
                screenTransition = null;
            } else {
                // render screens to FBOs
                currFbo.begin();
                if (currScreen != null) {
                    currScreen.render(deltaTime);
                }
                currFbo.end();
                nextFbo.begin();
                nextScreen.render(deltaTime);
                nextFbo.end();
                // render transition effect to screen
                float alpha = time / duration;
                screenTransition.render(batch, currFbo.getColorBufferTexture(), nextFbo.getColorBufferTexture(), alpha);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        if (currScreen != null) {
            currScreen.resize(width, height);
        }
        if (nextScreen != null) {
            nextScreen.resize(width, height);
        }
    }

    @Override
    public void pause() {
        if (currScreen != null) {
            currScreen.pause();
        }
    }

    @Override
    public void resume() {
        if (currScreen != null) {
            currScreen.resume();
        }
    }

    @Override
    public void dispose() {
        if (currScreen != null) {
            currScreen.hide();
        }
        if (nextScreen != null) {
            nextScreen.hide();
        }
        if (init) {
            currFbo.dispose();
            currScreen = null;
            nextFbo.dispose();
            nextScreen = null;
            batch.dispose();
            init = false;
        }
    }
}
