package frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import singleton.GameState;

/**
 * Shows the current level
 */
public class LevelPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MainFrame frame;
	private String levelStr;
	private String ready = "";

	/**
	 * Level panel construction method
	 * @param frame
	 *           Main form
	 */
	public LevelPanel(MainFrame frame) {
		this.frame = frame;
		levelStr = "Level: " + GameState.getGameState().getLevel();// Initialize the level string
		Thread t = new LevelPanelThread();
		t.start();
	}

	/**
	 * Rewrite drawing method
	 */
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setFont(new Font(Font.SERIF, Font.BOLD, 40));
		g.setColor(Color.getHSBColor(247, 14, 49));
		g.drawString(levelStr, 335, 300);
		g.setColor(Color.getHSBColor(3, 75, 76));
		g.drawString(ready, 335, 400);
	}

	/**
	 * Jump game panel
	 */
	private void gotoGamePanel() {
		System.gc();
		frame.setPanel(new GamePanel(frame));
	}

	/**
	 * Level panel animation thread
	 *
	 */
	private class LevelPanelThread extends Thread {
		public void run() {
			for (int i = 0; i < 6; i++) {
				if (i % 2 == 0) {
					levelStr = "Level: " + GameState.getGameState().getLevel();
				} else {
					levelStr = "";//Level string does not display anything
				}
				if (i == 4) {
					ready = "Ready!!!"; // Prepare to prompt to display text
				}
				repaint();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			gotoGamePanel();
		}
	}
}
