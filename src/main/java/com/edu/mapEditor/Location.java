package com.edu.mapEditor;

import java.awt.Dimension;
import java.awt.Toolkit;

public class Location {
	private static final double PERCENT = 0.8;
	
	public static int MapEditorSizeX;
	public static int MapEditorSizeY;
	public static int MapEditorPostionX;
	public static int MapEditorPostionY;
	
	static {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		 
		//对主窗口的初始化
		 MapEditorSizeX = (int)(width*PERCENT);
		 MapEditorSizeY = (int)(height*PERCENT);
		 MapEditorPostionX = (width-MapEditorSizeX)/2;
		 MapEditorPostionY = (height-MapEditorSizeY)/2;
	}
	
	
}
