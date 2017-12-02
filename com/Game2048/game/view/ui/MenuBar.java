package com.Game2048.game.view.ui;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import com.Game2048.game.Application;
import com.Game2048.game.config.Constant;
import com.Game2048.game.view.HighScoreModal;

public class MenuBar extends JMenuBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final URL STUFF_FOLDER = Constant.STUFF;

	private Application mainApp;

	/**
	 * 
	 */
	public MenuBar(Application mainApp) {
		this.setMainApp(mainApp);
		initComponent();
	}

	private void initComponent() {
		JMenu mnFile = new JMenu("File");
		addMenuItem(mnFile, "Main Menu", KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), mmHandler);
		addMenuItem(mnFile, "High Score", KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), highScoreHandler);
		addMenuItem(mnFile, "Exit", KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK), exitHandler);
		add(mnFile);

		JMenu mnAbout = new JMenu("About");
		addMenuItem(mnAbout, "Help", KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), helpHandler);
		addMenuItem(mnAbout, "Copyleft", KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), copyleftHandler);
		add(mnAbout);
	}

	private void addMenuItem(JMenu menu, String title, KeyStroke key, ActionListener listener) {
		JMenuItem mnItem = new JMenuItem(title);
		mnItem.setAccelerator(key);
		mnItem.addActionListener(listener);
		menu.add(mnItem);
	}

	private ActionListener mmHandler = o -> mainApp.getFrame().backToMainMenu();
	
	private ActionListener highScoreHandler = o -> {
		HighScoreModal modal = new HighScoreModal(mainApp.getFrame());
		modal.setVisible(true);
	};

	private ActionListener exitHandler = o -> mainApp.getFrame()
			.dispatchEvent(new WindowEvent(mainApp.getFrame(), WindowEvent.WINDOW_CLOSING));

	private ActionListener helpHandler = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				JOptionPane.showMessageDialog(mainApp.getFrame(),
						"Wut? This is 2048 Resolver App\nAnd still want more fucking help?\nGO TO HELL, FUCKING IDIOT!!!",
						"Help", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(new URL(STUFF_FOLDER, "36_50x50.png")));
			} catch (HeadlessException | MalformedURLException e1) {
				e1.printStackTrace();
			}
		}
	};

	private ActionListener copyleftHandler = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				JOptionPane.showMessageDialog(mainApp.getFrame(), "Chó To Xấu Tính", "Copyleft",
						JOptionPane.INFORMATION_MESSAGE, new ImageIcon(new URL(STUFF_FOLDER, "17_50x50.png")));
			} catch (HeadlessException | MalformedURLException e1) {
				e1.printStackTrace();
			}
		}
	};

	public Application getMainApp() {
		return mainApp;
	}

	public void setMainApp(Application mainApp) {
		this.mainApp = mainApp;
	}
}
