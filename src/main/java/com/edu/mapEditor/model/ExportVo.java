package com.edu.mapEditor.model;

import java.util.ArrayList;
import java.util.List;

import com.edu.mapEditor.MapEditorData;

/**
 * 导出数据的Vo类
 * @author Administrator
 */
public class ExportVo {
	/**长度*/
	private int width;
	/**高度*/
	private int height;
	/** 单位像素 */
	private int pixel;
	/** 数据 */
	private List<Integer> values = new ArrayList<Integer>();

	public static ExportVo valueOf(MapEditorData mapEditorData) {
		ExportVo vo = new ExportVo();
		vo.width = mapEditorData.getImgWeight();
		vo.height = mapEditorData.getImgHeight();
		vo.pixel = mapEditorData.getPrixel();
		return vo;
	}

	public int getWidth() {
		return width;
	}

	public void addValue(int value){
		values.add(value);
	}
	
	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getPixel() {
		return pixel;
	}

	public void setPixel(int pixel) {
		this.pixel = pixel;
	}

	public List<Integer> getValues() {
		return values;
	}

	public void setValues(List<Integer> values) {
		this.values = values;
	}
}
