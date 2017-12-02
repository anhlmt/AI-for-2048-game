package com.Game2048.game.model;

import javax.swing.SwingUtilities;

import com.Game2048.ai.GameAgent;
import com.Game2048.game.view.ui.SimulationModeUI;
import com.Game2048.util.MathUtils;

public class SimulatedTask implements Runnable {

	private SimulationModeUI gameUI;
	private double percent = 0;
	private double proportion;
	private boolean stop = false;

	/**
	 * 
	 */
	public SimulatedTask(SimulationModeUI gameUI) {
		this.gameUI = gameUI;
		this.proportion = 100 * 1.0 / gameUI.getNumberOfGame();
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			Board board = new Board(4);
			GameAgent gameAgent = new GameAgent(gameUI.getDepth());
			while (!board.isTerminated() && !stop) {
				publish(board);
				Direction direction = gameAgent.process(board);
				if (direction == Direction.NONE)
					break;
				board.move(direction);
				if (board.hasWon())
					break;
				board.addRandomCell();
			}
			if (stop)
				return;
			updateUI(proportion);
			showResult(board);
		} catch (IndexOutOfBoundsException e) {
			showResult(null);
		} catch (CloneNotSupportedException e) {
			showResult(null);
		}
	}

	/**
	 * @param board
	 */
	private void showResult(Board board) {
		SwingUtilities.invokeLater(new Runnable() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see java.lang.Runnable#run()
			 */
			@Override
			public void run() {
				gameUI.showResult(board);
			}
		});
	}

	private void updateUI(double currentPercent) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					gameUI.getMutex().acquire();
					gameUI.setProgressPercent(gameUI.getProgressPercent() - percent + currentPercent);
					percent = currentPercent;
					gameUI.getProgressLabel().setText(MathUtils.formatNumber(gameUI.getProgressPercent()) + "%");
					gameUI.getProgressBar().setValue((int) Math.floor(gameUI.getProgressPercent()));
					gameUI.getMutex().release();
				} catch (InterruptedException e) {
					System.out.println("Canceled");
				}
			}

		});
	}

	/**
	 * @param board
	 * @throws InterruptedException
	 */
	private void publish(Board board) {
		double currentPercent;
		if (board.getActualScore() < Board.MINIMUM_WIN_SCORE) {
			currentPercent = board.getActualScore() * proportion / Board.MINIMUM_WIN_SCORE;
		} else {
			currentPercent = 0.99 * proportion;
		}
		updateUI(currentPercent);
	}

}