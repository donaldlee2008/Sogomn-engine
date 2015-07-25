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
		final ITickable tickable = new ITickable() {
			@Override
			public void update(final float delta) {
				//...
			}
			
			@Override
			public void draw(final Graphics2D g) {
				g.setColor(Color.RED);
				g.fillRect(0, 0, WIDTH, HEIGHT);
			}
		};
		
		screen.setResizeBehaviour(ResizeBehaviour.KEEP_ASPECT_RATIO);
		screen.setMainTickable(tickable);
		screen.show();
		
		while (screen.isOpen()) {
			screen.update();
		}
	}
	
}
