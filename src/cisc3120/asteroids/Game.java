package cisc3120.asteroids;

import java.awt.*;
import java.awt.event.*;
//import java.awt.image.BufferStrategy;

/**
 * 
 * A painted canvas in its own window, updated every 0.01 second. This class sets
 * up the basic logic for a "game canvas" on which the game objects will be
 * displayed and moved.
 * 
 */

class Game extends Canvas {
	protected boolean on = true; // to end the game, set 'on' to false...
	protected int width, height; // dimensions of game canvas
	protected Image buffer; // used in our primitive double-buffering solution

	/**
	 * 
	 * Creates the game canvas. The canvas is a labeled window of the given
	 * dimensions.
	 * 
	 * @param name
	 *            String used to label the game window
	 * @param inWidth
	 *            Width of the game canvas
	 * @param inHeight
	 *            Height of the game canvas
	 */

	public Game(String name, int inWidth, int inHeight) {
		width = inWidth;
		height = inHeight;

		// Frame can be read as 'window' here.

		Frame frame = new Frame(name); // labeling works with one of Frame's
										// constructors
		frame.add(this);
		frame.setSize(width, height);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		requestFocus(); // 'focus' means that this window will receive our
						// keyclicks to control the game

		buffer = createImage(width, height); // again, for double-buffering
	}
	
	//game loop
	public void run()
	{
		
	}

	/**
	 * 
	 * Where the 'magic' happens. Paints a buffer to the screen, then waits a
	 * hundredth of a second before repeating itself.
	 * 
	 * @see java.awt.Canvas#update(java.awt.Graphics)
	 */
	@Override
	public void update(Graphics brush) {
		paint(buffer.getGraphics());
		brush.drawImage(buffer, 0, 0, this);
		if (on) {
			sleep(10);
			repaint();
			// super.update(brush);
		}
	}


	/**
	 * A simple helper function. "Sleeps" for a given period.
	 * 
	 * @param time	The sleep duration in milliseconds
	 * @see java.lang.Thread#sleep
	 */
	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (Exception exc) {

		}
		;
	}
}