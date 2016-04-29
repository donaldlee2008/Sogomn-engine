/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.engine.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class represents a TCP server which can accept connections.
 * @author Sogomn
 *
 */
public class TCPServer implements IClosable {
	
	private ServerSocket server;
	
	private boolean open;
	
	/**
	 * Constructs a new TCPServer object and binds the server to the given port.
	 * @param port The port
	 */
	public TCPServer(final int port) {
		try {
			server = new ServerSocket(port);
			open = true;
		} catch (final IOException ex) {
			handleException(ex);
		}
	}
	
	/**
	 * This method gets called when an exeption occurs. The default implementation prints the error and closes the server.
	 * @param ex The exception that has been thrown
	 */
	protected void handleException(final Exception ex) {
		if (ex instanceof NullPointerException) {
			System.err.println("Server has not been initialized successfully: " + ex.getMessage());
		} else if (ex instanceof IOException) {
			System.err.println("Server closed: " + ex.getMessage());
		} else {
			System.err.println("Server error: " + ex.getMessage());
		}
		
		close();
	}
	
	/**
	 * Closes the server.
	 * This method does nothing if the connection is not open.
	 */
	@Override
	public void close() {
		if (!open) {
			return;
		}
		
		open = false;
		
		try {
			server.close();
		} catch (final Exception ex) {
			//...
		}
	}
	
	/**
	 * Accepts the next incoming connection request.
	 * This will block the thread until a connection has been accepted or an exception has been thrown.
	 * @return The connection or null in case of failure
	 */
	public Socket acceptConnection() {
		try {
			final Socket socket = server.accept();
			
			return socket;
		} catch (final IOException | NullPointerException ex) {
			handleException(ex);
			
			return null;
		}
	}
	
	/**
	 * Returns the local port the server is bind to.
	 * @return The port
	 */
	public final int getPort() {
		return server.getLocalPort();
	}
	
	/**
	 * Returns true if the server is open and can accept connections, false otherwise.
	 * @return The state
	 */
	@Override
	public final boolean isOpen() {
		return open;
	}
	
}
