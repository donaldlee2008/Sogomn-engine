package de.sogomn.engine;

import java.awt.Graphics2D;

/**
 * Defines an interface to be used for the Screen class and any object that "ticks".
 * @author Sogomn
 *
 */
public interface ITickable {
	
	/**
	 * The method to update the ITickable object
	 * @param delta The time passed in seconds since the last call
	 */
	void update(final float delta);
	
	/**
	 * The method to draw the ITickable object
	 * @param g The Graphics2D object
	 */
	void draw(final Graphics2D g);
	
}
