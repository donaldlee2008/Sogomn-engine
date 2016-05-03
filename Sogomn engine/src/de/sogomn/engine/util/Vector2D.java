/*******************************************************************************
 * Copyright 2016 Johannes Boczek
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package de.sogomn.engine.util;


/**
 * This class defines a vector. There are several methods to make calculations easier.
 * All methods (exept "add", "set", ...) return a copy instead of the original object.
 * @author Sogomn
 *
 */
public final strictfp class Vector2D {
	
	private double x, y;
	
	/**
	 * Constructs a new vector with the given x and y coordinates.
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public Vector2D(final double x, final double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Constructs a new empty vector (0|0).
	 */
	public Vector2D() {
		this(0, 0);
	}
	
	/**
	 * Constructs a new vector with the given value for both coordinates.
	 * @param value The value for both coordinates
	 */
	public Vector2D(final double value) {
		this(value, value);
	}
	
	/**
	 * Returns a string containing the coordinates and the length of the vector.
	 * Keep in mind that the length will be calculated first.
	 */
	@Override
	public String toString() {
		final String string = "[" + x + " | " + y + "] - Length: " + length();
		
		return string;
	}
	
	/**
	 * Calculates the dot product of the passed vector and the calling one.
	 * @param other The other vector
	 * @return The dot product
	 */
	public double dot(final Vector2D other) {
		return dot(this, other);
	}
	
	/**
	 * Returns a rotated copy of the vector.
	 * @param degrees The angle of the rotation in degrees
	 * @return A roated copy of the vector
	 */
	public Vector2D rotate(final double degrees) {
		return rotate(this, degrees);
	}
	
	/**
	 * Returns a normalized copy of the vector.
	 * @return The normalized copy of the vector
	 */
	public Vector2D normalize() {
		return normalize(this);
	}
	
	/**
	 * Calculates the length of the vector.
	 * @return The length
	 */
	public double length() {
		return length(this);
	}
	
	/**
	 * Calculates the distance vector to another one
	 * @param other The other vector
	 * @return The distance between the two vectors
	 */
	public Vector2D distance(final Vector2D other) {
		return distance(this, other);
	}
	
	/**
	 * Adds the given amount to the x coordinate.
	 * @param x The amout to be added
	 */
	public void addX(final double x) {
		this.x += x;
	}
	
	/**
	 * Adds the given amount to the y coordinate.
	 * @param y The amout to be added
	 */
	public void addY(final double y) {
		this.y += y;
	}
	
	/**
	 * Sets the x coordinate
	 * @param x The x coordinate
	 */
	public void setX(final double x) {
		this.x = x;
	}
	
	/**
	 * Sets the y coordinate
	 * @param y The y coodinate
	 */
	public void setY(final double y) {
		this.y = y;
	}
	
	/**
	 * Sets the x and y coordinates.
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public void set(final double x, final double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the x coordinate
	 * @return The x coordinate
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Returns the y coordinate
	 * @return The y coordinate
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Calculates the dot product of the given vectors.
	 * @param one The first vector
	 * @param two The second vector
	 * @return The dot product
	 */
	public static double dot(final Vector2D one, final Vector2D two) {
		final double value = one.x * two.x + one.y * two.y;
		
		return value;
	}
	
	/**
	 * Returns a rotated copy of the given vector.
	 * @param vector The vector to be rotated
	 * @param degrees The angle in degrees the vector should be rotated
	 * @return A rotated copy of the vector
	 */
	public static Vector2D rotate(final Vector2D vector, final double degrees) {
		final double radians = Math.toRadians(degrees);
		final double newX = vector.x * Math.cos(radians) - vector.y * Math.sin(radians);
		final double newY = vector.y * Math.cos(radians) + vector.x * Math.sin(radians);
		final Vector2D result = new Vector2D(newX, newY);
		
		return result;
	}
	
	/**
	 * Returns a normalized copy of the given vector.
	 * @param vector The vector
	 * @return A normalized copy of the given vector
	 */
	public static Vector2D normalize(final Vector2D vector) {
		final double length = length(vector);
		final double newX = vector.x / length;
		final double newY = vector.y / length;
		final Vector2D result = new Vector2D(newX, newY);
		
		return result;
	}
	
	/**
	 * Calculates the length of the given vector.
	 * @param vector The vector
	 * @return The length
	 */
	public static double length(final Vector2D vector) {
		final double length = Math.sqrt(vector.x * vector.x + vector.y * vector.y);
		
		return length;
	}
	
	/**
	 * Calculates the distance between two vectors.
	 * The fist vector gets subtracted from the second one.
	 * @param one The first vector
	 * @param two The second vector
	 * @return The distance vector
	 */
	public static Vector2D distance(final Vector2D one, final Vector2D two) {
		final double distanceX = two.x - one.x;
		final double distanceY = two.y - one.y;
		final Vector2D distance = new Vector2D(distanceX, distanceY);
		
		return distance;
	}
	
}
