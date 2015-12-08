package de.sogomn.engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import de.sogomn.engine.util.AbstractListenerContainer;

final class Keyboard extends AbstractListenerContainer<IKeyboardListener> implements KeyListener {
	
	private static final boolean PRESSED = true;
	private static final boolean RELEASED = false;
	
	public Keyboard() {
		//...
	}
	
	private void fireKeyboardEvent(final KeyEvent k, final boolean flag) {
		final int key = k.getKeyCode();
		
		for (int i = 0; i < getListenerCount(); i++) {
			final IKeyboardListener listener = listeners.get(i);
			
			listener.keyboardEvent(key, flag);
		}
	}
	
	@Override
	public void keyPressed(final KeyEvent k) {
		fireKeyboardEvent(k, PRESSED);
	}
	
	@Override
	public void keyReleased(final KeyEvent k) {
		fireKeyboardEvent(k, RELEASED);
	}
	
	@Override
	public void keyTyped(final KeyEvent k) {
		//...
	}
	
}
