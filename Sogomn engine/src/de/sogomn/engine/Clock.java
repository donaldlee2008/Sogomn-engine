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
