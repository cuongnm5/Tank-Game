package model.wall;

import model.DisplayableImage;

/**
 * Wall, abstract class
 */
public abstract class Wall extends DisplayableImage {
	private boolean alive = true;

	/**
	 * Wall construction method
	 * 
	 * @param x
	 *          
	 * @param y
	 *      
	 * @param url
	 *    
	 */
	public Wall(int x, int y, String url) {
		super(x, y, url);// Call the parent class constructor
	}


	public boolean isAlive() {
		return alive;
	}


	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	/**
	 * Rewrite the judgment method. If the coordinates of the two wall blocks are the same, 
         * then the two wall blocks are considered to be the same wall block.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Wall) {
			Wall w = (Wall) obj;
			if (w.x == x && w.y == y) {
				return true;
			}
		}
		return super.equals(obj);
	}
}
