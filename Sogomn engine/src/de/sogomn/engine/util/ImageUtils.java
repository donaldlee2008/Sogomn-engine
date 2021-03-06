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

package de.sogomn.engine.util;

import static java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.KEY_INTERPOLATION;
import static java.awt.RenderingHints.KEY_RENDERING;
import static java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY;
import static java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_OFF;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC;
import static java.awt.RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
import static java.awt.RenderingHints.VALUE_RENDER_QUALITY;
import static java.awt.RenderingHints.VALUE_RENDER_SPEED;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

/**
 * This class holds some useful methods in terms of images.
 * @author Sogomn
 *
 */
public final class ImageUtils {
	
	/**
	 * A one pixel small transparent image.
	 */
	public static final BufferedImage EMPTY_IMAGE = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	
	private ImageUtils() {
		//...
	}
	
	/**
	 * Loads an image from the classpath. The path should be prefixed with a slash ('/').
	 * @param path The path to the image
	 * @return The image or null in case of faliure
	 */
	public static BufferedImage loadImage(final String path) {
		try {
			final BufferedImage image = ImageIO.read(ImageUtils.class.getResource(path));
			
			return image;
		} catch (final Exception ex) {
			ex.printStackTrace();
			
			return null;
		}
	}
	
	/**
	 * Loads an image from the given file.
	 * @param file The file to load the image from
	 * @return The image or null in case of faliure
	 */
	public static BufferedImage loadExternalImage(final File file) {
		try {
			final BufferedImage image = ImageIO.read(file);
			
			return image;
		} catch (final Exception ex) {
			ex.printStackTrace();
			
			return null;
		}
	}
	
	/**
	 * Loads an image from the given path.
	 * @param path The path to the image
	 * @return The image or null in case of faliure
	 */
	public static BufferedImage loadExternalImage(final String path) {
		final File file = new File(path);
		final BufferedImage image = loadExternalImage(file);
		
		return image;
	}
	
	/**
	 * Creates a copy of the passed image with the given width and height.
	 * If width and height of both images are the same, the passed image is returned.
	 * This method does not use interpolation.
	 * @param image The image to be scaled
	 * @param width The target width
	 * @param height The target height
	 * @return A scaled copy of the image
	 */
	public static BufferedImage scaleImage(final BufferedImage image, final int width, final int height) {
		if (image.getWidth() == width && image.getHeight() == height) {
			return image;
		}
		
		final int type = image.getType();
		final BufferedImage newImage = new BufferedImage(width, height, type);
		final Graphics2D g = newImage.createGraphics();
		
		applyLowGraphics(g);
		
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		
		return newImage;
	}
	
	/**
	 * Creates a copy of the passed image scaled by the passed factor.
	 * If the scale is 1 then the original image is returned.
	 * This method does not use interpolation.
	 * @param image The image to be scaled
	 * @param scale The scale
	 * @return A scaled copy of the image
	 */
	public static BufferedImage scaleImage(final BufferedImage image, final float scale) {
		if (scale == 1) {
			return image;
		}
		
		final int width = (int)(image.getWidth() * scale);
		final int height = (int)(image.getHeight() * scale);
		
		return scaleImage(image, width, height);
	}
	
	/**
	 * Applies low graphics settings to the given Graphics2D object.
	 * - Antialiasing: OFF
	 * - Rendering: SPEED
	 * - Interpolation: NEAREST_NEIGHBOUR
	 * - Alpha interpolation: SPEED
	 * @param g The Graphics2D object
	 */
	public static void applyLowGraphics(final Graphics2D g) {
		g.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_OFF);
		g.setRenderingHint(KEY_RENDERING, VALUE_RENDER_SPEED);
		g.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g.setRenderingHint(KEY_ALPHA_INTERPOLATION, VALUE_ALPHA_INTERPOLATION_SPEED);
	}
	
	/**
	 * Applies high graphics settings to the given Graphics2D object.
	 * Slows down the rendering process.
	 * - Antialiasing: ON
	 * - Rendering: QUALITY
	 * - Interpolation: BICUBIC
	 * - Alpha interpolation: QUALITY
	 * @param g The Graphics2D object
	 */
	public static void applyHighGraphics(final Graphics2D g) {
		g.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
		g.setRenderingHint(KEY_RENDERING, VALUE_RENDER_QUALITY);
		g.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(KEY_ALPHA_INTERPOLATION, VALUE_ALPHA_INTERPOLATION_QUALITY);
	}
	
	/**
	 * Converts an image to a byte array.
	 * @param image The image to convert
	 * @param format The image format
	 * @return The image data
	 */
	public static byte[] toByteArray(final BufferedImage image, final String format) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		try {
			ImageIO.write(image, "PNG", out);
		} catch (final IOException ex) {
			ex.printStackTrace();
		}
		
		final byte[] data = out.toByteArray();
		
		return data;
	}
	
	/**
	 * Converts an image to a byte array.
	 * Uses the PNG image format.
	 * @param image The image to convert
	 * @return The image data
	 */
	public static byte[] toByteArray(final BufferedImage image) {
		return toByteArray(image, "PNG");
	}
	
	/**
	 * Converts an image to a byte array.
	 * Uses the JPEG image format.
	 * The quality can be between 1 (best) and 0 (highest compression)
	 * @param image The image that should be converted
	 * @param quality The quality of the image
	 * @return The image as a byte array
	 */
	public static byte[] toByteArray(final BufferedImage image, final float quality) {
		final Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("JPG");
		final ImageWriter writer = writers.next();
		final ImageWriteParam param = writer.getDefaultWriteParam();
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(quality);
		
		try {
			final ImageOutputStream imageOut = ImageIO.createImageOutputStream(out);
			
			writer.setOutput(imageOut);
			writer.write(image);
			
			return out.toByteArray();
		} catch (final IOException ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Constructs an image from a byte array.
	 * @param data The image data
	 * @return The image
	 */
	public static BufferedImage toImage(final byte[] data) {
		final ByteArrayInputStream in = new ByteArrayInputStream(data);
		
		try {
			return ImageIO.read(in);
		} catch (final IOException ex) {
			ex.printStackTrace();
			
			return null;
		}
	}
	
	/**
	 * Converts a BufferedImage object to the specified type.
	 * This creates a copy and does not modify the original.
	 * @param image The image to be converted.
	 * @param type The new image type
	 * @return The converted image
	 */
	public static BufferedImage convert(final BufferedImage image, final int type) {
		final int width = image.getWidth();
		final int height = image.getHeight();
		final BufferedImage newImage = new BufferedImage(width, height, type);
		final Graphics2D g = newImage.createGraphics();
		
		g.drawImage(image, 0, 0, null);
		g.dispose();
		
		return newImage;
	}
	
	/**
	 * Converts an image to a shape.
	 * Every pixel that has a non-zero alpha value gets added to the shape.
	 * Does not change the image in any way.
	 * @param image The image to be converted
	 * @return The shape
	 */
	public static Shape getShape(final BufferedImage image) {
		final Area area = new Area();
		final int width = image.getWidth();
		final int height = image.getHeight();
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				final int argb = image.getRGB(x, y);
				final int alpha = (argb >> 24) & 0xff;
				
				if (alpha != 0) {
					final Rectangle rectangle = new Rectangle(x, y, 1, 1);
					final Area point = new Area(rectangle);
					
					area.add(point);
				}
			}
		}
		
		return area;
	}
	
}
