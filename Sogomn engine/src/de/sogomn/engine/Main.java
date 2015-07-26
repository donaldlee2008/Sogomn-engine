package de.sogomn.engine;

import java.awt.Color;
import java.awt.Graphics2D;

import de.sogomn.engine.Screen.ResizeBehaviour;


final class Main {
	
	private static final int WIDTH = 500;
	private static final int HEIGHT = 500;
	
	private Main() {
		//...
	}
	
	public static void main(final String[] args) {
		final Screen screen = new Screen(WIDTH, HEIGHT);
		
		screen.setGameController(new IGameController() {
			private int x, y;
			@Override
			public void update(final float delta) {
				//...
			}
			@Override
			public void draw(final Graphics2D g) {
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, WIDTH, HEIGHT);
				g.setColor(Color.RED);
				g.fillOval(x - 5, y - 5, 10, 10);
			}
			@Override
			public void mouseEvent(final int x, final int y, final int button, final boolean flag) {
				//...
			}
			@Override
			public void mouseMotionEvent(final int x, final int y, final int button, final boolean flag) {
				this.x = x;
				this.y = y;
			}
			@Override
			public void mouseWheelEvent(final int x, final int y, final int rotation) {
				//...
			}
		});
		screen.setResizeBehaviour(ResizeBehaviour.KEEP_ASPECT_RATIO);
		screen.show();
		
		while (screen.isOpen()) {
			screen.update();
		}
	}
	
}
