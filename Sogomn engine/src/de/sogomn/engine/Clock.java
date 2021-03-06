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

package de.sogomn.engine;

import de.sogomn.engine.util.AbstractListenerContainer;

/**
 * A class to be used for continuous updating (e.g. for a game loop).
 * It is recommended to call the "update" method in a regular interval.
 * @author Sogomn
 *
 */
public final class Clock extends AbstractListenerContainer<IUpdatable> {
	
	private long initialTime, lastTime;
	private long ticks;
	
	private static final double NANO_SECONDS_PER_SECOND = 1000000000f;
	
	/**
	 * Constructs a new Clock object.
	 */
	public Clock() {
		reset();
	}
	
	/**
	 * Updates the clock and notifies all listeners.
	 * @return The elapsed time since the last update in seconds
	 */
	public double update() {
		final long now = System.nanoTime();
		final double elapsed = (System.nanoTime() - lastTime) / NANO_SECONDS_PER_SECOND;
		
		notifyListeners(updatable -> updatable.update(elapsed));
		
		lastTime = now;
		ticks++;
		
		return elapsed;
	}
	
	/**
	 * Resets the tick counter and the starting time of the clock.
	 */
	public void reset() {
		initialTime = lastTime = System.nanoTime();
		ticks = 0;
	}
	
	/**
	 * Returns the tick counter.
	 * @return The tick counter
	 */
	public long tickCount() {
		return ticks;
	}
	
	/**
	 * Returns the elapsed time since the clock was started.
	 * @return The elapsed time in seconds
	 */
	public double elapsed() {
		final double elapsed = (System.nanoTime() - initialTime) / NANO_SECONDS_PER_SECOND;
		
		return elapsed;
	}
	
	/**
	 * Returns the elapsed time since the last update.
	 * @return The elapsed time in seconds
	 */
	public double elapsedSinceLastUpdate() {
		final double elapsed = (System.nanoTime() - lastTime) / NANO_SECONDS_PER_SECOND;
		
		return elapsed;
	}
	
}
