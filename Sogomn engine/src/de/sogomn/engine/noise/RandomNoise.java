/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.engine.noise;

import java.util.Random;

/**
 * This class generates random values which are independent of the input.
 * @author Sogomn
 *
 */
public final class RandomNoise implements INoise2D {
	
	private Random random;
	
	/**
	 * Constructs a new RandomNoise object with a random seed.
	 */
	public RandomNoise() {
		random = new Random();
	}
	
	/**
	 * Constructs a new RandomNoise object with the specified seed.
	 * @param seed The seed
	 */
	public RandomNoise(final long seed) {
		this();
		
		random.setSeed(seed);
	}
	
	/**
	 * Generates a completely random value.
	 * @return A random value from 0 (inclusive) to 1 (exclusive)
	 */
	@Override
	public double getValue(final double x, final double y) {
		final double value = random.nextDouble();
		
		return value;
	}
	
	/**
	 * Sets the seed for this noise.
	 * @param seed The new seed
	 */
	public void setSeed(final long seed) {
		random.setSeed(seed);
	}
	
}
