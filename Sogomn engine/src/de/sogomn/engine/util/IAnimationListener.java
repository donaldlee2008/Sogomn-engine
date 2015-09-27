package de.sogomn.engine.util;

/**
 * Defines an interface for an animation listener which can react to animation loops.
 * @author Sogomn
 *
 */
@FunctionalInterface
public interface IAnimationListener {
	
	/**
	 * Called when an animation loop has finished.
	 * @param source The calling Animation object
	 */
	void looped(final Animation source);
	
}
