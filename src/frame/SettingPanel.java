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

import singleton.Properties;
import util.ImageUtil;

/**
 * Settings panel (choose game mode)
 */
public class SettingPanel extends JPanel implements KeyListener {

	private static final long serialVersionUID = 1L;
	private static final int SET_BGM = 210, SET_SOUND = 270, SET_MODE = 330, RETURN_HOME = 390;// Tank icon selectable four Y coordinates
	private Image background;// Background picture
	private Image tank;// Tank icon
	private int tankY = 210;// ̹Tank icon Y coordinate
	private MainFrame mMainFrame;

	private String BGMState;
	private String soundState;
	private String gameMode;

	public SettingPanel(MainFrame mMainFrame) {
		this.mMainFrame = mMainFrame;
		addListener();

		getBGMState();
		getSoundState();
		getGameMode();
		try {
			background = ImageIO.read(new File(ImageUtil.LOGIN_BACKGROUD_IMAGE_URL));// Read background image
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
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);// Draw a background image to fill the entire panel
		Font font = new Font(Font.SERIF, Font.BOLD, 35);
		g.setFont(font);
		g.setColor(Color.getHSBColor(47, 9, 100));
		g.drawString("Background music: " + BGMState, 280, 240);
		g.drawString("Game sound effects: " + soundState, 280, 300);
		g.drawString("Game difficulty: " + gameMode, 280, 360);
		g.drawString("Return to main menu", 300, 420);
		g.drawImage(tank, 240, tankY, this);
	}

	private void getBGMState() {
		if (Properties.getProperties().getBGMState()) {
			BGMState = "On";
		} else {
			BGMState = "Off";
		}
	}

	/**
	 * Update BGM status
	 */
	private void updateBGMState() {
		if (!Properties.getProperties().getBGMState()) {
			Properties.getProperties().turnOnBGM();
			BGMState = "On";
		} else {
			Properties.getProperties().closeBGM();
			BGMState = "Off";
		}
	}

	private void getSoundState() {
		if (!Properties.getProperties().getSoundState()) {
			soundState = "Off";
		} else {
			soundState = "On";
		}
	}

	/**
	 * update SoundState status
	 */
	private void updateSoundState() {
		if (Properties.getProperties().getSoundState()) {
			Properties.getProperties().closeSound();
			soundState = "Off";
		} else {
			Properties.getProperties().turnOnSound();
			soundState = "On";
		}
	}

	private void getGameMode() {
		switch (Properties.getProperties().getGameMode()) {
		case 2:
			gameMode = "Medium";
			break;
		case 3:
			gameMode = "Hard";
			break;
		default:
			gameMode = "Easy";
		}
	}

	/**
	 * Update game difficulty
	 */
	private void updateGameMode() {
		switch (Properties.getProperties().getGameMode()) {
		case 1:
			Properties.getProperties().toMiddleMode();
			gameMode = "Medium";
			break;
		case 2:
			Properties.getProperties().toHardMode();
			gameMode = "Hard";
			break;
		default:
			Properties.getProperties().toEasyMode();
			gameMode = "Easy";
		}

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
		int code = e.getKeyCode();// Get key event
		if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
			if (tankY == SET_BGM) {
				tankY = RETURN_HOME;
			} else if (tankY == RETURN_HOME) {
				tankY = SET_MODE;
			} else if (tankY == SET_MODE) {
				tankY = SET_SOUND;
			} else if (tankY == SET_SOUND) {
				tankY = SET_BGM;
			}
			repaint();
		} else if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
			if (tankY == RETURN_HOME) {
				tankY = SET_BGM;
			} else if (tankY == SET_BGM) {
				tankY = SET_SOUND;
			} else if (tankY == SET_SOUND) {
				tankY = SET_MODE;
			} else if (tankY == SET_MODE) {
				tankY = RETURN_HOME;
			}
			repaint();
		} else if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_0 || code == KeyEvent.VK_SPACE) {
			if (tankY == SET_BGM) {
				updateBGMState();
			}
			if (tankY == SET_SOUND) {
				updateSoundState();
			}
			if (tankY == SET_MODE) {
				updateGameMode();
			}
			if (tankY == RETURN_HOME) {
				mMainFrame.removeKeyListener(this);
				mMainFrame.setPanel(new LoginPanel(mMainFrame));
			}
			repaint();
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

}
