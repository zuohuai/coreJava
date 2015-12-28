package com.edu.mapEditor.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edu.mapEditor.view.JImageComponent;
import com.edu.mapEditor.view.LeftImgePanel;

/**
 * 加载图片的的service
 * @author Administrator
 */
@Component
public class LoadImageService {
	/**  图片显示面板 */
	@Autowired
	private LeftImgePanel LeftImgePanel;
	/** 图片组件 */
	@Autowired
	private JImageComponent jImageComponent;

	public void loadImage(File file) {
		// 暂时不考虑作检查
		try {
			jImageComponent.loadImage(file);
			this.LeftImgePanel.add(jImageComponent);
			jImageComponent.repaint();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

}
