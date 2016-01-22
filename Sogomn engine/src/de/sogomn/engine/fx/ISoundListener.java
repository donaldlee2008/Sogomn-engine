package de.sogomn.engine.fx;

/**
 * Defines an interface for sound listeners to be notified when a clip stops or finishes a loop.
 * @author Sogomn
 *
 */
public interface ISoundListener {
	
	/**
	 * Called when a sound finished one loop.
	 * @param source The calling Sound object
	 */
	void looped(final Sound source);
	
	/**
	 * Called when a sound stops.
	 * @param source The calling Sound object
	 */
	void stopped(final Sound source);
	
}
