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
	
	private int maxLoops, currentLoop;
	
	public static final int INFINITE = -1;
	
	/**
	 * Constructs an Animation object with the given delay between the images and the passed images.
	 * @param delay The delay between the images in seconds
	 * @param images The images
	 */
	public Animation(final float delay, final BufferedImage... images) {
		this.delay = delay;
		this.images = images;
		
		maxLoops = INFINITE;
	}
	
	@Override
	public void update(final float delta) {
		if (maxLoops != INFINITE && currentLoop >= maxLoops) {
			return;
		}
		
		timer += delta;
		
		if (timer >= delay) {
			currentIndex++;
			
			if (currentIndex > images.length - 1) {
				currentIndex = 0;
				currentLoop++;
			}
		}
	}
	
	@Override
	public void draw(final Graphics2D g) {
		//...
	}
	
	/**
	 * Resets the timer, the index and the loop count.
	 */
	public void reset() {
		timer = 0;
		currentIndex = currentLoop = 0;
	}
	
	/**
	 * Sets the maximum amount on loops the animation will go through. Default is INFINITE (-1).
	 * @param maxLoops The maximum amount of loops
	 */
	public void setMaxLoops(final int maxLoops) {
		this.maxLoops = maxLoops;
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
	
	/**
	 * Returns the current loop the animation is in.
	 * @return The loop count
	 */
	public int getCurrentLoop() {
		return currentLoop;
	}
	
}
