package de.sogomn.engine.util;

import java.awt.image.BufferedImage;

import de.sogomn.engine.IUpdatable;

/**
 * A class to handle animations. The method "update" needs to be called every frame in order to work.
 * @author Sogomn
 *
 */
public final class Animation extends AbstractListenerContainer<IAnimationListener> implements IUpdatable {
	
	private BufferedImage[] images;
	private int currentIndex;
	
	private float delay;
	private double timer;
	
	private int maxLoops, currentLoop;
	
	/**
	 * Infinite number of loops.
	 */
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
	
	/**
	 * Constructs an Animation object with the given delay between the images and all images in the sprite sheet.
	 * @param delay The delay between the images in seconds
	 * @param spriteSheet The SpriteSheet object containing the images
	 */
	public Animation(final float delay, final SpriteSheet spriteSheet) {
		this(delay, spriteSheet.getSprites());
	}
	
	private void notifyListeners() {
		synchronized (listeners) {
			for (int i = 0; i < listeners.size(); i++) {
				final IAnimationListener listener = listeners.get(i);
				
				listener.looped(this);
			}
		}
	}
	
	/**
	 * Updates the animation.
	 */
	@Override
	public void update(final float delta) {
		if (!isLooping()) {
			return;
		}
		
		timer += delta;
		
		if (timer >= delay) {
			timer = 0;
			currentIndex++;
			
			if (currentIndex > images.length - 1) {
				currentIndex = 0;
				currentLoop++;
				
				notifyListeners();
			}
		}
	}
	
	/**
	 * Resets the timer, the index and the loop count.
	 */
	public void reset() {
		timer = 0;
		currentIndex = currentLoop = 0;
	}
	
	/**
	 * Sets the delay between the frames.
	 * @param delay The delay
	 */
	public void setDelay(final float delay) {
		this.delay = delay;
	}
	
	/**
	 * Sets the maximum amount on loops the animation will go through.
	 * If set to INFINITE (-1) the animation has no max loops. This is default.
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
	 * Returns the image at the given index.
	 * @param index The index
	 * @return The image or null if the index is out of range
	 */
	public BufferedImage getImage(final int index) {
		if (index < 0 || index > length() - 1) {
			return null;
		}
		
		return images[index];
	}
	
	/**
	 * Returns the current loop the animation is in.
	 * @return The loop count
	 */
	public int getCurrentLoop() {
		return currentLoop;
	}
	
	/**
	 * Returns the amount of frames in the animation.
	 * @return The length of the animation
	 */
	public int length() {
		return images.length;
	}
	
	/**
	 * Returns true if the animation is still looping; false otherwise.
	 * @return Whether the animation is looping
	 */
	public boolean isLooping() {
		final boolean looping = (maxLoops == INFINITE || currentLoop < maxLoops);
		
		return looping;
	}
	
}
