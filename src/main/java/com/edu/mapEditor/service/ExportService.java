package com.edu.mapEditor.service;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edu.jackson.JsonUtils;
import com.edu.mapEditor.MapEditorData;
import com.edu.mapEditor.core.AStar;
import com.edu.mapEditor.core.Node;
import com.edu.mapEditor.model.Point;
import com.edu.mapEditor.model.State;
import com.edu.mapEditor.view.JImageComponent;

/**
 * 导出数据按钮
 * @author Administrator
 */
@Component
public class ExportService {
	@Autowired
	private MapEditorData mapEditorData;
	/** 监听的组件 */
	@Autowired
	private JImageComponent component;
	
	

	public void export() {
		List<Point> points = new ArrayList<Point>(mapEditorData.getPoints().keySet());
		for (Point point : points) {
			State state = mapEditorData.getState(point);
			if (state == null) {
				throw new IllegalArgumentException("初始化异常，请检查");
			}
			point.mergeState(state);
		}
		Collections.sort(points);
		System.out.println(JsonUtils.object2String(points));

		int dive = mapEditorData.getImgHeight() % mapEditorData.getPrixel();
		int len = mapEditorData.getImgHeight() / mapEditorData.getPrixel();
		if (dive > 0) {
			len = len + 1;
		}

		System.out.println(len);
		List<List<Point>> xs = new LinkedList<>();;

		int start = 0;
		int end = 0;
		System.out.println("size:" + points.size());
		int size = points.size() - 1;
		System.out.println(points.size());
		while (start < size) {
			end = start + len;
			List<Point> result = points.subList(start, end);
			xs.add(result);
			start = end;
		}

		List<List<Integer>> maps = new LinkedList<>();
		for (List<Point> result : xs) {
			List<Integer> resPoint = new LinkedList<>();
			for (Point point : result) {
				if (point.getState().getState() == State.BLOCK.getState()) {
					System.out.println(JsonUtils.object2String(point));
				}
				resPoint.add(point.getState().getState());

			}
			maps.add(resPoint);
		}

		// 寻找路径
		findPath(maps);
	}

	private void findPath(List<List<Integer>> maps) {
		Node startNode = Node.valueOf(1, 0);
		Node endNode = Node.valueOf(2, 2);
		AStar aStar = new AStar(maps);
		Node parent = aStar.findPath(startNode, endNode);

		for (int i = 0; i < maps.size(); i++) {
			for (int j = 0; j < maps.get(0).size(); j++) {
				System.out.print(maps.get(i).get(j) + ", ");
			}
			System.out.println();
		}
		List<Node> result = new LinkedList<Node>();
		while (parent != null) {
			result.add(Node.valueOf(parent.getX(), parent.getY()));
			parent = parent.getParent();
		}
		System.out.println("\n");
		// 排序
		Collections.sort(result);

		System.out.println(JsonUtils.object2String(result));
		for (Node node : result) {
			drawPoint(Color.GREEN, node);
		}

		for (int i = 0; i < maps.size(); i++) {
			for (int j = 0; j < maps.get(0).size(); j++) {
				if (AStar.exists(result, i, j)) {
					System.out.print("@, ");
				} else {
					System.out.print(maps.get(i).get(j) + ", ");
				}

			}
			System.out.println();
		}
	}

	private void drawPoint(Color color, Node node) {

		// 这里是相反的
		int afterX = node.getY() * mapEditorData.getPrixel();
		int afterY = node.getX() * mapEditorData.getPrixel();

		Graphics g = this.component.getGraphics();
		Color c = g.getColor();

		Graphics2D g2d = (Graphics2D) g;
		if (color != null) {
			g.setColor(color);
		}
		// 设置透明度
		Composite current = g2d.getComposite();
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
		g2d.setComposite(ac);

		g2d.fill3DRect(afterX, afterY, mapEditorData.getPrixel(), mapEditorData.getPrixel(), false);
		g2d.draw3DRect(afterX, afterY, mapEditorData.getPrixel(), mapEditorData.getPrixel(), false);
		// 将颜色还原
		g.setColor(c);
		g2d.setComposite(current);

		this.component.repaint();
	}
}
