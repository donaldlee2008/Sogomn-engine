package de.sogomn.engine.util;

import java.awt.Graphics2D;

import de.sogomn.engine.IUpdatable;

public final class Camera implements IUpdatable {
	
	private double x, y;
	private double targetX, targetY;
	
	private double minX, minY;
	private double maxX, maxY;
	
	private float smoothness;
	
	/**
	 * If passed to the method "setSmoothness" the camera position will automatically be the target position.
	 */
	public static final float NO_SMOOTHNESS = 0;
	
	/**
	 * Represents a minimum value of any size.
	 */
	public static final int NO_MINIMUM = Integer.MIN_VALUE;
	
	/**
	 * Represents a maximum value of any size.
	 */
	public static final int NO_MAXIMUM = Integer.MAX_VALUE;
	
	/**
	 * Constructs a new Camera object with the default smoothness of 0 and no minimum or maximum values.
	 */
	public Camera() {
		minX = minY = NO_MINIMUM;
		maxX = maxY = NO_MAXIMUM;
		smoothness = NO_SMOOTHNESS;
	}
	
	private void clampPosition() {
		x = Math.max(Math.min(x, maxX), minX);
		y = Math.max(Math.min(y, maxY), minY);
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
		
		clampPosition();
	}
	
	/**
	 * Applies the camera translation to the given Graphics2D object.
	 * @param g The Graphics2D object
	 */
	public void apply(final Graphics2D g) {
		g.translate(-x, -y);
	}
	
	/**
	 * Reverts the camera translation of the given Graphics2D object.
	 * This should only be called if the translation was applied before.
	 * @param g The Graphics2D object
	 */
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
		this.x = targetX = x;
		this.y = targetY = y;
		
		clampPosition();
	}
	
	/**
	 * Sets the minimum position the camera can have.
	 * @param minX The minimum x value
	 * @param minY The minimum y value
	 */
	public void setMinimum(final double minX, final double minY) {
		this.minX = minX;
		this.minY = minY;
	}
	
	/**
	 * Sets the maximum position the camera can have.
	 * @param maxX The maximum x value
	 * @param maxY The maximum y value
	 */
	public void setMaximum(final double maxX, final double maxY) {
		this.maxX = maxX;
		this.maxY = maxY;
	}
	
	/**
	 * Sets the minimum and maximum values the camera can have.
	 * @param minX The minimum x value
	 * @param minY The minimum y value
	 * @param maxX The maximum x value
	 * @param maxY The maximum y value
	 */
	public void setBounds(final double minX, final double minY, final double maxX, final double maxY) {
		setMinimum(minX, minY);
		setMaximum(maxX, maxY);
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
