package frame;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


@SuppressWarnings("serial")
public class BasicFrame extends Frame {
	private static final int FRAME_WIDTH = 800;
	private static final int FRAME_HEIGHT = 600;
	private static final Color BACKGROUND_COLOR = Color.WHITE;
	private static int x_tank = 50;
	private static int y_tank = 50;
	private Image offScreenImage = null;
	
	private Tank myTank = new Tank(x_tank, y_tank, this);
	
	private Missile myMissile;
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		myTank.draw(g);
		if (myMissile != null) {
			myMissile.draw(g);
		}
	}

	public void lauchFrame() {
		
		setLocation(400, 300);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setTitle("TANK");

		setBackground(BACKGROUND_COLOR);
		
		new Thread(new PaintThread()).start();
		
		this.addKeyListener(new KeyMonitor());
		
		setVisible(true);
		
	}

	public void update(Graphics g) {
		if (offScreenImage == null) {
			offScreenImage = this.createImage(FRAME_WIDTH, FRAME_HEIGHT);
		}

		Graphics gOffScreen = offScreenImage.getGraphics();

		Color c = gOffScreen.getColor();
		gOffScreen.setColor(BACKGROUND_COLOR);

		gOffScreen.fillRect(0, 0, FRAME_WIDTH , FRAME_HEIGHT );

		gOffScreen.setColor(c);

		paint(gOffScreen);
		
		g.drawImage(offScreenImage, 0, 0, null);
	}

	private class PaintThread implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {				
				repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private class KeyMonitor extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			myTank.keyReleased(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			myTank.keyPressed(e);
		}
	}
	
	public void setMissile(Missile missile) {
		this.myMissile = missile;
	}
}