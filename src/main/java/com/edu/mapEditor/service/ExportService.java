package com.edu.mapEditor.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
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
		Collections.sort(points);
		for (Point point : points) {
			State state = mapEditorData.getState(point);
			if (state == null) {
				throw new IllegalArgumentException("初始化异常，请检查");
			}
			exportVo.addValue(state.getState());
		}
		String values = JsonUtils.object2String(exportVo);
		try {
			FileUtils.writeStringToFile(new File(filePath), values);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

	}
}
