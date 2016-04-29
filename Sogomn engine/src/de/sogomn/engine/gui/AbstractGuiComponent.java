/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.engine.gui;

import java.awt.event.MouseEvent;

import de.sogomn.engine.IDrawable;
import de.sogomn.engine.IKeyboardListener;
import de.sogomn.engine.IMouseListener;

/**
 * This class is kind of a template for GUI components.
 * The component should be added as a listener for drawing calls and input events.
 * @author Sogomn
 *
 */
public abstract class AbstractGuiComponent implements IDrawable, IMouseListener, IKeyboardListener {
	
	/**
	 * Location variable.
	 * Should be self-explanatory.
	 */
	protected int x, y;
	
	/**
	 * Dimension variable.
	 * Should be self-explanatory.
	 */
	protected int width, height;
	
	/**
	 * Indicates whether the component is hovered.
	 */
	protected boolean hovered;
	
	/**
	 * Indicates whether the component is focused.
	 */
	protected boolean focused;
	
	/**
	 * Indicates whether the component is being pressed.
	 */
	protected boolean leftPressed, middlePressed, rightPressed;
	
	/**
	 * Constructs a new GUI component.
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param width The width
	 * @param height The height
	 */
	public AbstractGuiComponent(final int x, final int y, final int width, final int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Called when the user clicked the component.
	 * @param button The mouse button
	 */
	protected abstract void clicked(final int button);
	
	/**
	 * Called when the user scrolled the component.
	 * @param rotation The mouse wheel rotation
	 */
	protected abstract void scrolled(final int rotation);
	
	/**
	 * Called when the user typed a key and the component is focused.
	 * @param key The key code
	 */
	protected abstract void typed(final int key);
	
	@Override
	public final void mouseEvent(final int x, final int y, final int button, final boolean flag) {
		final boolean clicked = (leftPressed || rightPressed) && !flag;
		
		if (clicked) {
			clicked(button);
		}
		
		if (button == MouseEvent.BUTTON1) {
			leftPressed = hovered && flag;
			
			if (!flag) {
				focused = hovered;
			}
		} else if (button == MouseEvent.BUTTON2) {
			middlePressed = hovered && flag;
		}
		else if (button == MouseEvent.BUTTON3) {
			rightPressed = hovered && flag;
		}
	}
	
	@Override
	public final void mouseMotionEvent(final int x, final int y, final int modifiers) {
		hovered = contains(x, y);
	}
	
	@Override
	public final void mouseWheelEvent(final int x, final int y, final int rotation) {
		if (hovered) {
			scrolled(rotation);
		}
	}
	
	@Override
	public final void keyboardEvent(final int key, final boolean flag) {
		if (focused && flag) {
			typed(key);
		}
	}
	
	/**
	 * Returns true if the point specified by x and y lies inside the component.
	 * @param xPos The x coordinate
	 * @param yPos The y coordinate
	 * @return True if the point intersects the component; false otherwise
	 */
	public boolean contains(final int xPos, final int yPos) {
		final boolean contains = xPos > x && xPos < x + width && yPos > y && yPos < y + height;
		
		return contains;
	}
	
	/**
	 * Sets the x coordinate of the component.
	 * @param x The new x coordinate
	 */
	public final void setX(final int x) {
		this.x = x;
	}
	
	/**
	 * Sets the y coordinate of the component.
	 * @param y The new y coordinate
	 */
	public final void setY(final int y) {
		this.y = y;
	}
	
	/**
	 * Sets the width of the component.
	 * @param width The new width
	 */
	public final void setWidth(final int width) {
		this.width = width;
	}
	
	/**
	 * Sets the height of the component.
	 * @param height The new height
	 */
	public final void setHeight(final int height) {
		this.height = height;
	}
	
	/**
	 * Returns the x coordinate of the component.
	 * @return The x coordinate
	 */
	public final int getX() {
		return x;
	}
	
	/**
	 * Returns the y coordinate of the component.
	 * @return The y coordinate
	 */
	public final int getY() {
		return y;
	}
	
	/**
	 * Returns the width of the component.
	 * @return The width
	 */
	public final int getWidth() {
		return width;
	}
	
	/**
	 * Returns the height of the component.
	 * @return The height
	 */
	public final int getHeight() {
		return height;
	}
	
	/**
	 * Returns whether the component is hovered.
	 * @return The state
	 */
	public final boolean isHovered() {
		return hovered;
	}
	
	/**
	 * Returns whether the component is focused.
	 * @return The state
	 */
	public final boolean isFocused() {
		return focused;
	}
	
	/**
	 * Returns whether the component is being pressed by the left mouse button.
	 * @return The state
	 */
	public final boolean isLeftPressed() {
		return leftPressed;
	}
	
	/**
	 * Returns whether the component is being pressed by the middle mouse button.
	 * @return The state
	 */
	public final boolean isMiddlePressed() {
		return middlePressed;
	}
	
	/**
	 * Returns whether the component is being pressed by the right mouse button.
	 * @return The state
	 */
	public final boolean isRightPressed() {
		return rightPressed;
	}
	
}
