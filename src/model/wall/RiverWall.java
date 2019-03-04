package model.wall;

import util.ImageUtil;

/**
 * River wall
 *
 */
public class RiverWall extends Wall {
	/**
	 * 
	 * 
	 * @param x
	 *            
	 * @param y
	 *        
	 */
	public RiverWall(int x, int y) {
		super(x, y, ImageUtil.RIVERWALL_IMAGE_URL);
	}

}
