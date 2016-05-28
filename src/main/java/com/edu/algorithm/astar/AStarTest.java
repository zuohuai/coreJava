package com.edu.algorithm.astar;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.edu.mapEditor.core.Node;

public class AStarTest {
	/** 设置地图 */
	public static final int[][] NODES = {
		{0, 1, 0, 0, 0, 0}, 
		{0, 1, 0, 0, 0, 0}, 
		{0, 1, 0, 0, 0, 0}, 
		{0, 1, 0, 0, 0, 0}, 
		{0, 1, 0, 0, 0, 0}, 
		{0, 1, 1, 1, 1, 0}, 
		{0, 0, 0, 0, 0, 0},};

	public static final int STEP = 10;

	private List<Node> openList = new LinkedList<Node>();
	private List<Node> closeList = new LinkedList<Node>();

	public Node findMinFNodeInOpneList() {
		// 从open list中移除
		Node tempNode = openList.get(0);
		for (Node node : openList) {
			if (node.getF() < tempNode.getF()) {
				tempNode = node;
			}
		}
		return tempNode;
	}

	public List<Node> findNeighborNodes(Node currentNode) {
		List<Node> arrayList = new LinkedList<Node>();
		// 只考虑上下左右，不考虑斜对角
		int topX = currentNode.getX();
		int topY = currentNode.getY() - 1;
		if (canReach(topX, topY) && !exists(closeList, topX, topY)) {
			arrayList.add(Node.valueOf(topX, topY));
		}
		int bottomX = currentNode.getX();
		int bottomY = currentNode.getY() + 1;
		if (canReach(bottomX, bottomY) && !exists(closeList, bottomX, bottomY)) {
			arrayList.add(Node.valueOf(bottomX, bottomY));
		}
		int leftX = currentNode.getX() - 1;
		int leftY = currentNode.getY();
		if (canReach(leftX, leftY) && !exists(closeList, leftX, leftY)) {
			arrayList.add(Node.valueOf(leftX, leftY));
		}
		int rightX = currentNode.getX() + 1;
		int rightY = currentNode.getY();
		if (canReach(rightX, rightY) && !exists(closeList, rightX, rightY)) {
			arrayList.add(Node.valueOf(rightX, rightY));
		}
		return arrayList;
	}

	/**
	 * 检查是否超过边界
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean canReach(int x, int y) {
		if (x >= 0 && x < NODES.length && y >= 0 && y < NODES[0].length) {
			return NODES[x][y] == 0;
		}
		return false;
	}

	public Node findPath(Node startNode, Node endNode) {
		// 把起点加入 open list
		openList.add(startNode);

		while (openList.size() > 0) {
			// 遍历 open list ，查找 F值最小的节点，把它作为当前要处理的节点
			Node currentNode = findMinFNodeInOpneList();

			openList.remove(currentNode);
			// 把这个节点移到 close list
			closeList.add(currentNode);

			List<Node> neighborNodes = findNeighborNodes(currentNode);
			for (Node node : neighborNodes) {
				if (exists(openList, node)) {
					foundPoint(currentNode, node);
				} else {
					notFoundPoint(currentNode, endNode, node);
				}
			}
			if (find(openList, endNode) != null) {
				return find(openList, endNode);
			}
		}

		return find(openList, endNode);
	}

	private void foundPoint(Node tempStart, Node node) {
		int G = calcG(tempStart, node);
		if (G < node.getG()) {
			node.setParent(tempStart);
			node.setG(G);
			node.calcF();
		}
	}

	private void notFoundPoint(Node tempStart, Node end, Node node) {
		node.setParent(tempStart);
		node.setG(calcG(tempStart, node));
		node.setH(calcH(end, node));
		node.calcF();
		openList.add(node);
	}

	private int calcG(Node start, Node node) {
		int G = STEP;
		int parentG = node.getParent() != null ? node.getParent().getG() : 0;
		return G + parentG;
	}

	private int calcH(Node end, Node node) {
		int step = Math.abs(node.getX() - end.getX()) + Math.abs(node.getY() - end.getY());
		return step * STEP;
	}

	public static void main(String[] args) {
		Node startNode = Node.valueOf(1, 0);
		Node endNode = Node.valueOf(2, 2);
		Node parent = new AStarTest().findPath(startNode, endNode);

		for (int i = 0; i < NODES.length; i++) {
			for (int j = 0; j < NODES[0].length; j++) {
				System.out.print(NODES[i][j] + ", ");
			}
			System.out.println();
		}
		ArrayList<Node> arrayList = new ArrayList<Node>();

		while (parent != null) {
			arrayList.add(Node.valueOf(parent.getX(), parent.getY()));
			parent = parent.getParent();
		}
		System.out.println("\n");

		for (int i = 0; i < NODES.length; i++) {
			for (int j = 0; j < NODES[0].length; j++) {
				if (exists(arrayList, i, j)) {
					System.out.print("@, ");
				} else {
					System.out.print(NODES[i][j] + ", ");
				}

			}
			System.out.println();
		}

	}

	public static Node find(List<Node> nodes, Node point) {
		for (Node n : nodes)
			if ((n.getX() == point.getX()) && (n.getY() == point.getY())) {
				return n;
			}
		return null;
	}

	public static boolean exists(List<Node> nodes, Node node) {
		return nodes.contains(node);
	}

	public static boolean exists(List<Node> nodes, int x, int y) {

		return nodes.contains(Node.valueOf(x, y));
	}
}
