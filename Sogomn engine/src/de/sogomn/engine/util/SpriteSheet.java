package de.sogomn.engine.util;

import java.awt.image.BufferedImage;

/**
 * This class can be used to manage sprite sheets.
 * @author Sogomn
 *
 */
public final class SpriteSheet {
	
	private BufferedImage[][] sprites;
	private int width, height;
	private int spriteWidth, spriteHeight;
	private int spritesWide, spritesHigh;
	
	/**
	 * Constructs a SpriteSheet object from the given image and the sprite dimensions.
	 * @param image The base image
	 * @param spriteWidth The width of every sprite
	 * @param spriteHeight The height of every sprite
	 */
	public SpriteSheet(final BufferedImage image, final int spriteWidth, final int spriteHeight) {
		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;
		
		width = image.getWidth();
		height = image.getHeight();
		spritesWide = width / spriteWidth;
		spritesHigh = height / spriteHeight;
		sprites = new BufferedImage[spritesWide][spritesHigh];
		
		loadImages(image);
	}
	
	/**
	 * Constructs a SpriteSheet object from the given path (classpath!) and sprite dimensions.
	 * @param path The path to the base image
	 * @param spriteWidth The width of every sprite
	 * @param spriteHeight The height of every sprite
	 */
	public SpriteSheet(final String path, final int spriteWidth, final int spriteHeight) {
		this(ImageUtils.loadImage(path), spriteWidth, spriteHeight);
	}
	
	private void loadImages(final BufferedImage image) {
		for (int x = 0; x < spritesWide; x++) {
			for (int y = 0; y < spritesHigh; y++) {
				final int imageX = x * spriteWidth;
				final int imageY = y * spriteHeight;
				final BufferedImage subimage = image.getSubimage(imageX, imageY, spriteWidth, spriteHeight);
				
				sprites[x][y] = subimage;
			}
		}
	}
	
	/**
	 * Returns the sprite at the given row (y) and column (x).
	 * @param x The x index (column)
	 * @param y The y index (row)
	 * @return The sprite as a BufferedImage or null if the indices are wrong
	 */
	public BufferedImage getSprite(final int x, final int y) {
		if (x < 0 || y < 0 || x > spritesWide - 1 || y > spritesHigh - 1) {
			return null;
		}
		
		final BufferedImage image = sprites[x][y];
		
		return image;
	}
	
	/**
	 * Returns the sprite at the given index. The orientation might either be left to right or top to bottom.
	 * @param index The index
	 * @param orientation The orientation (left to right or top to bottom)
	 * @return The sprite as a BufferedImage or null if the index is wrong
	 */
	public BufferedImage getSprite(final int index, final Orientation orientation) {
		int x = 0;
		int y = 0;
		
		if (orientation == Orientation.LEFT_TO_RIGHT) {
			x = index % spritesWide;
			y = index / spritesWide;
		} else if (orientation == Orientation.TOP_TO_BOTTOM) {
			x = index / spritesWide;
			y = index % spritesWide;
		}
		
		final BufferedImage image = getSprite(x, y);
		
		return image;
	}
	
	/**
	 * Returns the sprite at the given index and left to right orientation.
	 * @param index The index
	 * @return The sprite as a BufferedImage or null if the index is wrong
	 */
	public BufferedImage getSprite(final int index) {
		final BufferedImage image = getSprite(index, Orientation.LEFT_TO_RIGHT);
		
		return image;
	}
	
	/**
	 * Returns all sprites this sprite sheet holds.
	 * @return The sprites as an array
	 */
	public BufferedImage[] getSprites() {
		final BufferedImage[] images = new BufferedImage[spritesWide * spritesHigh];
		
		for (int x = 0; x < spritesWide; x++) {
			for (int y = 0; y < spritesHigh; y++) {
				final BufferedImage image = getSprite(x, y);
				final int index = x + y * spritesWide;
				
				images[index] = image;
			}
		}
		
		return images;
	}
	
	/**
	 * Holds the different orientations for the SpriteSheet class.
	 * @author Sogomn
	 *
	 */
	public enum Orientation {
		
		LEFT_TO_RIGHT,
		TOP_TO_BOTTOM;
		
	}
	
}
