package com.Game2048.game;

import java.awt.EventQueue;

import com.Game2048.game.view.ApplicationWindow;

public class Application {

	private ApplicationWindow frame;
	private boolean ingame = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Application window = new Application();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Application() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new ApplicationWindow(this);
	}

	public ApplicationWindow getFrame() {
		return frame;
	}

	public boolean isIngame() {
		return ingame;
	}

	public void setIngame(boolean ingame) {
		this.ingame = ingame;
	}

}
