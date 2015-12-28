package com.edu.mapEditor.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.edu.mapEditor.model.Location;

@Component
public class MapEditorFrame extends JFrame implements ApplicationListener<ContextRefreshedEvent> {
	private static final long serialVersionUID = -8744723234939432765L;
	@Autowired
	private RigthOperatorPanel rigthOperatorPanel;
	@Autowired
	private LeftImgePanel leftImgePanel;

	public MapEditorFrame() {

	}

	public RigthOperatorPanel getRigthOperatorPanel() {
		return rigthOperatorPanel;
	}

	public LeftImgePanel getLeftImgePanel() {
		return leftImgePanel;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// 获取屏幕的大小
		this.setSize(Location.MapEditorSizeX, Location.MapEditorSizeY);
		this.setLocation(Location.MapEditorPostionX, Location.MapEditorPostionY);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

		JPanel contentPane = new JPanel();
		this.setContentPane(contentPane);
		BorderLayout lay = new BorderLayout();// 创建一个布局管理器对象，将中间容器设置为此布局管理
		this.setLayout(lay);
		contentPane.add(rigthOperatorPanel, "East");
		contentPane.add(leftImgePanel, "Center");

	}
}
