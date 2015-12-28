package com.edu.mapEditor.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edu.mapEditor.MapEditorData;
import com.edu.mapEditor.service.LoadImageService;

/**
 * 取消网格的划线重现编辑
 * @author Administrator
 *
 */
@Component
public class JBtnCancelLineListener implements ActionListener {
	@Autowired
	private LoadImageService loadImageService;
	@Autowired
	private MapEditorData mapEditorData;

	@Override
	public void actionPerformed(ActionEvent e) {
		//检查地址是否正常
		String imgPath =mapEditorData.getImgPath();
		if(StringUtils.isEmpty(imgPath)){
			return;
		}
		File file = new File(imgPath);
		loadImageService.loadImage(file);
	}
}
