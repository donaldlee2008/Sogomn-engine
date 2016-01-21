package de.sogomn.engine.util;

import java.util.ArrayList;

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
	
	private ArrayList<Task> tasks;
	private Task currentTask;
	
	/**
	 * Constructs a new Scheduler which can execute tasks.
	 */
	public Scheduler() {
		tasks = new ArrayList<Task>();
	}
	
	private boolean isDone(final Task task) {
		if (timer >= task.time) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Updates the scheduler.
	 */
	@Override
	public void update(final double delta) {
		timer += delta;
		
		if (currentTask != null && isDone(currentTask)) {
			currentTask.runnable.run();
			removeTask(currentTask);
		}
	}
	
	/**
	 * Removes all tasks from the schedule.
	 */
	public void clearTasks() {
		currentTask = null;
		tasks.clear();
	}
	
	/**
	 * Adds a task to the schedule.
	 * @param task The task
	 */
	public void addTask(final Task task) {
		tasks.add(task);
		
		if (currentTask == null) {
			timer = 0;
			currentTask = task;
		}
	}
	
	/**
	 * Removes a task from the schedule.
	 * @param task The task to be removed
	 */
	public void removeTask(final Task task) {
		tasks.remove(task);
		
		if (currentTask == task) {
			timer = 0;
			currentTask = getNextTask();
		}
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
	public Task getNextTask() {
		if (!hasTask()) {
			return null;
		}
		
		final Task task = tasks.get(0);
		
		return task;
	}
	
	/**
	 * Returns whether the scheduler has a task scheduled or not.
	 * @return True if there is a task scheduled; false otherwise.
	 */
	public boolean hasTask() {
		return !tasks.isEmpty();
	}
	
	/**
	 * Can be scheduled with the help of the Scheduler class.
	 * @author Sogomn
	 *
	 */
	public static class Task {
		
		private Runnable runnable;
		private float time;
		
		/**
		 * Constructs a new Task object.
		 * @param runnable The method "run" will be called when the task gets executed
		 * @param time The time the task should be executed after
		 */
		public Task(final Runnable runnable, final float time) {
			this.runnable = runnable;
			this.time = time;
		}
		
		/**
		 * Returns the time the task should be executed after.
		 * @return The time
		 */
		public final float getTime() {
			return time;
		}
		
	}
	
}
