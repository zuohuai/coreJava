package com.edu.math.service;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYBoxAnnotation;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.DefaultXYDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edu.math.MapEditorData;
import com.edu.math.view.JImageComponent;
import com.edu.math.view.LeftImgePanel;

/**
 * 分析服务类
 * @author zuohuai
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
	}

	private File draw() {
		try {
			// 构建xydataSet
			double[][] data = build();
			DefaultXYDataset xydataset = new DefaultXYDataset();
			xydataset.addSeries(mapEditorData.getFocusBtnName(), data);


			
			// 构建jfreechart
			JFreeChart jfreechart = ChartFactory.createScatterPlot(mapEditorData.getFocusBtnName(), "times", "gap", xydataset,
					PlotOrientation.VERTICAL, true, false, false);
			
			Font titleFont = new Font("黑体", Font.BOLD, 20);  
			TextTitle textTitle = jfreechart.getTitle();  
			textTitle.setFont(titleFont);// 为标题设置上字体  
			
			Font LegendFont = new Font("楷体", Font.PLAIN, 18);  
			LegendTitle legend = jfreechart.getLegend(0);  
			legend.setItemFont(LegendFont);// 为图例说明设置字体 
			
			jfreechart.setBackgroundPaint(Color.white);
			jfreechart.setBorderPaint(Color.GREEN);
			jfreechart.setBorderStroke(new BasicStroke(1.5f));
			XYPlot xyplot = (XYPlot) jfreechart.getPlot();
			
			//构建边线
	        XYBoxAnnotation box = new XYBoxAnnotation(0, 0, leftImgePanel.getWidth(), mapEditorData.getStardY()); //正常血压所在区域内边界  	    
	        xyplot.addAnnotation(box);    ;  

			File focusImgFile = new File(mapEditorData.getFocusImg());
			ChartUtilities.saveChartAsJPEG(focusImgFile, jfreechart, leftImgePanel.getWidth(),
					leftImgePanel.getHeight());
			return focusImgFile;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private double[][] build() {
		List<Integer> data = mapEditorData.getTimeByFocus();
		int size = data.size();
		double[][] result = new double[2][size];
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
