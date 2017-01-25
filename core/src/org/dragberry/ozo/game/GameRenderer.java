package org.dragberry.ozo.game;

import org.dragberry.ozo.game.objects.Unit;
import org.dragberry.ozo.game.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class GameRenderer extends InputAdapter implements Disposable {
	
	private static final String TAG = GameRenderer.class.getName();
	
	private OrthographicCamera camera;
    private SpriteBatch batch;
	private GameController gameController;
	
	private OrthographicCamera cameraGUI;
	
	private ShapeRenderer shapeRenderer;
	
	private boolean debug = false;

	public GameRenderer(GameController gameController) {
		this.gameController = gameController;
		init();
	}
	
	public void init() {
		Gdx.input.setInputProcessor(this);
		
		batch = new SpriteBatch();
		
		float height = Gdx.graphics.getHeight() * (Constants.VIEWPORT_WIDTH / Gdx.graphics.getWidth());
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, height);
        camera.position.set(-Constants.VIEWPORT_WIDTH / 2, -height / 2, 0);
        camera.update();
        
        gameController.cameraHelper.setPosition(
        		gameController.gameWidth * Constants.UNIT_SIZE / 2, 
        		gameController.gameHeight * Constants.UNIT_SIZE / 2);
        float screenAspectRatio = camera.viewportWidth / camera.viewportHeight;
        float gameAspectRatio = gameController.gameWidth / gameController.gameHeight;
        float zoom = 0;
        if (screenAspectRatio > gameAspectRatio) {
        	zoom = gameController.gameHeight * Constants.UNIT_SIZE / camera.viewportHeight;
        } else {
        	zoom = gameController.gameHeight * Constants.UNIT_SIZE / camera.viewportHeight;
        }
        gameController.cameraHelper.setZoom(zoom);
        
        cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        cameraGUI.position.set(0, 0, 0);
        cameraGUI.setToOrtho(true);
        cameraGUI.update();
        initDebug();
	}
	
	private void initDebug() {
		if (!debug) {
			return;
		}
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);
	}

	public void render() {
		renderGame(batch);
		renderDebugInfo(shapeRenderer);
	}
	
	private void renderDebugInfo(ShapeRenderer shapeRenderer) {
		if (!debug) {
			return;
		}
		gameController.cameraHelper.applyTo(camera);
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin();
		shapeRenderer.set(ShapeType.Line);
		shapeRenderer.setColor(Color.BLACK);
		
		float width = gameController.gameWidth * Constants.UNIT_SIZE;
		float height = gameController.gameHeight * Constants.UNIT_SIZE;
		for (int x = 0; x <= gameController.gameWidth; x++) {
			float xCoord = x * Constants.UNIT_SIZE;
			shapeRenderer.line(xCoord, 0, xCoord, height);
		}
		for (int y = 0; y <= gameController.gameHeight; y++) {
			float yCoord = y * Constants.UNIT_SIZE;
			shapeRenderer.line(0, yCoord, width, yCoord);
		}
		shapeRenderer.end();
	}

	void renderGame(SpriteBatch batch) {
		gameController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (Unit[] row : gameController.units) {
			for (Unit unit : row) {
				unit.render(batch);
			}
		}
		batch.end();
	}
	
	public void resize(int width, int height) {
        camera.viewportHeight = (Constants.VIEWPORT_WIDTH / width) * height;
        camera.update();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
    
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    	Vector3 touchCoord = camera.unproject(new Vector3(screenX, screenY, 0));
    	gameController.onScreenTouch(touchCoord.x, touchCoord.y);
    	return false;
    }

}
