package de.sogomn.engine.util;

/**
 * Defines an interface for sound listeners to be notified when a clip stops.
 * @author Sogomn
 *
 */
@FunctionalInterface
public interface ISoundListener {
	
	/**
	 * Called when a sound clip stops.
	 * @param source The calling Sound object
	 * @param id The clip id
	 */
	void stopped(final Sound source, final long id);
	
}
