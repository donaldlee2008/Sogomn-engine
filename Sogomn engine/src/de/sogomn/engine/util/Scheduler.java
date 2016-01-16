package de.sogomn.engine.util;

import de.sogomn.engine.IUpdatable;

/**
 * The Scheduler class can be used to schedule tasks. It is basically a timer.
 * The method "update" needs to be called regularly in order to work.
 * It will loop until "disable" is called.
 * @author Sogomn
 *
 */
public final class Scheduler extends AbstractListenerContainer<Runnable> implements IUpdatable {
	
	private float interval;
	private double timer;
	
	private boolean enabled;
	
	/**
	 * Constructs a new Scheduler which will execute the task after the given time.
	 * @param interval The interval the task should be executed in seconds.
	 */
	public Scheduler(final float interval) {
		this.interval = interval;
		
		enable();
	}
	
	private void notifyListeners() {
		for (final Runnable runnable : listeners) {
			runnable.run();
		}
	}
	
	/**
	 * Updates the scheduler.
	 */
	@Override
	public void update(final float delta) {
		if (!enabled) {
			return;
		}
		
		timer += delta;
		
		if (timer >= interval) {
			reset();
			notifyListeners();
		}
	}
	
	/**
	 * Enables the scheduler. It can now be updated.
	 */
	public void enable() {
		enabled = true;
	}
	
	/**
	 * Enables the scheduler. It can no longer be updated.
	 */
	public void disable() {
		enabled = false;
	}
	
	/**
	 * Resets the internal timer.
	 */
	public void reset() {
		timer = 0;
	}
	
	/**
	 * Sets the interval the task should be executed.
	 * @param interval The interval in seconds
	 */
	public void setInterval(final float interval) {
		this.interval = interval;
	}
	
	/**
	 * Returns the interval.
	 * @return The interval
	 */
	public float getInterval() {
		return interval;
	}
	
	/**
	 * Returns the internal timer in seconds.
	 * @return The timer
	 */
	public double getTimer() {
		return timer;
	}
	
	/**
	 * Return whether the scheduler is enabled or not.
	 * @return True if it is enabled; false otherwise
	 */
	public boolean isEnabled() {
		return enabled;
	}
	
}
