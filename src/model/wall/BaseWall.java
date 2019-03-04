package model.wall;

import util.ImageUtil;

/**
 * Base
 *
 */
public class BaseWall extends Wall {
	/**
	 * Base construction method
	 * 
	 * @param x
	 *           
	 * @param y
	 *        
	 */
	public BaseWall(int x, int y) {
		super(x, y, ImageUtil.BASE_IMAGE_URL);//Call the parent class constructor, using the default base image
	}

}
