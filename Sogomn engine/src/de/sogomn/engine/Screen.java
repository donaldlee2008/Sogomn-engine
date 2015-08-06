package de.sogomn.engine;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

/**
 * This class represents a screen.
 * Uses JFrame and Canvas classes internally.
 * @author Sogomn
 *
 */
public final class Screen {
	
	private JFrame frame;
	private Canvas canvas;
	private Mouse mouse;
	private Keyboard keyboard;
	private BufferedImage image;
	
	private String title;
	private boolean open, visible;
	
	private int initialWidth, initialHeight;
	private int canvasWidth, canvasHeight;
	private int renderWidth, renderHeight;
	private int renderX, renderY;
	private ResizeBehaviour resizeBehaviour;
	
	private IGameController controller;
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
		this.title = title;
		
		final WindowAdapter closingAdapter = new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent w) {
				close();
			}
		};
		
		final ComponentAdapter resizeAdapter = new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent c) {
				canvasWidth = canvas.getWidth();
				canvasHeight = canvas.getHeight();
				
				if (resizeBehaviour == ResizeBehaviour.STRETCH) {
					stretchContent();
				} else if (resizeBehaviour == ResizeBehaviour.KEEP_ASPECT_RATIO) {
					fitContent();
				}
				
				final float scaleX = (float)renderWidth / initialWidth;
				final float scaleY = (float)renderHeight / initialHeight;
				
				mouse.setScale(scaleX, scaleY);
				mouse.setOffset(renderX, renderY);
			}
		};
		
		
		frame = new JFrame(title);
		canvas = new Canvas();
		mouse = new Mouse();
		keyboard = new Keyboard();
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		open = true;
		visible = false;
		initialWidth = width;
		initialHeight = height;
		canvasWidth = renderWidth = width;
		canvasHeight = renderHeight = height;
		resizeBehaviour = ResizeBehaviour.STRETCH;
		
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.addMouseListener(mouse);
		canvas.addMouseMotionListener(mouse);
		canvas.addMouseWheelListener(mouse);
		canvas.addKeyListener(keyboard);
		
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(closingAdapter);
		frame.addComponentListener(resizeAdapter);
		frame.add(canvas, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationByPlatform(true);
	}
	
	/**
	 * Constructs a new Screen object with the given width, height and no title.
	 * @param width The screen width
	 * @param height The screen height
	 */
	public Screen(final int width, final int height) {
		this(width, height, NO_TITLE);
	}
	
	private void stretchContent() {
		renderWidth = canvasWidth;
		renderHeight = canvasHeight;
		renderX = renderY = 0;
	}
	
	private void fitContent() {
		final float ratioX = (float)canvasWidth / initialWidth;
		final float ratioY = (float)canvasHeight / initialHeight;
		
		if (ratioX < ratioY) {
			renderWidth = (int)(initialWidth * ratioX);
			renderHeight = (int)(initialHeight * ratioX);
		} else {
			renderWidth = (int)(initialWidth * ratioY);
			renderHeight = (int)(initialHeight * ratioY);
		}
		
		renderX = (canvasWidth / 2) - (renderWidth / 2);
		renderY = (canvasHeight / 2) - (renderHeight / 2);
	}
	
	private void updateMainTickable(final float delta) {
		if (!isVisible()) {
			return;
		}
		
		if (controller != null) {
			controller.update(delta);
		}
	}
	
	private void drawMainTickable() {
		if (!isVisible()) {
			return;
		}
		
		final BufferStrategy bs = canvas.getBufferStrategy();
		final Graphics2D g2 = (Graphics2D)bs.getDrawGraphics();
		
		if (controller != null) {
			final Graphics2D g = image.createGraphics();
			
			controller.draw(g);
			
			g.dispose();
		}
		
		g2.clearRect(0, 0, canvasWidth, canvasHeight);
		g2.drawImage(image, renderX, renderY, renderWidth, renderHeight, null);
		
		g2.dispose();
		bs.show();
	}
	
	/**
	 * Updates the screen and calls the methods in the ITickable object, if existent.
	 */
	public void update() {
		if (!isOpen()) {
			return;
		}
		
		final long now = System.nanoTime();
		final float elapsed = (now - lastTime) / NANO_SECONDS_PER_SECOND;
		
		updateMainTickable(elapsed);
		drawMainTickable();
		
		lastTime = now;
	}
	
	/**
	 * Shows the screen.
	 */
	public void show() {
		if (!isOpen()) {
			return;
		}
		
		frame.setVisible(true);
		canvas.createBufferStrategy(BUFFER_COUNT);
		
		lastTime = System.nanoTime();
		visible = true;
	}
	
	/**
	 * Hides the screen. It can not be updated anymore.
	 */
	public void hide() {
		if (!isOpen()) {
			return;
		}
		
		visible = false;
		frame.setVisible(false);
	}
	
	/**
	 * Closes the screen.
	 */
	public void close() {
		hide();
		open = false;
		frame.dispose();
	}
	
	/**
	 * Sets the main ITickable object this screen should update.
	 * 
	 * @param newController The IGameController object to be notified every frame
	 */
	public void setGameController(final IGameController newController) {
		if (controller != null) {
			mouse.removeListener(controller);
			keyboard.removeListener(controller);
		}
		
		controller = newController;
		
		mouse.addListener(controller);
		keyboard.addListener(controller);
	}
	
	/**
	 * Sets the resizable flag of the screen.
	 * @param resizable The state
	 */
	public void setResizable(final boolean resizable) {
		frame.setResizable(resizable);
	}
	
	/**
	 * Sets the title for the screen.
	 * @param title The title the screen should have from now on
	 */
	public void setTitle(final String title) {
		this.title = title;
	}
	
	/**
	 * Sets the resize behavior of the screen. Either stretched or fitted.
	 * @param resizeBehaviour The resize behavior
	 */
	public void setResizeBehaviour(final ResizeBehaviour resizeBehaviour) {
		this.resizeBehaviour = resizeBehaviour;
	}
	
	/**
	 * Sets the icon for this screen. It will be displayed on the top left of the frame as well as in the taskbar.
	 * @param image The icon as an image
	 */
	public void setIcon(final BufferedImage image) {
		frame.setIconImage(image);
	}
	
	/**
	 * Returns whether the screen is open and can be used.
	 * @return The state
	 */
	public boolean isOpen() {
		return open;
	}
	
	/**
	 * Returns whether the screen is visible.
	 * @return The state
	 */
	public boolean isVisible() {
		return visible;
	}
	
	/**
	 * Returns the inial width of the screen. This will not change, even when resized.
	 * @return The width
	 */
	public int getInitialWidth() {
		return initialWidth;
	}
	
	/**
	 * Returns the inial height of the screen. This will not change, even when resized.
	 * @return The height
	 */
	public int getInitialHeight() {
		return initialHeight;
	}
	
	/**
	 * Returns the width of the screen. This does not include frame borders.
	 * @return The inner width
	 */
	public int getWidth() {
		return canvasWidth;
	}
	
	/**
	 * Returns the height of the screen. This does not include frame borders.
	 * @return The inner height
	 */
	public int getHeight() {
		return canvasHeight;
	}
	
	/**
	 * Returns the current title of the screen.
	 * @return The title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Returns the current resize behavior of the screen.
	 * @return The resize behavior
	 */
	public ResizeBehaviour getResizeBehaviour() {
		return resizeBehaviour;
	}
	
	/**
	 * Holds the different resize behaviors for the Screen class.
	 * @author Sogomn
	 *
	 */
	public enum ResizeBehaviour {
		
		STRETCH,
		KEEP_ASPECT_RATIO;
		
	}
	
}
