package com.edu.mapEditor.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edu.mapEditor.MapEditorData;
import com.edu.mapEditor.service.LoadImageService;
import com.edu.mapEditor.service.SetUpGirdService;

@Component
public class JBtnInputPixedListener implements ActionListener {

	private String[] selectValues = new String[] { "16px", "20px", "30px", "40px", "50px", "60px" };

	private String initValue = "16px";
	@Autowired
	private MapEditorData mapEditorData;
	@Autowired
	private LoadImageService loadImageService;
	@Autowired
	private SetUpGirdService setUpGirdService;

	@Override
	public void actionPerformed(ActionEvent e) {
		Object value = JOptionPane.showInputDialog(null, "请选择：\n", "像素设置", JOptionPane.PLAIN_MESSAGE, null, selectValues, initValue);
		if (value != null) {
			mapEditorData.modifyPrixel(value.toString());
			String imgPath = mapEditorData.getImgPath();
			if (StringUtils.isBlank(imgPath)) {
				return;
			}
			File file = new File(imgPath);
			loadImageService.loadImage(file);
			setUpGirdService.setUpGird();
		}
	}

}
