package de.sogomn.engine;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import de.sogomn.engine.util.AbstractListenerContainer;

final class Mouse extends AbstractListenerContainer<IMouseListener> implements MouseListener, MouseMotionListener, MouseWheelListener {
	
	private float scaleX, scaleY;
	private int offsetX, offsetY;
	
	private static final boolean PRESSED = true;
	private static final boolean RELEASED = false;
	private static final int NO_SCALE = 1;
	private static final int NO_OFFSET = 0;
	
	public Mouse() {
		scaleX = scaleY = NO_SCALE;
		offsetX = offsetY = NO_OFFSET;
	}
	
	private int getRelativeX(final int x) {
		final int relativeX = (int)((x / scaleX) - (offsetX / scaleX));
		
		return relativeX;
	}
	
	private int getRelativeY(final int y) {
		final int relativeY = (int)((y / scaleY) - (offsetY / scaleY));
		
		return relativeY;
	}
	
	private void fireMouseEvent(final MouseEvent m, final boolean flag) {
		final int x = getRelativeX(m.getX());
		final int y = getRelativeY(m.getY());
		final int button = m.getButton();
		
		synchronized (listeners) {
			for (int i = 0; i < getListenerCount(); i++) {
				final IMouseListener listener = listeners.get(i);
				
				listener.mouseEvent(x, y, button, flag);
			}
		}
	}
	
	private void fireMouseMovedEvent(final MouseEvent m, final boolean flag) {
		final int x = getRelativeX(m.getX());
		final int y = getRelativeY(m.getY());
		final int button = m.getButton();
		
		synchronized (listeners) {
			for (int i = 0; i < getListenerCount(); i++) {
				final IMouseListener listener = listeners.get(i);
				
				listener.mouseMotionEvent(x, y, button, flag);
			}
		}
	}
	
	private void fireMouseWheelEvent(final MouseWheelEvent m) {
		final int x = getRelativeX(m.getX());
		final int y = getRelativeY(m.getY());
		final int rotation = m.getWheelRotation();
		
		synchronized (listeners) {
			for (int i = 0; i < getListenerCount(); i++) {
				final IMouseListener listener = listeners.get(i);
				
				listener.mouseWheelEvent(x, y, rotation);
			}
		}
	}
	
	@Override
	public void mousePressed(final MouseEvent m) {
		fireMouseEvent(m, PRESSED);
	}
	
	@Override
	public void mouseReleased(final MouseEvent m) {
		fireMouseEvent(m, RELEASED);
	}
	
	@Override
	public void mouseMoved(final MouseEvent m) {
		fireMouseMovedEvent(m, RELEASED);
	}
	
	@Override
	public void mouseDragged(final MouseEvent m) {
		fireMouseMovedEvent(m, PRESSED);
	}
	
	@Override
	public void mouseWheelMoved(final MouseWheelEvent m) {
		fireMouseWheelEvent(m);
	}
	
	@Override
	public void mouseClicked(final MouseEvent m) {
		//...
	}
	
	@Override
	public void mouseEntered(final MouseEvent m) {
		//...
	}
	
	@Override
	public void mouseExited(final MouseEvent m) {
		//...
	}
	
	public void reset() {
		scaleX = scaleY = 0;
		offsetX = offsetY = 0;
	}
	
	public void setScale(final float scaleX, final float scaleY) {
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}
	
	public void setOffset(final int offsetX, final int offsetY) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}
	
}
