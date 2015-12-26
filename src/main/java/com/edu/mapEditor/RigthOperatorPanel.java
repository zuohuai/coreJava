package com.edu.mapEditor;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * 右侧面板
 * 
 * @author Administrator
 * 
 */
public class RigthOperatorPanel extends JPanel {

	private static final long serialVersionUID = -4680757892101282548L;

	private MapEditorData mapEditorData;
	
	public RigthOperatorPanel() {

		// 设置布局
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);

		JButton btnOpen = new JButton("打开文件");
		JButton btnExportFile = new JButton("导出文件");
		JButton btnExitSystem = new JButton("退出系统");
		JButton btnSetGird = new JButton("设置网格");
		JButton btnCancelGird = new JButton("取消网格");
		JButton btnEditorGird = new JButton("编辑网格");
		JButton btnSetPixel = new JButton("设置像素");

		this.addButton(Ids.BTN_OPEN_FILE, btnOpen, new OpenFileActionListener());
		this.addButton(Ids.BTN_EXPORT_FILE, btnExportFile);
		this.addButton(Ids.BTN_EXIT_FILE, btnExitSystem);
		this.addButton(Ids.BTN_SET_UP_GIRD, btnSetGird);
		this.addButton(Ids.BTN_CANCEL_GIRD, btnCancelGird);
		this.addButton(Ids.BTN_EDITOR_GIRD, btnEditorGird);
		this.addButton(Ids.BTN_SET_PIXEL, btnSetPixel);

	}

	public void addButton(String name, JButton jbutton,
			ActionListener... listeners) {
		jbutton.setMargin(new Insets(40, 20, 40, 20));
		if (listeners != null) {
			for (ActionListener listener : listeners) {
				jbutton.addActionListener(listener);
			}
		}

		this.add(jbutton, name);

	}

	private class OpenFileActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser dlg = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"jpg&gif&png", new String[]{"jpg", "gif", "png"});
			dlg.setFileFilter(filter);

			dlg.setDialogType(JFileChooser.OPEN_DIALOG);
			dlg.showOpenDialog(null);
		}

	}
}
