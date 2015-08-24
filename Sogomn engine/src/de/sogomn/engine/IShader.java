package de.sogomn.engine;

/**
 * Defines an interface for shaders to be applied to an integer raster of pixels.
 * @author Sogomn
 *
 */
public interface IShader {
	
	/**
	 * Called when the shader is applied to an pixel raster.
	 * @param pixelRaster The integer raster of pixels
	 */
	void apply(final int[] pixelRaster);
	
}
