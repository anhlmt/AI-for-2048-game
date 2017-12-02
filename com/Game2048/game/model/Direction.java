package com.Game2048.game.model;

public enum Direction {
	NONE(0, "None"), UP(1, "Up"), RIGHT(2, "Right"), DOWN(3, "Down"), LEFT(4, "Left");

	private final int code;
	private final String description;

	private Direction(final int code, final String description) {
		this.code = code;
		this.description = description;
	}

	public int getCode() {
		return this.code;
	}

	public String getDescription() {
		return this.description;
	}

	@Override
	public String toString() {
		return this.description;
	}
}
