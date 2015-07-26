package de.sogomn.engine;

/**
 * This class represents an interface between keyboard and code.
 * @author Sogomn
 *
 */
public interface IKeyboardListener {
	
	/**
	 * Usually called when a key was pressed or released.
	 * @param button The button
	 * @param flag Whether the key was pressed or released
	 */
	void keyboardEvent(final int key, final boolean flag);
	
}
