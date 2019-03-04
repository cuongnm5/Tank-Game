package frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import singleton.GameState;
import singleton.Properties;

/**
 * Display current level
 */
public class EndPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MainFrame frame;
	private String levelStr;// String of blinking levels in the center of the control panel
	private String ready = "";

	/**
	 * @param frame
	 *          
	 */
	public EndPanel(MainFrame frame) {
		this.frame = frame;
		levelStr = "Level " + GameState.getGameState().getLevel();// Create level
		Thread t = new LevelPanelThread();// Create level display lab
		t.start();
	}

	/**
	 * Rewrite paint function
	 */
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setFont(new Font(Font.SERIF, Font.BOLD, 40));
		g.setColor(Color.getHSBColor(247, 14, 49));
		g.drawString("Player 1", 150, 200); // draw player's name
		g.setColor(Color.getHSBColor(3, 75, 76));
		g.drawString("Level : " + GameState.getGameState().getLevel(), 120, 320);
		g.drawString("Point : " + GameState.getGameState().getScore1(), 120, 390);
		if (Properties.getProperties().getPalyerNum() == 2) {
			g.setColor(Color.getHSBColor(247, 14, 49));
			g.drawString("Player 2", 550, 200);// draw player's name
			g.setColor(Color.getHSBColor(3, 75, 76));// 
			g.drawString("Level : " + GameState.getGameState().getLevel(), 520, 320);
			g.drawString("Point : " + GameState.getGameState().getScore2(), 520, 390);
		}
		long temp = GameState.getGameState().getTotalTime();
		g.drawString("Time: " + temp / 60 + "Minute" + temp % 60 + "Second", 180, 480);

	}

	/**
	 * Main panel
	 */
	private void gotoLoginPanel() {
		System.gc();
		frame.setPanel(new LoginPanel(frame)); 
		GameState.getGameState().reset();
	}

	/**
	 * Level panel animation thread
	 *
	 */
	private class LevelPanelThread extends Thread {
		public void run() {
			for (int i = 0; i < 6; i++) {// Cycle 6 times
				try {
					Thread.sleep(1000);// Sleep for 1 second
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			gotoLoginPanel();// Go to the game panel
		}
	}
}
