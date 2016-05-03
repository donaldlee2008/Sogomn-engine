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

package de.sogomn.engine.debug;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An interface to indicate that something has been hardcoded.
 * The retention is source only.
 * This class is completely useless.
 * Makes your code more fun to read, tho.
 * @author Sogomn
 *
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Hardcoded {
	
	/**
	 * The excuse why you hardcoded the values.
	 * @return The excuse
	 */
	String value() default "";
	
}
