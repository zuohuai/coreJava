package com.edu.mapEditor.view;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.filechooser.FileSystemView;

/**
 * 截图程序演示 按确定键截图
 */
public class WindowTest {
	public static void main(String[] args) throws Exception {
		Deom d = new Deom();
		// 全屏显示
		GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(d);
	}

}

class Deom extends JFrame {
	Point sp = new Point();
	Point ep = new Point();
	Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

	BufferedImage img = new BufferedImage((int) d.getWidth(), (int) d.getHeight(), BufferedImage.TYPE_INT_RGB);

	public Deom() throws Exception {

		Robot r = new Robot();
		img = r.createScreenCapture(new Rectangle(0, 0, d.width, d.height));
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				sp = e.getPoint();
			}
		});
		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				ep = e.getPoint();
				repaint();
			}
		});

		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				try {
					saveImg();
				} catch (IOException e1) {

					e1.printStackTrace();
				}

				System.exit(0);
			}

		});
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		// 设置透明度
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
		g2d.setComposite(ac);

		g2d.fill3DRect((int) Math.min(sp.getX(), ep.getX()), (int) Math.min(sp.getY(), ep.getY()),
				(int) Math.abs(sp.getX() - ep.getX()) + 1, // 防止值为零
				(int) Math.abs(sp.getY() - ep.getY()) + 1, false);
		g2d.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
	}

	public void saveImg() throws IOException {
		BufferedImage saveImg = img.getSubimage((int) Math.min(sp.getX(), ep.getX()),
				(int) Math.min(sp.getY(), ep.getY()), (int) Math.abs(sp.getX() - ep.getX()) + 1, // 防止值为零
				(int) Math.abs(sp.getY() - ep.getY()) + 1);
		SimpleDateFormat sd = new SimpleDateFormat("yyyymmddhhss");
		String name = sd.format(new Date());

		File path = FileSystemView.getFileSystemView().getHomeDirectory();
		File filepath = new File(path + File.separator + name + ".jpg");
		ImageIO.write(saveImg, "jpg", filepath);

	}
}
