package com.edu.mapEditor.view;

import java.awt.Color;

import javax.swing.JPanel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edu.mapEditor.MapEditorData;

/**
 * 左侧图片面板
 * 
 * @author Administrator
 *
 */
@Component
public class LeftImgePanel extends JPanel {
	private static final long serialVersionUID = 4769307775499380031L;
	/** 核心数据存储 */
	@Autowired
	private MapEditorData mapEditorData;
	/** 图片组件 */
	@Autowired
	private JImageComponent jImageComponent;

	public LeftImgePanel() {
		this.setLayout(null);
		this.setBackground(Color.RED);
	}

	public MapEditorData getMapEditorData() {
		return mapEditorData;
	}

	public JImageComponent getjImageComponent() {
		return jImageComponent;
	}
	
}
