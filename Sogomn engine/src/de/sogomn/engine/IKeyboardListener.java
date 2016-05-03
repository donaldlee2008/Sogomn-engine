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
 * This class represents an interface between keyboard and code.
 * @author Sogomn
 *
 */
public interface IKeyboardListener {
	
	/**
	 * Usually called when a key was pressed or released.
	 * @param key The key
	 * @param flag Whether the key was pressed or released
	 */
	void keyboardEvent(final int key, final boolean flag);
	
}
