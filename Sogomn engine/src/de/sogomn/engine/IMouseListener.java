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
	 * Use the modifier masks located in the MouseEvent class to use the modifiers.
	 * If the button one is pressed then
	 * {@code
	 * (modifiers & MouseEvent.BUTTON1_MASK) != 0
	 * }
	 * is true.
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param modifiers The non-extended button modifiers
	 */
	void mouseMotionEvent(final int x, final int y, final int modifiers);
	
	/**
	 * Usually called when the mouse wheel was moved.
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param rotation The scroll amount
	 */
	void mouseWheelEvent(final int x, final int y, final int rotation);
	
}
