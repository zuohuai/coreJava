package com.edu.mapEditor;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MapEditorFrame extends JFrame {
	
	public static void main(String[] args) {
		MapEditorFrame mapEditorFrame = new MapEditorFrame();
	}

	public MapEditorFrame() {
		//获取屏幕的大小
		this.setSize(Location.MapEditorSizeX, Location.MapEditorSizeY);
	    this.setLocation(Location.MapEditorPostionX,Location.MapEditorPostionY);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
        JPanel contentPane=new JPanel();
        this.setContentPane(contentPane);
        BorderLayout lay=new BorderLayout();//创建一个布局管理器对象，将中间容器设置为此布局管理
        this.setLayout(lay);
        contentPane.add(new RigthOperatorPanel(),"East");
        contentPane.add(new LeftImgePanel(),"Center");
	}	
}
