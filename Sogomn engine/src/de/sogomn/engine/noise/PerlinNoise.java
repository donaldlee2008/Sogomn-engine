package de.sogomn.engine.noise;

import java.util.Random;

import de.sogomn.engine.util.Vector2D;

/**
 * This class has a grid of 2D vectors of the length 1 which are rotated randomly.
 * The noise values of a point in the 2D space can then be calculated.
 * @author Sogomn
 *
 */
public final class PerlinNoise implements INoise2D {
	
	private int gridWidth, gridHeight;
	private Vector2D[][] grid;
	
	private double contrast;
	
	private Random ran;
	
	private static final double VECTOR_LENGTH = 1.0;
	private static final double DEFAULT_CONTRAST = 1.0;
	
	/**
	 * Constructs a new PerlinNoise object with the given grid width and height.
	 * @param gridWidth The grid width
	 * @param gridHeight The grid height
	 */
	public PerlinNoise(final int gridWidth, final int gridHeight) {
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		
		grid = new Vector2D[gridWidth][gridHeight];
		contrast = DEFAULT_CONTRAST;
		ran = new Random();
		
		randomizeGrid();
	}
	
	/**
	 * Constructs a new PerlinNoise object with the given grid width and height and a given seed.
	 * @param gridWidth The grid width
	 * @param gridHeight The grid height
	 * @param seed The seed
	 */
	public PerlinNoise(final int gridWidth, final int gridHeight, final long seed) {
		this(gridWidth, gridHeight);
		
		ran.setSeed(seed);
	}
	
	/*Interpolation function ripped from here: http://freespace.virgin.net/hugo.elias/models/m_perlin.htm*/
	private double interpolate(final double one, final double two, final double weight) {
		final double advancedWeight = (1 - Math.cos(weight * Math.PI)) / 2;
		final double value = one * (1 - advancedWeight) + two * advancedWeight;
		
		return value;
	}
	
	/**
	 * Returns the noise value at the given point.
	 * The value is the relation of the nearest four grid vectors and the distance of the given point to those.
	 */
	@Override
	public double getValue(final double x, final double y) {
		/*Grid coordinates*/
		final int x0 = (int)x;
		final int y0 = (int)y;
		final int x1 = x0 + 1;
		final int y1 = y0 + 1;
		
		if (x0 < 0 || y0 < 0 || x1 > gridWidth - 1 || y1 > gridHeight - 1) {
			return 0;
		}
		
		/*Points*/
		final Vector2D point = new Vector2D(x, y);
		
		final Vector2D gridPoint00 = new Vector2D(x0, y0);
		final Vector2D gridPoint10 = new Vector2D(x1, y0);
		final Vector2D gridPoint01 = new Vector2D(x0, y1);
		final Vector2D gridPoint11 = new Vector2D(x1, y1);
		
		/*Distances from point to grid point*/
		final Vector2D distance00 = point.distance(gridPoint00);
		final Vector2D distance10 = point.distance(gridPoint10);
		final Vector2D distance01 = point.distance(gridPoint01);
		final Vector2D distance11 = point.distance(gridPoint11);
		
		/*Grid vectors*/
		final Vector2D grid00 = grid[x0][y0];
		final Vector2D grid10 = grid[x1][y0];
		final Vector2D grid01 = grid[x0][y1];
		final Vector2D grid11 = grid[x1][y1];
		
		/*Direction relations of distances and grid vectors*/
		final double dot00 = distance00.dot(grid00);
		final double dot10 = distance10.dot(grid10);
		final double dot01 = distance01.dot(grid01);
		final double dot11 = distance11.dot(grid11);
		
		/*Interpolation weights*/
		final double weightX = x - x0;
		final double weightY = y - y0;
		
		/*Interpolation of the relations*/
		final double top = interpolate(dot00, dot10, weightX);
		final double bottom = interpolate(dot01, dot11, weightX);
		
		double value = interpolate(top, bottom, weightY) * contrast;
		value = Math.max(Math.min(value, 1), -1);
		
		return value;
	}
	
	/**
	 * Randomizes the vector grid. The noise values will be completely different after that.
	 */
	public void randomizeGrid() {
		for (int x = 0; x < gridWidth; x++) {
			for (int y = 0; y < gridHeight; y++) {
				final double rotation = ran.nextDouble() * 360; //360 degrees
				final Vector2D vector = new Vector2D(VECTOR_LENGTH, 0).rotate(rotation);
				
				grid[x][y] = vector;
			}
		}
	}
	
	/**
	 * Returns every vector on the vector grid by the given angle.
	 * A value of 180 will invert the values.
	 * @param degrees The rotation angle in degrees
	 */
	public void rotateGrid(final double degrees) {
		for (int x = 0; x < gridWidth; x++) {
			for (int y = 0; y < gridHeight; y++) {
				final Vector2D vector = grid[x][y].rotate(degrees);
				
				grid[x][y] = vector;
			}
		}
	}
	
	/**
	 * Sets the contrast for the noise. The grid does not need to be recalculated.
	 * @param contrast The new contrast value
	 */
	public void setContrast(final double contrast) {
		this.contrast = contrast;
	}
	
	/**
	 * Returns the grid width.
	 * @return The grid width
	 */
	public int getGridWidth() {
		return gridWidth;
	}
	
	/**
	 * Returns the grid height.
	 * @return The grid height
	 */
	public int getGridHeight() {
		return gridHeight;
	}
	
}
