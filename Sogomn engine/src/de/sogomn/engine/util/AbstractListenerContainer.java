package de.sogomn.engine.util;

import java.util.ArrayList;

/**
 * An abstract class that represents a container to hold multiple instances of one type.
 * All business methods are synchonized with the item list. Therefore it is useful for observables.
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
	public synchronized void addListener(final T t) {
		synchronized (listeners) {
			listeners.add(t);
		}
	}
	
	/**
	 * Removes a listener from the container.
	 * @param t The listener
	 */
	public synchronized void removeListener(final T t) {
		synchronized (listeners) {
			listeners.remove(t);
		}
	}
	
	/**
	 * Returns the size of the container.
	 * @return The size
	 */
	public synchronized int getListenerCount() {
		synchronized (listeners) {
			return listeners.size();
		}
	}
	
}
