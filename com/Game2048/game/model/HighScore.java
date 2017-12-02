package com.Game2048.game.model;

public class HighScore implements Comparable<HighScore>{

	private String name; // Name of Player
	private int score; // score after game completed

	public HighScore(String name, int score) {
		this.name = name;
		this.score = score;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getName() {
		return this.name;
	}

	public int getScore() {
		return this.score;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(HighScore other) {
		return this.score - other.score;
	}

}
