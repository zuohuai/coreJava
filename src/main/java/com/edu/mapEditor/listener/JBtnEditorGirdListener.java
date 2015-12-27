package com.edu.mapEditor.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import org.springframework.stereotype.Component;

/**
 * 编辑路径的监听器
 * @author zuohuai
 *
 */
@Component
public class JBtnEditorGirdListener implements MouseMotionListener{

	@Override
	public void mouseDragged(MouseEvent e) {
		//TODO
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		System.out.println("鼠标移动,X:"+e.getX() +",Y:"+e.getY());
	}

}
