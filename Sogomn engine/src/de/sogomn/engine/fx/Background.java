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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import de.sogomn.engine.IDrawable;
import de.sogomn.engine.IUpdatable;
import de.sogomn.engine.util.ImageUtils;

/**
 * This class can be used for backgrounds in games.
 * The methods "update" and "draw" should called in a loop in order to work properly.
 * @author Sogomn
 *
 */
public final class Background implements IUpdatable, IDrawable {
	
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
	public void update(final double delta) {
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
	 * Draws the background.
	 */
	@Override
	public void draw(final Graphics2D g) {
		final int offsetX = (int)(x - width * Math.signum(x));
		final int offsetY = (int)(y - height * Math.signum(y));
		
		g.drawImage(image, (int)x, (int)y, null);
		
		if (offsetX != 0 && offsetY != 0) {
			g.drawImage(image, offsetX, (int)y, null);
			g.drawImage(image, (int)x, offsetY, null);
			g.drawImage(image, offsetX, offsetY, null);
		} else if (offsetX != 0) {
			g.drawImage(image, offsetX, (int)y, null);
		} else if (offsetY != 0) {
			g.drawImage(image, (int)x, offsetY, null);
		}
	}
	
	/**
	 * Sets the background x coordinate
	 * @param x The coordinate
	 */
	public void setX(final double x) {
		this.x = x;
	}
	
	/**
	 * Sets the background y coordinate
	 * @param y The coordinate
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
	 * Sets the background image.
	 * @param image The new image
	 */
	public void setImage(final BufferedImage image) {
		this.image = image;
	}
	
	/**
	 * Sets the background animation.
	 * @param animation The new animation or null for no animation
	 */
	public void setAnimation(final Animation animation) {
		this.animation = animation;
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
	 * Returns the current x coordinate of the background.
	 * @return The coordinate
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Returns the current y coordinate of the background.
	 * @return The coordinate
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
