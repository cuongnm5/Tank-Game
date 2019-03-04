package frame;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Recalculate the Missile coordinates to ensure they are emitted from the center of the tank
 *
 */
public class Missile {
	private static final Color TANK_COLOR = Color.RED;
	private static final int MOVE_LENGTH = 10;
	private static final int WIDTH = 10;
	private static final int HEIGHT = 10;
	
	private Tank.Direction dir;
	private boolean left = false;
	private boolean right = false;
	private boolean up = false;
	private boolean down = false;
	private int x;
	private int y;
	
	public Missile(int x, int y, Tank.Direction dir){
		this.x = x - WIDTH / 2;
		this.y = y - HEIGHT / 2;
		this.dir = dir;
	}
	
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.BLACK);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		
		move();
	}
	
	private void move() {
		switch (dir) {
		case UP:
			y -= MOVE_LENGTH;
			break;
		case DOWN:
			y += MOVE_LENGTH;
			break;
		case LEFT:
			x -= MOVE_LENGTH;
			break;
		case RIGHT:
			x += MOVE_LENGTH;
			break;
		case STOP:
			break;
		}
	}
}
