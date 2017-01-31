package org.dragberry.ozo.game.util;

import org.dragberry.ozo.game.objects.AbstractGameObject;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class CameraHelper {
	
	private static final String TAG = CameraHelper.class.getName();

	public final static CameraHelper INSTANCE = new CameraHelper();
	
	private final float MAX_ZOOM_IN = 0.25f;
	private final float MAX_ZOOM_OUT = 10.0f;

	public Camera camera;
	public Camera cameraGui;

	private Vector2 position;
	private float zoom;
	
	private AbstractGameObject target;
	
	private CameraHelper() {
		position = new Vector2(0, 0);
		zoom = 1.0f;
	}

	public void update(float deltaTime) {
		if (!hasTarget()) {
			return;
		}
		position.x = target.position.x + target.origin.x;
		position.y = target.position.y + target.origin.y;
	}
	
	public void setPosition(float x, float y) {
		this.position.set(x, y);
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public void addZoom(float amount) {
		setZoom(zoom + amount);
	}
	
	public void setZoom(float zoom) {
		this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
	}
	
	public float getZoom() {
		return zoom;
	}

	public AbstractGameObject getTarget() {
		return target;
	}
	
	public void setTarget(AbstractGameObject target) {
		this.target = target;
	}
	
	public boolean hasTarget() {
		return target != null;
	}
	
	public boolean hasTarget(AbstractGameObject target) {
		return target != null && this.target.equals(target);
	}
	
	public void applyTo(OrthographicCamera camera) {
		camera.position.x = position.x;
		camera.position.y = position.y;
		camera.zoom = zoom;
		camera.update();
	}
	
}
