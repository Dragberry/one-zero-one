package org.dragberry.ozo.screen;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import org.dragberry.ozo.LevelProvider;
import org.dragberry.ozo.admob.AdsController;
import org.dragberry.ozo.game.Assets;
import org.dragberry.ozo.game.level.Level;
import org.dragberry.ozo.game.level.settings.LevelSettings;
import org.dragberry.ozo.screen.popup.AbstractPopup;
import org.dragberry.ozo.screen.transitions.PopupTransition;
import org.dragberry.ozo.screen.transitions.ScreenTransition;
import org.dragberry.ozo.screen.transitions.ScreenTransitionFade;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by maksim on 29.01.17.
 */

public abstract class DirectedGame implements ApplicationListener {

	private final static String TAG = DirectedGame.class.getName();

	public final AdsController adsController;

	public LevelProvider levelProvider;
	public final Map<String, Level<?>> levelsCache = new HashMap<String, Level<?>>();
	
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

	public DirectedGame(AdsController adsController) {
		this.adsController = adsController;
	}

    @Override
    public void create() {
    	Assets.instance.init(new AssetManager());
		levelProvider = new LevelProvider();
	 	levelProvider.loadResults(httpClient);
		popupState = PopupState.HIDDEN;
		blackoutShader = new ShaderProgram(
     		Gdx.files.internal("shaders/blackout.vert"),
     		Gdx.files.internal("shaders/blackout.frag"));
		this.popupTransition = PopupTransition.init(blackoutShader);

		Gdx.input.setCatchBackKey(true);
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
			if (currScreen != null) {
				currScreen.pause();
			}
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
    	try {
	    	if (callerScreen != null) {
	    		Constructor<? extends AbstractGameScreen> constructor = callerScreen.getConstructor(DirectedGame.class);
	    		constructor.newInstance(this);
	    		setScreen(constructor.newInstance(this), ScreenTransitionFade.init(), null);
	    		Gdx.app.debug(TAG, "Navigate to " + callerScreen);
	    	} else {
	    		setScreen(new MainMenuScreen(this), ScreenTransitionFade.init(), null);
	    		Gdx.app.debug(TAG, "Navigate to " + MainMenuScreen.class);
	    	}
	    	callerScreen = null;
    	} catch (Exception exc) {
    		Gdx.app.error(TAG, "An error has occured during navigation! Application is terminated!", exc);
    		Gdx.app.exit();
    	}
    }

    public void setCurrentLevelSettings(LevelSettings currentLevelSettings) {
    	this.currentLevelSettings = currentLevelSettings;
    }
    
    public void playNextLevel() {
    	int currLevelIndex = levelProvider.levels.indexOf(currentLevelSettings, true);
    	if (currLevelIndex < levelProvider.levels.size - 1) {
    		setCurrentLevelSettings(levelProvider.levels.get(currLevelIndex + 1));
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
        this.currentLevelSettings = currentLevelSettings;
        try {
			Level<? extends LevelSettings> level = levelsCache.get(currentLevelSettings.nameKey);
			if (level == null) {
				Gdx.app.debug(TAG, "New level was created: " + currentLevelSettings.nameKey);
				Constructor<? extends Level<? extends LevelSettings>> constructor = currentLevelSettings.clazz.getConstructor(currentLevelSettings.getClass());
				level = constructor.newInstance(currentLevelSettings);
				levelsCache.put(currentLevelSettings.nameKey, level);
			} else {
				Gdx.app.debug(TAG, "Level was loaded from cache: " + currentLevelSettings.nameKey);
				level.reset();
			}
            setScreen(new GameScreen(this, level), ScreenTransitionFade.init(), callerClass);
        } catch (Exception exc) {
            Gdx.app.error(TAG, "An exception has occured during level creation", exc);
        }
    }

}