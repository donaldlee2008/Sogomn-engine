package de.sogomn.engine.util;

import java.awt.image.BufferedImage;
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
	
}
