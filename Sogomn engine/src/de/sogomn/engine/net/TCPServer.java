package de.sogomn.engine.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer implements IClosable {
	
	private int port;
	
	private ServerSocket server;
	
	public TCPServer(final int port) {
		this.port = port;
		
		try {
			server = new ServerSocket(port);
		} catch (final IOException ex) {
			handleException(ex);
		}
	}
	
	protected void handleException(final Exception ex) {
		System.err.println("Server error: " + ex.getMessage());
		
		close();
	}
	
	@Override
	public void close() {
		try {
			server.close();
		} catch (final IOException | NullPointerException ex) {
			handleException(ex);
		}
	}
	
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
	
	public final int getPort() {
		return port;
	}
	
}
