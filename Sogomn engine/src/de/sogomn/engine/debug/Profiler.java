package de.sogomn.engine.debug;

import java.io.PrintStream;
import java.util.LinkedList;

import de.sogomn.engine.Clock;

/**
 * The Profiler class is useful for debugging. It measures how much time one or more operations take.
 * @author Sogomn
 *
 */
public final class Profiler {
	
	private PrintStream out;
	
	private Clock clock;
	private LinkedList<Section> sections;
	private boolean profiling;
	
	private String lastName;
	
	/**
	 * Constructs a Profiler object.
	 * @param out The results will be printed to this stream
	 */
	public Profiler(final PrintStream out) {
		this.out = out;
		
		clock = new Clock();
		sections = new LinkedList<Section>();
	}
	
	private void newSection() {
		final double elapsed = clock.update();
		final Section section = new Section(lastName, elapsed);
		
		sections.add(section);
	}
	
	private void printSections() {
		final double totalTime = clock.elapsed();
		
		for (final Section section : sections) {
			out.println(section);
		}
		
		out.println("Total - " + totalTime);
	}
	
	/**
	 * Starts a new section of the profiler.
	 * If this is the first section, it also starts profiling.
	 * @param name The name of the section
	 */
	public void startSection(final String name) {
		if (!profiling) {
			profiling = true;
			clock.reset();
		} else {
			newSection();
		}
		
		lastName = name;
	}
	
	/**
	 * Ends profiling and prints the results to the passed PrintStream.
	 */
	public void end() {
		if (!profiling) {
			return;
		}
		
		newSection();
		
		profiling = false;
		lastName = null;
		
		clock.reset();
		printSections();
		sections.clear();
	}
	
	private final class Section {
		
		private final String name;
		private final double time;
		
		public Section(final String name, final double time) {
			this.name = name;
			this.time = time;
		}
		
		@Override
		public String toString() {
			return name + " - " + time;
		}
		
	}
	
}
