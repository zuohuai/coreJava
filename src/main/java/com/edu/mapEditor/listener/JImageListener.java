package com.edu.mapEditor.listener;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edu.mapEditor.MapEditorData;
import com.edu.mapEditor.view.JImageComponent;

/**
 * 用来实现图片的拖拽
 * 
 * @author zuohuai
 *
 */
@Component
public class JImageListener extends MouseInputAdapter {

	/** 坐标点 */
	private Point point = new Point(0, 0);
	/** 监听的组件 */
	@Autowired
	private JImageComponent component;
	@Autowired
	private MapEditorData mapEditorData;

	/**
	 * 当鼠标左键拖动时触发该事件。 记录下鼠标按下(开始拖动)的位置。
	 */
	public void mouseDragged(MouseEvent e) {
		int modify = e.getModifiers();
		if (modify == InputEvent.BUTTON3_MASK) {
			// 转换坐标系统
			Point newPoint = SwingUtilities.convertPoint(component, e.getPoint(), component.getParent());
			// 设置标签的新位置
			component.setLocation(component.getX() + (newPoint.x - point.x), component.getY() + (newPoint.y - point.y));
			// 更改坐标点
			point = newPoint;
			// 修改数据存储中的坐标位置
			mapEditorData.modiyfImgPosition(component.getX(), component.getY());
		} else if (modify == InputEvent.BUTTON1_MASK) {
			System.out.println("左边鼠标被拖动");
		}

	}

	/**
	 * 当鼠标左键按下时触发该事件。 记录下鼠标按下(开始拖动)的位置。
	 */
	public void mousePressed(MouseEvent e) {
		int modify = e.getModifiers();
		if (modify == InputEvent.BUTTON3_MASK) {
			// 得到当前坐标点
			point = SwingUtilities.convertPoint(component, e.getPoint(), component.getParent());
		} else if (modify == InputEvent.BUTTON1_MASK) {
			int currentX = e.getX();
			int currentY = e.getY();

			int numX = currentX / mapEditorData.getPrixel();
			int numY = currentY / mapEditorData.getPrixel();

			System.out.println("current:" + currentX + "," + currentY);
			System.out.println("num:" + numX + "," + numY);

			int afterX = mapEditorData.getPrixel() * numX;
			int afterY = mapEditorData.getPrixel() * numY;
			System.out.println("after:" + afterX + "," + afterY);
			Graphics g = this.component.getGraphics();
			Color c = g.getColor();
			g.setColor(new Color(0, 255, 0));
			g.fillRect(afterX, afterY, mapEditorData.getPrixel(), mapEditorData.getPrixel());
			g.setColor(new Color(255, 0, 0));
			g.drawRect(afterX, afterY, mapEditorData.getPrixel(), mapEditorData.getPrixel());
			g.setColor(c);
			this.component.repaint();
		}

	}

	public JImageComponent getComponent() {
		return component;
	}
}
