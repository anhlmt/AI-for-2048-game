package com.Game2048.game.view;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.Game2048.game.Application;
import com.Game2048.game.config.Constant;
import com.Game2048.game.view.component.CardPanel;
import com.Game2048.game.view.ui.MainMenu;
import com.Game2048.game.view.ui.MenuBar;

public class ApplicationWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final URL STUFF_FOLDER = Constant.STUFF;

	private Application mainApp;
	private CardLayout layout;

	public ApplicationWindow(Application mainApp) {
		this.setMainApp(mainApp);
		initialize();
	}

	private void initialize() {
		try {
			setIconImage(Toolkit.getDefaultToolkit().getImage(new URL(STUFF_FOLDER, "logo.jpg")));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		setTitle("2048");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				Object objButtons[] = { "Sure... Byte Byte", "No" };
				int promptResult;
				try {
					promptResult = JOptionPane.showOptionDialog(mainApp.getFrame(), "Are you sure you want to exit?",
							"Hello... It's me!", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
							new ImageIcon(new URL(STUFF_FOLDER, "9_50x50.png")), objButtons, objButtons[1]);
					if (promptResult == JOptionPane.YES_OPTION) {
						System.exit(0);
					}
				} catch (HeadlessException | MalformedURLException e) {
					e.printStackTrace();
				}
			}
		});

		setMenuBar();
		setLayout();
		showMainMenu();
	}

	private void setMenuBar() {
		MenuBar menuBar = new MenuBar(mainApp);
		setJMenuBar(menuBar);
	}

	private void setLayout() {
		layout = new CardLayout(0, 0);
		getContentPane().setLayout(layout);
		getContentPane().setPreferredSize(new Dimension(700, 500));
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void showMainMenu() {
		getContentPane().add(new MainMenu(mainApp), "Main Menu");
		layout.show(getContentPane(), "Main Menu");
	}

	public Application getMainApp() {
		return mainApp;
	}

	public void setMainApp(Application mainApp) {
		this.mainApp = mainApp;
	}

	public void backToMainMenu() {
		if (mainApp.isIngame()) {
			Object objButtons[] = { "Yes", "No" };
			int promptResult;
			try {
				promptResult = JOptionPane.showOptionDialog(mainApp.getFrame(),
						"Are you sure you want to quit current game?", "Back to Main Menu!", JOptionPane.DEFAULT_OPTION,
						JOptionPane.WARNING_MESSAGE, new ImageIcon(new URL(STUFF_FOLDER, "8_50x50.png")), objButtons,
						objButtons[1]);
				if (promptResult == JOptionPane.NO_OPTION) {
					return;
				}
			} catch (HeadlessException | MalformedURLException e) {
				e.printStackTrace();
			}
			mainApp.setIngame(false);
		}
		Component[] components = mainApp.getFrame().getContentPane().getComponents();
		for (Component component : components) {
			if (component.isVisible() && component instanceof CardPanel) {
				((CardPanel) component).closed();
				getContentPane().remove(component);
				break;
			}
		}
	}

	@Override
	public CardLayout getLayout() {
		return layout;
	}
}
