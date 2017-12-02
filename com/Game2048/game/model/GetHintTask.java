package com.Game2048.game.model;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import com.Game2048.ai.GameAgent;
import com.Game2048.game.controller.GameController;

public class GetHintTask extends SwingWorker<Direction, Object> {

	private GameController gameController;
	private GameAgent gameAgent;

	/**
	 * 
	 */
	public GetHintTask(GameController gameController) {
		this.gameController = gameController;
		gameAgent = new GameAgent(gameController.getDepth());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.SwingWorker#doInBackground()
	 */
	@Override
	protected Direction doInBackground() throws Exception {
		return gameAgent.process(gameController.getBoard());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.SwingWorker#done()
	 */
	@Override
	protected void done() {
		if(gameAgent.isCancelled())
			return;
		try {
			gameController.handleHint(get());
		} catch (InterruptedException e) {
			gameController.handleHint(Direction.NONE);
			e.printStackTrace();
		} catch (ExecutionException e) {
			gameController.handleHint(Direction.NONE);
			e.printStackTrace();
		}
	}

	public void cancel() {
		gameAgent.cancel();
	}

}