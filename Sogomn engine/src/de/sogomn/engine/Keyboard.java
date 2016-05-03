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
		
		notifyListeners(listener -> listener.keyboardEvent(key, flag));
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
