package model;

import java.awt.Graphics;

import frame.GamePanel;
import util.ImageUtil;

/**
 * Show explosion effect (picture)
 *
 */
public class Boom extends DisplayableImage {

	private int timer = 0;
	private int fresh = GamePanel.FRESHTIME;
	private boolean alive = true;

	/**
	 * Explosive effect method
	 * 
	 * @param x
	 * @param y
	 *          
	 */
	public Boom(int x, int y) {
		super(x, y, ImageUtil.BOOM_IMAGE_URL);// Call the parent class constructor, using the default explosion effect image
	}

	/**
	 * Show explosion image, this photo only shows 0.3 seconds
	 * 
	 * @param g
	 *            
	 */
	public void show(Graphics g) {
		if (timer >= 300) {// When the timer has been recorded for 0.3 seconds
			alive = false;
		} else {
			g.drawImage(getImage(), x, y, null);// Draw an explosion effect
			timer += fresh;//The timer increments according to the refresh time
		}
	}

	/**
	 * Whether the explosion image is valid
	 * 
	 * @return
	 */
	public boolean isAlive() {
		return alive;
	}
}
