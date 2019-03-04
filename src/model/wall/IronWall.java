package model.wall;

import util.ImageUtil;

/**
 * Iron wall
 *
 */
public class IronWall extends Wall {
	/**
	 * 
	 * 
	 * @param x
	 *
	 * @param y
	 *    
	 */
	public IronWall(int x, int y) {
		super(x, y, ImageUtil.IRONWALL_IMAGE_URL);
	}

}
