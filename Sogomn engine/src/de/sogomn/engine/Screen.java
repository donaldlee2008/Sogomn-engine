package de.sogomn.engine;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

import javax.swing.JFrame;

import de.sogomn.engine.util.AbstractListenerContainer;
import de.sogomn.engine.util.ImageUtils;

/**
 * This class represents a screen.
 * Uses JFrame and Canvas classes internally.
 * Uses double buffering.
 * @author Sogomn
 *
 */
public final class Screen extends AbstractListenerContainer<IDrawable> {
	
	private JFrame frame;
	private Canvas canvas;
	private Mouse mouse;
	private Keyboard keyboard;
	
	private GraphicsDevice display;
	private VolatileImage screenImage;
	
	private boolean open;
	private int initialWidth, initialHeight;
	private int canvasWidth, canvasHeight;
	private int renderWidth, renderHeight;
	private int renderX, renderY;
	private ResizeBehavior resizeBehavior;
	
	private static final int BUFFER_COUNT = 2;
	private static final String NO_TITLE = "";
	
	/**
	 * Represents a hidden cursor.
	 */
	public static final Cursor NO_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "No cursor");
	
	/**
	 * Constructs a new Screen object with the given width, height and title.
	 * The screen can be resized.
	 * @param width The screen width
	 * @param height The screen height
	 * @param title The screen title
	 */
	public Screen(final int width, final int height, final String title) {
		final WindowAdapter closingAdapter = new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent w) {
				close();
			}
		};
		
		final ComponentAdapter resizeAdapter = new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent c) {
				calculateViewport();
				canvas.requestFocus();
			}
		};
		
		frame = new JFrame(title);
		canvas = new Canvas();
		mouse = new Mouse();
		keyboard = new Keyboard();
		
		display = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		
		open = true;
		initialWidth = canvasWidth = renderWidth = width;
		initialHeight = canvasHeight = renderHeight = height;
		resizeBehavior = ResizeBehavior.STRETCH;
		
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
	
	private VolatileImage createScreenImage() {
		final VolatileImage image = canvas.createVolatileImage(initialWidth, initialHeight);
		
		return image;
	}
	
	private void calculateViewport() {
		canvasWidth = canvas.getWidth();
		canvasHeight = canvas.getHeight();
		
		if (resizeBehavior == ResizeBehavior.STRETCH) {
			stretchContentSize();
		} else if (resizeBehavior == ResizeBehavior.KEEP_ASPECT_RATIO) {
			fitContentSize();
		} else if (resizeBehavior == ResizeBehavior.KEEP_SIZE) {
			keepContentSize();
		} else if (resizeBehavior == ResizeBehavior.DO_NOTHING) {
			return;
		}
		
		renderX = (canvasWidth / 2) - (renderWidth / 2);
		renderY = (canvasHeight / 2) - (renderHeight / 2);
		
		final float scaleX = (float)renderWidth / initialWidth;
		final float scaleY = (float)renderHeight / initialHeight;
		
		mouse.setScale(scaleX, scaleY);
		mouse.setOffset(renderX, renderY);
	}
	
	private void stretchContentSize() {
		renderWidth = canvasWidth;
		renderHeight = canvasHeight;
	}
	
	private void fitContentSize() {
		final float ratioX = (float)canvasWidth / initialWidth;
		final float ratioY = (float)canvasHeight / initialHeight;
		
		if (ratioX < ratioY) {
			renderWidth = (int)(initialWidth * ratioX);
			renderHeight = (int)(initialHeight * ratioX);
		} else {
			renderWidth = (int)(initialWidth * ratioY);
			renderHeight = (int)(initialHeight * ratioY);
		}
	}
	
	private void keepContentSize() {
		renderWidth = initialWidth;
		renderHeight = initialHeight;
	}
	
	private void notifyDrawables(final Graphics2D g) {
		synchronized (listeners) {
			for (int i = 0; i < getListenerCount(); i++) {
				final IDrawable drawable = listeners.get(i);
				
				drawable.draw(g);
			}
		}
	}
	
	private int validateScreenImage() {
		final GraphicsConfiguration graphicsConfiguration = canvas.getGraphicsConfiguration();
		final int returnCode = screenImage.validate(graphicsConfiguration);
		
		return returnCode;
	}
	
	private void drawOffscreen() {
		do {
			final int returnCode = validateScreenImage();
			
			if (returnCode == VolatileImage.IMAGE_INCOMPATIBLE) {
				screenImage = createScreenImage();
			}
			
			final Graphics2D g = screenImage.createGraphics();
			
			ImageUtils.applyLowGraphics(g);
			
			notifyDrawables(g);
			
			g.dispose();
		} while (screenImage.contentsLost());
	}
	
	private void drawToScreen() {
		final BufferStrategy bufferStrategy = canvas.getBufferStrategy();
		
		do {
			final int returnCode = validateScreenImage();
			
			if (returnCode == VolatileImage.IMAGE_RESTORED) {
				drawOffscreen();
			} else if (returnCode == VolatileImage.IMAGE_INCOMPATIBLE) {
				screenImage = createScreenImage();
				
				drawOffscreen();
			}
			
			final Graphics2D canvasGraphics = (Graphics2D)bufferStrategy.getDrawGraphics();
			
			ImageUtils.applyLowGraphics(canvasGraphics);
			
			canvasGraphics.clearRect(0, 0, canvasWidth, canvasHeight);
			canvasGraphics.drawImage(screenImage, renderX, renderY, renderWidth, renderHeight, null);
			
			canvasGraphics.dispose();
		} while (bufferStrategy.contentsLost());
		
		bufferStrategy.show();
	}
	
	/**
	 * Validates the screen image and creates a new one, if incompatible.
	 * This uses hardware accelerated graphics.
	 * Notifies all listening IDrawable objects.
	 * Then swaps buffers.
	 */
	public synchronized void redraw() {
		if (!isVisible()) {
			return;
		}
		
		drawOffscreen();
		drawToScreen();
	}
	
	/**
	 * Shows the screen and initializes the graphics.
	 */
	public synchronized void show() {
		if (!isOpen()) {
			return;
		}
		
		frame.setVisible(true);
		screenImage = createScreenImage();
		calculateViewport();
		canvas.createBufferStrategy(BUFFER_COUNT);
		canvas.requestFocus();
	}
	
	/**
	 * Hides the screen. It can not be updated anymore.
	 */
	public synchronized void hide() {
		if (!isOpen()) {
			return;
		}
		
		frame.setVisible(false);
	}
	
	/**
	 * Closes the screen.
	 */
	public synchronized void close() {
		hide();
		open = false;
		frame.dispose();
	}
	
	/**
	 * Adds a mouse listener to the screen.
	 * @param listener The listener
	 */
	public void addMouseListener(final IMouseListener listener) {
		mouse.addListener(listener);
	}
	
	/**
	 * Removes a mouse listener from the screen.
	 * @param listener The listener
	 */
	public void removeMouseListener(final IMouseListener listener) {
		mouse.removeListener(listener);
	}
	
	/**
	 * Adds a keyboard listener to the screen.
	 * @param listener The listener
	 */
	public void addKeyboardListener(final IKeyboardListener listener) {
		keyboard.addListener(listener);
	}
	
	/**
	 * Removes a keyboard listener from the screen.
	 * @param listener The listener
	 */
	public void removeKeyboardListener(final IKeyboardListener listener) {
		keyboard.removeListener(listener);
	}
	
	/**
	 * Sets the resizable flag of the screen.
	 * @param resizable The state
	 */
	public void setResizable(final boolean resizable) {
		frame.setResizable(resizable);
	}
	
	/**
	 * Sets the title of the screen.
	 * @param title The title the screen should have from now on
	 */
	public void setTitle(final String title) {
		frame.setTitle(title);
	}
	
	/**
	 * Sets the full screen flag for the screen.
	 * If true and supported, this will toggle full screen mode.
	 * This will cap the drawing rate to what the default display configuration supports.
	 * There may be performance issues.
	 * @param fullScreen The full screen flag
	 */
	public void setFullScreen(final boolean fullScreen) {
		final boolean fullScreenAllowed = display.isFullScreenSupported();
		
		if (fullScreen && fullScreenAllowed) {
			display.setFullScreenWindow(frame);
		} else {
			display.setFullScreenWindow(null);
		}
		
		canvas.requestFocus();
	}
	
	/**
	 * Sets the resize behavior of the screen.
	 * @param resizeBehavior The resize behavior
	 */
	public void setResizeBehavior(final ResizeBehavior resizeBehavior) {
		this.resizeBehavior = resizeBehavior;
	}
	
	/**
	 * Sets the icon of the screen. It will be displayed on the top left of the frame as well as in the taskbar.
	 * @param image The icon as an image
	 */
	public void setIcon(final BufferedImage image) {
		frame.setIconImage(image);
	}
	
	/**
	 * Sets the size of the inner screen. The method "hide" will be called.
	 * If the screen was visible before it will reappear.
	 * @param width The target width
	 * @param height The target height
	 */
	public void setSize(final int width, final int height) {
		final boolean wasVisible = isVisible();
		
		hide();
		
		canvas.setPreferredSize(new Dimension(width, height));
		frame.pack();
		frame.setLocationRelativeTo(null);
		
		if (wasVisible) {
			show();
		}
	}
	
	/**
	 * Sets the cursor for the screen. Pass the static variable NO_CURSOR to hide it.
	 * @param cursor The new cursor
	 */
	public void setCursor(final Cursor cursor) {
		frame.setCursor(cursor);
	}
	
	/**
	 * Sets the background color of the screen.
	 * @param color The background color
	 */
	public void setBackgroundColor(final Color color) {
		canvas.setBackground(color);
	}
	
	/**
	 * Returns whether the screen is open and can be used or not.
	 * @return The state
	 */
	public boolean isOpen() {
		return open;
	}
	
	/**
	 * Returns whether the screen is visible or not.
	 * This does not respect minimization.
	 * @return The state
	 */
	public boolean isVisible() {
		return frame.isVisible();
	}
	
	/**
	 * Returns whether the screen is resizable or not.
	 * @return True if it is resizable; false otherwise
	 */
	public boolean isResizable() {
		return frame.isResizable();
	}
	
	/**
	 * Returns the full screen flag of the screen.
	 * @return True if the screen is in full screen mode; false otherwise
	 */
	public boolean isFullScreen() {
		final Window window = display.getFullScreenWindow();
		final boolean fullScreen = window != null && window == frame;
		
		return fullScreen;
	}
	
	/**
	 * Returns the inial width of the screen.
	 * This will not change, even when resized.
	 * @return The width
	 */
	public int getInitialWidth() {
		return initialWidth;
	}
	
	/**
	 * Returns the inial height of the screen.
	 * This will not change, even when resized.
	 * @return The height
	 */
	public int getInitialHeight() {
		return initialHeight;
	}
	
	/**
	 * Returns the width of the whole screen.
	 * This does not include frame borders.
	 * @return The inner width
	 */
	public int getWidth() {
		return canvasWidth;
	}
	
	/**
	 * Returns the height of the whole screen.
	 * This does not include frame borders.
	 * @return The inner height
	 */
	public int getHeight() {
		return canvasHeight;
	}
	
	/**
	 * Returns the width of the content.
	 * May differ from the screen width depending on the resize behavior.
	 * @return The render width
	 */
	public int getRenderWidth() {
		return renderWidth;
	}
	
	/**
	 * Returns the height of the content.
	 * May differ from the screen width depending on the resize behavior.
	 * @return The render height
	 */
	public int getRenderHeight() {
		return renderHeight;
	}
	
	/**
	 * Returns the current title of the screen.
	 * @return The title
	 */
	public String getTitle() {
		return frame.getTitle();
	}
	
	/**
	 * Returns the current resize behavior of the screen.
	 * @return The resize behavior
	 */
	public ResizeBehavior getResizeBehavior() {
		return resizeBehavior;
	}
	
	/**
	 * Holds the different resize behaviors for the Screen class.
	 * @author Sogomn
	 *
	 */
	public enum ResizeBehavior {
		
		/**
		 * The content fills the screen.
		 */
		STRETCH,
		
		/**
		 * The content fills as much as possible but maintains the aspect ratio.
		 */
		KEEP_ASPECT_RATIO,
		
		/**
		 * The content size does not change.
		 * It is displayed in the center of the screen.
		 */
		KEEP_SIZE,
		
		/**
		 * There is no action when resizing.
		 */
		DO_NOTHING;
		
	}
	
}
