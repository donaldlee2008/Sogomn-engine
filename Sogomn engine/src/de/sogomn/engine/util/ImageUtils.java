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
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * This class holds some useful methods in terms of images.
 * @author Sogomn
 *
 */
public final class ImageUtils {
	
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
		} catch (final IOException ex) {
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
		} catch (final IOException ex) {
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
		
		final BufferedImage newImage = new BufferedImage(width, height, image.getType());
		final Graphics2D g = newImage.createGraphics();
		
		applyLowGraphics(g);
		
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		
		return newImage;
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
	
}
