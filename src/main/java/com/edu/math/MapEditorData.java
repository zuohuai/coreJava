package com.edu.math;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.stereotype.Component;

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

	/** 统计次数 */
	private int total = 10000;
	/** key:物品ID, value:权重 */
	private Map<Integer, Integer> src = new HashMap<>();
	/** key:权重, value:次数 */
	private Map<Integer, Integer> values = new HashMap<>();
	/** key:次数, value: 值 */
	private Map<Integer, Integer> times = new HashMap<>();

	/** 需要关注的焦点数据 */
	private int focus = -1;
	/** 关注点的Btn的名称 */
	public static final String FOCUS_BTN = "FOCUS_BTN";
	/** 已经构建的Button 名称 */
	public static final Set<String> BTN_NAMES = new HashSet<>();

	private int stardY = -1;
	/** 整体的概率分布图 */
	private String percentImg = "percent.png";
	/** 关注点分布图 */
	private String focusImg = "focus.png";

	{
		// 來源数据促使的初始化 5%
		src.put(1, 1);
		src.put(0, 19);
		/** 我们这里只是关注物品ID为1的次数分布图 */
		focus = 1;

		stardY = 20;

		String path = System.getProperty("user.dir") + File.separator + "file" + File.separator;
		percentImg = path + percentImg;
		focusImg = path + focusImg;
	}

	public int getStardY() {
		return stardY;
	}

	public boolean containName(String name) {
		return BTN_NAMES.contains(name);
	}

	public void addBtnName(String name) {
		BTN_NAMES.add(name);
	}

	public String getFocusBtnName() {
		int weight = src.get(focus);
		return "物品" + focus + "-" + weight + "分析";
	}

	public List<Integer> getTimeByFocus() {
		List<Integer> result = new LinkedList<>();
		for (Entry<Integer, Integer> entry : times.entrySet()) {
			int value = entry.getValue();
			int key = entry.getKey();
			if (value == focus) {
				result.add(key);
			}
		}
		return result;
	}

	public void reset() {
		this.values.clear();
		this.times.clear();
	}

	public String getPercentImg() {
		return percentImg;
	}

	public String getFocusImg() {
		return focusImg;
	}

	public Map<Integer, Integer> getTimes() {
		return times;
	}

	public Map<Integer, Integer> getSrc() {
		return src;
	}

	public Map<Integer, Integer> getValues() {
		return values;
	}

	public int getFocus() {
		return focus;
	}

	public void modiyfImgPosition(int imgX, int imgY) {
		this.imgX = imgX;
		this.imgY = imgY;
	}

	public void modiyfImgSize(int imgWeight, int imgHeight) {
		this.imgWeight = imgWeight;
		this.imgHeight = imgHeight;
	}

	public int getImgX() {
		return imgX;
	}

	public int getImgY() {
		return imgY;
	}

	public int getImgHeight() {
		return imgHeight;
	}

	public int getImgWeight() {
		return imgWeight;
	}

	public int getTotal() {
		return total;
	}
}
