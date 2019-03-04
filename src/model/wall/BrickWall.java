package model.wall;

import util.ImageUtil;

/**
 * Brick wall
 *
 */
public class BrickWall extends Wall {
	/**
	 * 
	 * 
	 * @param x
	 *        
	 * @param y
	 *       
	 */
	public BrickWall(int x, int y) {
		super(x, y, ImageUtil.BRICKWALL_IMAGE_URL);
	}

}
