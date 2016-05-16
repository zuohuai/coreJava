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

import com.edu.math.MapEditorData;
import com.edu.math.listener.JBtnOpenFileListener;
import com.edu.math.model.Ids;

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
	private JBtnOpenFileListener jBtnOpenFileListener;
	
	private JButton btnOpen = new JButton("生成报表");
	
	private JButton btnTest = new JButton("");

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

		this.addButton(Ids.BTN_OPEN_FILE, btnOpen, jBtnOpenFileListener);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JButton getBtnOpen() {
		return btnOpen;
	}
}
