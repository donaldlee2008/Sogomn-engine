package de.sogomn.engine;

/**
 * An interface for a class to handle mouse events.
 * @author Sogomn
 *
 */
public interface IMouseListener {
	
	/**
	 * Usually called when the mouse was pressed or released.
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param button The mouse button
	 * @param flag True when pressed, false when released
	 */
	void mouseEvent(final int x, final int y, final int button, final boolean flag);
	
	/**
	 * Usually called when the mouse was moved.
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param button The button pressed during the movement - might be undefined
	 * @param flag True when dragged, false otherwise
	 */
	void mouseMotionEvent(final int x, final int y, final int button, final boolean flag);
	
	/**
	 * Usually called when the mouse wheel was moved.
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param rotation The scroll amount
	 */
	void mouseWheelEvent(final int x, final int y, final int rotation);
	
}
