package de.sogomn.engine.util;

import java.util.ArrayList;

import de.sogomn.engine.IUpdatable;

/**
 * The Scheduler class can be used to schedule tasks.
 * The method "update" needs to be called regularly in order to work.
 * Does not use a separate thread.
 * @author Sogomn
 *
 */
public final class Scheduler implements IUpdatable {
	
	private ArrayList<Task> tasks;
	
	/**
	 * Constructs a new Scheduler which can execute tasks.
	 */
	public Scheduler() {
		tasks = new ArrayList<Task>();
	}
	
	/**
	 * Updates the scheduler.
	 */
	@Override
	public void update(final double delta) {
		final int size = tasks.size();
		
		for (int i = 0; i < size; i++) {
			final Task task = tasks.get(i);
			
			task.update(delta);
			
			if (task.isDone()) {
				task.execute();
			}
		}
		
		tasks.removeIf(Task::isDone);
	}
	
	/**
	 * Removes all tasks from the schedule.
	 */
	public void clearTasks() {
		tasks.clear();
	}
	
	/**
	 * Adds a task to the schedule.
	 * @param task The task
	 */
	public void addTask(final Task task) {
		tasks.add(task);
	}
	
	/**
	 * Removes a task from the schedule.
	 * @param task The task to be removed
	 */
	public void removeTask(final Task task) {
		tasks.remove(task);
	}
	
	/**
	 * Returns whether the scheduler has a task scheduled or not.
	 * @return True if there is a task scheduled; false otherwise.
	 */
	public boolean hasTask() {
		return !tasks.isEmpty();
	}
	
	/**
	 * Can be scheduled with the help of the Scheduler class
	 * @author Sogomn
	 *
	 */
	public static final class Task implements IUpdatable {
		
		private Runnable runnable;
		private double timer;
		private float time;
		
		/**
		 * Constructs a new Task object.
		 * @param runnable The method "execute" will be called when the task gets executed
		 * @param time The time the task should be executed after in seconds
		 */
		public Task(final Runnable runnable, final float time) {
			this.runnable = runnable;
			this.time = time;
		}
		
		/**
		 * Updates the task.
		 */
		@Override
		public void update(final double delta) {
			timer += delta;
		}
		
		/**
		 * Resets the internal timer of the task.
		 * If this method is called on a task's execution, the task will reschedule.
		 */
		public void reset() {
			timer = 0;
		}
		
		/**
		 * Returns the time the task should be executed after in seconds.
		 * @return The time
		 */
		public float getTime() {
			return time;
		}
		
		/**
		 * Returns whether this Task is done or not.
		 * @return The state
		 */
		public boolean isDone() {
			return timer >= time;
		}
		
		/**
		 * Executes this task. Ignores the timer.
		 */
		public void execute() {
			runnable.run();
		}
		
	}
	
}
