package com.edu.mapEditor.listener;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.commons.collections.functors.WhileClosure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edu.mapEditor.MapEditorData;
import com.edu.mapEditor.view.JImageComponent;

/**
 * 设置单位像素
 * 
 * @author zuohuai
 *
 */
@Component
public class JBtnSetUpGirdListener implements ActionListener {

	@Autowired
	private JImageComponent component;
	@Autowired
	private MapEditorData mapeditorData;

	@Override
	public void actionPerformed(ActionEvent e) {
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
		//画竖线
		int startX = 0;
		while(true){
			if(startX >= component.getWidth()){
				break;
			}
			g.drawLine(startX, 0, startX, component.getHeight());
			startX += mapeditorData.getPrixel();
		}
		
		//画横线
		int startY = 0;
		while(true){
			if(startY >= component.getHeight()){
				break;
			}
			g.drawLine(0, startY, component.getWidth(), startY);
			startY += mapeditorData.getPrixel();
		}
		
		this.component.repaint();
		// 设置绘制的状态
		mapeditorData.modifyDrawLine(true);
	}
	

	
}
