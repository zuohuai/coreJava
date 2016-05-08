package com.edu.math.service;

import java.awt.Dimension;
import java.awt.RenderingHints;
import java.io.File;
import java.util.List;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.FastScatterPlot;
import org.omg.PortableServer.POA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edu.math.MapEditorData;
import com.edu.math.view.JImageComponent;
import com.edu.math.view.LeftImgePanel;

/**
 * 分析服务类
 * 
 * @author zuohuai
 *
 */
@Component
public class AnalysisService {

	/**  图片显示面板 */
	@Autowired
	private LeftImgePanel leftImgePanel;
	/** 图片组件 */
	@Autowired
	private JImageComponent jImageComponent;
	@Autowired
	private MapEditorData mapEditorData;

	public void analysis() {
		// 暂时不考虑作检查
		try {
			File file = draw();
			jImageComponent.loadImage(file);
			this.leftImgePanel.add(jImageComponent);
			this.leftImgePanel.repaint();

		} catch (Exception exception) {
			exception.printStackTrace();
		}
		System.out.println("分析生成的数据");
	}

	private File draw() {
		try {
			final NumberAxis domainAxis = new NumberAxis("X");
			domainAxis.setAutoRangeIncludesZero(false);
			final NumberAxis rangeAxis = new NumberAxis("Y");
			rangeAxis.setAutoRangeIncludesZero(false);

			float[][] data = build();
			final FastScatterPlot plot = new FastScatterPlot(data, domainAxis, rangeAxis);
			final JFreeChart chart = new JFreeChart(mapEditorData.getFocusBtnName(), plot);
			chart.getRenderingHints().put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			final ChartPanel panel = new ChartPanel(chart, true);
			panel.setMinimumDrawHeight(50);
			panel.setMinimumSize(new Dimension(50,50));
			panel.setMaximumDrawHeight(leftImgePanel.getWidth());
			panel.setMinimumDrawWidth(100);
			panel.setMaximumDrawWidth(leftImgePanel.getHeight());
			File focusImgFile = new File(mapEditorData.getFocusImg());
			ChartUtilities.saveChartAsJPEG(focusImgFile, chart, leftImgePanel.getWidth(), leftImgePanel.getHeight());
			return focusImgFile;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private float[][] build() {
		List<Integer> data = mapEditorData.getTimeByFocus();
		int size = data.size();
		float[][] result = new float[2][size];
		for (int i = 0; i < size; i++) {
			result[0][i] = i;
			if (i == 0) {
				result[1][i] = 0;
			} else {
				result[1][i] = data.get(i) - data.get(i - 1);
			}
		}
		return result;
	}
}
