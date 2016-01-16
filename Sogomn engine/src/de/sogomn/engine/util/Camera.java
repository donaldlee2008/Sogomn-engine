package de.sogomn.engine.util;

import java.awt.Graphics2D;

import de.sogomn.engine.IUpdatable;

/**
 * This class can be used as a camera for games.
 * In order to apply the translation the method "apply" needs to be called before rendering. It also needs to get updated every tick.
 * After rendering the method "revert" should be called to undo the translation.
 * @author Sogomn
 *
 */
public final class Camera implements IUpdatable {
	
	private double x, y;
	private double targetX, targetY;
	private double xOffset, yOffset;
	
	private double minX, minY;
	private double maxX, maxY;
	
	private float smoothness;
	
	/**
	 * If passed to the method "setSmoothness" the camera position will automatically be the target position.
	 */
	public static final float NO_SMOOTHNESS = 0;
	
	/**
	 * Represents a minimum value of no fixed size.
	 */
	public static final int NO_MINIMUM = Integer.MIN_VALUE;
	
	/**
	 * Represents a maximum value of no fixed size.
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
	
	private void clampTarget() {
		targetX = Math.max(Math.min(targetX, maxX), minX);
		targetY = Math.max(Math.min(targetY, maxY), minY);
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
	
	/**
	 * Applies the camera translation to the given Graphics2D object.
	 * @param g The Graphics2D object
	 */
	public void apply(final Graphics2D g) {
		clampPosition();
		
		final double actualX = x + xOffset;
		final double actualY = y + yOffset;
		
		g.translate(-actualX, -actualY);
	}
	
	/**
	 * Reverts the camera translation of the given Graphics2D object.
	 * This should only be called if the translation was applied before.
	 * @param g The Graphics2D object
	 */
	public void revert(final Graphics2D g) {
		final double actualX = x + xOffset;
		final double actualY = y + yOffset;
		
		g.translate(actualX, actualY);
	}
	
	/**
	 * Resets the camera position and target.
	 */
	public void reset() {
		x = y = 0;
		targetX = targetY = 0;
		
		resetShake();
	}
	
	/**
	 * Offsets the screen by a random value without affecting its coordinates.
	 * Call "resetShake" to revert it.
	 * The offset will be from -value to +value.
	 * @param xAmount The amount of offset on the x-axis
	 * @param yAmount The amount of offset on the y-axis
	 */
	public void shake(final double xAmount, final double yAmount) {
		xOffset = Math.random() * xAmount * 2 - xAmount;
		yOffset = Math.random() * yAmount * 2 - yAmount;
	}
	
	/**
	 * Resets the camera offset.
	 */
	public void resetShake() {
		xOffset = yOffset = 0;
	}
	
	/**
	 * Offsets the target position of the camera by the given one.
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public void moveBy(final double x, final double y) {
		targetX += x;
		targetY += y;
		
		clampTarget();
	}
	
	/**
	 * Sets the target position of the camera to the given one.
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public void moveTo(final double x, final double y) {
		targetX = x;
		targetY = y;
		
		clampTarget();
	}
	
	/**
	 * Sets the camera and the target position to the given one.
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public void set(final double x, final double y) {
		this.x = targetX = x;
		this.y = targetY = y;
		
		clampTarget();
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
	
	/**
	 * Returns the minimum x value.
	 * @return The value
	 */
	public double getMinimumX() {
		return minX;
	}
	
	/**
	 * Returns the minimum y value.
	 * @return The value
	 */
	public double getMinimumY() {
		return minY;
	}
	
	/**
	 * Returns the maximum x value.
	 * @return The value
	 */
	public double getMaximumX() {
		return maxX;
	}
	
	/**
	 * Returns the maximum y value.
	 * @return The value
	 */
	public double getMaximumY() {
		return maxY;
	}
	
}
