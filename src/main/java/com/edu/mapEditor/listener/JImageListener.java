package com.edu.mapEditor.listener;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edu.mapEditor.MapEditorData;
import com.edu.mapEditor.model.State;
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
			//如果不需要绘图，则不处理
			if(!mapEditorData.isEditorGird()){
				return;
			}
			drawPoint(e);
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
			//如果不需要绘图，则不处理
			if(!mapEditorData.isEditorGird()){
				return;
			}
			drawPoint(e);
		}

	}

	private void drawPoint(MouseEvent e) {
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
		
		Graphics2D g2d = (Graphics2D) g;
		// 设置透明度
		Composite current = g2d.getComposite();
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
		g2d.setComposite(ac);

		g2d.fill3DRect(afterX, afterY, mapEditorData.getPrixel(), mapEditorData.getPrixel(), false);
		g2d.draw3DRect(afterX, afterY, mapEditorData.getPrixel(), mapEditorData.getPrixel(), false);
		//将颜色还原
		g.setColor(c);
		g2d.setComposite(current);
		
		mapEditorData.modifyPoint(com.edu.mapEditor.model.Point.valueOf(afterX, afterY), State.UNBLOCK);
		this.component.repaint();
	}

	public JImageComponent getComponent() {
		return component;
	}
	
}
