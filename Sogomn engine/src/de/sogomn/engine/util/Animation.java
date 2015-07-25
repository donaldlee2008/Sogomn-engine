package de.sogomn.engine.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import de.sogomn.engine.ITickable;

/**
 * A class to handle animations. The method "update" needs to be called in order to work.
 * Calling the "draw" method will not do anything.
 * @author Sogomn
 *
 */
public final class Animation implements ITickable {
	
	private BufferedImage[] images;
	private int currentIndex;
	
	private float delay, timer;
	
	/**
	 * Constructs an Animation object with the given delay between the images and the passed images.
	 * @param delay The delay between the images in seconds
	 * @param images The images
	 */
	public Animation(final float delay, final BufferedImage... images) {
		this.delay = delay;
		this.images = images;
	}
	
	@Override
	public void update(final float delta) {
		timer += delta;
		
		if (timer >= delay) {
			currentIndex++;
			currentIndex %= (images.length - 1);
		}
	}
	
	@Override
	public void draw(final Graphics2D g) {
		//...
	}
	
	/**
	 * Returns the index the animation is currently at.
	 * @return The current index
	 */
	public int getCurrentIndex() {
		return currentIndex;
	}
	
	/**
	 * Returns the delay between each image.
	 * @return The delay
	 */
	public float getImageDelay() {
		return delay;
	}
	
	/**
	 * Returns the image the animation is currently at.
	 * @return The image
	 */
	public BufferedImage getImage() {
		return images[currentIndex];
	}
	
}
