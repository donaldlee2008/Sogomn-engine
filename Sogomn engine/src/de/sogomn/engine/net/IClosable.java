/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.engine.net;

interface IClosable {
	
	void close();
	
	boolean isOpen();
	
}
