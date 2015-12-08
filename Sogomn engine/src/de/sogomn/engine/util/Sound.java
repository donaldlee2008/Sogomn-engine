package de.sogomn.engine.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


/**
 * Sound class to load and play sounds. Use the static methods to create new objects.
 * @author Sogomn
 *
 */
public final class Sound extends AbstractListenerContainer<ISoundListener> {
	
	private byte[] data;
	private AudioFormat format;
	
	private float gain;
	
	private HashMap<Long, Clip> clips;
	private long currentId;
	
	private static final int BUFFER_SIZE = 24;
	
	/**
	 * Passed to loop infinitely.
	 */
	public static final int INFINITE = Clip.LOOP_CONTINUOUSLY + 1;
	
	/**
	 * Returned instead of an id if an error occurs.
	 */
	public static final int ERROR = -1;
	
	private Sound(final byte[] data, final AudioFormat format) {
		this.data = data;
		this.format = format;
		
		clips = new HashMap<Long, Clip>();
	}
	
	private void notifyListeners(final long id) {
		for (int i = 0; i < getListenerCount(); i++) {
			final ISoundListener listener = listeners.get(i);
			
			listener.stopped(this, id);
		}
	}
	
	private Clip createClip(final long id) {
		try {
			final Clip clip = AudioSystem.getClip();
			
			clip.open(format, data, 0, data.length);
			clip.addLineListener(l -> {
				final LineEvent.Type type = l.getType();
				
				if (type == LineEvent.Type.STOP) {
					stop(id);
					notifyListeners(id);
				}
			});
			
			clips.put(id, clip);
			
			return clip;
		} catch (final LineUnavailableException ex) {
			ex.printStackTrace();
			
			return null;
		}
	}
	
	/**
	 * Plays the sound a given amount of times.
	 * This method is non-blocking.
	 * @param loops The amount of times the sound should be played
	 * @return The clip id or ERROR (-1) in case of faliure
	 */
	public long play(final int loops) {
		final long id = currentId++;
		final Clip clip = createClip(id);
		
		if (clip == null) {
			return ERROR;
		}
		
		final FloatControl control = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
		
		gain = Math.min(Math.max(gain, control.getMinimum()), control.getMaximum());
		
		control.setValue(gain);
		clip.loop(loops - 1);
		
		return id;
	}
	
	/**
	 * Plays the sound once.
	 * Same as passing "1" to the method "play".
	 * @return The clip id or ERROR (-1) in case of faliure
	 */
	public long play() {
		return play(1);
	}
	
	/**
	 * Stops the clip with the given id.
	 * @param id The sound id
	 */
	public void stop(final long id) {
		final Clip clip = clips.get(id);
		
		if (clip != null) {
			clip.stop();
			clip.flush();
			clip.close();
			clips.remove(id);
		}
	}
	
	/**
	 * Sets the gain for the sound.
	 * The value gets clamped between the maximum and minumum gain which differs.
	 * Negative values are allowed.
	 * @param gain The new gain value
	 */
	public void setGain(final float gain) {
		this.gain = gain;
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
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			final byte[] buffer = new byte[BUFFER_SIZE];
			
			while (in.read(buffer, 0, BUFFER_SIZE) != -1) {
				out.write(buffer, 0, BUFFER_SIZE);
			}
			
			final byte[] data = out.toByteArray();
			final Sound sound = new Sound(data, format);
			
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
			final int length = (int)file.length();
			final byte[] data = new byte[length];
			final AudioFormat format = in.getFormat();
			
			in.read(data, 0, length);
			
			final Sound sound = new Sound(data, format);
			
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
