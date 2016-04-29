/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.engine;

import java.awt.Graphics2D;

/**
 * Defines an interface for objects that can be drawn.
 * @author Sogomn
 *
 */
@FunctionalInterface
public interface IDrawable {
	
	/**
	 * Usually called to draw the object.
	 * @param g The Graphics2D object to be drawn on
	 */
	void draw(final Graphics2D g);
	
}
