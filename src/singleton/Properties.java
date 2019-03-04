package singleton;

import enumtype.*;

/**
 * Properties class for saving game configuration
 */
public class Properties {
	private static final Properties mProperties = new Properties();
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;

	private int playerNum;// numbers of player
	private boolean isBGMon;// BGM status
	private int botMaxInMap;
	private boolean isSoundOn;// check sound
	private int gameMode;// hard or medium or easy
	private double rate;
	private double tempRate;

	private Properties() {
		isBGMon = true;
		playerNum = 1;
		isSoundOn = true;
		gameMode = 1;
		botMaxInMap = 4;
		rate = 0.3;
		tempRate = rate;
	}

	public static Properties getProperties() {
		return mProperties;
	}

	public int getWidth() {
		return WIDTH;
	}

	public int getHeight() {
		return HEIGHT;
	}


	public boolean getBGMState() {
		return isBGMon;
	}

	/**
	 * turn off background music
	 */
	public void closeBGM() {
		isBGMon = false;
	}

	/**
	 * turn on background music
	 */
	public void turnOnBGM() {
		isBGMon = true;
	}


	public boolean getSoundState() {
		return isSoundOn;
	}

	/**
	 * turn off
	 */
	public void closeSound() {
		isSoundOn = false;
	}

	/**
	 * turn on
	 */
	public void turnOnSound() {
		isSoundOn = true;
	}


	public int getPalyerNum() {
		return playerNum;
	}


	public void setOnePlayer() {
		playerNum = 1;
	}


	public void setTwoPlayer() {
		playerNum = 2;
	}

	public void toEasyMode() {
		botMaxInMap = 4;
		gameMode = 1;
		rate = 0.3;
	}

	public void toMiddleMode() {
		botMaxInMap = 6;
		gameMode = 2;
		rate = 0.5;
	}

	public void toHardMode() {
		botMaxInMap = 8;
		gameMode = 3;
		rate = 0.7;
	}

	public int getBotMaxInMap() {
		return botMaxInMap;
	}

	public int getGameMode() {
		return gameMode;
	}

	public double getRate() {
		return tempRate;
	}

	public void addRate() {
		tempRate += 0.05;
	}

	public void resetRate() {
		tempRate = rate;
	}
}
