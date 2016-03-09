package com.edu.work;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;

import com.edu.jackson.JsonUtils;
import com.edu.utils.time.DateUtils;

public class WorkTest {

	private static final String PATH = "E:\\workspace_nine\\审计\\20150101-20151231";

	private static final String PREFIX = "@@@";

	private Map<Long, Id2Name> id2NameMap = new HashMap<Long, Id2Name>();

	@Test
	public void test_work() throws Exception {
		// 构建id2NameMap
		String id2NameFilePath = PATH + File.separator + "id2name";
		FileReader id2NameReader = new FileReader(new File(id2NameFilePath));
		BufferedReader reader = new BufferedReader(id2NameReader);
		String line = reader.readLine();
		String[] array = null;
		while (line != null) {
			array = line.split(PREFIX);
			String account = array[0];
			long owner = Long.parseLong(array[1]);
			String name = array[2];
			id2NameMap.put(owner, Id2Name.valueOf(account, owner, name));
			line = reader.readLine();
		}
		reader.close();
		id2NameReader.close();

		// 读取文件
		HSSFWorkbook workbook = new HSSFWorkbook();
		String exportPath = PATH + File.separator + "主要玩家消费信息.xls";
		OutputStream out = new FileOutputStream(new File(exportPath));

		for (Entry<Long, Id2Name> entry : id2NameMap.entrySet()) {
			Id2Name id2Name = entry.getValue();
			String srcFilePath = PATH + File.separator + "zhangzuohuai";
			File srcFile = new File(srcFilePath);
			if (srcFile.isDirectory()) {
				File[] childs = srcFile.listFiles();
				for (File file : childs) {
					if (file.getName().contains(String.valueOf(id2Name.getOwner()))) {
						// 生成一个表格
						HSSFSheet sheet = workbook.createSheet(id2Name.getName());
						// 设置表格默认列宽度为15个字节
						sheet.setDefaultColumnWidth(15);

						// 设置标题
						HSSFCellStyle titleStyle = workbook.createCellStyle();
						// 居中显示
						titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
						// 标题字体
						HSSFFont titleFont = workbook.createFont();
						// 字体大小
						titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
						titleStyle.setFont(titleFont);

						HSSFCellStyle contentStyle = workbook.createCellStyle();
						contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
						HSSFFont contentFont = workbook.createFont();
						contentFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
						contentStyle.setFont(contentFont);

						// 产生表格标题行
						HSSFRow row = sheet.createRow(0);
						String[] headers = new String[] { "用户ID", "用户昵称", "操作时间", "消费方式名称", "SonType", "消费方式名称" };
						for (int i = 0; i < headers.length; i++) {
							HSSFCell cell = row.createCell(i);
							HSSFRichTextString text = new HSSFRichTextString(headers[i]);
							cell.setCellValue(text);
							cell.setCellStyle(titleStyle);
						}

						FileReader currentFileReader = new FileReader(file);
						BufferedReader currentBfReader = new BufferedReader(currentFileReader);
						String data = currentBfReader.readLine();
						int rowCount = 1;
						while (data != null) {
							CurrencyRecord currencyRecord = JsonUtils.string2Object(data, CurrencyRecord.class);
							if(currencyRecord.getChange() < 0){
								HSSFRow dataRow = sheet.createRow(rowCount);
								
								// 用户ID
								HSSFCell cell0 = dataRow.createCell(0);
								cell0.setCellValue(String.valueOf(currencyRecord.getUserId()));
								cell0.setCellStyle(contentStyle);

								// 用户昵称
								HSSFCell cell1 = dataRow.createCell(1);
								cell1.setCellValue(id2Name.getName());
								cell1.setCellStyle(contentStyle);

								// 操作时间
								HSSFCell cell2 = dataRow.createCell(2);
								cell2.setCellValue(DateUtils.date2String(new Date(currencyRecord.getTime()), DateUtils.PATTERN_DATE_TIME));
								cell2.setCellStyle(contentStyle);

								// 消费方式名称
								HSSFCell cell3 = dataRow.createCell(3);
								cell3.setCellValue(currencyRecord.getSource());
								cell3.setCellStyle(contentStyle);

								// SonType
								HSSFCell cell4 = dataRow.createCell(4);
								cell4.setCellValue("紫晶");
								cell4.setCellStyle(contentStyle);

								// 消耗钻石
								HSSFCell cell5 = dataRow.createCell(5);
								cell5.setCellValue(currencyRecord.getChange());
								cell5.setCellStyle(contentStyle);

								rowCount++;
							}
							data = currentBfReader.readLine();
						}
						currentBfReader.close();
						currentFileReader.close();
					}
				}
			}
		}
		workbook.write(out);
		out.close();
	}
}
