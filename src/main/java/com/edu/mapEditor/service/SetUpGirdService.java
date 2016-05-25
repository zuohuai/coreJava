package com.edu.mapEditor.service;

import java.awt.Graphics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edu.mapEditor.MapEditorData;
import com.edu.mapEditor.model.Point;
import com.edu.mapEditor.model.State;
import com.edu.mapEditor.view.JImageComponent;

/**
 * 设置网格
 * @author Administrator
 */
@Component
public class SetUpGirdService {
	@Autowired
	private JImageComponent component;
	@Autowired
	private MapEditorData mapeditorData;

	public void setUpGird() {
		if (component == null) {
			return;
		}
		if (component.getBufferedImage() == null) {
			return;
		}
		// 检查绘制的状态
		if (mapeditorData.isDrawLine()) {
			return;
		}
		Graphics g = component.getGraphics();
		// 画竖线
		int startX = 0;
		int timeX = 1;
		while (true) {
			if (startX >= component.getWidth()) {
				break;
			}
			g.drawLine(startX, 0, startX, component.getHeight());
			startX += mapeditorData.getPrixel();
			timeX++;
		}
		System.out.println("timeX:" + timeX);
		// 画横线
		int startY = 0;
		int timeY = 1;
		while (true) {
			if (startY >= component.getHeight()) {
				break;
			}
			g.drawLine(0, startY, component.getWidth(), startY);
			startY += mapeditorData.getPrixel();
			timeY++;
		}
		System.out.println("timeY:"+timeY);
		this.component.repaint();
		// 设置绘制的状态
		mapeditorData.modifyDrawLine(true);
		//设置初始坐标和状态
		startX =0;
		startY = 0;
		mapeditorData.clearPoints();
		for(int i=1; i< timeX;i++){
			for(int j =1;j <timeY; j++){
				Point point = Point.valueOf(startX, startY);
				startX += mapeditorData.getPrixel();
				mapeditorData.addPoint(point, State.UNBLOCK);
			}
			startY += mapeditorData.getPrixel();
			startX =0;
		}
		System.out.println("size:"+mapeditorData.getPointsLength());
	}
}
