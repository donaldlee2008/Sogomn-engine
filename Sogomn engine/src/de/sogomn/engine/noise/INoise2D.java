package de.sogomn.engine.noise;

/**
 * Defines an interface for 2D noise algorithms.
 * @author Sogomn
 *
 */
@FunctionalInterface
public interface INoise2D {
	
	/**
	 * Returns a noise value relative to the given x and y coordinates.
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @return The noise value
	 */
	double getValue(final double x, final double y);
	
}
