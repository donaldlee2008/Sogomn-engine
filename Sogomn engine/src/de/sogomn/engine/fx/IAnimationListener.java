/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.engine.fx;

/**
 * Defines an interface for an animation listener which can react to animation loops.
 * @author Sogomn
 *
 */
public interface IAnimationListener {
	
	/**
	 * Called when an animation loop has finished.
	 * @param source The calling Animation object
	 */
	void looped(final Animation source);
	
}
