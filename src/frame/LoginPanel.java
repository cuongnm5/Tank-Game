package frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import singleton.GameState;
import singleton.Properties;
import util.ImageUtil;

/**
 * 
 * Login panel (select game mode)
 * 
 */

public class LoginPanel extends JPanel implements KeyListener {

	private static final long serialVersionUID = 1L;
	private static final int TITLE_1 = 210, TITLE_2 = 270, TITLE_3 = 330, TITLE_4 = 390, TITLE_5 = 450;// ̹Tank icon selectable four Y coordinates
	private Image backgroud;
	private Image tank;
	private int tankY = 210;// Tank icon Y coordinate
	private MainFrame mMainFrame;

	/**
	 * Landing panel construction method
	 * 
	 * @param mMainFrame
	 *            Main form
	 */
	public LoginPanel(MainFrame mMainFrame) {
		this.mMainFrame = mMainFrame;
		addListener();// Add component listener
		try {
			backgroud = ImageIO.read(new File(ImageUtil.LOGIN_BACKGROUD_IMAGE_URL));// Read background image
			this.setBackground(Color.black);
			tank = ImageIO.read(new File(ImageUtil.PLAYER1_RIGHT_IMAGE_URL));// Read tank icon
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Rewrite drawing method
	 */
	@Override
	public void paint(Graphics g) {
		g.drawImage(backgroud, 0, 0, getWidth(), getHeight(), this);// Draw a background image to fill the entire panel
		Font font = new Font(Font.SERIF, Font.BOLD, 35);
		g.setFont(font);
		g.setColor(Color.getHSBColor(47, 9, 100));
		g.drawString("Single player mode", 290, 240);
		g.drawString("Multiplayer mode", 290, 300);
		g.drawString("Game Settings", 320, 360);
                g.drawString("Detail", 320, 420);
		g.drawImage(tank, 250, tankY, this);
	}

	/**
	 * Jump level panel
	 */
	private void gotoLevelPanel() {
		mMainFrame.removeKeyListener(this);// Main form delete keyboard listener
		mMainFrame.setPanel(new LevelPanel(mMainFrame));// The main form jumps to the level panel
	}

	/**
	 * Add component listener
	 */
	private void addListener() {
		mMainFrame.addKeyListener(this);
	}

	/**
	 * When the button is pressed
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();// get key event
		if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
			if (tankY == TITLE_1) {
				tankY = TITLE_4;
			} else if (tankY == TITLE_3) {
				tankY = TITLE_2;
			} else if (tankY == TITLE_2) {
				tankY = TITLE_1;
			} else if (tankY == TITLE_4) {
                                tankY = TITLE_3;
                        }
			repaint();// After the button is pressed, re-draw
		} else if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
			if (tankY == TITLE_1) {
				tankY = TITLE_2;
			} else if (tankY == TITLE_2) {
				tankY = TITLE_3;
			} else if (tankY == TITLE_3) {
				tankY = TITLE_4;
			} else if(tankY == TITLE_4) {
                                tankY = TITLE_1;
                        }
			repaint();// After the button is pressed, re-draw
		} else if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_NUMPAD0 || code == KeyEvent.VK_SPACE) {
			if (tankY == TITLE_1) {
				Properties.getProperties().setOnePlayer();
				gotoLevelPanel();
				GameState.getGameState().startTime();
			}
			if (tankY == TITLE_2) {
				Properties.getProperties().setTwoPlayer();
				gotoLevelPanel();
				GameState.getGameState().startTime();
			}
			if (tankY == TITLE_3) {
				mMainFrame.removeKeyListener(this);
				mMainFrame.setPanel(new SettingPanel(mMainFrame));
			}
                        if (tankY == TITLE_4) {
                            mMainFrame.removeKeyListener(this);
                            mMainFrame.setPanel(new ControlPanel(mMainFrame));
                        }
		}

	}

	/**
	 * When the button is raised
	 */
	@Override
	public void keyReleased(KeyEvent e) {
            
	}

	/**
	 * Type a button event
	 */
	@Override
	public void keyTyped(KeyEvent e) {
            
	}

}
