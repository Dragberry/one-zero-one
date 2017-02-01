package org.dragberry.ozo.screen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.ArrayMap;

import java.lang.reflect.Constructor;

import org.dragberry.ozo.game.level.ChessboardLevel;
import org.dragberry.ozo.game.level.Level;
import org.dragberry.ozo.game.level.MashroomRainLevel;
import org.dragberry.ozo.game.level.NoAnnihilationLevel;
import org.dragberry.ozo.game.level.ReachMultiGoalLevel;
import org.dragberry.ozo.game.level.ReachTheGoalLevel;
import org.dragberry.ozo.game.level.goal.JustReachGoal;
import org.dragberry.ozo.screen.transitions.ScreenTransition;
import org.dragberry.ozo.screen.transitions.ScreenTransitionFade;

/**
 * Created by maksim on 29.01.17.
 */

public abstract class DirectedGame implements ApplicationListener {

    public static final ArrayMap<String, LevelInfo> LEVELS = new ArrayMap<String, LevelInfo>(true, 1);
    static {
        LevelInfo li = null;
        li = new LevelInfo(ReachTheGoalLevel.class, "Let's start!", -10, 2, JustReachGoal.Operator.MORE);
        LEVELS.put(li.getName(), li);
        li = new LevelInfo(ReachTheGoalLevel.class, "A little bit harder", -5, 10);
        LEVELS.put(li.getName(), li);
        li = new LevelInfo(ReachTheGoalLevel.class, "We need more!", -10, 33);
        LEVELS.put(li.getName(), li);
        li = new LevelInfo(ReachMultiGoalLevel.class, "Double 5", -10, new Integer[] { 5, 5 });
        LEVELS.put(li.getName(), li);
        li = new LevelInfo(ReachMultiGoalLevel.class, "Roulette", -10, new Integer[] { 7, 7, 7 });
        LEVELS.put(li.getName(), li);
        li = new LevelInfo(NoAnnihilationLevel.class, "Save Us", 5, 10);
        LEVELS.put(li.getName(), li);
        li = new LevelInfo(MashroomRainLevel.class, "The Mashroom Rain", -10, 25);
        LEVELS.put(li.getName(), li);
        li = new LevelInfo(ChessboardLevel.class, "The Chessboard", -10, 25);
        LEVELS.put(li.getName(), li);
    }

    private LevelInfo currentLevelInfo;

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
    
    public void setScreen(AbstractGameScreen screen, ScreenTransition screenTransition) {
        setScreen(screen, screenTransition, null);
    }
    
    public void setScreen(AbstractGameScreen screen, Class<? extends AbstractGameScreen> callerScreen) {
        setScreen(screen, null, callerScreen);
    }

    public void setScreen(AbstractGameScreen screen, ScreenTransition screenTransition, Class<? extends AbstractGameScreen> callerScreen) {
        if (callerScreen != null) {
        	this.callerScreen = callerScreen;
        }
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
                callerScreen = null;
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

    public void setCurrentLevelInfo(LevelInfo currentLevelInfo) {
    	this.currentLevelInfo = currentLevelInfo;
    }
    
    public void playNextLevel() {
    	int currLevelIndex = LEVELS.indexOfKey(currentLevelInfo.getName());
    	if (currLevelIndex < LEVELS.size - 1) {
    		setCurrentLevelInfo(LEVELS.getValueAt(currLevelIndex + 1));
    		playLevel();
    	} else {
    		back();
    	}
    }
    
    public void playLevel() {
    	if (currentLevelInfo != null) {
    		playLevel(currentLevelInfo, null);
    	}
    }
    
    public void playLevel(LevelInfo currentLevelInfo, Class<? extends AbstractGameScreen> callerClass) {
        this.currentLevelInfo = currentLevelInfo;
        Class<?>[] paramClasses = new Class<?>[currentLevelInfo.params.length];
        for (int i = 0; i < currentLevelInfo.params.length; i++) {
            paramClasses[i] = currentLevelInfo.params[i].getClass();
        }
        try {
            Constructor<? extends Level> constructor = currentLevelInfo.clazz.getConstructor(paramClasses);
            Level level = constructor.newInstance(currentLevelInfo.params);
            setScreen(new GameScreen(this, level), ScreenTransitionFade.init(), callerClass);
        } catch (Exception exc) {
            Gdx.app.error(getClass().getName(), "An exception has occured during level creation", exc);
        }
    }

}