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
import jdk.nashorn.internal.runtime.PropertyListeners;
import util.ImageUtil;

public class ControlPanel extends JPanel implements KeyListener{
    
    private static final long serialVersionUID = 1L;
    private static final int RETURN_HOME = 390;
    private MainFrame mMainFrame;
    private Image background;
    private Image tank;
    private Image botTank1, botTank2, botTank3;
    private Image playerTank1, playerTank2;
            
    public ControlPanel(MainFrame mMainFrame){
        this.mMainFrame = mMainFrame;
        addListener();
        try {
            background = ImageIO.read(new File(ImageUtil.LOGIN_BACKGROUD_IMAGE_URL));// Read background image
            tank = ImageIO.read(new File(ImageUtil.PLAYER1_RIGHT_IMAGE_URL));// Read tank icon
            botTank1 = ImageIO.read(new File(ImageUtil.NORMAL_BOT_UP_IMAGE_URL));
            botTank2 = ImageIO.read(new File(ImageUtil.QUICK_BOT_UP_IMAGE_URL));
            botTank3 = ImageIO.read(new File(ImageUtil.WEIGHT_BOT_UP_IMAGE_URL));
            playerTank1 = ImageIO.read(new File(ImageUtil.PLAYER1_UP_IMAGE_URL));
            playerTank2 = ImageIO.read(new File(ImageUtil.PLAYER2_UP_IMAGE_URL));
	} catch (IOException e) {
            e.printStackTrace();
	}
    }
    @Override
    public void paint(Graphics g) {
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        Font font = new Font(Font.SERIF, Font.BOLD, 20);
        g.setFont(font);
        g.setColor(Color.BLACK);
        g.drawImage(botTank1, 200, 60, this);
        g.drawString("Normal Bot", 170 , 110);
        g.drawString("Life: 1", 170, 130);
        g.drawString("Speed: 3", 170, 150);
        g.drawImage(botTank2, 400, 60, this);
        g.drawString("Quick Bot", 370 , 110);
        g.drawString("Life: 1", 370, 130);
        g.drawString("Speed: 6", 370, 150);        
        g.drawImage(botTank3, 600, 60, this);
        g.drawString("Weight Bot", 570 , 110);
        g.drawString("Life: 3", 570, 130);
        g.drawString("Speed: 1", 570, 150);         
        g.drawImage(playerTank1, 300, 200, this);
        g.setColor(Color.WHITE);
        g.drawString("Player 1", 270 , 250);
        g.drawString("Life: 1", 270, 270);
        g.drawString("Speed: 4", 270, 290);
        g.drawString("Move: A-S-D-W", 250, 310);
        g.drawString("Fire: Space", 250, 330);
        g.drawImage(playerTank2, 500, 200, this);
        g.drawString("Player 2", 470 , 250);
        g.drawString("Life: 1", 470, 270);
        g.drawString("Speed: 4", 470, 290);
        g.drawString("Move: Up-Down-Left-Right", 440, 310);
        g.drawString("Fire: Number 0", 450, 330);
        font = new Font(Font.SERIF, Font.BOLD, 25);
        g.setFont(font);
        g.setColor(Color.getHSBColor(47, 9, 100));
        g.drawString("Return to main menu", 300, 420);
        g.drawImage(tank, 250, 390, this);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();// Get key event
        if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_0 || code == KeyEvent.VK_SPACE) {
		mMainFrame.removeKeyListener(this);
		mMainFrame.setPanel(new LoginPanel(mMainFrame));
		repaint();
        }
    }    

    private void addListener() {
        mMainFrame.addKeyListener(this);
    }   
    
    @Override
    public void keyTyped(KeyEvent e) {
 
    }

    @Override
    public void keyReleased(KeyEvent e) {
       
    }


    
}
