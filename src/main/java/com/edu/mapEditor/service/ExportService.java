package com.edu.mapEditor.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edu.jackson.JsonUtils;
import com.edu.mapEditor.MapEditorData;
import com.edu.mapEditor.model.ExportVo;
import com.edu.mapEditor.model.Point;
import com.edu.mapEditor.model.State;

/**
 * 导出数据按钮
 * @author Administrator
 */
@Component
public class ExportService {
	@Autowired
	private MapEditorData mapEditorData;

	private String exportPath = StringUtils.EMPTY;
	{
		exportPath = System.getProperty("user.dir") + File.separator + "export";
	}

	public void export() {
		String imgName = mapEditorData.getImgName();
		int indx = imgName.lastIndexOf(".");
		String fileName = imgName.substring(0, indx) + ".json";
		String filePath = exportPath + File.separator + fileName;
		ExportVo exportVo = ExportVo.valueOf(mapEditorData);
		List<Point> points = new ArrayList<Point>(mapEditorData.getPoints().keySet());
		for (Point point : points) {
			State state = mapEditorData.getState(point);
			if (state == null) {
				throw new IllegalArgumentException("初始化异常，请检查");
			}
			point.mergeState(state);
		}
		Collections.sort(points);
		System.out.println(JsonUtils.object2String(points));
		
		int dive = mapEditorData.getImgHeight() % mapEditorData.getPrixel();
		int len = mapEditorData.getImgHeight() / mapEditorData.getPrixel();
		if(dive > 0){
			len =len +1;
		}
		
		System.out.println(len);
		int idx = 1;
		List<Integer> xs = new LinkedList<>();;
		for (Point point : points) {
			if (indx == len) {
				xs = new LinkedList<>();
			}
		}
		/* String values = JsonUtils.object2String(exportVo); try { FileUtils.writeStringToFile(new File(filePath),
		 * values); } catch (Exception e) { e.printStackTrace(); } finally {
		 * 
		 * } */

	}
}
