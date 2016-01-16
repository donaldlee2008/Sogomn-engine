package de.sogomn.engine.util;

import java.util.LinkedHashMap;

import de.sogomn.engine.IUpdatable;

/**
 * The Scheduler class can be used to schedule tasks.
 * The method "update" needs to be called regularly in order to work.
 * It will execute the tasks one after another.
 * @author Sogomn
 *
 */
public final class Scheduler implements IUpdatable {
	
	private double timer;
	
	private LinkedHashMap<Runnable, Float> tasks;
	private Runnable currentTask;
	
	/**
	 * Constructs a new Scheduler which can execute tasks.
	 */
	public Scheduler() {
		tasks = new LinkedHashMap<Runnable, Float>();
	}
	
	private boolean isCurrentTaskDone() {
		if (currentTask == null) {
			return false;
		}
		
		final float time = tasks.get(currentTask);
		
		if (timer >= time) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Updates the scheduler.
	 */
	@Override
	public void update(final float delta) {
		timer += delta;
		
		if (isCurrentTaskDone()) {
			currentTask.run();
			removeTask(currentTask);
			reset();
			currentTask = getNextTask();
		}
	}
	
	/**
	 * Resets the internal timer.
	 */
	public void reset() {
		timer = 0;
	}
	
	/**
	 * Adds a task to the schedule.
	 * @param task The task
	 * @param time The time the task should be executed in seconds
	 */
	public void addTask(final Runnable task, final float time) {
		tasks.put(task, time);
		
		if (currentTask == null) {
			currentTask = getNextTask();
		}
	}
	
	/**
	 * Removes a task from the schedule.
	 * @param task The task to be removed
	 */
	public void removeTask(final Runnable task) {
		tasks.remove(task);
	}
	
	/**
	 * Returns the internal timer in seconds.
	 * @return The timer
	 */
	public double getTimer() {
		return timer;
	}
	
	/**
	 * Returns the next task to be executed.
	 * @return The task
	 */
	public Runnable getNextTask() {
		if (tasks.isEmpty()) {
			return null;
		}
		
		final Runnable task = tasks.keySet().iterator().next();
		
		return task;
	}
	
}
