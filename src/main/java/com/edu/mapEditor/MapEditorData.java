package com.edu.mapEditor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 图形编辑器数据存储
 * 
 * @author zuohuai
 *
 */
@Component
public class MapEditorData {
	/** 设置图片的X */
	private int imgX = 0;
	/** 设置图片的Y */
	private int imgY = 0;
	/** 设置图片的路径 */
	private String imgPath = StringUtils.EMPTY;

	/** 像素的X坐标 */
	private int prixel = 16;
	public static final String PRIXEL_END = "px";
	/** 检查是否已经绘制了线条 */
	private boolean drawLine;

	public void modifyPrixel(String value) {
		// 去除单位
		if (value == null) {
			return;
		}
		// 检查是否存在结束标志位
		int index = value.indexOf(PRIXEL_END);
		if (index == -1) {
			return;
		}
		value = value.substring(0, index);

		// 设置像素值
		this.prixel = Integer.parseInt(value);
	}

	public void modifyDrawLine(boolean drawLine) {
		this.drawLine = drawLine;
	}

	public void modiyfImgPosition(int imgX, int imgY) {
		this.imgX = imgX;
		this.imgY = imgY;
	}

	public void modifyImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public int getPrixel() {
		return prixel;
	}

	public boolean isDrawLine() {
		return drawLine;
	}

	public int getImgX() {
		return imgX;
	}

	public int getImgY() {
		return imgY;
	}

	public String getImgPath() {
		return imgPath;
	}
}
