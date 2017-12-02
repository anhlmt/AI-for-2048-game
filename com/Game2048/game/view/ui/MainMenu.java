package com.Game2048.game.view.ui;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.Game2048.game.Application;
import com.Game2048.game.config.Constant;
import com.Game2048.game.view.HighScoreModal;

public class MainMenu extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int NEW_GAME_ICON = 1;
	private static final int SIMULATION_ICON = 8;
	private static final int HIGH_SCORE_ICON = 27;

	public static final URL STUFF_FOLDER = Constant.STUFF;

	private Application mainApp;
	private JPanel mainMn;

	/**
	 * Create the panel.
	 */
	public MainMenu(Application mainApp) {
		this.setMainApp(mainApp);
		initialize();
	}

	private void initialize() {
		setLayout();
		initComponent();
	}

	private void setLayout() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.3, 0.4, 0.3 };
		gridBagLayout.rowWeights = new double[] { 0.3, 0.4, 0.3 };
		setLayout(gridBagLayout);
	}

	private void initComponent() {
		mainMn = new JPanel();
		mainMn.setOpaque(false);
		GridBagConstraints gbc_mainMn = new GridBagConstraints();
		gbc_mainMn.fill = GridBagConstraints.BOTH;
		gbc_mainMn.gridx = 1;
		gbc_mainMn.gridy = 1;
		add(mainMn, gbc_mainMn);
		mainMn.setLayout(new GridLayout(0, 1, 0, 30));

		addBtn("New Game", NEW_GAME_ICON, newGameHandler);
		addBtn("Simulation", SIMULATION_ICON, simulationHandler);
		addBtn("High Score", HIGH_SCORE_ICON, highScoreHandler);
	}

	private ActionListener newGameHandler = o -> {
		NormalGameModeUI gameUI = new NormalGameModeUI(mainApp);
		mainApp.getFrame().getContentPane().add(gameUI, "content");
		mainApp.getFrame().getLayout().show(mainApp.getFrame().getContentPane(), "content");
	};

	private ActionListener simulationHandler = o -> {
		SimulationModeUI gameUI = new SimulationModeUI(mainApp);
		mainApp.getFrame().getContentPane().add(gameUI, "content");
		mainApp.getFrame().getLayout().show(mainApp.getFrame().getContentPane(), "content");
	};
	
	private ActionListener highScoreHandler = o->{
		HighScoreModal modal = new HighScoreModal(mainApp.getFrame());
		modal.setVisible(true);
	};

	private void addBtn(String title, int imgNum, ActionListener listener) {
		JButton btn = new JButton(title);
		try {
			btn.setIcon(new ImageIcon(new URL(STUFF_FOLDER, imgNum + "_50x50.png")));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		btn.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		btn.setFocusable(false);
		btn.setBackground(SystemColor.inactiveCaption);
		btn.addActionListener(listener);
		btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainMn.add(btn);
	}

	public Application getMainApp() {
		return mainApp;
	}

	public void setMainApp(Application mainApp) {
		this.mainApp = mainApp;
	}

	@Override
	protected void paintComponent(Graphics g) {

		BufferedImage bgImage;
		try {
			bgImage = ImageIO.read(new URL(STUFF_FOLDER, "background.jpg"));
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null);
		} catch (IOException e) {
			super.paintComponent(g);
		}
	}

}
