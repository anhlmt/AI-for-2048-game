package com.Game2048.game.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Node {

	private int value;
	private List<Node> children = new ArrayList<Node>();
	private boolean leaf = false;
	private Direction direction = Direction.NONE;
	private Board board;

	public Node(Board board) {
		setBoard(board);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public List<Node> getChildren() {
		return children;
	}

	public void appendChild(Node child) {
		this.children.add(child);
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public static final Comparator<Node> ASC_COMPARATOR = new Comparator<Node>() {
		public int compare(Node node1, Node node2) {
			return node1.getValue() - node2.getValue();
		}
	};
	
	public static final Comparator<Node> DESC_COMPARATOR = new Comparator<Node>() {
		public int compare(Node node1, Node node2) {
			return node2.getValue() - node1.getValue();
		}
	};

}
