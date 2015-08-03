package de.sogomn.engine;

import java.awt.Graphics2D;

/**
 * Defines an interface for objects that can be drawn.
 * This class is not used by the engine directly.
 * @author Sogomn
 *
 */
public interface IDrawable {
	
	/**
	 * Usually called to draw the object.
	 * @param g The Graphics2D object
	 */
	void draw(final Graphics2D g);
	
}
