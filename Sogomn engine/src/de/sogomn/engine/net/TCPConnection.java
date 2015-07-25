package de.sogomn.engine.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * TCPConnection is for writing and reading simple data (well - text).
 * @author Sogomn
 *
 */
public class TCPConnection implements IClosable {
	
	private String address;
	private int port;
	
	private Socket socket;
	private BufferedReader reader;
	private BufferedWriter writer;
	
	private boolean open;
	
	private static final String NEW_LINE = "\r\n";
	
	/**
	 * Constructs a new TCPConnection object. The internal socket will automatically connect.
	 * @param address The host address
	 * @param port The port
	 */
	public TCPConnection(final String address, final int port) {
		this.address = address;
		this.port = port;
		
		try {
			socket = new Socket(address, port);
			
			initIO(socket);
		} catch (final IOException ex) {
			handleException(ex);
		}
	}
	
	/**
	 * Constructs a new TCPConnection object from the given socket.
	 * @param socket The socket which this connection uses
	 */
	public TCPConnection(final Socket socket) {
		this.socket = socket;
		
		address = socket.getInetAddress().getHostAddress();
		port = socket.getPort();
		
		try {
			initIO(socket);
		} catch (final IOException ex) {
			handleException(ex);
		}
	}
	
	private void initIO(final Socket socket) throws IOException {
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		open = true;
	}
	
	/**
	 * This method gets called when an exeption occurs. The default implementation prints the error and closes the connection.
	 * @param ex The exception that has been thrown
	 */
	protected void handleException(final Exception ex) {
		System.err.println("Connection error: " + ex.getMessage());
		
		close();
	}
	
	/**
	 * Closes the connection and all its streams.
	 */
	@Override
	public void close() {
		open = false;
		try {
			reader.close();
			writer.close();
			socket.close();
		} catch (final IOException | NullPointerException ex) {
			System.err.println("Connection has already been closed");
		}
	}
	
	/**
	 * Writes the data passed to this method.
	 * @param data The data to be written
	 */
	public void write(final String string) {
		try {
			writer.write(string + NEW_LINE);
			writer.flush();
		} catch (final IOException | NullPointerException ex) {
			handleException(ex);
		}
	}
	
	/**
	 * Reads the next segment of bytes.
	 * @return The read bytes
	 */
	public String read() {
		try {
			final String string = reader.readLine();
			
			return string;
		} catch (final IOException | NullPointerException ex) {
			handleException(ex);
			
			return null;
		}
	}
	
	/**
	 * Returns the host address.
	 * @return The address
	 */
	public final String getAddress() {
		return address;
	}
	
	/**
	 * Returns the port.
	 * @return The port
	 */
	public final int getPort() {
		return port;
	}
	
	/**
	 * Returns true if the connection is open, false otherwise.
	 * @return The state
	 */
	public final boolean isOpen() {
		return open;
	}
	
}
