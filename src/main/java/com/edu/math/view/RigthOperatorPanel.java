package com.edu.math.view;

import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.edu.mapEditor.MapEditorData;
import com.edu.mapEditor.model.Ids;

/**
 * 右侧面板
 * 
 * @author Administrator
 * 
 */
@Component
public class RigthOperatorPanel extends JPanel implements ApplicationListener<ContextRefreshedEvent> {

	private static final long serialVersionUID = -4680757892101282548L;
	@Autowired
	private MapEditorData mapEditorData;

	private JButton btnOpen = new JButton("打开文件");
	private JButton btnExportFile = new JButton("导出文件");
	private JButton btnExitSystem = new JButton("退出系统");
	private JButton btnSetGird = new JButton("设置网格");
	private JButton btnCancelGird = new JButton("取消网格");
	private JButton btnEditorGird = new JButton("编辑网格");
	private JButton btnSetPixel = new JButton("设置像素");

	public RigthOperatorPanel() {

	}

	public void addButton(String name, JButton jbutton, ActionListener... listeners) {
		jbutton.setMargin(new Insets(20, 10, 20, 10));
		if (listeners != null) {
			for (ActionListener listener : listeners) {
				jbutton.addActionListener(listener);
			}
		}

		this.add(jbutton, name);
	}

	public MapEditorData getMapEditorData() {
		return mapEditorData;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// 设置布局
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);

		this.addButton(Ids.BTN_OPEN_FILE, btnOpen);
		this.addButton(Ids.BTN_EXPORT_FILE, btnExportFile);
		this.addButton(Ids.BTN_EXIT_FILE, btnExitSystem);
		this.addButton(Ids.BTN_SET_UP_GIRD, btnSetGird);
		this.addButton(Ids.BTN_CANCEL_GIRD, btnCancelGird);
		this.addButton(Ids.BTN_EDITOR_GIRD, btnEditorGird);
		this.addButton(Ids.BTN_SET_PIXEL, btnSetPixel);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JButton getBtnOpen() {
		return btnOpen;
	}

	public JButton getBtnExportFile() {
		return btnExportFile;
	}

	public JButton getBtnExitSystem() {
		return btnExitSystem;
	}

	public JButton getBtnSetGird() {
		return btnSetGird;
	}

	public JButton getBtnCancelGird() {
		return btnCancelGird;
	}

	public JButton getBtnEditorGird() {
		return btnEditorGird;
	}

	public JButton getBtnSetPixel() {
		return btnSetPixel;
	}
	
	
}
