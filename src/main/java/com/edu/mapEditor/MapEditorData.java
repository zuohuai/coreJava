package com.edu.mapEditor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.edu.mapEditor.model.Point;
import com.edu.mapEditor.model.State;

/**
 * 图形编辑器数据存储
 * @author zuohuai
 */
@Component
public class MapEditorData {
	/** 设置图片的X */
	private int imgX = 0;
	/** 设置图片的Y */
	private int imgY = 0;
	/** 图片的宽 */
	private int imgWeight = 0;
	/** 图片的高 */
	private int imgHeight = 0;
	/** 设置图片的路径 */
	private String imgPath = StringUtils.EMPTY;
	/** 图片名称 */
	private String imgName = StringUtils.EMPTY;

	/** 像素的X坐标 */
	private int prixel = 16;
	public static final String PRIXEL_END = "px";
	/** 检查是否已经绘制了线条 */
	private boolean drawLine;
	/** 设置像素 */
	private boolean editorGird;
	/** 存储每个像素点的状态 */
	private Map<Point, State> points = new HashMap<Point, State>();

	/**
	 * 修改节点的状态
	 * @param point
	 * @param state
	 */
	public void modifyPoint(Point point, State state) {
		State current = points.get(point);
		if (current == null) {
			return;
		}
		points.put(point, state);
	}

	public int getPointsLength() {
		return points.size();
	}

	public State getState(Point point) {
		return points.get(point);
	}

	/**
	 * 添加节点
	 * @param point
	 * @param state
	 */
	public void addPoint(Point point, State state) {
		points.put(point, state);
	}

	/**
	 * 清理所有的像素点
	 */
	public void clearPoints() {
		this.points.clear();
	}

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

	public void modifyEditorGird(boolean editorGird) {
		this.editorGird = editorGird;
	}

	public void modiyfImgPosition(int imgX, int imgY) {
		this.imgX = imgX;
		this.imgY = imgY;
	}

	public void modiyfImgSize(int imgWeight, int imgHeight) {
		this.imgWeight = imgWeight;
		this.imgHeight = imgHeight;
	}

	public void modifyImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public void modifyImgName(String imgName) {
		this.imgName = imgName;
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

	public String getImgName() {
		return imgName;
	}

	public boolean isEditorGird() {
		return editorGird;
	}

	public Map<Point, State> getPoints() {
		return points;
	}

	public int getImgHeight() {
		return imgHeight;
	}

	public int getImgWeight() {
		return imgWeight;
	}

	public static String getPrixelEnd() {
		return PRIXEL_END;
	}
}
