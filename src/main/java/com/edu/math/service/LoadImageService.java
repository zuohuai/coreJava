package com.edu.math.service;

import java.awt.Font;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.edu.math.MapEditorData;
import com.edu.math.RatioProbability;
import com.edu.math.listener.JButtonFocusAnalysisListener;
import com.edu.math.view.JImageComponent;
import com.edu.math.view.LeftImgePanel;
import com.edu.math.view.RigthOperatorPanel;

/**
 * 生成一张报表
 * 
 * @author zuohuai
 *
 */
@Component
public class LoadImageService {
	/**  图片显示面板 */
	@Autowired
	private LeftImgePanel leftImgePanel;
	/** 图片组件 */
	@Autowired
	private JImageComponent jImageComponent;
	@Autowired
	private MapEditorData mapEditorData;
	@Autowired
	private RigthOperatorPanel rigthOperatorPanel;
	@Autowired
	private JButtonFocusAnalysisListener jButtonFocusAnalysisListener;

	

	public void export() {
		// 暂时不考虑作检查
		try {
			File file = draw();
			jImageComponent.loadImage(file);
			this.leftImgePanel.add(jImageComponent);
			
			//生成关注点的Btn
			if(!mapEditorData.containName(MapEditorData.FOCUS_BTN)){
				JButton btnExportFile = new JButton(mapEditorData.getFocusBtnName());
				this.rigthOperatorPanel.addButton(MapEditorData.FOCUS_BTN, btnExportFile, jButtonFocusAnalysisListener);
				mapEditorData.addBtnName(MapEditorData.FOCUS_BTN);
				this.rigthOperatorPanel.updateUI();
			}

			this.leftImgePanel.updateUI();
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private File draw() throws Exception {
		// 种类数据集
		DefaultCategoryDataset ds = new DefaultCategoryDataset();
		build(ds);

		Font font = new Font("宋体", Font.BOLD, 20);
		// 创建柱状图,柱状图分水平显示和垂直显示两种
		JFreeChart chart = ChartFactory.createBarChart("次数分布", "次数", "值", ds, PlotOrientation.VERTICAL, true, true,
				true);

		// 设置整个图片的标题字体
		chart.getTitle().setFont(font);

		// 设置提示条字体
		font = new Font("宋体", Font.BOLD, 15);
		chart.getLegend().setItemFont(font);

		// 得到绘图区
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		// 得到绘图区的域轴(横轴),设置标签的字体
		plot.getDomainAxis().setLabelFont(font);

		// 设置横轴标签项字体
		plot.getDomainAxis().setTickLabelFont(font);

		// 设置范围轴(纵轴)字体
		plot.getRangeAxis().setLabelFont(font);
		// 存储成图片

		plot.setForegroundAlpha(1.0f);
		File target = new File(mapEditorData.getPercentImg());
		ChartUtilities.saveChartAsJPEG(target, chart, leftImgePanel.getWidth(), leftImgePanel.getHeight());
		return target;
	}

	/**
	 * 构建对应的数据来源
	 * 
	 * @param ds
	 */
	private void build(DefaultCategoryDataset ds) {
		mapEditorData.reset();
		Map<Integer, Integer> resultTimes = mapEditorData.getValues();
		Map<Integer, Integer> times = mapEditorData.getTimes();
		int total = mapEditorData.getTotal();
		for (int i = 1; i <= total; i++) {
			// 计算出现的次数
			Integer result = RatioProbability.valueOf(mapEditorData.getSrc()).getResult();
			Integer value = resultTimes.get(result);
			if (value == null) {
				value = 0;
			}
			value++;
			resultTimes.put(result, value);
			times.put(i, result);
		}
		List<Integer> keys = new LinkedList<>(resultTimes.keySet());
		Collections.sort(keys);
		for (Integer key : keys) {
			Integer value = resultTimes.get(key);
			ds.setValue(value, "随机抽卡(" + mapEditorData.getSrc().get(key) + ")-" + key, "随机值");
		}
	}
}
