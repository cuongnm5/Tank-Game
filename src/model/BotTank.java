package model;

import java.awt.Rectangle;
import java.util.List;
import java.util.Random;

import enumtype.Direction;
import enumtype.TankType;
import frame.GamePanel;
import util.ImageUtil;

/**
 * Computer tanks in order to control the difficulty of the game, the probability of the computer tank moving randomly upward should be controlled to be smaller than other directions.
 * And the tank will move in the same direction every time, and the time to move continuously in a certain direction should be different.
 *
 */
public class BotTank extends Tank {
	private Random random = new Random();
	private Direction dir;
	private int freshTime = GamePanel.FRESHTIME;
	private int moveTimer = 0;
	private int beatBy = 1;
	private boolean pause = false;

	/**
	 * Get computer tank pause status
	 */
	public boolean isPause() {
		return pause;
	}

	/**
	 * Set computer tank pause status
	 */
	public void setPause(boolean pause) {
		this.pause = pause;
	}

	/**
	 * 
	 * Bot tank construction method
	 * 
	 * @param x
	 *            
	 * @param y
	 *            
	 * @param gamePanel
	 *            
	 * @param type
	 *            
	 */

	public BotTank(int x, int y, GamePanel gamePanel, TankType type) {
		super(x, y, getUrl(type), gamePanel, type);// Call the parent class constructor, using the default robot tank image
		dir = Direction.DOWN;// The direction of movement defaults downward
		// setSpeed(2);//Set the computer tank moving speed
		setAttackCoolDownTime(1000);// Set attack cooldown
	}

	private static String getUrl(TankType type) {
		String downImage = ImageUtil.NORMAL_BOT_DOWN_IMAGE_URL;
		switch (type) {//Check tank type
		case BOTTANK_NORMAL:
			downImage = ImageUtil.NORMAL_BOT_DOWN_IMAGE_URL;
			break;
		case BOTTANK_WEIGHT:
			downImage = ImageUtil.WEIGHT_BOT_DOWN_IMAGE_URL;
			break;
		case BOTTANK_QUICK:
			downImage = ImageUtil.QUICK_BOT_DOWN_IMAGE_URL;
			break;
		}
		return downImage;
	}

	/**
	 * Method of launching a bot tank
	 */
	public void go() {
		if (isAttackCoolDown() && !hasBullet) {// Can attack
			attack();
		}
		if (moveTimer > random.nextInt(1000) + 500) {//if the movement timer exceeds a random 0.5~1.5 seconds, then one direction is random
			dir = randomDirection();
			moveTimer = 0;
		} else {
			moveTimer += freshTime;// The timer increments according to the refresh time
		}
                
		switch (dir) {
		case UP:
			upWard();
			break;
		case DOWN:
			downWard();
			break;
		case LEFT:
			leftWard();
			break;
		case RIGHT:
			rightWard();
			break;
		}
	}

	/**
	 * Get random directions
	 * 
	 * @return
	 */
	private Direction randomDirection() {
		Direction[] dirs = Direction.values();//Get the enumeration value of the direction
		Direction oldDir = dir;
		Direction newDir = dirs[random.nextInt(4)];
		if (oldDir == newDir || newDir == Direction.UP) {
// If the original direction of the computer tank is the same as the random direction, or if the new direction of the computer tank is up, then re-randomly redirect the direction.
			return dirs[random.nextInt(4)];
		}
		return newDir;
	}

	/**
	 * Rewrite the boundary event moved to the panel
	 */
	protected void moveToBorder() {
		if (x < 0) {// if the tank move to wrong position
			x = 0;
			dir = randomDirection();// Randomly adjust the direction of movement
		} else if (x > gamePanel.getWidth() - width) {
			x = gamePanel.getWidth() - width;
			dir = randomDirection();
		}
		if (y < 0) {
			y = 0;
			dir = randomDirection();
		} else if (y > gamePanel.getHeight() - height) {
			y = gamePanel.getHeight() - height;
			dir = randomDirection();
		}
	}

	/**
	 * Rewrite the tank method
	 */
	@Override
	boolean hitTank(int x, int y) {
		Rectangle next = new Rectangle(x, y, width, height);// Create a collision location
		List<Tank> tanks = gamePanel.getTanks();
		for (int i = 0, lengh = tanks.size(); i < lengh; i++) {
			Tank t = tanks.get(i);
			if (!this.equals(t)) {// If this tank object is not the same as this object
				if (t.isAlive() && t.hit(next)) {
					if (t instanceof BotTank) {
						dir = randomDirection();
					}
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Rewrite the attack method, only 4% probability per attack will trigger the parent class attack method
	 */
	@Override
	public void attack() {
		int rnum = random.nextInt(100);// Create a random number in the range 0-99
		if (rnum < 4) {
			super.attack();
		}
	}

	public int getBeatBy() {
		return beatBy;
	}

	public void setBeatBy(int i) {
		beatBy = i;
	}
}
