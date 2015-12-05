package de.sogomn.engine.util;

import java.util.ArrayList;

/**
 * An abstract class that represents a container to hold multiple instances of one type.
 * Useful for listeners.
 * Not synchronized.
 * @author Sogomn
 *
 * @param <T> The object type the container should hold
 */
public abstract class AbstractListenerContainer<T> {
	
	/**
	 * The listener list.
	 */
	protected ArrayList<T> listeners;
	
	/**
	 * Initializes the listener list.
	 */
	public AbstractListenerContainer() {
		listeners = new ArrayList<T>();
	}
	
	/**
	 * Adds a listener to the container.
	 * @param t The listener
	 */
	public synchronized final void addListener(final T t) {
		listeners.add(t);
	}
	
	/**
	 * Removes a listener from the container.
	 * @param t The listener
	 */
	public synchronized final void removeListener(final T t) {
		listeners.remove(t);
	}
	
	/**
	 * Returns the size of the container.
	 * @return The size
	 */
	public synchronized final int getListenerCount() {
		return listeners.size();
	}
	
}
