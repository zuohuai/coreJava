package com.edu.mapEditor.core;

import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * 定义节点信息
 * @author Administrator
 */
public class Node implements Comparable<Node>{
	public static Node valueOf(int x, int y) {
		Node node = new Node();
		node.x = x;
		node.y = y;
		return node;
	}

	private int x;

	private int y;
	/** F值 */
	private int F;
	/** G值 */
	private int G;
	/** H值 */
	private int H;

	private Node parent;

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	// 计算F值
	public void calcF() {
		this.F = this.G + this.H;
	}

	public int getF() {
		return F;
	}

	public void setF(int f) {
		F = f;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getH() {
		return H;
	}

	public void setH(int h) {
		H = h;
	}

	public int getG() {
		return G;
	}

	public void setG(int g) {
		G = g;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public int compareTo(Node o) {
		return new CompareToBuilder().append(this.y, o.getY()).append(this.x, o.getX()).toComparison();
	}
}