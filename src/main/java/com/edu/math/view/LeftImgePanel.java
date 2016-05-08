package com.edu.math.view;

import javax.swing.JPanel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edu.math.MapEditorData;

/**
 * 左侧图片面板
 * 
 * @author Administrator
 */
@Component
public class LeftImgePanel extends JPanel {
	private static final long serialVersionUID = 4769307775499380031L;
	/** 核心数据存储 */
	@Autowired
	private MapEditorData mapEditorData;

	public LeftImgePanel(){
		try {
			this.setLayout(null);
		} catch (Exception e) {

		}
		
	}

}
