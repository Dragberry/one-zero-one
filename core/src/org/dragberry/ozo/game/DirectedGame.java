package org.dragberry.ozo.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.ArrayMap;

import org.dragberry.ozo.game.level.LevelProvider;
import org.dragberry.ozo.common.CommonConstants;
import org.dragberry.ozo.common.audit.AuditEventRequest;
import org.dragberry.ozo.common.audit.AuditEventType;
import org.dragberry.ozo.game.level.Level;
import org.dragberry.ozo.game.level.settings.LevelSettings;
import org.dragberry.ozo.http.HttpClient;
import org.dragberry.ozo.http.PostHttpTask;
import org.dragberry.ozo.platform.Platform;
import org.dragberry.ozo.screen.AbstractGameScreen;
import org.dragberry.ozo.screen.ActionExecutor;
import org.dragberry.ozo.screen.GameScreen;
import org.dragberry.ozo.screen.MainMenuScreen;
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

	private static final String USER_ID = "userID";
	private static final String SETTINGS_EXTENSION = ".settings";

	public static DirectedGame game;

	public boolean obsolete;

	public final Platform platform;

	private boolean auditEnabled;

	public LevelProvider levelProvider;
	public final Map<String, Level<?>> levelsCache = new HashMap<String, Level<?>>();

	public final ArrayMap<Class<? extends AbstractGameScreen>, AbstractGameScreen> screensCache = new ArrayMap<Class<? extends AbstractGameScreen>, AbstractGameScreen>();

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

	public DirectedGame(Platform platform, boolean auditEnabled) {
		this.platform = platform;
		this.auditEnabled = auditEnabled;
		game = this;
	}

	public <S extends AbstractGameScreen> S getScreen(Class<S> screenClass) {
		S screen = (S) screensCache.get(screenClass);
		if (screen == null) {
			try {
				Constructor<S> constructor = screenClass.getConstructor(DirectedGame.class);
				screen = constructor.newInstance(this);
				screensCache.put(screenClass, screen);
			} catch (Exception exc) {
				Gdx.app.debug(TAG, "An error has occured screen creation", exc);
				Gdx.app.exit();
			}
		}
		return screen;
	}

	/**
	 * Loads game settings
	 * If user id hasn't stored yet, send a POST request to create a new user
	 * @param platform
     */
	public void loadGameSettings(final Platform platform) {
		Gdx.app.debug(TAG, "load game settings...");
		final Preferences prefs = Gdx.app.getPreferences(getClass() + SETTINGS_EXTENSION);
		String userId = prefs.getString(USER_ID);
		if (userId.isEmpty()) {
			Gdx.app.debug(TAG, "User id is not exist for taht application. Sending request to create new user");
			platform.getHttpClient().executeTask(new PostHttpTask<String, String>(
					CommonConstants.DEFAULT_USER_ID, String.class, HttpClient.URL.NEW_USER) {

				@Override
				public void onComplete(String result) {
					Gdx.app.debug(TAG, "New user has been created with id=" + result);
					prefs.putString(USER_ID, result);
					prefs.flush();
					platform.getUser().setUserId(result);

					logAuditEvent(createSimpleAuditRequest(AuditEventType.LAUNCH_APPLICATION));
				}
			});
		} else {
			platform.getUser().setUserId(userId);
		}
	}

    @Override
    public void create() {
		Assets.instance.init(new AssetManager());

		loadGameSettings(platform);

		logAuditEvent(createSimpleAuditRequest(AuditEventType.LAUNCH_APPLICATION));

		levelProvider = new LevelProvider();
	 	levelProvider.loadResults();

		popupState = PopupState.HIDDEN;

		blackoutShader = new ShaderProgram(
     		Gdx.files.internal("shaders/blackout.vert"),
     		Gdx.files.internal("shaders/blackout.frag"));
		this.popupTransition = PopupTransition.init(blackoutShader);

		Gdx.input.setCatchBackKey(true);

    }

	public void exit() {
		logAuditEvent(createSimpleAuditRequest(AuditEventType.EXIT_APPLICATION));
		Gdx.app.exit();
	}

	private AuditEventRequest createSimpleAuditRequest(AuditEventType eventType) {
		AuditEventRequest req = new AuditEventRequest();
		req.setUserId(platform.getUser().getId());
		req.setType(eventType);
		return req;
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

		for (AbstractGameScreen screen : screensCache.values()) {
			screen.dispose();
		}
		screensCache.clear();

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
    		exit();
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
    
    public <LS extends LevelSettings> void playLevel(LS currentLevelSettings, Class<? extends AbstractGameScreen> callerClass) {
        logAuditEvent(createSimpleAuditRequest(AuditEventType.START_LEVEL));
		this.currentLevelSettings = currentLevelSettings;
        try {
			boolean restore = false;
			Level<LS> level = (Level<LS>) levelsCache.get(currentLevelSettings.levelId);
			if (level == null) {
				level = (Level<LS>) currentLevelSettings.loadLevelState();
				if (level != null) {
					restore = true;
					level.setSettings(currentLevelSettings);
					Gdx.app.debug(TAG, "Incomplete level was loaded: " + currentLevelSettings.levelId);
				} else {
					Gdx.app.debug(TAG, "New level was created: " + currentLevelSettings.levelId);
					Constructor<? extends Level<LS>> constructor = (Constructor<? extends Level<LS>>) currentLevelSettings.clazz.getConstructor(currentLevelSettings.getClass());
					level = constructor.newInstance(currentLevelSettings);
				}
				levelsCache.put(currentLevelSettings.levelId, level);
			} else {
				restore = level.savedState;
				Gdx.app.debug(TAG, "Level was loaded from cache: " + currentLevelSettings.levelId);
			}
            setScreen(getScreen(GameScreen.class).init(level, restore), ScreenTransitionFade.init(), callerClass);
        } catch (Exception exc) {
            Gdx.app.error(TAG, "An exception has occured during level creation", exc);
        }
    }

	public void logAuditEvent(final AuditEventRequest request) {
		if (auditEnabled && !platform.getUser().isDefault()) {
			platform.getHttpClient().executeTask(new PostHttpTask<AuditEventRequest, Void>(
					request, Void.class, HttpClient.URL.NEW_AUDIT_EVENT + request.getUrl()) {

				@Override
				public void onComplete(Void result) {
					Gdx.app.debug(TAG, "Audit event was logged: " + request);
				}
			});
		}
	}

}