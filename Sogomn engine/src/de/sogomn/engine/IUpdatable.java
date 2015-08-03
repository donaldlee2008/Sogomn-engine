package de.sogomn.engine;

/**
 * Defines an interface for objects that can be updated.
 * This class is not used by the engine directly.
 * @author Sogomn
 *
 */
public interface IUpdatable {
	
	/**
	 * Usually called to update the object
	 * @param delta The time passed in seconds since the last call
	 */
	void update(final float delta);
	
}
