package de.sogomn.engine.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import de.sogomn.engine.ITickable;

/**
 * This class can be used for backgrounds in games.
 * The methods "update" and "draw" should called in order to work properly.
 * @author Sogomn
 *
 */
public final class Background implements ITickable {
	
	private int width, height;
	
	private double x, y;
	
	private double horizontalSpeed, verticalSpeed;
	
	private BufferedImage image;
	private Animation animation;
	
	/**
	 * Constructs a new Background object from the given image.
	 * The image will be streched to the passed with and height.
	 * @param image The image
	 * @param width The width
	 * @param height The height
	 */
	public Background(final BufferedImage image, final int width, final int height) {
		this.width = width;
		this.height = height;
		this.image = ImageUtils.scaleImage(image, width, height);
	}
	
	/**
	 * Constructs a new Background object from the given image.
	 * @param image The image
	 */
	public Background(final BufferedImage image) {
		this(image, image.getWidth(), image.getHeight());
	}
	
	/**
	 * Loads an image from the given path (classpath!) and constructs a new Background object from it.
	 * The image will be streched to the passed with and height.
	 * @param path The path to the image
	 * @param width The width
	 * @param height The height
	 */
	public Background(final String path, final int width, final int height) {
		this(ImageUtils.loadImage(path), width, height);
	}
	
	/**
	 * Loads an image from the given path (classpath!) and constructs a new Background object from it.
	 * @param path The path to the image
	 */
	public Background(final String path) {
		this(ImageUtils.loadImage(path));
	}
	
	/**
	 * Constructs a new Background object from the passed animation.
	 * @param animation The animation
	 */
	public Background(final Animation animation) {
		this(animation.getImage());
		
		this.animation = animation;
	}
	
	/**
	 * Updates the background.
	 */
	@Override
	public void update(final float delta) {
		if (animation != null) {
			animation.update(delta);
			
			image = animation.getImage();
		}
		
		x += horizontalSpeed * delta;
		y += verticalSpeed * delta;
		x %= width;
		y %= height;
	}
	
	/**
	 * Draws the background. The image will loop.
	 */
	@Override
	public void draw(final Graphics2D g) {
		final int offsetX = (int)(x - width * Math.signum(horizontalSpeed));
		final int offsetY = (int)(y - height * Math.signum(verticalSpeed));
		
		g.drawImage(image, (int)x, (int)y, null);
		g.drawImage(image, offsetX, (int)y, null);
		g.drawImage(image, (int)x, offsetY, null);
		g.drawImage(image, offsetX, offsetY, null);
	}
	
	/**
	 * Sets the background x position
	 * @param x The position
	 */
	public void setX(final double x) {
		this.x = x;
	}
	
	/**
	 * Sets the background y position
	 * @param y The position
	 */
	public void setY(final double y) {
		this.y = y;
	}
	
	/**
	 * Sets the horizontal speed of the background.
	 * @param horizontalSpeed The speed
	 */
	public void setHorizontalSpeed(final double horizontalSpeed) {
		this.horizontalSpeed = horizontalSpeed;
	}
	
	/**
	 * Sets the vertical speed of the background.
	 * @param verticalSpeed The speed
	 */
	public void setVerticalSpeed(final double verticalSpeed) {
		this.verticalSpeed = verticalSpeed;
	}
	
	/**
	 * Returns the width of the background.
	 * If an animation was used this is the width of the first image.
	 * @return The width
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Returns the height of the background.
	 * If an animation was used this is the height of the first image.
	 * @return The height
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Returns the current x position of the background.
	 * @return The position
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Returns the current y position of the background.
	 * @return The position
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Returns the horizontal speed of the background.
	 * @return The speed
	 */
	public double getHorizontalSpeed() {
		return horizontalSpeed;
	}
	
	/**
	 * Returns the vertical speed of the background.
	 * @return The speed
	 */
	public double getVerticalSpeed() {
		return verticalSpeed;
	}
	
}