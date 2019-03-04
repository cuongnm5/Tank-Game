package model;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import enumtype.Direction;
import enumtype.TankType;
import frame.GamePanel;
import model.wall.GrassWall;
import model.wall.Wall;
import singleton.Properties;
import util.AudioPlayer;
import util.AudioUtil;
import util.ImageUtil;

/**
 * Tank
 */
public class Tank extends DisplayableImage {
	GamePanel gamePanel;
	Direction direction;
	protected boolean alive = true;
	protected int speed;
	TankType type;
	private String upImage;
	private String downImage;
	private String rightImage;
	private String leftImage;

	private boolean attackCoolDown = true;// Attack cooling
	private int attackCoolDownTime = 500;// Attack cooldown, milliseconds

	protected boolean hasBullet;// Check whether the tank bullet exists and cannot continue to fire bullets when it exists.

	private int life;

	/**
	 * Tank construction method
	 * 
	 * @param x
	 *            
	 * @param y
	 *            
	 * @param url
	 *           
	 * @param gamePanel
	 *           
	 * @param type
	 *        
	 */
	public Tank(int x, int y, String url, GamePanel gamePanel, TankType type) {
		super(x, y, url);
		hasBullet = false;
		this.gamePanel = gamePanel;
		this.type = type;
		direction = Direction.UP;
		switch (type) {
		case PLAYER1:
			upImage = ImageUtil.PLAYER1_UP_IMAGE_URL;
			downImage = ImageUtil.PLAYER1_DOWN_IMAGE_URL;
			rightImage = ImageUtil.PLAYER1_RIGHT_IMAGE_URL;
			leftImage = ImageUtil.PLAYER1_LEFT_IMAGE_URL;
			life = 1;
                        speed = 4;
			break;
		case PLAYER2:
			upImage = ImageUtil.PLAYER2_UP_IMAGE_URL;
			downImage = ImageUtil.PLAYER2_DOWN_IMAGE_URL;
			rightImage = ImageUtil.PLAYER2_RIGHT_IMAGE_URL;
			leftImage = ImageUtil.PLAYER2_LEFT_IMAGE_URL;
			life = 1;
                        speed = 4;
			break;
		case BOTTANK_NORMAL:
			upImage = ImageUtil.NORMAL_BOT_UP_IMAGE_URL;
			downImage = ImageUtil.NORMAL_BOT_DOWN_IMAGE_URL;
			rightImage = ImageUtil.NORMAL_BOT_RIGHT_IMAGE_URL;
			leftImage = ImageUtil.NORMAL_BOT_LEFT_IMAGE_URL;
			life = 1;
                        speed = 3;
			break;
		case BOTTANK_WEIGHT:
			upImage = ImageUtil.WEIGHT_BOT_UP_IMAGE_URL;
			downImage = ImageUtil.WEIGHT_BOT_DOWN_IMAGE_URL;
			rightImage = ImageUtil.WEIGHT_BOT_RIGHT_IMAGE_URL;
			leftImage = ImageUtil.WEIGHT_BOT_LEFT_IMAGE_URL;
			life = 3;
			speed = 1;
			break;
		case BOTTANK_QUICK:
			upImage = ImageUtil.QUICK_BOT_UP_IMAGE_URL;
			downImage = ImageUtil.QUICK_BOT_DOWN_IMAGE_URL;
			rightImage = ImageUtil.QUICK_BOT_RIGHT_IMAGE_URL;
			leftImage = ImageUtil.QUICK_BOT_LEFT_IMAGE_URL;
			life = 1;
			speed = 6;
			break;
		}
	}

	@Override
	public Rectangle getRect() {
		return new Rectangle(x, y, width - 3, height - 3);
	}

	/**
	 * move to the left
	 */
	public void leftWard() {
		if (direction != Direction.LEFT) {// If the direction before the move is not left shift
			setImage(leftImage);// Change left shift image
		}
		direction = Direction.LEFT;//Move direction is set to left
		if (!hitWall(x - speed, y) && !hitTank(x - speed, y)) {// If the position after the left shift does not hit the wall block and the tank
			x -= speed;
			moveToBorder();// Determine if you are moving to the border of the panel
		}
	}

	/**
	 * move to the right
	 */
	public void rightWard() {
		if (direction != Direction.RIGHT) {
			setImage(rightImage);
		}
		direction = Direction.RIGHT;
		if (!hitWall(x + speed, y) && !hitTank(x + speed, y)) {
			x += speed;
			moveToBorder();
		}
	}

	/**
	 * move up
	 */
	public void upWard() {
		if (direction != Direction.UP) {
			setImage(upImage);
		}
		direction = Direction.UP;
		if (!hitWall(x, y - speed) && !hitTank(x, y - speed)) {
			y -= speed;
			moveToBorder();
		}
	}

	/**
	 * move down
	 */
	public void downWard() {
		if (direction != Direction.DOWN) {
			setImage(downImage);
		}
		direction = Direction.DOWN;
		if (!hitWall(x, y + speed) && !hitTank(x, y + speed)) {
			y += speed;
			moveToBorder();
		}
	}

	/**
	 * Determine if it hits the wall
	 * 
	 * @param x
	 *           
	 * @param y
	 *            
	 */
	private boolean hitWall(int x, int y) {
		Rectangle next = new Rectangle(x, y, width - 3, height - 3);// Create a target area after the tank is moved
		List<Wall> walls = gamePanel.getWalls();
		for (int i = 0, lengh = walls.size(); i < lengh; i++) {
			Wall w = walls.get(i);
			if (w instanceof GrassWall) {// If it is grass
				continue;
			} else if (w.hit(next)) {// If you hit a wall block
				return true;
			}
		}
		return false;
	}

	/**
	 * Judge whether to hit other tanks
	 * 
	 * @param x
	 *            
	 * @param y
	 *          
	 * @return Return true if it hits any tank
	 */
	boolean hitTank(int x, int y) {
		Rectangle next = new Rectangle(x, y, width, height);
		List<Tank> tanks = gamePanel.getTanks();
		for (int i = 0, lengh = tanks.size(); i < lengh; i++) {
			Tank t = tanks.get(i);
			if (!this.equals(t)) {// If this tank is not the same object as itself
				if (t.isAlive() && t.hit(next)) {// If this tank survives and collides with itself
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Move to the border of the panel
	 */
	protected void moveToBorder() {
		if (x < 0) {
			x = 0;
		} else if (x > gamePanel.getWidth() - width) {
			x = gamePanel.getWidth() - width;
		}
		if (y < 0) {
			y = 0;
		} else if (y > gamePanel.getHeight() - height) {
			y = gamePanel.getHeight() - height;
		}
	}

	/**
	 * Get the tank head
	 * 
	 * @return Head object
	 */
	private Point getHeadPoint() {
		Point p = new Point();// Create a point object as a starting point
		switch (direction) {// Judge the direction of movement
		case UP:
			p.x = x + width / 2;
			p.y = y;
			break;
		case DOWN:
			p.x = x + width / 2;
			p.y = y + height;
			break;
		case RIGHT:
			p.x = x + width;
			p.y = y + height / 2;
			break;
		case LEFT:
			p.x = x;
			p.y = y + height / 2;
			break;
		default:
			p = null;
		}
		return p;
	}

	/**
	 * Attack method
	 */
	public void attack() {
		if (attackCoolDown && !hasBullet) {// If the attack function is cooled
			Point p = getHeadPoint();

			Bullet b = new Bullet(p.x - Bullet.LENGTH / 2, p.y - Bullet.LENGTH / 2, direction, gamePanel, this);
                        // Launching the same bullet angle as the tank at the tank head position
			gamePanel.addBullet(b);
			if (Properties.getProperties().getSoundState()) {
				AudioPlayer fire = new AudioPlayer(AudioUtil.FIRE);
				fire.new AudioThread().start();
			}
			new AttackCD().start();
		}
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	/**
	 * Set Speed
	 * 
	 * @param speed
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * Get the number of tank lives
	 * 
	 * @return
	 */
	public synchronized final int getLife() {
		return this.life;
	}

	/**
	 * Reduce the number of tank's lives
	 */
	public final void setLife() {
		if (life > 0) {
			life--;
		} else {
			return;
		}
	}

	/**
	 * Get tank type
	 * 
	 * @return
	 */
	public TankType getTankType() {
		return type;
	}

	/**
	 * Set whether bullets exist
	 * 
	 * @param hasBullet
	 */
	public void setHasBullet(Boolean hasBullet) {
		this.hasBullet = hasBullet;
	}

	/**
	 * Attack cooldown thread
	 */
	private class AttackCD extends Thread {
		public void run() {
			attackCoolDown = false;
			try {
				Thread.sleep(attackCoolDownTime);// Sleep for 0.5 seconds
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			attackCoolDown = true;// Unlock the attack function
		}
	}

	/**
	 * Get the attack function is cooling
	 * 
	 */
	public boolean isAttackCoolDown() {
		return attackCoolDown;
	}

	/**
	 * Set attack cooldown
	 * 
	 * @param attackCoolDownTime
	 */
	public void setAttackCoolDownTime(int attackCoolDownTime) {
		this.attackCoolDownTime = attackCoolDownTime;
	}
}
