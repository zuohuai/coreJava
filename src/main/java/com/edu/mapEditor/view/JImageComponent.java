package com.edu.mapEditor.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.edu.mapEditor.MapEditorData;
import com.edu.mapEditor.listener.JImageListener;

/**
 * 图片读取组建 使用了JImageComponent的开发者应该记得在修改或者编辑了图像之后调用repaint()方法.
 * 
 * @author zuohuai
 *
 */
@Component
public class JImageComponent extends JComponent implements ApplicationListener<ContextRefreshedEvent> {

	private static final long serialVersionUID = 8618441444312688356L;

	private BufferedImage bufferedImage = null;

	private Graphics imageGraphics = null;
	@Autowired
	private JImageListener jImageListener;
	@Autowired
	private MapEditorData mapEditorData;

	public JImageComponent() {

	}

	public JImageComponent(BufferedImage bufferedImage) {
		this.setBufferedImage(bufferedImage);
	}

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	public void setBufferedImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
		int x = mapEditorData.getImgX();
		int y = mapEditorData.getImgY();
		if (this.bufferedImage == null) {
			this.imageGraphics = null;
			this.setBounds(x, y, 0, 0);
		} else {
			this.imageGraphics = this.bufferedImage.createGraphics();
			this.setBounds(x, y, this.bufferedImage.getWidth(), this.bufferedImage.getHeight());
		}

		// 设置绘制状态
		mapEditorData.modifyDrawLine(false);
		this.mapEditorData.modiyfImgSize(this.bufferedImage.getWidth(), this.bufferedImage.getHeight());
	}

	public Graphics getImageGraphics() {
		return imageGraphics;
	}

	public void setImageGraphics(Graphics imageGraphics) {
		this.imageGraphics = imageGraphics;
	}

	public void loadImage(URL imageLocation) throws IOException {
		this.bufferedImage = ImageIO.read(imageLocation);
		this.setBufferedImage(this.bufferedImage);
	}

	public void loadImage(File imageLocation) throws IOException {
		this.bufferedImage = ImageIO.read(imageLocation);
		this.setBufferedImage(this.bufferedImage);

		// 设置文件的路径
		this.mapEditorData.modifyImgPath(imageLocation.getAbsolutePath());
		this.mapEditorData.modifyImgName(imageLocation.getName());
		//this.repaint();
	}

	/*
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {

		// Exit if no image is loaded.
		if (this.bufferedImage == null) {
			return;
		}

		// Paint the visible region.
		Rectangle rectangle = this.getVisibleRect();
		paintImmediately(g, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
	};

	/*
	 * @see javax.swing.JComponent#paintImmediately(int, int, int, int)
	 */
	@Override
	public void paintImmediately(int x, int y, int width, int height) {

		// Exit if no image is loaded.
		if (this.bufferedImage == null) {
			return;
		}

		// Paint the region specified.
		this.paintImmediately(super.getGraphics(), x, y, width, height);
	}

	/*
	 * @see javax.swing.JComponent#paintImmediately(java.awt.Rectangle)
	 */
	@Override
	public void paintImmediately(Rectangle rectangle) {

		// Exit if no image is loaded.
		if (this.bufferedImage == null) {
			return;
		}

		// Paint the region specified.
		this.paintImmediately(super.getGraphics(), rectangle.x, rectangle.y, rectangle.width, rectangle.height);
	}

	private void paintImmediately(Graphics g, int x, int y, int width, int height) {

		// Exit if no image is loaded.
		if (this.bufferedImage == null) {
			return;
		}

		int imageWidth = this.bufferedImage.getWidth();
		int imageHeight = this.bufferedImage.getHeight();

		// Exit if the dimension is beyond that of the image.
		if (x >= imageWidth || y >= imageHeight) {
			return;
		}

		// Calculate the rectangle of the image that should be rendered.
		int x1 = x < 0 ? 0 : x;
		int y1 = y < 0 ? 0 : y;
		int x2 = x + width - 1;
		int y2 = y + height - 1;

		if (x2 >= imageWidth) {
			x2 = imageWidth - 1;
		}

		if (y2 >= imageHeight) {
			y2 = imageHeight - 1;
		}

		// Draw the image.
		g.drawImage(this.bufferedImage, x1, y1, x2, y2, x1, y1, x2, y2, null);
	}

	/**
	 * Returns the height of the image.
	 */
	@Override
	public int getHeight() {
		if (this.bufferedImage == null) {
			return 0;
		}

		return this.bufferedImage.getHeight();
	}

	/**
	 * Returns the size of the image.
	 */
	@Override
	public Dimension getPreferredSize() {
		if (this.bufferedImage == null) {
			return new Dimension(0, 0);
		}

		return new Dimension(this.bufferedImage.getWidth(), this.bufferedImage.getHeight());
	}

	/**
	 * Returns the size of the image.
	 */
	@Override
	public Dimension getSize() {
		if (this.bufferedImage == null) {
			return new Dimension(0, 0);
		}

		return new Dimension(this.bufferedImage.getWidth(), this.bufferedImage.getHeight());
	}

	/**
	 * Returns the width of the image.
	 */
	@Override
	public int getWidth() {
		if (this.bufferedImage == null) {
			return 0;
		}

		return this.bufferedImage.getWidth();
	}

	/**
	 * Returns the Graphics object for the image.
	 */
	@Override
	public Graphics getGraphics() {
		return this.imageGraphics;
	}

	/**
	 * Resizes the image to the given dimensions and type.
	 * 
	 * Note that the image is "cropped" from the left top corner).
	 * 
	 * @param width
	 *            The new width of the image.
	 * @param height
	 *            The new height of the image.
	 * @param imageType
	 *            The new image type (<code>BufferedImage type)
	 * @see type java.awt.image.BufferedImage#BufferedImage(int, int, int)
	 */
	public void resize(int width, int height, int imageType) {

		// Create a new image if none is loaded.
		if (this.bufferedImage == null) {
			setBufferedImage(new BufferedImage(width, height, imageType));
			return;
		}

		// Create a new temporary image.
		BufferedImage tempImage = new BufferedImage(width, height, imageType);
		int w = this.bufferedImage.getWidth();
		int h = this.bufferedImage.getHeight();

		// Crop width if necessary.
		if (width < w) {
			w = width;
		}

		// Crop height if necessary.
		if (height < h) {
			h = height;
		}

		// Copy if the type is the same.
		if (this.bufferedImage.getType() == imageType) {

			Graphics g = tempImage.getGraphics();
			g.drawImage(this.bufferedImage, 0, 0, w, h, null);
		}

		// Copy pixels to force conversion.
		else {

			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					tempImage.setRGB(x, y, this.bufferedImage.getRGB(x, y));
				}
			}
		}

		// Set the new image.
		setBufferedImage(tempImage);
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// 增加标签的事件处理
		this.addMouseListener(jImageListener);
		this.addMouseMotionListener(jImageListener);

	}

}
