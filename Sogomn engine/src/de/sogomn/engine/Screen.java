package de.sogomn.engine;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public final class Screen {
	
	private int width, height;
	private String title;
	private boolean open, visible;
	
	private JFrame frame;
	private Canvas canvas;
	private BufferedImage image;
	
	private int renderWidth, renderHeight;
	
	private ITickable mainTickable;
	private long lastTime;
	
	private static final float NANO_SECONDS_PER_SECOND = 1000000000.0f;
	private static final int BUFFER_COUNT = 2;
	private static final String NO_TITLE = "";
	
	/**
	 * Constructs a new Screen object with the given width, height and title.
	 * The screen can still be resized. The rendered image will be streched then.
	 * @param width The screen width
	 * @param height The screen height
	 * @param title The screen title
	 */
	public Screen(final int width, final int height, final String title) {
		this.width = width;
		this.height = height;
		this.title = title;
		
		final WindowAdapter closingAdapter = new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent w) {
				close();
			}
		};
		
		open = true;
		visible = false;
		frame = new JFrame(title);
		canvas = new Canvas();
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		renderWidth = width;
		renderHeight = height;
		lastTime = System.nanoTime();
		
		canvas.setPreferredSize(new Dimension(width, height));
		
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(closingAdapter);
		frame.add(canvas, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}
	
	/**
	 * Constructs a new Screen object with the given width, height and no title.
	 * @param width The screen width
	 * @param height The screen height
	 */
	public Screen(final int width, final int height) {
		this(width, height, NO_TITLE);
	}
	
	private void updateMainTickable(final float delta) {
		if (mainTickable != null) {
			mainTickable.update(delta);
		}
	}
	
	private void drawMainTickable() {
		if (!visible) {
			return;
		}
		
		final BufferStrategy bs = canvas.getBufferStrategy();
		final Graphics2D g2 = (Graphics2D)bs.getDrawGraphics();
		
		if (mainTickable != null) {
			final Graphics2D g = image.createGraphics();
			
			mainTickable.draw(g);
			
			g.dispose();
		}
		
		g2.drawImage(image, 0, 0, renderWidth, renderHeight, null);
		
		g2.dispose();
		bs.show();
	}
	
	/**
	 * Updates the screen and calls the methods in the ITickable object, if existent
	 */
	public void update() {
		final long now = System.nanoTime();
		final float elapsed = (now - lastTime) / NANO_SECONDS_PER_SECOND;
		
		updateMainTickable(elapsed);
		drawMainTickable();
		
		lastTime = now;
	}
	
	/**
	 * Shows the screen
	 */
	public void show() {
		frame.setVisible(true);
		canvas.createBufferStrategy(BUFFER_COUNT);
		visible = true;
	}
	
	/**
	 * Hides the screen
	 */
	public void hide() {
		visible = false;
		frame.setVisible(false);
	}
	
	/**
	 * Closes the screen
	 */
	public void close() {
		hide();
		open = false;
		frame.dispose();
	}
	
	/**
	 * Sets the main ITickable object this screen should update
	 * 
	 * @param mainTickable The main ITickable object
	 */
	public void setMainTickable(final ITickable mainTickable) {
		this.mainTickable = mainTickable;
	}
	
	/**
	 * Returns whether the screen is open and can be used
	 * @return The state
	 */
	public boolean isOpen() {
		return open;
	}
	
	/**
	 * Returns wheter the screen is visible
	 * @return The state
	 */
	public boolean isVisible() {
		return visible;
	}
	
	/**
	 * Returns the inial width of the screen. This will not change, even when resized
	 * @return The width
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Returns the inial height of the screen. This will not change, even when resized
	 * @return The height
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Returns the current title of the screen
	 * @return The title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Sets the title for the screen
	 * @param title The title the screen should have from now on
	 */
	public void setTitle(final String title) {
		this.title = title;
	}
	
}
