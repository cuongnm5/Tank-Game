package frame;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.swing.JPanel;

import enumtype.TankType;
import model.Boom;
import model.BotTank;
import model.Bullet;
import model.Map;
import model.Tank;
import model.wall.BaseWall;
import model.wall.Wall;
import singleton.GameState;
import singleton.Properties;
import util.AudioPlayer;
import util.AudioUtil;
import util.ImageUtil;

/**
 * Game panel
 */
public class GamePanel extends JPanel implements KeyListener {
	public static final int FRESHTIME = 10;// Interface refresh time
	private static final int botX[] = { 10, 367, 754 };// 3 horizontal position of the computer tank born

	private BufferedImage image;// The main image displayed in the panel
	private Graphics g;
	private MainFrame frame;

	private Tank play1, play2;
	private boolean space_key, s_key, w_key, a_key, d_key, up_key, 
                down_key, left_key, right_key, num0_key;// The button name
	private volatile boolean finish;// Whether the game is over

	private List<Bullet> bullets;
	private volatile List<Tank> allTanks;
	private List<Tank> botTanks;
	private List<Tank> playerTanks;
	private BaseWall base;
	private List<Wall> walls;
	private List<Boom> boomImage;

	private Random r = new Random();
	private int createBotTimer = 0;
	private Tank survivor; // (player) survivor, used to draw the last explosion effect

	private List<AudioClip> audios = AudioUtil.getAudios();//A collection of all background sound effects

	private int botReadyCount;// The total number of computer tanks that are ready to appear
	private int botSurplusCount;// Computer tanks remaining

	/**
	 * Game panel construction method
	 * 
	 * @param frame
	 *            
	 * @param level
	 *            
	 */
	public GamePanel(MainFrame frame) {
		botReadyCount = GameState.getGameState().getBotCount();
		botSurplusCount = GameState.getGameState().getBotCount();

		this.frame = frame;
		frame.setSize(Properties.getProperties().getWidth(), Properties.getProperties().getHeight());
		setBackground(Color.BLACK);
		init();
		Thread t = new FreshThead();
		t.start();
		setBGM();// Set background music
		addListener();// Turn on monitoring
	}

	/**
	 * Set background music
	 */
	private void setBGM() {
		if (Properties.getProperties().getBGMState() == true) {
			new AudioPlayer(AudioUtil.START).new AudioThread().start();// Play sound
		}
	}

	/**
	 * Component initialization
	 */
	private void init() {
		bullets = new ArrayList<>();
		allTanks = new ArrayList<>();
		walls = new ArrayList<>();
		boomImage = new ArrayList<>();

		image = new BufferedImage(Properties.getProperties().getWidth(), Properties.getProperties().getHeight(),
				BufferedImage.TYPE_INT_BGR);// Instantiate the main image, using the actual size of the panel
		g = image.getGraphics();// Get the main picture drawing object

		this.initPlayerTanks();// Instantiate player tank
		this.initBotTanks();// Instantiate player tank

		allTanks.addAll(playerTanks);
		allTanks.addAll(botTanks);

		base = new BaseWall(360, 520);// Instantiated base
		initWalls();// Initialize the wall block in the map
	}

	/**
	 * Instantiate player tank collection
	 */
	private void initPlayerTanks() {
		playerTanks = new Vector<>();
		play1 = new Tank(278, 537, ImageUtil.PLAYER1_UP_IMAGE_URL, this, TankType.PLAYER1); // init player 1
		if (Properties.getProperties().getPalyerNum() == 2) {// In multiplayer mode
			play2 = new Tank(448, 537, ImageUtil.PLAYER2_UP_IMAGE_URL, this, TankType.PLAYER2);// init player 2
			playerTanks.add(play2);
		}
		playerTanks.add(play1);
	}

	/**
	 * Instantiate bot tank collection
	 */
	private void initBotTanks() {
		botTanks = new ArrayList<>();
		botTanks.add(new BotTank(botX[0], 1, this, TankType.BOTTANK_QUICK));// Add a computer tank in the first position
		botTanks.add(new BotTank(botX[1], 1, this, TankType.BOTTANK_NORMAL));// Add a computer tank in the second position
		botTanks.add(new BotTank(botX[2], 1, this, TankType.BOTTANK_NORMAL));// Add a computer tank in the third position
		botReadyCount -= 3;// The total number of tanks to be played minus the initial number
	}

	/**
	 * Component monitoring
	 */
	private void addListener() {
		frame.addKeyListener(this);
	}

	/**
	 * Initialize the wall block in the map
	 */
	@SuppressWarnings("static-access")
	public void initWalls() {
		Random rand = new Random();
		int temp = rand.nextInt(14) + 1;
		while (!GameState.getGameState().judgeLeve(temp)) {
			temp = (temp + 3) % 14;
		}
		Map map = Map.getMap(temp);// Get the map object of the current level
		walls.addAll(map.getWalls());
		walls.add(base);
	}

	/**
	 * Rewrite drawing component method
	 */
	public void paint(Graphics g) {
		paintTankAction();
		createBotTank();
		paintImage();
		g.drawImage(image, 0, 0, this);
		System.gc();
	}

	/**
	 * Draw the main picture
	 */
	private void paintImage() {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());

		panitBoom();// Draw an explosion effect
		paintBotCount();// Draw the number of remaining tanks at the top of the screen
		panitBotTanks();// Drawing computer tank
		panitPlayerTanks();// Drawing player tank

		allTanks.addAll(playerTanks);
		allTanks.addAll(botTanks);
		panitWalls();
		panitBullets();

		if (botSurplusCount == 0) { // If all computers in the current level are destroyed
			stopThread();// End game frame refresh thread
			paintBotCount();
			g.setFont(new Font(Font.SERIF, Font.BOLD, 50));
			g.setColor(Color.green);
			g.drawString("Victory !!!", 250, 400);
			gotoNextLevel();
		}

		if (Properties.getProperties().getPalyerNum() == 1) {// If it is a single mode
			if (!play1.isAlive()) {// If player 1 is killed and player 1's life is equal to 0
				stopThread();
				boomImage.add(new Boom(play1.x, play1.y));// Add player 1 explosion effect
				panitBoom();
				paintGameOver(); 

				gotoEndPanel();
			}
		} else if (Properties.getProperties().getPalyerNum() == 2) {// If it is a dual mode
			if (play1.isAlive() && !play2.isAlive() && play2.getLife() == 0) {// If player 1 is alive and player 2 is killed
																				// �Ҵ���
				survivor = play1;// Survivor is player 1
			} 
                        else if (!play1.isAlive() && play1.getLife() == 0 && play2.isAlive()) {
                            
				survivor = play2;
			} else if (!(play1.isAlive() || play2.isAlive())) {// If both of player are killed
				stopThread();
				boomImage.add(new Boom(survivor.x, survivor.y));
				panitBoom();
				paintGameOver();

				gotoEndPanel();
			}
		}

		if (!base.isAlive()) {// If base is detroyed
			stopThread();
			paintGameOver();
			base.setImage(ImageUtil.BREAK_BASE_IMAGE_URL);// Base use killed picture
			gotoEndPanel();
		}
		g.drawImage(base.getImage(), base.x, base.y, this);
	}

	/**
	 * Draw the number of remaining tanks at the top of the screen
	 */
	private void paintBotCount() {
		g.setColor(Color.ORANGE);
		g.drawString("The remaining enemy tanks: " + botSurplusCount, 337, 15);
	}

	/**
	 * Draw game over in the center of the screen
	 */
	private void paintGameOver() {
		g.setFont(new Font(Font.SERIF, Font.BOLD, 50));// Set font
		g.setColor(Color.RED);
		g.drawString("Game Over !", 250, 400);
		if (Properties.getProperties().getSoundState()) {
			new AudioPlayer(AudioUtil.GAMEOVER).new AudioThread().start();// Create a new sound thread for playing sound effects
		}
	}

	/**
	 * Draw an explosion effect
	 */
	private void panitBoom() {
		for (int i = 0; i < boomImage.size(); i++) {
			Boom boom = boomImage.get(i);//Get the explosion object
			if (boom.isAlive()) {// If the explosion effect is effective
				if (Properties.getProperties().getSoundState()) {
					AudioClip blast = audios.get(2);// Get an explosion sound object
					blast.play();// Play explosion sound
				}
				boom.show(g);// Show explosion effect
			} else {// If the explosion effect is invalid
				boomImage.remove(i);
				i--;
			}
		}
	}

	/**
	 * Draw wall
	 */
	private void panitWalls() {
		for (int i = 0; i < walls.size(); i++) {
			Wall w = walls.get(i);
			if (w.isAlive()) {// If the wall block is valid
				g.drawImage(w.getImage(), w.x, w.y, this);
			} else {// If the wall block is invalid
				walls.remove(i);
				i--;
			}
		}
	}

	/**
	 * Drawing bullets
	 */
	private void panitBullets() {
		for (int i = 0; i < bullets.size(); i++) {
			Bullet b = bullets.get(i);// Get bullet object
			if (b.isAlive()) {
				b.move();// Bullet performs a move operation
				b.hitBase();
				b.hitWall();
				b.hitTank();
				b.hitBullet();
				g.drawImage(b.getImage(), b.x, b.y, this);
			} else {
				bullets.remove(i);
				i--;
			}
		}
	}

	/**
	 * Drawing computer tank
	 * 
	 */
	private void panitBotTanks() {
		for (int i = 0; i < botTanks.size(); i++) {
			BotTank t = (BotTank) botTanks.get(i);
			if (!t.isAlive() && t.getLife() > 1) {
				t.setAlive(true);
				t.setLife();
			}
			if (t.isAlive()) {// If the tank survives
				if (!t.isPause()) {// If the computer tank is not in a suspended state
					t.go();
				}
				g.drawImage(t.getImage(), t.x, t.y, this);
			} else {// If the tank is killed
				updateScore(t.getBeatBy(), t.getTankType());
				botTanks.remove(i);
				i--;
				boomImage.add(new Boom(t.x, t.y));
				decreaseBot();
				System.out.println(t.getTankType());
			}
		}
	}

	/**
	 * 5 points for ordinary tanks, 10 points for special tanks
	 * 
	 * @param i
	 * @param tankType
	 */
	private void updateScore(int i, TankType tankType) {
		int num = 0;
		switch (tankType) {// Judging tank type
		case BOTTANK_NORMAL:
			num = 5;
			break;
		default:
			num = 10;
		}
		if (i == 1) {
			GameState.getGameState().addScore1(num);
		} else {
			GameState.getGameState().addScore2(num);
		}
		// System.out.println(GameState.getGameState().getScore1());
	}

	/**
	 * Draw player tank
	 */
	private void panitPlayerTanks() {
		for (int i = 0; i < playerTanks.size(); i++) {
			Tank t = playerTanks.get(i);
                        
			if (!t.isAlive() && t.getLife() > 1) {
				t.setAlive(true);
				t.setLife();
			}
			if (t.isAlive()) {
				g.drawImage(t.getImage(), t.x, t.y, this);
			} else {
				playerTanks.remove(i);
				boomImage.add(new Boom(t.x, t.y));
				if (Properties.getProperties().getSoundState()) {
					AudioClip blast = audios.get(2);
					blast.play();
				}
				t.setLife();
				if (t.isAlive()) {
					if (t.getTankType() == TankType.PLAYER1) {
						play1 = new Tank(278, 537, ImageUtil.PLAYER1_UP_IMAGE_URL, this, TankType.PLAYER1);
						playerTanks.add(play1);
					}
					if (t.getTankType() == TankType.PLAYER2) {
						play2 = new Tank(448, 537, ImageUtil.PLAYER2_UP_IMAGE_URL, this, TankType.PLAYER2);
						playerTanks.add(play2);
					}
				}

			}
		}
	}

	/**
	 * End game frame refresh
	 */
	private synchronized void stopThread() {
		frame.removeKeyListener(this);
		finish = true;// Game stop flag is true
	}

	/**
	 * Game frame refresh thread inner class
	 */
	private class FreshThead extends Thread {
		public void run() {// Thread master method
			while (!finish) {// If the game is not stopped
				repaint();// ִPerform this class redraw method
				System.gc();// Draw once to generate a lot of garbage objects, reclaim memory
				try {
					Thread.sleep(FRESHTIME);// ָRepaint the interface after the specified time
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Add a computer tank, If the tank on the field does not reach the maximum, 
         * select one of the three birth positions randomly after every 4 seconds to create a computer tank.
	 */
	private void createBotTank() {
		int index = r.nextInt(3);
		createBotTimer += FRESHTIME;// The timer increments according to the refresh time
		// "When the computer on the field is less than the maximum number on the field" and "The number of tanks ready to play is greater than 0" and "Timer record has passed 2 seconds"
		if (botTanks.size() < Properties.getProperties().getBotMaxInMap() && botReadyCount > 0
				&& createBotTimer >= 2000) {
			Rectangle bornRect = new Rectangle(botX[index], 1, 35, 35);// Create a tank random birth area
			for (int i = 0, lengh = allTanks.size(); i < lengh; i++) {
				Tank t = allTanks.get(i);
				if (t.isAlive() && t.hit(bornRect)) {
					return;
				}
			}
			Random rand = new Random();
			int temp = rand.nextInt(100) + 1;
			if (temp < GameState.getGameState().getBotCount()) {
				botTanks.add(new BotTank(botX[index], 1, GamePanel.this, TankType.BOTTANK_WEIGHT));
			} else {
				temp = rand.nextInt(100) + 1;
				if (temp >= GameState.getGameState().getBotCount()) {
					botTanks.add(new BotTank(botX[index], 1, GamePanel.this, TankType.BOTTANK_NORMAL));
				} else {
					botTanks.add(new BotTank(botX[index], 1, GamePanel.this, TankType.BOTTANK_QUICK));
				}
			}
			if (Properties.getProperties().getSoundState()) {
				new AudioPlayer(AudioUtil.ADD).new AudioThread().start();
			}
			botReadyCount--;
			createBotTimer = 0;
		}
	}

	/**
	 * Go to the next level
	 */
	private void gotoNextLevel() {
		GameState.getGameState().addLevel();
		Random rand = new Random();
		int temp = rand.nextInt(14) + 1;
		while (!GameState.getGameState().judgeLeve(temp)) {
			temp = (temp + 3) % 14;
		}
		Thread jump = new JumpPageThead(temp);// Create a thread that jumps to the next level
		jump.start();
	}

	/**
	 * Go to the end page
	 */
	private void gotoEndPanel() {
//		 Thread jump = new
//		 JumpPageThead(GameState.getGameState().getLevel());// Create a thread to re-enter this level
//		// Thread jump = new JumpPageThead(Level.level.previsousLevel());
//		 jump.start();
		System.gc();
		frame.setPanel(new EndPanel(frame));// The main form jumps to this level game panel
	}

	/**
	 * The number of remaining tanks is reduced by 1
	 */
	public void decreaseBot() {
		botSurplusCount--;
	}

	/**
	 * When the button is pressed
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {// Determine the value of the pressed button
		case KeyEvent.VK_SPACE:// If you press "space"
			space_key = true;
			break;
		case KeyEvent.VK_W:// If you press "W"
			w_key = true;
			a_key = false;
			s_key = false;
			d_key = false;
			break;
		case KeyEvent.VK_A:// If you press "A"
			w_key = false;
			a_key = true;
			s_key = false;
			d_key = false;
			break;
		case KeyEvent.VK_S:// If you press "S"
			w_key = false;
			a_key = false;
			s_key = true;
			d_key = false;
			break;
		case KeyEvent.VK_D:// If you press "D"
			w_key = false;
			a_key = false;
			s_key = false;
			d_key = true;
			break;
		case KeyEvent.VK_0:// // If you press "Number 0"
			num0_key = true;
			break;
		case KeyEvent.VK_UP:// If you press "UP"
			up_key = true;
			down_key = false;
			right_key = false;
			left_key = false;
			break;
		case KeyEvent.VK_DOWN:// If you press "DOWN"
			up_key = false;
			down_key = true;
			right_key = false;
			left_key = false;
			break;
		case KeyEvent.VK_LEFT:// If you press "LEFT"
			up_key = false;
			down_key = false;
			right_key = false;
			left_key = true;
			break;
		case KeyEvent.VK_RIGHT:// If you press "RIGHT"
			up_key = false;
			down_key = false;
			right_key = true;
			left_key = false;
			break;
		}
		if (Properties.getProperties().getPalyerNum() == 1 && num0_key) {
			space_key = num0_key;
		}
	}

	/**
	 * Let the tank perform the corresponding action according to the state of the button press
	 */
	private void paintTankAction() {
		if (space_key) {
			play1.attack();
		}
		if (w_key) {
			play1.upWard();
		}
		if (d_key) {
			play1.rightWard();
		}
		if (a_key) {
			play1.leftWard();
		}
		if (s_key) {
			play1.downWard();
		}
		if (Properties.getProperties().getPalyerNum() == 2) {
			if (num0_key) {
				play2.attack();
			}
			if (up_key) {
				play2.upWard();
			}
			if (right_key) {
				play2.rightWard();
			}
			if (left_key) {
				play2.leftWard();
			}
			if (down_key) {
				play2.downWard();
			}
		}
	}

	/**
	 * When the button is raised
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_SPACE:
			space_key = false;
			break;
		case KeyEvent.VK_W:
			w_key = false;
			break;
		case KeyEvent.VK_A:
			a_key = false;
			break;
		case KeyEvent.VK_S:
			s_key = false;
			break;
		case KeyEvent.VK_D:
			d_key = false;
			break;
		case KeyEvent.VK_0:
			num0_key = false;
			break;
		case KeyEvent.VK_UP:
			up_key = false;
			break;
		case KeyEvent.VK_DOWN:
			down_key = false;
			break;
		case KeyEvent.VK_LEFT:
			left_key = false;
			break;
		case KeyEvent.VK_RIGHT:
			right_key = false;
			break;
		}
		if (Properties.getProperties().getPalyerNum() == 1 && !num0_key) {
			space_key = num0_key;
		}
	}

	/**
	 * Add bullets to the bullet collection
	 * 
	 * @param b
	 *          
	 */
	public void addBullet(Bullet b) {
		bullets.add(b);
	}

	/**
	 * Get all wall block collections
	 * 
	 * @return All wall blocks
	 */
	public List<Wall> getWalls() {
		return walls;
	}

	/**
	 * Get base object
	 * 
	 * @return base
	 */
	public BaseWall getBase() {
		return base;
	}

	/**
	 * Get all tank collections
	 * 
	 * @return all tanks
	 */
	public List<Tank> getTanks() {
		return allTanks;
	}

	/**
	 * Get all bullets in the game panel
	 * 
	 * @return
	 */
	public List<Bullet> getBullets() {
		return bullets;
	}

	/**
	 * Get all the computer tanks in the game panel
	 * 
	 * @return All existing computer tanks in the panel
	 */
	public List<Tank> getBotTanks() {
		return botTanks;
	}

	/**
	 * Game end jump thread
	 */
	private class JumpPageThead extends Thread {
		int level;

		/**
		 * Jump thread construction method
		 * 
		 * @param level
		 *           
		 */
		public JumpPageThead(int level) {
			this.level = level;
		}

		/**
		 * Thread master method
		 */
		public void run() {
			try {
				Thread.sleep(1000);
				frame.setPanel(new LevelPanel(frame));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void keyTyped(KeyEvent e) {
//		This method is not implemented, but cannot be deleted
	}

	/**
	 * Get the player tank
	 * 
	 * @return
	 */
	public List<Tank> getPlayerTanks() {
		return playerTanks;
	}
}
