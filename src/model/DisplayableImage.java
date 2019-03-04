    package model;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Display image abstract class
 *
 */
public abstract class DisplayableImage {
	/**
	 * Image position
	 */
	public int x;
	/**
	 * Image position
	 */
	public int y;
	/**
	 * Image width
	 */
	int width;
	/**
	 * Image height
	 */
	int height;
	/**
	 * Image object
	 */
	BufferedImage image;

	/**
	 * Construction method
	 * 
	 * @param x
	 *           
	 * @param y
	 *           
	 * @param width
	 *        
	 * @param height
	 *         
	 */
	public DisplayableImage(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
	}

	/**
	 * Construction method
	 * 
	 * @param x
	 *           
	 * @param y
	 *           
	 * @param url
	 *           Picture path
	 */
	public DisplayableImage(int x, int y, String url) {
		this.x = x;
		this.y = y;
		try {
			image = ImageIO.read(new File(url));// Get the image object of this path
			this.width = image.getWidth();
			this.height = image.getHeight();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public DisplayableImage() {

	}

	/**
	 * Get image
	 * 
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 *Set picture
	 * 
	 * @param image
	 *           
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
	}

	/**
	 * Set picture
	 * 
	 * @param image
	 *         
	 */
	public void setImage(String url) {
		try {
			this.image = ImageIO.read(new File(url));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Determine if a collision has occurred
	 * 
	 * @param v
	 *           Target image object
	 * @return Return true if the two intersect, otherwise return false
	 */
	public boolean hit(DisplayableImage v) {
		return hit(v.getRect());// ִExecution overload method
	}

	/**
	 * Determine if a collision has occurred
	 * 
	 * @param r
	 *            Target boundary
	 * @return Return true if the two intersect, otherwise return false
	 */
	public boolean hit(Rectangle r) {
		if (r == null) {// If the target is empty
			return false;// Return without collision
		}
		return getRect().intersects(r);
	}

	/**
	 * Get the boundary object
	 */
	public Rectangle getRect() {
		//Create a rectangular bounding object with coordinates (x, y) and width (height, height) and return
		return new Rectangle(x, y, width, height);
	}

	public int getWidth() {
		return width;
	}


	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
