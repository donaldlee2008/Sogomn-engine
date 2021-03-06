/*******************************************************************************
 * Copyright 2016 Johannes Boczek
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package de.sogomn.engine.fx;

import java.awt.image.BufferedImage;

import de.sogomn.engine.IUpdatable;
import de.sogomn.engine.util.AbstractListenerContainer;

/**
 * A class to handle animations. The method "update" needs to be called every frame in order to work.
 * Uses the Scheduler class.
 * @author Sogomn
 *
 */
public final class Animation extends AbstractListenerContainer<IAnimationListener> implements IUpdatable {
	
	private BufferedImage[] images;
	private int currentIndex;
	
	private double timer;
	private float interval;
	
	private int maxLoops, currentLoop;
	
	/**
	 * Infinite number of loops.
	 */
	public static final int INFINITE = -1;
	
	/**
	 * Constructs an Animation object with the given interval between the images and the passed images.
	 * @param interval The interval between the images in seconds
	 * @param images The images
	 */
	public Animation(final float interval, final BufferedImage... images) {
		this.interval = interval;
		this.images = images;
		
		maxLoops = INFINITE;
	}
	
	/**
	 * Constructs an Animation object with the given interval between the images and all images in the sprite sheet.
	 * @param interval The interval between the images in seconds
	 * @param spriteSheet The SpriteSheet object containing the images
	 */
	public Animation(final float interval, final SpriteSheet spriteSheet) {
		this(interval, spriteSheet.getSprites());
	}
	
	/**
	 * Updates the animation.
	 */
	@Override
	public void update(final double delta) {
		if (!isLooping()) {
			return;
		}
		
		timer += delta;
		
		if (timer >= interval) {
			timer = 0;
			
			nextFrame();
		}
	}
	
	/**
	 * Resets the timer, the index and the loop count.
	 */
	public void reset() {
		timer = 0;
		currentIndex = 0;
		currentLoop = 0;
	}
	
	/**
	 * Skips the current frame.
	 */
	public void nextFrame() {
		currentIndex++;
		
		if (currentIndex > images.length - 1) {
			currentIndex = 0;
			currentLoop++;
			
			notifyListeners(listener -> listener.looped(this));
		}
	}
	
	/**
	 * Sets the interval between the frames.
	 * @param interval The new interval between the frames
	 */
	public void setInterval(final float interval) {
		this.interval = interval;
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
	 * Returns the interval between each image.
	 * @return The interval between the frames
	 */
	public float getInterval() {
		return interval;
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
