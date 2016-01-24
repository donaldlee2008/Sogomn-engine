package de.sogomn.engine.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * TCPConnection is for communication between a client and a server.
 * Should not be used with other protocols since the first four bytes of every buffer will be lost then.
 * @author Sogomn
 *
 */
public class TCPConnection implements IClosable {
	
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	
	private boolean open;
	
	/**
	 * Constructs a new TCPConnection object. The internal socket will automatically connect.
	 * @param address The host address
	 * @param port The port
	 */
	public TCPConnection(final String address, final int port) {
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
		
		try {
			initIO(socket);
		} catch (final IOException ex) {
			handleException(ex);
		}
	}
	
	private void initIO(final Socket socket) throws IOException {
		in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
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
			in.close();
			out.close();
			socket.close();
		} catch (final IOException | NullPointerException ex) {
			System.err.println("Connection has already been closed");
		}
	}
	
	/**
	 * Writes four bytes equal to the data length.
	 * Then writes the data.
	 * @param data The data to be written
	 */
	public void write(final byte[] data) {
		try {
			final int length = data.length;
			
			out.writeInt(length);
			out.write(data);
			out.flush();
		} catch (final IOException | NullPointerException ex) {
			handleException(ex);
		}
	}
	
	/**
	 * First reads four bytes which determine the data length.
	 * Then reads the next bytes and returns them.
	 * @return The read data
	 */
	public byte[] read() {
		try {
			final int length = in.readInt();
			final byte[] data = new byte[length];
			
			in.readFully(data);
			
			return data;
		} catch (final IOException | NullPointerException ex) {
			handleException(ex);
			
			return null;
		}
	}
	
	/**
	 * Returns the remote host address.
	 * @return The address
	 */
	public final String getAddress() {
		return socket.getInetAddress().getHostAddress();
	}
	
	/**
	 * Returns the remote port.
	 * @return The port
	 */
	public final int getPort() {
		return socket.getPort();
	}
	
	/**
	 * Returns true if the connection is open, false otherwise.
	 * @return The state
	 */
	@Override
	public final boolean isOpen() {
		return open;
	}
	
}
