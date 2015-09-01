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
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

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
	
	private BufferedImage screenImage;
	private Graphics2D imageGraphics;
	private int[] pixelRaster;
	private IShader shader;
	
	private String title;
	private boolean open, visible;
	
	private int initialWidth, initialHeight;
	private int canvasWidth, canvasHeight;
	private int renderWidth, renderHeight;
	private int renderX, renderY;
	private ResizeBehavior resizeBehavior;
	
	private static final int BUFFER_COUNT = 2;
	private static final String NO_TITLE = "";
	
	/**
	 * Constructs a new Screen object with the given width, height and title.
	 * The screen can be resized.
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
				
				if (resizeBehavior == ResizeBehavior.STRETCH) {
					stretchContentSize();
				} else if (resizeBehavior == ResizeBehavior.KEEP_ASPECT_RATIO) {
					fitContentSize();
				} else if (resizeBehavior == ResizeBehavior.KEEP_SIZE) {
					keepContentSize();
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
		screenImage = frame.getGraphicsConfiguration().createCompatibleImage(width, height);
		pixelRaster = ((DataBufferInt)screenImage.getRaster().getDataBuffer()).getData();
		
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
	
	private void initGraphics() {
		canvas.createBufferStrategy(BUFFER_COUNT);
		imageGraphics = screenImage.createGraphics();
		
		ImageUtils.applyLowGraphics(imageGraphics);
	}
	
	private void stretchContentSize() {
		renderWidth = canvasWidth;
		renderHeight = canvasHeight;
		renderX = renderY = 0;
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
		
		renderX = (canvasWidth / 2) - (renderWidth / 2);
		renderY = (canvasHeight / 2) - (renderHeight / 2);
	}
	
	private void keepContentSize() {
		renderWidth = initialWidth;
		renderHeight = initialHeight;
		
		renderX = (canvasWidth / 2) - (renderWidth / 2);
		renderY = (canvasHeight / 2) - (renderHeight / 2);
	}
	
	private void drawDrawables(final Graphics2D g) {
		synchronized (listeners) {
			for (int i = 0; i < size(); i++) {
				final IDrawable drawable = listeners.get(i);
				
				drawable.draw(g);
			}
		}
	}
	
	/**
	 * Notifies all listening IDrawable objects.
	 * Then applies the shader.
	 * Then swaps buffers.
	 */
	public synchronized void draw() {
		if (!isVisible()) {
			return;
		}
		
		drawDrawables(imageGraphics);
		
		if (shader != null) {
			shader.apply(pixelRaster);
		}
		
		final BufferStrategy bufferStrategy = canvas.getBufferStrategy();
		final Graphics2D canvasGraphics = (Graphics2D)bufferStrategy.getDrawGraphics();
		
		ImageUtils.applyLowGraphics(canvasGraphics);
		
		canvasGraphics.clearRect(0, 0, canvasWidth, canvasHeight);
		canvasGraphics.drawImage(screenImage, renderX, renderY, renderWidth, renderHeight, null);
		
		canvasGraphics.dispose();
		bufferStrategy.show();
	}
	
	/**
	 * Shows the screen.
	 */
	public void show() {
		if (!isOpen()) {
			return;
		}
		
		frame.setVisible(true);
		initGraphics();
		canvas.requestFocus();
		
		visible = true;
	}
	
	/**
	 * Hides the screen. It can not be updated anymore.
	 */
	public synchronized void hide() {
		if (!isOpen()) {
			return;
		}
		
		visible = false;
		imageGraphics.dispose();
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
	 * Sets the shader to be applied to the screen image.
	 * @param shader The shader
	 */
	public void setShader(final IShader shader) {
		this.shader = shader;
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
		this.title = title;
	}
	
	/**
	 * Sets the resize behavior of the screen.
	 * @param resizeBehaviour The resize behavior
	 */
	public void setResizeBehaviour(final ResizeBehavior resizeBehaviour) {
		this.resizeBehavior = resizeBehaviour;
	}
	
	/**
	 * Sets the icon of the screen. It will be displayed on the top left of the frame as well as in the taskbar.
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
	 * Returns whether the screen is visible or not.
	 * @return The state
	 */
	public boolean isVisible() {
		return visible;
	}
	
	/**
	 * Returns whether the screen is resizable or not.
	 * @return True if it is resizable; false otherwise
	 */
	public boolean isResizable() {
		return frame.isResizable();
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
	 * Returns the width of the content.
	 * Differs from the canvas width when the resize behavior is set to KEEP_ASPECT_RATIO.
	 * @return The render width
	 */
	public int getRenderWidth() {
		return renderWidth;
	}
	
	/**
	 * Returns the height 0of the content.
	 * Differs from the canvas width when the resize behavior is set to KEEP_ASPECT_RATIO.
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
		return title;
	}
	
	/**
	 * Returns the current resize behavior of the screen.
	 * @return The resize behavior
	 */
	public ResizeBehavior getResizeBehaviour() {
		return resizeBehavior;
	}
	
	/**
	 * Holds the different resize behaviors for the Screen class.
	 * @author Sogomn
	 *
	 */
	public enum ResizeBehavior {
		
		STRETCH,
		KEEP_ASPECT_RATIO,
		KEEP_SIZE;
		
	}
	
}
