package com.Game2048.game.view.component;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import com.Game2048.game.config.Constant;
import com.Game2048.game.model.Board;

public class BoardPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public static int HGAP = 15;
	public static int VGAP = 15;
	public static int CELL_SIZE = 106;
	public static URL TILE_FOLDER = Constant.TILES;

	private Board board;
	Image bgImage;
	Map<Integer, Image> mapImage = new HashMap<>();

	public BoardPanel(Board board) {
		setBoard(board);
		loadImage();
	}

	private void loadImage() {
		try {
			bgImage = Toolkit.getDefaultToolkit().createImage(new URL(TILE_FOLDER, "background.png"));
			mapImage.put(0, Toolkit.getDefaultToolkit().createImage(new URL(TILE_FOLDER, "0.gif")));
			for (int i = 2; i < 2048; i = i * 2) {
				mapImage.put(i, Toolkit.getDefaultToolkit().createImage(new URL(TILE_FOLDER, i + ".gif")));
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawBackground(g);
		drawBoard(g);
	}

	private void drawBackground(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(bgImage, 0, 0, this);
	}

	private void drawBoard(Graphics g) {
		if (board == null)
			return;
		for (int row = 0; row < board.getSize(); row++) {
			for (int col = 0; col < board.getSize(); col++) {
				int x = VGAP * (col + 1) + CELL_SIZE * col;
				int y = HGAP * (row + 1) + CELL_SIZE * row;
				int cellValue = board.getCells()[row][col];
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g2.drawImage(mapImage.get(cellValue), x, y, this);
			}
		}
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}
}
