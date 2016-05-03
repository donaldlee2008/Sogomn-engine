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

package de.sogomn.engine.util;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class holds several useful methods related to files and IO-streams.
 * @author Sogomn
 *
 */
public final class FileUtils {
	
	private static final int BUFFER_SIZE = 1024;
	
	private FileUtils() {
		//...
	}
	
	/**
	 * Reads all data from the given file (classpath!).
	 * @param path The path to the file
	 * @return The data or null in case of failure
	 */
	public static byte[] readInternalData(final String path) {
		final InputStream in = FileUtils.class.getResourceAsStream(path);
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final byte[] buffer = new byte[BUFFER_SIZE];
		
		int bytesRead = 0;
		
		try {
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
		} catch (final Exception ex) {
			ex.printStackTrace();
			
			return null;
		}
		
		return out.toByteArray();
	}
	
	/**
	 * Reads all data from the given external file.
	 * @param path The path to the file
	 * @return The data or null in case of failure
	 */
	public static byte[] readExternalData(final String path) {
		final Path file = Paths.get(path);
		
		try {
			final byte[] data = Files.readAllBytes(file);
			
			return data;
		} catch (final Exception ex) {
			ex.printStackTrace();
			
			return null;
		}
	}
	
	/**
	 * Reads all lines from the given file (classpath!).
	 * @param path The path to the file
	 * @return The lines or null in case of failure
	 */
	public static String[] readInternalLines(final String path) {
		final InputStream in = FileUtils.class.getResourceAsStream(path);
		final InputStreamReader inReader = new InputStreamReader(in);
		final BufferedReader reader = new BufferedReader(inReader);
		final ArrayList<String> lines = new ArrayList<String>();
		
		String line = null;
		
		try {
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} catch (final Exception ex) {
			ex.printStackTrace();
			
			return null;
		}
		
		final String[] lineArray = lines.stream().toArray(String[]::new);
		
		return lineArray;
	}
	
	/**
	 * Reads all lines from the given external file.
	 * @param path The path to the file
	 * @return The lines or null in case of failure
	 */
	public static String[] readExternalLines(final String path) {
		final Path file = Paths.get(path);
		
		try {
			final List<String> lines = Files.readAllLines(file);
			final String[] lineArray = lines.stream().toArray(String[]::new);
			
			return lineArray;
		} catch (final Exception ex) {
			ex.printStackTrace();
			
			return null;
		}
	}
	
	/**
	 * Writes data to the given path.
	 * @param path The path
	 * @param data The data
	 * @return True on success; false otherwise
	 */
	public static boolean writeData(final String path, final byte[] data) {
		final Path file = Paths.get(path);
		
		try {
			Files.write(file, data);
			
			return true;
		} catch (final Exception ex) {
			ex.printStackTrace();
			
			return false;
		}
	}
	
	/**
	 * Writes lines of text to the given path.
	 * @param path The path
	 * @param lines The lines
	 * @return True on success; false otherwise
	 */
	public static boolean writeLines(final String path, final String... lines) {
		final Path file = Paths.get(path);
		final List<String> lineList = Arrays.asList(lines);
		
		try {
			Files.write(file, lineList);
			
			return true;
		} catch (final Exception ex) {
			ex.printStackTrace();
			
			return false;
		}
	}
	
	/**
	 * Creates a new file at the given path.
	 * Also creates all nonexistent parent directories.
	 * If the file already exists, nothing will happen.
	 * @param path The path
	 * @return True on success; false otherwise
	 */
	public static boolean createFile(final String path) {
		final Path file = Paths.get(path);
		final Path parent = file.getParent();
		
		try {
			Files.createDirectories(parent);
			Files.createFile(file);
			
			return true;
		} catch (final Exception ex) {
			return false;
		}
	}
	
	/**
	 * Deletes the specified file.
	 * @param path The path to the file
	 * @return True on success; false otherwise
	 */
	public static boolean deleteFile(final String path) {
		final Path file = Paths.get(path);
		
		try {
			Files.delete(file);
			
			return true;
		} catch (final Exception ex) {
			return false;
		}
	}
	
	/**
	 * Creates a new folder and all necessary parent folders.
	 * If the folder already exists, nothing will happen.
	 * @param path The path
	 * @return True on success; false otherwise
	 */
	public static boolean createDirectory(final String path) {
		final Path file = Paths.get(path);
		
		try {
			Files.createDirectories(file);
			
			return true;
		} catch (final Exception ex) {
			return false;
		}
	}
	
	/**
	 * Copies the source file to the destination and replaces it, if existent.
	 * @param source The source file
	 * @param destination The destination file
	 * @return True on success; false otherwise
	 */
	public static boolean copyFile(final Path source, final Path destination) {
		try {
			Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
			
			return true;
		} catch (final Exception ex) {
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
		final Path sourceFile = Paths.get(source);
		final Path destinationFile = Paths.get(destination);
		
		return copyFile(sourceFile, destinationFile);
	}
	
	/**
	 * Executes the given file.
	 * @param file The file to be executed
	 * @return True on success; false otherwise
	 */
	public static boolean executeFile(final File file) {
		final boolean desktopSupported = Desktop.isDesktopSupported();
		
		if (desktopSupported && file.exists()) {
			final Desktop desktop = Desktop.getDesktop();
			final boolean canOpen = desktop.isSupported(Action.OPEN);
			
			if (canOpen) {
				try {
					desktop.open(file);
					
					return true;
				} catch (final IOException ex) {
					return false;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Executes the given file.
	 * @param path The path to the file
	 * @return True on success; false otherwise
	 */
	public static boolean executeFile(final String path) {
		final File file = new File(path);
		
		return executeFile(file);
	}
	
}
