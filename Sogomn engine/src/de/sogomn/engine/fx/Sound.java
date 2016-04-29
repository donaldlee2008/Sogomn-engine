/*
 * Copyright 2016 Johannes Boczek
 */

package de.sogomn.engine.fx;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import de.sogomn.engine.util.AbstractListenerContainer;


/**
 * This is a class to load and play sounds. Use the static methods to create new objects.
 * If looping a sound too often in a tiny interval it might throw a NullPointerException. Ignore it. It will play anyway.
 * @author Sogomn
 *
 */
public final class Sound extends AbstractListenerContainer<ISoundListener> {
	
	private byte[] data;
	private AudioFormat format;
	private SourceDataLine line;
	private boolean playing;
	
	private float gain;
	
	private Sound(final byte[] data, final AudioFormat format) {
		this.data = data;
		this.format = format;
	}
	
	private void writeDataToLine() {
		try {
			playing = true;
			line = AudioSystem.getSourceDataLine(format);
			
			if (line == null) {
				return;
			}
			
			line.open();
			
			final FloatControl gainControl = (FloatControl)line.getControl(Type.MASTER_GAIN);
			final float minimum = gainControl.getMinimum();
			final float maximum = gainControl.getMaximum();
			final float actualGain = Math.max(minimum, Math.min(maximum, gain));
			
			gainControl.setValue(actualGain);
			
			line.start();
			line.write(data, 0, data.length);
			line.drain();
		} catch (final LineUnavailableException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Loops the sound the given amount of times.
	 * @param loops The loop count
	 */
	public synchronized void play(final int loops) {
		final Runnable runnable = () -> {
			for (int i = 0; i < loops; i++) {
				writeDataToLine();
				
				if (!playing) {
					break;
				}
				
				notifyListeners(listener -> listener.looped(this));
			}
			
			notifyListeners(listener -> listener.stopped(this));
		};
		final Thread thread = new Thread(runnable);
		
		thread.start();
	}
	
	/**
	 * Plays the sound. This method is not blocking.
	 */
	public synchronized void play() {
		play(1);
	}
	
	/**
	 * Stops the sound.
	 */
	public synchronized void stop() {
		if (line == null || !playing) {
			return;
		}
		
		playing = false;
		
		line.stop();
		line.flush();
		line.close();
		
		line = null;
	}
	
	/**
	 * Sets the gain for the sound. Negative values mean less gain.
	 * Zero is the default.
	 * @param gain The new gain value
	 */
	public void setGain(final float gain) {
		this.gain = gain;
	}
	
	/**
	 * Returns whether the sound is playing or not.
	 * @return The state
	 */
	public boolean isPlaying() {
		return playing;
	}
	
	/**
	 * Returns the current gain.
	 * @return The gain
	 */
	public float getGain() {
		return gain;
	}
	
	/**
	 * Constructs a sound from the given data.
	 * Since no AudioFormat is specified, this might not work.
	 * @param data The audio data
	 * @return The sound
	 */
	public static Sound loadSound(final byte[] data) {
		final Sound sound = new Sound(data, null);
		
		return sound;
	}
	
	/**
	 * Loads a sound from the classpath. The path should be prefixed with a slash ('/').
	 * @param path The path
	 * @return The sound or null in case of faliure
	 */
	public static Sound loadSound(final String path) {
		try {
			final AudioInputStream in = AudioSystem.getAudioInputStream(new BufferedInputStream(Sound.class.getResourceAsStream(path)));
			final AudioFormat format = in.getFormat();
			final int bufferSize = in.available();
			final byte[] buffer = new byte[bufferSize];
			
			in.read(buffer, 0, bufferSize);
			
			final Sound sound = new Sound(buffer, format);
			
			return sound;
		} catch (final IOException | UnsupportedAudioFileException ex) {
			ex.printStackTrace();
			
			return null;
		}
	}
	
	/**
	 * Loads a sound from the given file.
	 * @param file The file the sound should be loaded from
	 * @return The sound or null in case of faliure
	 */
	public static Sound loadExternalSound(final File file) {
		try {
			final AudioInputStream in = AudioSystem.getAudioInputStream(file);
			final int bufferSize = in.available();
			final byte[] buffer = new byte[bufferSize];
			final AudioFormat format = in.getFormat();
			
			in.read(buffer, 0, bufferSize);
			
			final Sound sound = new Sound(buffer, format);
			
			return sound;
		} catch (final IOException | UnsupportedAudioFileException ex) {
			ex.printStackTrace();
			
			return null;
		}
	}
	
	/**
	 * Loads a sound from the given path.
	 * @param path The path the sound should be loaded from
	 * @return The sound or null in case of faliure
	 */
	public static Sound loadExternalSound(final String path) {
		final File file = new File(path);
		final Sound sound = loadExternalSound(file);
		
		return sound;
	}
	
}
