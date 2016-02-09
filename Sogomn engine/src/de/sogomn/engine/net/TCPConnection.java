package de.sogomn.engine.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * TCPConnection is for communication between a client and a server.
 * @author Sogomn
 *
 */
public class TCPConnection implements IClosable {
	
	private String address;
	private int port;
	
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
		in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
		open = true;
	}
	
	/**
	 * This method gets called when an exeption occurs. The default implementation prints the error and closes the connection.
	 * @param ex The exception that has been thrown
	 */
	protected void handleException(final Exception ex) {
		if (ex instanceof NullPointerException) {
			System.err.println("Tried to close connection but it already is.");
		} else if (ex instanceof IOException) {
			System.err.println("Connection closed.");
		} else {
			System.err.println("Connection error: " + ex.getMessage());
		}
		
		close();
	}
	
	/**
	 * Closes the connection and all its streams.
	 * This method does nothing if the connection is not open.
	 */
	@Override
	public void close() {
		if (!open) {
			return;
		}
		
		open = false;
		
		try {
			in.close();
			out.close();
			socket.close();
		} catch (final Exception ex) {
			handleException(ex);
		}
	}
	
	/**
	 * Writes the data to the output stream.
	 * @param data The data to be sent
	 */
	public void write(final byte[] data) {
		try {
			out.write(data);
			out.flush();
		} catch (final Exception ex) {
			handleException(ex);
		}
	}
	
	/**
	 * Writes an integer to the output stream.
	 * @param i The integer to be sent
	 */
	public void writeInt(final int i) {
		try {
			out.writeInt(i);
			out.flush();
		} catch (final Exception ex) {
			handleException(ex);
		}
	}
	
	/**
	 * Writes a byte to the output stream.
	 * @param b The byte to be written
	 */
	public void writeByte(final byte b) {
		try {
			out.writeByte(b);
			out.flush();
		} catch (final Exception ex) {
			handleException(ex);
		}
	}
	
	/**
	 * Writes a string in the modified UTF-8 format to the output stream.
	 * @param message The string to be written
	 */
	public void writeUTF(final String message) {
		try {
			out.writeUTF(message);
			out.flush();
		} catch (final Exception ex) {
			handleException(ex);
		}
	}
	
	/**
	 * Writes a long to the output stream.
	 * @param l The long to be sent
	 */
	public void writeLong(final long l) {
		try {
			out.writeLong(l);
			out.flush();
		} catch (final Exception ex) {
			handleException(ex);
		}
	}
	
	/**
	 * Writes a short to the output stream.
	 * @param s The short to be sent
	 */
	public void writeShort(final short s) {
		try {
			out.writeShort(s);
			out.flush();
		} catch (final Exception ex) {
			handleException(ex);
		}
	}
	
	/**
	 * Reads data from the input stream.
	 * The amount of bytes read is equal to the length of the buffer.
	 * @param buffer The buffer the read data should be stored in
	 */
	public void read(final byte[] buffer) {
		try {
			in.readFully(buffer);
		} catch (final Exception ex) {
			handleException(ex);
		}
	}
	
	/**
	 * Reads an integer from the input stream and returns it.
	 * @return The integer read or zero in case of failure
	 */
	public int readInt() {
		try {
			final int i = in.readInt();
			
			return i;
		} catch (final Exception ex) {
			handleException(ex);
			
			return 0;
		}
	}
	
	/**
	 * Reads the next byte from the input stream and returns it.
	 * @return The next byte or zero in case of failure
	 */
	public byte readByte() {
		try {
			final byte b = in.readByte();
			
			return b;
		} catch (final Exception ex) {
			handleException(ex);
			
			return 0;
		}
	}
	
	/**
	 * Reads a string in the modified UTF-8 format and returns it.
	 * @return The string or null in case of failure
	 */
	public String readUTF() {
		try {
			final String message = in.readUTF();
			
			return message;
		} catch (final Exception ex) {
			handleException(ex);
			
			return null;
		}
	}
	
	/**
	 * Reads the next long from the input stream and returns it.
	 * @return The next long or zero in case of failure
	 */
	public long readLong() {
		try {
			final long l = in.readLong();
			
			return l;
		} catch (final Exception ex) {
			handleException(ex);
			
			return 0;
		}
	}
	
	/**
	 * Reads the next short from the input stream and returns it.
	 * @return The next short or zero in case of failure
	 */
	public short readShort() {
		try {
			final short s = in.readShort();
			
			return s;
		} catch (final Exception ex) {
			handleException(ex);
			
			return 0;
		}
	}
	
	/**
	 * Returns the remote host address.
	 * @return The address
	 */
	public final String getAddress() {
		return address;
	}
	
	/**
	 * Returns the remote port.
	 * @return The port
	 */
	public final int getPort() {
		return port;
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
