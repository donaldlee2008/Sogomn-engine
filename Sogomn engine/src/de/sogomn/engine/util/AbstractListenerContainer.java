package de.sogomn.engine.util;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * An abstract class that represents a container to hold multiple instances of one type.
 * Useful for listeners.
 * All methods are synchronized with the internal ArrayList.
 * @author Sogomn
 *
 * @param <T> The object type the container should hold
 */
public abstract class AbstractListenerContainer<T> {
	
	private ArrayList<T> listeners;
	
	/**
	 * Initializes the listener list.
	 */
	public AbstractListenerContainer() {
		listeners = new ArrayList<T>();
	}
	
	protected void notifyListeners(final Consumer<? super T> consumer) {
		synchronized (listeners) {
			for (int i = 0; i < listeners.size(); i++) {
				final T listener = listeners.get(i);
				
				consumer.accept(listener);
			}
		}
	}
	
	/**
	 * Adds a listener to the container.
	 * @param t The listener
	 */
	public final void addListener(final T t) {
		synchronized (listeners) {
			listeners.add(t);
		}
	}
	
	/**
	 * Removes a listener from the container.
	 * @param t The listener
	 */
	public final void removeListener(final T t) {
		synchronized (listeners) {
			listeners.remove(t);
		}
	}
	
	/**
	 * Removes all listeners from the container.
	 */
	public final void removeAllListeners() {
		synchronized (listeners) {
			listeners.clear();
		}
	}
	
}
