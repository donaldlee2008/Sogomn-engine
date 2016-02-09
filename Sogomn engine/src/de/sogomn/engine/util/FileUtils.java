package de.sogomn.engine.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

/**
 * This class holds several useful methods related to files and IO-streams.
 * @author Sogomn
 *
 */
public final class FileUtils {
	
	private static final String NEW_LINE = "\r\n";
	
	private FileUtils() {
		//...
	}
	
	private static byte[] readData(final InputStream in) {
		try {
			final int length = in.available();
			final byte[] data = new byte[length];
			
			in.read(data);
			
			return data;
		} catch (final IOException ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
	
	private static String[] readLines(final InputStream in) {
		final InputStreamReader inReader = new InputStreamReader(in);
		final BufferedReader reader = new BufferedReader(inReader);
		final ArrayList<String> lines = new ArrayList<String>();
		
		String line = null;
		
		try {
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} catch (final IOException ex) {
			ex.printStackTrace();
		}
		
		final String[] linesArray = lines.stream().toArray(String[]::new);
		
		return linesArray;
	}
	
	/**
	 * Reads all data from the given file (classpath!).
	 * @param path The path to the file
	 * @return The data or null in case of failure
	 */
	public static byte[] readInternalData(final String path) {
		final InputStream in = FileUtils.class.getResourceAsStream(path);
		
		return readData(in);
	}
	
	/**
	 * Reads all data from the given external file.
	 * @param file The file
	 * @return The data or null in case of failure
	 */
	public static byte[] readExternalData(final File file) {
		try {
			final FileInputStream in = new FileInputStream(file);
			
			return readData(in);
		} catch (final IOException ex) {
			ex.printStackTrace();
			
			return null;
		}
	}
	
	/**
	 * Reads all data from the given external file.
	 * @param path The path to the file
	 * @return The data or null in case of failure
	 */
	public static byte[] readExternalData(final String path) {
		final File file = new File(path);
		
		return readExternalData(file);
	}
	
	/**
	 * Reads all lines from the given file (classpath!).
	 * @param path The path to the file
	 * @return The lines or null in case of failure
	 */
	public static String[] readInternalLines(final String path) {
		final InputStream in = FileUtils.class.getResourceAsStream(path);
		
		return readLines(in);
	}
	
	/**
	 * Reads all lines from the given external file.
	 * @param file The file
	 * @return The lines or null in case of failure
	 */
	public static String[] readExternalLines(final File file) {
		try {
			final FileInputStream in = new FileInputStream(file);
			
			return readLines(in);
		} catch (final IOException ex) {
			ex.printStackTrace();
			
			return null;
		}
	}
	
	/**
	 * Reads all lines from the given external file.
	 * @param path The path to the file
	 * @return The lines or null in case of failure
	 */
	public static String[] readExternalLines(final String path) {
		final File file = new File(path);
		
		return readExternalLines(file);
	}
	
	/**
	 * Writes data to the given file.
	 * @param file The file
	 * @param data The data
	 */
	public static void writeData(final File file, final byte[] data) {
		try {
			final FileOutputStream out = new FileOutputStream(file);
			
			out.write(data);
			out.flush();
			out.close();
		} catch (final IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Writes data to the given path.
	 * @param path The path
	 * @param data The data
	 */
	public static void writeData(final String path, final byte[] data) {
		final File file = new File(path);
		
		writeData(file, data);
	}
	
	/**
	 * Writes lines of text to the given file.
	 * @param file The file
	 * @param lines The lines
	 */
	public static void writeLines(final File file, final String... lines) {
		try {
			final FileOutputStream out = new FileOutputStream(file);
			final OutputStreamWriter outWriter = new OutputStreamWriter(out);
			final BufferedWriter writer = new BufferedWriter(outWriter);
			
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
	 * Writes lines of text to the given path.
	 * @param path The path
	 * @param lines The lines
	 */
	public static void writeLines(final String path, final String... lines) {
		final File file = new File(path);
		
		writeLines(file, lines);
	}
	
	/**
	 * Creates a new file at the given path.
	 * Also creates all nonexistent parent directories.
	 * If the file already exists, nothing will happen.
	 * @param path The path
	 */
	public static void createFile(final String path) {
		final File file = new File(path);
		final File parent = file.getParentFile();
		
		if (parent != null) {
			createFolder(parent.getPath());
		}
		
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
	
	/**
	 * Copies the source file to the destination and replaces it, if existent.
	 * @param source The source file
	 * @param destination The destination file
	 * @return True on success; false otherwise
	 */
	public static boolean copyFile(final File source, final File destination) {
		final Path sourcePath = source.toPath();
		final Path destinationPath = destination.toPath();
		
		try {
			Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
			
			return true;
		} catch (final IOException ex) {
			ex.printStackTrace();
			
			return false;
		}
	}
	
	/**
	 * Copies the source file to the destination and replaces it, if existent.
	 * @param source The source file
	 * @param destination The destination file
	 * @return True on success; false otherwise
	 */
	public static boolean copyFile(final String source, final String destination) {
		final File sourceFile = new File(source);
		final File destinationFile = new File(destination);
		
		return copyFile(sourceFile, destinationFile);
	}
	
}
