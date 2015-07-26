package de.sogomn.engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

final class Keyboard implements KeyListener {
	
	private ArrayList<IKeyboardListener> listeners;
	
	private static final boolean PRESSED = true;
	private static final boolean RELEASED = false;
	
	public Keyboard() {
		listeners = new ArrayList<IKeyboardListener>();
	}
	
	private void fireKeyboardEvent(final KeyEvent k, final boolean flag) {
		final int key = k.getKeyCode();
		
		synchronized (listeners) {
			for (int i = 0; i < listeners.size(); i++) {
				final IKeyboardListener listener = listeners.get(i);
				
				listener.keyboardEvent(key, flag);
			}
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
	
	public void addListener(final IKeyboardListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}
	
	public void removeListener(final IKeyboardListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}
	
}
