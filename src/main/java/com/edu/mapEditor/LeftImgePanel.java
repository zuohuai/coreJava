package com.edu.mapEditor;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * 左侧图片面板
 * @author Administrator
 *
 */
public class LeftImgePanel extends JPanel{
	private static final long serialVersionUID = 4769307775499380031L;

	private MapEditorData mapEditorData;
	
	public LeftImgePanel(){
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		this.setBackground(Color.RED);
	}
}
