package de.sogomn.engine.util;

import java.util.ArrayList;
import java.util.Iterator;

import de.sogomn.engine.IUpdatable;

/**
 * The Scheduler class can be used to schedule tasks.
 * The method "update" needs to be called regularly in order to work.
 * Does not use a separate thread. All methods are synchronized with the internal list.
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
		synchronized (tasks) {
			final Iterator<Task> iterator = tasks.iterator();
			
			while (iterator.hasNext()) {
				final Task task = iterator.next();
				
				if (task.isDone()) {
					iterator.remove();
					
					task.execute();
				}
				
				task.update(delta);
			}
		}
	}
	
	/**
	 * Removes all tasks from the schedule.
	 */
	public void clearTasks() {
		synchronized (tasks) {
			tasks.clear();
		}
	}
	
	/**
	 * Adds a task to the schedule.
	 * @param task The task
	 */
	public void addTask(final Task task) {
		synchronized (tasks) {
			tasks.add(task);
		}
	}
	
	/**
	 * Removes a task from the schedule.
	 * @param task The task to be removed
	 */
	public void removeTask(final Task task) {
		synchronized (tasks) {
			tasks.remove(task);
		}
	}
	
	/**
	 * Returns whether the scheduler has a task scheduled or not.
	 * @return True if there is a task scheduled; false otherwise.
	 */
	public boolean hasTask() {
		synchronized (tasks) {
			return !tasks.isEmpty();
		}
	}
	
	/**
	 * Can be scheduled with the help of the Scheduler class
	 * @author Sogomn
	 *
	 */
	public static final class Task {
		
		private Runnable runnable;
		private double timer;
		private float time;
		
		/**
		 * Constructs a new Task object.
		 * @param executable The method "execute" will be called when the task gets executed
		 * @param time The time the task should be executed after
		 */
		public Task(final Runnable runnable, final float time) {
			this.runnable = runnable;
			this.time = time;
		}
		
		/**
		 * Updates the task. Automatically called by the Scheduler class.
		 */
		public void update(final double delta) {
			timer += delta;
		}
		
		/**
		 * Returns the time the task should be executed after.
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
