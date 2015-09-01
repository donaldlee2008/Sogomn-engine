package de.sogomn.engine;

/**
 * A class to be used for continuous updating (e.g. for a game loop).
 * It is recommended to call the "update" method in a regular interval.
 * @author Sogomn
 *
 */
public final class Clock extends AbstractListenerContainer<IUpdatable> {
	
	private long initialTime;
	private long lastTime;
	private long ticks;
	
	private static final float NANO_SECONDS_PER_SECOND = 1000000000.0f;
	
	/**
	 * Constructs a new Clock object.
	 */
	public Clock() {
		initialTime = lastTime = System.nanoTime();
		
		reset();
	}
	
	private void updateUpdatables(final float delta) {
		synchronized (listeners) {
			for (int i = 0; i < size(); i++) {
				final IUpdatable updatable = listeners.get(i);
				
				updatable.update(delta);
			}
		}
	}
	
	/**
	 * Updates the clock and notifies all listening IUpdatable objects.
	 * The passed value "delta" is the elapsed time in seconds.
	 */
	public void update() {
		final long now = System.nanoTime();
		final float elapsedInSeconds = (now - lastTime) / NANO_SECONDS_PER_SECOND;
		
		updateUpdatables(elapsedInSeconds);
		
		ticks++;
	}
	
	/**
	 * Resets the tick counter and the starting time of the clock.
	 */
	public void reset() {
		initialTime = System.nanoTime();
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
	 * Returns the elapsed time since the clock was started in seconds.
	 * @return The elapsed time
	 */
	public float elapsed() {
		final float elapsed = (System.nanoTime() - initialTime) / NANO_SECONDS_PER_SECOND;
		
		return elapsed;
	}
	
}
