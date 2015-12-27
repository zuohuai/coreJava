package com.edu.mapEditor.listener;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edu.mapEditor.view.JImageComponent;
import com.edu.mapEditor.view.LeftImgePanel;

@Component
public class JBtnOpenFileListener implements ActionListener {
	/**  图片显示面板 */
	@Autowired
	private LeftImgePanel LeftImgePanel;

	public JBtnOpenFileListener() {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser dlg = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("jpg&gif&png",
				new String[] { "jpg", "gif", "png" });
		dlg.setFileFilter(filter);

		dlg.setDialogType(JFileChooser.OPEN_DIALOG);
		dlg.showOpenDialog(null);

		File file = dlg.getSelectedFile();
		if (file == null) {

		} else {
			// 暂时不考虑作检查
			try {
				JImageComponent jImageComponent = this.LeftImgePanel.getjImageComponent();
				jImageComponent.loadImage(file);

				this.LeftImgePanel.add(jImageComponent);
				jImageComponent.repaint();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}

	public LeftImgePanel getLeftImgePanel() {
		return LeftImgePanel;
	}
}
