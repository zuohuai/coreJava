package com.edu.mapEditor.view;

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
import com.edu.mapEditor.listener.JBtnCancelLineListener;
import com.edu.mapEditor.listener.JBtnEditorGirdListener;
import com.edu.mapEditor.listener.JBtnExitListener;
import com.edu.mapEditor.listener.JBtnExportListener;
import com.edu.mapEditor.listener.JBtnInputPixedListener;
import com.edu.mapEditor.listener.JBtnOpenFileListener;
import com.edu.mapEditor.listener.JBtnSetGirdListener;
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
	@Autowired
	private JBtnOpenFileListener openFileListener;
	@Autowired
	private JBtnExitListener exitListener;
	@Autowired
	private JBtnInputPixedListener inputPixedListener;
	@Autowired
	private JBtnSetGirdListener setUpGirdListener;
	@Autowired
	private JBtnEditorGirdListener editorGirdListenr;
	@Autowired
	private JBtnCancelLineListener cancelLineListener;
	@Autowired
	private JBtnExportListener exportListener;

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

		this.addButton(Ids.BTN_OPEN_FILE, btnOpen, openFileListener);
		this.addButton(Ids.BTN_EXPORT_FILE, btnExportFile, exportListener);
		this.addButton(Ids.BTN_EXIT_FILE, btnExitSystem, exitListener);
		this.addButton(Ids.BTN_SET_UP_GIRD, btnSetGird, setUpGirdListener);
		this.addButton(Ids.BTN_CANCEL_GIRD, btnCancelGird,cancelLineListener);
		this.addButton(Ids.BTN_EDITOR_GIRD, btnEditorGird, editorGirdListenr);
		this.addButton(Ids.BTN_SET_PIXEL, btnSetPixel, inputPixedListener);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JBtnOpenFileListener getOpenFileListener() {
		return openFileListener;
	}

	public JBtnExitListener getExitListener() {
		return exitListener;
	}

	public JBtnInputPixedListener getInputPixedListener() {
		return inputPixedListener;
	}

	public JBtnSetGirdListener getSetUpGirdListener() {
		return setUpGirdListener;
	}

	public JBtnEditorGirdListener getEditorGirdListenr() {
		return editorGirdListenr;
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
