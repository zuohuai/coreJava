package com.edu.mapEditor.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edu.mapEditor.service.LoadImageService;

@Component
public class JBtnOpenFileListener implements ActionListener {
	@Autowired
	private LoadImageService loadImageService;

	public JBtnOpenFileListener() {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser dlg = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("jpg&gif&png", new String[] { "jpg", "gif", "png" });
		dlg.setFileFilter(filter);

		dlg.setDialogType(JFileChooser.OPEN_DIALOG);
		dlg.showOpenDialog(null);

		File file = dlg.getSelectedFile();
		if (file == null) {

		} else {
			loadImageService.loadImage(file);
		}
	}
}
