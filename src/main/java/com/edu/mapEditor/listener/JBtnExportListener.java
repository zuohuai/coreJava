package com.edu.mapEditor.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edu.mapEditor.service.ExportService;

/**
 * 导出按钮监听器
 * @author Administrator
 */
@Component
public class JBtnExportListener implements ActionListener {
	@Autowired
	private ExportService exportService;

	@Override
	public void actionPerformed(ActionEvent e) {
		exportService.export();
	}

}
