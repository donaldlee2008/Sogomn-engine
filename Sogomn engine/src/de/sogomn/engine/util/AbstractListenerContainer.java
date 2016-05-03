/*******************************************************************************
 * Copyright 2016 Johannes Boczek
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

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
	public void addListener(final T t) {
		synchronized (listeners) {
			listeners.add(t);
		}
	}
	
	/**
	 * Removes a listener from the container.
	 * @param t The listener
	 */
	public void removeListener(final T t) {
		synchronized (listeners) {
			listeners.remove(t);
		}
	}
	
	/**
	 * Removes all listeners from the container.
	 */
	public void removeAllListeners() {
		synchronized (listeners) {
			listeners.clear();
		}
	}
	
}
