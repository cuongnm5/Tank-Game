package model.wall;

import util.ImageUtil;

/**
 * Grass wall
 *
 */
public class GrassWall extends Wall {
	/**
	 * 
	 * 
	 * @param x
	 *   
	 * @param y
	 *   
	 */
	public GrassWall(int x, int y) {
		super(x, y, ImageUtil.GRASSWALL_IMAGE_URL);
	}
}
