package de.sogomn.engine.util;

import java.awt.Graphics2D;

import de.sogomn.engine.IUpdatable;

public final class Camera implements IUpdatable {
	
	private double x, y;
	
	private double targetX, targetY;
	
	private float smoothness;
	
	/**
	 * If passed to the method "setSmoothness", the camera position will automatically be the target position.
	 */
	public static final float NO_SMOOTHNESS = 0;
	
	/**
	 * Constructs a new Camera object with the default smoothness of 0.
	 */
	public Camera() {
		smoothness = NO_SMOOTHNESS;
	}
	
	/**
	 * Updates the camera.
	 */
	@Override
	public void update(final float delta) {
		if (smoothness == NO_SMOOTHNESS) {
			x = targetX;
			y = targetY;
		} else {
			final double distX = targetX - x;
			final double distY = targetY - y;
			
			x += (distX / smoothness) * delta;
			y += (distY / smoothness) * delta;
		}
	}
	
	public void apply(final Graphics2D g) {
		g.translate(-x, -y);
	}
	
	public void revert(final Graphics2D g) {
		g.translate(x, y);
	}
	
	/**
	 * Translates the given coordinate to the camera space.
	 * @param x The x coordinate
	 * @return The translated coordinate
	 */
	public double translateX(final double x) {
		return (x + this.x);
	}
	
	/**
	 * Translates the given coordinate to the camera space.
	 * @param y The y coordinate
	 * @return The translated coordinate
	 */
	public double translateY(final double y) {
		return (y + this.y);
	}
	
	/**
	 * Resets the camera position and target.
	 */
	public void reset() {
		x = y = 0;
		targetX = targetY = 0;
	}
	
	/**
	 * Offsets the target position of the camera by the given one.
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public void moveBy(final double x, final double y) {
		targetX += x;
		targetY += y;
	}
	
	/**
	 * Sets the target position of the camera to the given one.
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public void moveTo(final double x, final double y) {
		targetX = x;
		targetY = y;
	}
	
	/**
	 * Sets the camera position to the given one.
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public void set(final double x, final double y) {
		this.x = x;
		this.y = y;
		
		targetX = x;
		targetY = y;
	}
	
	/**
	 * Sets the smoothness for the camera.
	 * Good values are between 0 an 1.
	 * @param smoothness The smoothness
	 */
	public void setSmoothness(final float smoothness) {
		this.smoothness = smoothness;
	}
	
	/**
	 * Returns the x coordinate of the camera.
	 * @return The coordinate
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Returns the y coordinate of the camera.
	 * @return The coordinate
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Returns the target x coordinate of the camera.
	 * @return The coordinate
	 */
	public double getTargetX() {
		return targetX;
	}
	
	/**
	 * Returns the target y coordinate of the camera.
	 * @return The coordinate
	 */
	public double getTargetY() {
		return targetY;
	}
	
}
