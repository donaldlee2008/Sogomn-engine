/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.engine.debug;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An interface to indicate that something has been hardcoded.
 * The retention is source only.
 * This class is completely useless.
 * Makes your code more fun to read, tho.
 * @author Sogomn
 *
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Hardcoded {
	
	/**
	 * The excuse why you hardcoded the values.
	 * @return The excuse
	 */
	String value();
	
}
