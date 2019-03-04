package frame;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import singleton.Properties;

/**
 * Main form
 */
public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Construction method
	 */
	public MainFrame() {
		setTitle("TANK");
		setSize(Properties.getProperties().getWidth(), Properties.getProperties().getHeight());
		setResizable(false);
		Toolkit tool = Toolkit.getDefaultToolkit(); // Create the system the default component kit
		Dimension d = tool.getScreenSize(); // Get the screen size and assign it to a 2D coordinate object
		//Let the main form appear in the middle of the screen
		setLocation((d.width - getWidth()) / 2, (d.height - getHeight()) / 2);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);// Turn on the window
		addListener();
		setPanel(new LoginPanel(this));
		// this.add(new LoginPanel(this));
		setVisible(true);
		// System.gc();
	}

	/**
	 * Add component listener
	 */
	private void addListener() {
		addWindowListener(new WindowAdapter() {// Add a form event listener
			public void windowClosing(WindowEvent e) {// When the form is closed
				int closeCode = JOptionPane.showConfirmDialog(MainFrame.this, "Quit game", "Cancel",
						JOptionPane.YES_NO_OPTION);// Pop up a selection dialog and record the user selection
				if (closeCode == JOptionPane.YES_OPTION) {// If chose YES -> quit
					System.exit(0);
				}
			}
		});
	}

	/**
	 * Replace the panel in the main container
	 * 
	 * @param panel
	 *            Replacement panel
	 */
	public void setPanel(JPanel panel) {
		Container c = getContentPane();// Replacement panel
		if (panel instanceof LoginPanel) {
			panel.addKeyListener((KeyListener) panel);
		}
		c.removeAll();// Delete all components in the container
		c.add(panel);// Container add panel
		c.validate();// Container revalidates all components
	}
}
