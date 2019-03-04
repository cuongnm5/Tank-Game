package model;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import enumtype.Direction;
import enumtype.TankType;
import frame.GamePanel;
import model.wall.BaseWall;
import model.wall.BrickWall;
import model.wall.GrassWall;
import model.wall.IronWall;
import model.wall.RiverWall;
import model.wall.Wall;
import singleton.Properties;
import util.AudioPlayer;
import util.AudioUtil;

/**
 * Bullet
 *
 */
public class Bullet extends DisplayableImage {
	Direction direction;
	static final int LENGTH = 8;// Bullet (square) side length
	private GamePanel gamePanel;
	private int speed = 9;// moving speed
	private boolean alive = true;
	Color color = Color.ORANGE;
	private Tank owner;

	private boolean isHitIronWall = false;

	/**
	 * 
	 * Bullet construction method
	 * 
	 * @param x
	 *            
	 * @param y
	 *            
	 * @param direction
	 *            
	 * @param gamePanel
	 *            
	 * @param owner
	 *            
	 */
	public Bullet(int x, int y, Direction direction, GamePanel gamePanel, Tank owner) {
		super(x, y, LENGTH, LENGTH);
		this.direction = direction;
		this.gamePanel = gamePanel;
		this.owner = owner;
		owner.setHasBullet(true);
		init();//Initialization module
	}

	/**
	 * Initialization module
	 */
	private void init() {
		Graphics g = image.getGraphics();// Get the drawing method of the image
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, LENGTH, LENGTH);
		g.setColor(color);
		g.fillOval(0, 0, LENGTH, LENGTH);
		g.drawOval(0, 0, LENGTH - 1, LENGTH - 1);
	}

	/**
	 * Bullet moving
	 */
	public void move() {
		switch (direction) {// Check the direction of movement
		case UP:
			upward();
			break;
		case DOWN:
			downward();
			break;
		case LEFT:
			leftward();
			break;
		case RIGHT:
			rightward();
			break;
		}
	}

	/**
	 * move to the left
	 */
	private void leftward() {
		x -= speed;
		moveToBorder();// Destroy bullets when moving out of the panel boundary
	}

	/**
	 * move to the right
	 */
	private void rightward() {
		x += speed;
		moveToBorder();
	}

	private void upward() {
		y -= speed;
		moveToBorder();
	}

	private void downward() {
		y += speed;
		moveToBorder();
	}

	/**
	 * Hit the tank
	 */
	public void hitTank() {
		List<Tank> tanks = gamePanel.getTanks();// Get tank list
		for (int i = 0, lengh = tanks.size(); i < lengh; i++) {
			Tank t = tanks.get(i);
			if (t.isAlive() && this.hit(t)) {// If the tank is alive and the bullet hits the tank
				int temp = 1;
				switch (owner.type) {// Determine what tank the bullet belongs to
				case PLAYER1:// If it is the bullet of player 1
					temp = 1;
					if (t instanceof BotTank) {// If the tank hit is a bot
						this.dispose();
						t.setAlive(false);
						((BotTank) t).setBeatBy(temp);
					} else if (t instanceof Tank) {// If the tank hit is a player's tank
						this.dispose();
					}
					break;
				case PLAYER2:// If it is the bullet of player 2
					temp = 2;
					if (t instanceof BotTank) {
						this.dispose();
						t.setAlive(false);
						((BotTank) t).setBeatBy(temp);
					} else if (t instanceof Tank) {
						this.dispose();
					}
					break;
				default:
					if (t instanceof BotTank) {
						this.dispose();
					} else if (t instanceof Tank) {
						this.dispose();
                                                t.setAlive(false);
					}
					break;
				}
			}
		}
	}

	/**
	 * Hit the base
	 */
	public void hitBase() {
		BaseWall b = gamePanel.getBase();
		if (this.hit(b)) {
			this.dispose();
			b.setAlive(false);
		}
	}

	/**
	 * Hit the wall
	 */
	public void hitWall() {
		List<Wall> walls = gamePanel.getWalls();
		for (int i = 0, lengh = walls.size(); i < lengh; i++) {
			Wall w = walls.get(i);
			if (this.hit(w)) {
				if (w instanceof BrickWall) {
					if (Properties.getProperties().getSoundState()) {
						new AudioPlayer(AudioUtil.HIT).new AudioThread().start();
					}
					this.dispose();
					w.setAlive(false);
				}
				if (w instanceof IronWall) {
					this.dispose();
					if (this.isHitIronWall) {
						w.setAlive(false);
					}
					if (Properties.getProperties().getSoundState()) {
						new AudioPlayer(AudioUtil.HIT).new AudioThread().start();
					}
				}
				if (w instanceof RiverWall) {
					if (this.isHitIronWall) {
						this.dispose();
						w.setAlive(false);
					}
				}
				if (w instanceof GrassWall) {
					if (this.isHitIronWall) {
						this.dispose();
						w.setAlive(false);
					}
				}
			}
		}
	}

	/**
	 * Hit the other bullet
	 */
	public void hitBullet() {
		List<Bullet> bullets = gamePanel.getBullets();
		for (int i = 0; i < bullets.size(); i++) {
			Bullet b = bullets.get(i);
			if (this.alive) {
				if (this.hit(b) && this.owner != b.owner) {
					b.dispose();
					this.dispose();
				}
			}
		}
	}

	/**
	 * Destroy bullets when moving out of the panel boundary
	 */
	private void moveToBorder() {
		if (x < 0 || x > gamePanel.getWidth() - getWidth() || y < 0 || y > gamePanel.getHeight() - getHeight()) {
			this.dispose();
		}
	}

	/**
	 * Destroy bullet
	 */
	private synchronized void dispose() {
		this.alive = false;// Set status of bullet is false
		owner.setHasBullet(false);
	}

	public boolean isAlive() {
		return alive;
	}

	public void setIsHitIronWall(boolean b) {
		this.isHitIronWall = b;
	}
}
