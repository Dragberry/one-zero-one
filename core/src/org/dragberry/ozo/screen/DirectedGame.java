package org.dragberry.ozo.screen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;

import java.lang.reflect.Constructor;

import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.level.ChessboardLevel;
import org.dragberry.ozo.game.level.Level;
import org.dragberry.ozo.game.level.MashroomRainLevel;
import org.dragberry.ozo.game.level.NoAnnihilationQueueLevel;
import org.dragberry.ozo.game.level.QueueLevel;
import org.dragberry.ozo.game.level.WavesLevel;
import org.dragberry.ozo.game.level.goal.JustReachGoal;
import org.dragberry.ozo.game.level.settings.LevelSettings;
import org.dragberry.ozo.game.level.settings.NoAnnihilationLevelSettings;
import org.dragberry.ozo.game.level.settings.ReachMultiGoalLevelSettings;
import org.dragberry.ozo.game.level.settings.ReachTheGoalLevelSettings;
import org.dragberry.ozo.screen.popup.AbstractPopup;
import org.dragberry.ozo.screen.transitions.PopupTransition;
import org.dragberry.ozo.screen.transitions.ScreenTransition;
import org.dragberry.ozo.screen.transitions.ScreenTransitionFade;

/**
 * Created by maksim on 29.01.17.
 */

public abstract class DirectedGame implements ApplicationListener {

	public final Array<LevelSettings> levels = new Array<LevelSettings>();
	
    private LevelSettings currentLevelSettings;

    private boolean init;
    private AbstractGameScreen currScreen;
    private AbstractGameScreen nextScreen;
    private AbstractPopup popup;
    private FrameBuffer currFbo;
    private FrameBuffer nextFbo;
    private FrameBuffer popupFbo;
    private SpriteBatch batch;
    private float time;
    private float timePopup;
    private ScreenTransition screenTransition;
    private ScreenTransition popupTransition;
    private PopupState popupState;
    
    private ActionExecutor onPopupClose;
    
    private enum PopupState {
    	SHOWN, SHOWING, HIDING, HIDDEN
    }
    
    private ShaderProgram blackoutShader;
    
    private Class<? extends AbstractGameScreen> callerScreen;
    
    @Override
    public void create() {
    	Assets.instance.init(new AssetManager());
		levels.add(new ReachTheGoalLevelSettings("ozo.lvl.test", -10, 2, JustReachGoal.Operator.MORE));
		levels.add(new ReachTheGoalLevelSettings("ozo.lvl.letsStart", -10, 10, JustReachGoal.Operator.MORE));
		levels.add(new ReachTheGoalLevelSettings("ozo.lvl.littleBitHarder", -5, 25));
		levels.add(new ReachTheGoalLevelSettings("ozo.lvl.needMore", -7, 49));
		levels.add(new ReachMultiGoalLevelSettings("ozo.lvl.double5", -10, 5, 5));
		levels.add(new NoAnnihilationLevelSettings("ozo.lvl.saveUs", 5, 25));
		levels.add(new ReachMultiGoalLevelSettings("ozo.lvl.roulette", -7, 7, 7, 7));
		levels.add(new ReachTheGoalLevelSettings(MashroomRainLevel.class, "ozo.lvl.mushroomRain", -10, 25));
		levels.add(new ReachTheGoalLevelSettings(QueueLevel.class, "ozo.lvl.queues", -10, 25));
		levels.add(new ReachTheGoalLevelSettings(ChessboardLevel.class, "ozo.lvl.chessboard", -10, 25));
		levels.add(new ReachTheGoalLevelSettings(MashroomRainLevel.class, "ozo.lvl.mushroomShower", -25, 75));
		levels.add(new ReachTheGoalLevelSettings(WavesLevel.class, "ozo.lvl.waves", -15, 50));
		levels.add(new ReachMultiGoalLevelSettings("ozo.lvl.casinoRoyale", -99, 99, 99, 99));
		levels.add(new ReachTheGoalLevelSettings(QueueLevel.class, "ozo.lvl.regularity", -33, 99));
		levels.add(new NoAnnihilationLevelSettings("ozo.lvl.unsafePlace", 49, 99));
		levels.add(new NoAnnihilationLevelSettings(NoAnnihilationQueueLevel.class, "ozo.lvl.unsafeRegularity", 4, 50));


		popupState = PopupState.HIDDEN;
		blackoutShader = new ShaderProgram(
     		Gdx.files.internal("shaders/blackout.vert"), 
     		Gdx.files.internal("shaders/blackout.frag"));
		this.popupTransition = PopupTransition.init(blackoutShader);
    }
    
    public void setScreen(AbstractGameScreen screen) {
        setScreen(screen, null, null);
    }

    protected void initialise(int width, int height) {
		if (!init) {
	        currFbo = new FrameBuffer(Pixmap.Format.RGB888, width, height, false);
	        nextFbo = new FrameBuffer(Pixmap.Format.RGB888, width, height, false);
	        popupFbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
	        batch = new SpriteBatch();
	        init = true;
        }
	}
    
    public void setScreen(AbstractGameScreen screen, ScreenTransition screenTransition, Class<? extends AbstractGameScreen> callerScreen) {
        if (callerScreen != null) {
        	this.callerScreen = callerScreen;
        }
        initialise(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        nextScreen = screen;
        this.screenTransition = screenTransition;
        if (popup != null) {
        	setPopup(null);
        } else {
	        showNextScreen();
	        if (currScreen != null) {
	        	currScreen.render(Gdx.graphics.getDeltaTime());
	        }
        }
    }

	protected void showNextScreen() {
		if (nextScreen == null) {
			return;
		}
		int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
		nextScreen.show(); // activate next screen
		nextScreen.resize(width, height);
		nextScreen.render(0); // let screen update() once
		if (currScreen != null) {
		    currScreen.pause();
		}
		nextScreen.pause();
		Gdx.input.setInputProcessor(null); // disable input
		time = 0;
	}
    
    public void setPopup(AbstractPopup popupScreen) {
    	int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        
        if (popupScreen != null) {
        	popupState = PopupState.SHOWING;
        	popup = popupScreen;
        	popup.show();
        	popup.resize(width, height);
        	popup.render(0);
        	currScreen.pause();
        } else {
        	popupState = PopupState.HIDING;
        }
    	Gdx.input.setInputProcessor(null);
    	timePopup = 0;
    }
    
    public void hidePopup(ActionExecutor onPopupClose) {
    	this.onPopupClose = onPopupClose;
    	setPopup(null);
    }
    
    @Override
    public void render() {
        float deltaTime = Math.min(Gdx.graphics.getDeltaTime(), 1.0f / 60.0f);
        if (nextScreen == null || popupState == PopupState.HIDING) {
            // no ongoing transition
            if (currScreen != null) {
            	float popupDuration = popupTransition.getDuration();
				switch (popupState) {
            		case HIDDEN:
            			currScreen.render(deltaTime);
            			break;
	            	case SHOWING:
	            		timePopup = Math.min(timePopup + deltaTime, popupDuration);
	        			currFbo.begin();
	        			if (currScreen != null) {
	        				currScreen.render(deltaTime);
	        			}
	        			currFbo.end();
	        			popupFbo.begin();
	        			popup.render(deltaTime);
	        			popupFbo.end();
	        			popupTransition.render(batch, 
	        					currFbo.getColorBufferTexture(),
	        					popupFbo.getColorBufferTexture(), 
	        					timePopup / popupDuration);
	        			if (timePopup >= popupDuration) {
	            			popupState = PopupState.SHOWN;
	            			timePopup = 0;
	            			Gdx.input.setInputProcessor(popup.getInputProcessor());
	            		}
	            		break;
	            	case HIDING:
	            		timePopup = Math.min(timePopup + deltaTime, popupDuration);
	            		currFbo.begin();
	        			if (currScreen != null) {
	        				currScreen.render(deltaTime);
	        			}
	        			currFbo.end();
	        			popupFbo.begin();
	        			popup.render(deltaTime);
	        			popupFbo.end();
	        			popupTransition.render(batch, 
	        					currFbo.getColorBufferTexture(),
	        					popupFbo.getColorBufferTexture(), 
	        					 1 - timePopup / popupDuration);
	        			if (timePopup >= popupDuration) {
	        				popup.hide();
	        				popup = null;
	            			popupState = PopupState.HIDDEN;
	            			timePopup = 0;
	            			if (onPopupClose != null) {
	            				onPopupClose.execute();
	            				onPopupClose = null;
	            			}
	            			currScreen.resume();
	            			Gdx.input.setInputProcessor(currScreen.getInputProcessor());
	            		}
	            		break;
	            	case SHOWN:
	            		currFbo.end();
	        			popupFbo.begin();
	        			popup.render(deltaTime);
	        			popupFbo.end();
	        			popupTransition.render(batch, currFbo.getColorBufferTexture(), popupFbo.getColorBufferTexture(), 1);
	            		break;
	            	default: 
	            		throw new IllegalArgumentException();
            	}
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
        	popupFbo.dispose();
            currFbo.dispose();
            currScreen = null;
            nextFbo.dispose();
            nextScreen = null;
            batch.dispose();
            blackoutShader.dispose();
            init = false;
        }
    }
    
    public void back() {
        Gdx.input.setCatchBackKey(false);
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
	    	callerScreen = null;
    	} catch (Exception exc) {
    		Gdx.app.error(getClass().getName(), "An error has occured during navigation! Application is terminated!", exc);
    		Gdx.app.exit();
    	}
    }

    public void setCurrentLevelSettings(LevelSettings currentLevelSettings) {
    	this.currentLevelSettings = currentLevelSettings;
    }
    
    public void playNextLevel() {
    	int currLevelIndex = levels.indexOf(currentLevelSettings, true);
    	if (currLevelIndex < levels.size - 1) {
    		setCurrentLevelSettings(levels.get(currLevelIndex + 1));
    		playLevel();
    	} else {
    		back();
    	}
    }
    
    public void playLevel() {
    	if (currentLevelSettings != null) {
    		playLevel(currentLevelSettings, null);
    	}
    }
    
    public void playLevel(LevelSettings currentLevelSettings, Class<? extends AbstractGameScreen> callerClass) {
        Gdx.input.setCatchBackKey(true);
        this.currentLevelSettings = currentLevelSettings;
        try {
            Constructor<? extends Level<? extends LevelSettings>> constructor = currentLevelSettings.clazz.getConstructor(currentLevelSettings.getClass());
            Level<? extends LevelSettings> level = constructor.newInstance(currentLevelSettings);
            setScreen(new GameScreen(this, level), ScreenTransitionFade.init(), callerClass);
        } catch (Exception exc) {
            Gdx.app.error(getClass().getName(), "An exception has occured during level creation", exc);
        }
    }

}