package com.edu.mapEditor;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.MatteBorder;
import javax.swing.event.MouseInputAdapter;

//图片的拖动效果

public class DragPictrue extends JFrame {

	private static final long serialVersionUID = -7246103224064240652L;

	// 显示图片图片，并拖动。
	private JLabel dragPicLabel;

	public DragPictrue() {
		String path = System.getProperty("user.dir");
		// 实例化图标
		Icon image = new ImageIcon(path + java.io.File.separator +"demo.jpg");
		// 实例化带图片的标签
		dragPicLabel = new JLabel(image);
		dragPicLabel.setBackground(Color.red);
		dragPicLabel.setBorder(new MatteBorder(10, 10, 10, 10, Color.black));

		dragPicLabel.setBounds(5, 5, image.getIconWidth() + 20, image
				.getIconHeight() + 20);

		getContentPane().setLayout(null);
		// 增加标签到容器上
		getContentPane().add(dragPicLabel);

		// 鼠标事件处理
		DragPicListener listener = new DragPicListener();
		// 增加标签的事件处理
		dragPicLabel.addMouseListener(listener);
		dragPicLabel.addMouseMotionListener(listener);

		setTitle("ZakiSoft.com 图片的拖动效果");
		// 设置窗口尺寸
		setSize(900, 600);

		// 关闭窗口时退出程序
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(getOwner());
		// 设置窗口为可视
		setVisible(true);
	}

	class DragPicListener extends MouseInputAdapter {
		/** 坐标点 */
		Point point = new Point(0, 0);

		/**
		 * 当鼠标拖动时触发该事件。 记录下鼠标按下(开始拖动)的位置。
		 */
		public void mouseDragged(MouseEvent e) {
			// 转换坐标系统
			Point newPoint = SwingUtilities.convertPoint(dragPicLabel, e
					.getPoint(), dragPicLabel.getParent());
			System.out.println(newPoint.x + " : " + newPoint.y);
			// 设置标签的新位置
			dragPicLabel.setLocation(dragPicLabel.getX()
					+ (newPoint.x - point.x), dragPicLabel.getY()
					+ (newPoint.y - point.y));
			// 更改坐标点
			point = newPoint;
		}

		/**
		 * 当鼠标按下时触发该事件。 记录下鼠标按下(开始拖动)的位置。
		 */
		public void mousePressed(MouseEvent e) {
			// 得到当前坐标点
			point = SwingUtilities.convertPoint(dragPicLabel, e.getPoint(),
					dragPicLabel.getParent());
			System.out.println(e.getPoint().x + " : " + e.getPoint().y);
			System.out.println(point.x + " : " + point.y);
		}
	}

	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		new DragPictrue();
	}
}