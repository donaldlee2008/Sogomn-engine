package de.sogomn.engine.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * This class holds several useful methods related to files and IO-streams.
 * @author Sogomn
 *
 */
public final class FileUtils {
	
	private static final int BUFFER_SIZE = 16;
	private static final String NEW_LINE = "\r\n";
	
	private FileUtils() {
		//...
	}
	
	private static byte[] readData(final InputStream in) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final byte[] buffer = new byte[BUFFER_SIZE];
		
		try {
			while (in.read(buffer) != -1) {
				out.write(buffer);
			}
		} catch (final IOException ex) {
			ex.printStackTrace();
		}
		
		return out.toByteArray();
	}
	
	private static String[] readLines(final InputStream in) {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		final ArrayList<String> lines = new ArrayList<String>();
		
		String line = null;
		
		try {
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} catch (final IOException ex) {
			ex.printStackTrace();
		}
		
		final String[] lineArray = new String[lines.size()];
		
		lines.toArray(lineArray);
		
		return lineArray;
	}
	
	/**
	 * Reads all data from the given file (classpath!).
	 * @param path The path to the file
	 * @return The data
	 */
	public static byte[] readInternalData(final String path) {
		return readData(FileUtils.class.getResourceAsStream(path));
	}
	
	/**
	 * Reads all data from the given external file.
	 * @param path The path to the file
	 * @return The data
	 */
	public static byte[] readExternalData(final String path) {
		try {
			return readData(new FileInputStream(path));
		} catch (final IOException ex) {
			ex.printStackTrace();
			
			return null;
		}
	}
	
	/**
	 * Reads all lines from the given file (classpath!).
	 * @param path The path to the file
	 * @return The lines
	 */
	public static String[] readInternalLines(final String path) {
		return readLines(FileUtils.class.getResourceAsStream(path));
	}
	
	/**
	 * Reads all lines from the given external file.
	 * @param path The path to the file
	 * @return The lines
	 */
	public static String[] readExternalLines(final String path) {
		try {
			return readLines(new FileInputStream(path));
		} catch (final IOException ex) {
			ex.printStackTrace();
			
			return null;
		}
	}
	
	/**
	 * Writes data to the given path.
	 * @param path The path
	 * @param data The data
	 */
	public static void writeData(final String path, final byte[] data) {
		try {
			final FileOutputStream out = new FileOutputStream(path);
			
			out.write(data);
			out.flush();
			out.close();
		} catch (final IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Writes lines to the given path.
	 * @param path The path
	 * @param lines The lines
	 */
	public static void writeLines(final String path, final String[] lines) {
		try {
			final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path)));
			
			for (final String line : lines) {
				writer.write(line + NEW_LINE);
			}
			
			writer.flush();
			writer.close();
		} catch (final IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Creates a new file at the given path.
	 * If the file already exists, nothing will happen.
	 * @param path The path
	 */
	public static void createFile(final String path) {
		final File file = new File(path);
		
		try {
			file.createNewFile();
		} catch (final IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Creates a new folder and all necessary parent folders.
	 * If the folder already exists, nothing will happen.
	 * @param path The path
	 */
	public static void createFolder(final String path) {
		final File file = new File(path);
		
		file.mkdirs();
	}
	
}