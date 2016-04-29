/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.engine.fx;

import java.awt.image.BufferedImage;

import de.sogomn.engine.util.ImageUtils;

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
		
		loadSprites(image);
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
	
	private void loadSprites(final BufferedImage image) {
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
	 * Returns the sprite at the given index.
	 * The orientation may either be left to right or top to bottom.
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
	 * Returns the sprites at the given indices as an array.
	 * The orientation may either be left to right or top to bottom.
	 * @param orientation The orientation (left to right or top to bottom)
	 * @param indices The indices
	 * @return The sprites as an array
	 */
	public BufferedImage[] getSprites(final Orientation orientation, final int... indices) {
		final int length = indices.length;
		final BufferedImage[] images = new BufferedImage[length];
		
		for (int i = 0; i < length; i++) {
			final int index = indices[i];
			
			images[i] = getSprite(index, orientation);
		}
		
		return images;
	}
	
	/**
	 * Returns the sprites at the given indices as an array.
	 * The orientation is left to right.
	 * @param indices The indices
	 * @return The sprites as an array
	 */
	public BufferedImage[] getSprites(final int... indices) {
		return getSprites(Orientation.LEFT_TO_RIGHT, indices);
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
	 * Returns the width of each sprite.
	 * @return The width
	 */
	public int getSpriteWidth() {
		return spriteWidth;
	}
	
	/**
	 * Returns the height of each sprite.
	 * @return The height
	 */
	public int getSpriteHeight() {
		return spriteHeight;
	}
	
	/**
	 * Returns the amount of sprites per row.
	 * @return The sprites per row
	 */
	public int getSpritesWide() {
		return spritesWide;
	}
	
	/**
	 * Returns the amount of sprites per column.
	 * @return The sprites per column
	 */
	public int getSpritesHigh() {
		return spritesHigh;
	}
	
	/**
	 * Holds the different orientations for the SpriteSheet class.
	 * @author Sogomn
	 *
	 */
	public enum Orientation {
		
		/**
		 * Left-to-right orientation.
		 */
		LEFT_TO_RIGHT,
		
		/**
		 * Top-to-bottom orientation.
		 */
		TOP_TO_BOTTOM;
		
	}
	
}
