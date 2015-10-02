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
		System.err.println("Server error: " + ex.getMessage());
		
		close();
	}
	
	/**
	 * Closes the server.
	 */
	@Override
	public void close() {
		open = false;
		
		try {
			server.close();
		} catch (final IOException | NullPointerException ex) {
			handleException(ex);
		}
	}
	
	/**
	 * Accepts a connection.
	 * Will block the thread until a connection has been accepted or an exception has been thrown.
	 * @return The connection
	 */
	public TCPConnection acceptConnection() {
		try {
			final Socket socket = server.accept();
			final TCPConnection connection = new TCPConnection(socket);
			
			return connection;
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
