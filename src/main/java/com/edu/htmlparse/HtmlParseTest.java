package com.edu.htmlparse;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.edu.utils.json.JsonUtils;
import com.edu.utils.time.DateUtils;

public class HtmlParseTest {

	private static final String FILE = "E:\\git\\coreJava\\src\\main\\java\\com\\edu\\htmlparse\\进行中需求.html";

	private static final String BR = "<br>";

	private static Map<Integer, String> headMap = new HashMap<>();

	private static int maxSize = 0;

	private static HeadVo before = null;

	private static String mainDevelop = "主开发";

	private static String startDateStr = "2017-1-3";
	private static String endDateStr = "2017-1-6";

	public static void main(String[] args) throws Exception {
		List<TaskVo> taskVos = buildTaskVo();

		// 过滤时间
		Date startDate = DateUtils.string2Date(startDateStr, DateUtils.PATTERN_DATE);
		Date endDate = DateUtils.string2Date(endDateStr, DateUtils.PATTERN_DATE);
		Iterator<TaskVo> iterator = taskVos.iterator();
		while (iterator.hasNext()) {
			TaskVo taskVo = iterator.next();
			try {
				if (StringUtils.isEmpty(taskVo.getStartDate())) {
					iterator.remove();
				} else {
					Date taskStateDate = DateUtils.string2Date(taskVo.getStartDate(), DateUtils.PATTERN_DATE);
					Date testDate = DateUtils.string2Date(taskVo.getTestDate(), DateUtils.PATTERN_DATE);
					if (taskStateDate.getTime() >= startDate.getTime()) {
						if (taskStateDate.getTime() <= endDate.getTime()) {
							continue;
						} else {
							iterator.remove();
						}
					} else {
						if (testDate.getTime() >= startDate.getTime()) {
							if (testDate.getTime() <= endDate.getTime()) {
								continue;
							} else {
								iterator.remove();
							}
						} else {
							iterator.remove();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(JsonUtils.object2String(taskVo));
			}
		}
		output(taskVos);
	}

	public static void output(List<TaskVo> taskVos) {
		String noMessage = "无";
		for (TaskVo taskVo : taskVos) {
			String line = StringUtils.EMPTY;
			line += taskVo.getLevel() + "\t";
			line += taskVo.getProjects() + "\t";
			String plcs = taskVo.getPlcs();
			if (StringUtils.isEmpty(plcs)) {
				plcs = noMessage;
			}
			line += plcs + "\t";
			line += taskVo.getJob() + "\t";
			String main = taskVo.getMain();
			if (StringUtils.isEmpty(main)) {
				main = taskVo.getName();
			}
			line += main + "\t";
			line += taskVo.getName() + "\t";

			String codeReview = taskVo.getVpmm();
			line += codeReview + "\t";

			line += taskVo.getStartDate() + "\t";
			line += taskVo.getTestDate() + "\t";

			String wiki = noMessage;
			line += wiki + "\t";

			line += taskVo.getProcess() + "\t";
			line += taskVo.getOnlineDate() + "\t";
			System.out.println(line);
		}
	}

	private static List<TaskVo> buildTaskVo() throws ParserException {
		List<TaskVo> taskVos = new LinkedList<>();
		Parser parser = new Parser(FILE);
		parser.setEncoding("UTF-8");
		NodeClassFilter filter = new NodeClassFilter(TableTag.class); // 对表格的过滤获取
		NodeList nodeList = parser.parse(filter);
		TableTag tableTag = (TableTag) nodeList.elementAt(0);
		TableRow[] rows = tableTag.getRows();

		int index = 1;
		for (TableRow row : rows) {
			if (index == 3) {
				TableColumn[] columns = row.getColumns();
				int i = 1;
				maxSize = columns.length;
				for (TableColumn column : columns) {
					String text = column.getStringText().replace(BR, "").replace("\n", "").replace("\t", "");
					headMap.put(i, text);
					i++;
				}
			}
			if (index >= 5) {
				TableColumn[] columns = row.getColumns();

				if (columns.length >= maxSize) {
					String taskText = columns[4].getStringText();
					if (!StringUtils.isEmpty(taskText)) {
						if (columns.length >= maxSize) {
							before = new HeadVo();
							before.setVpmm(columns[0].getStringText());
							before.setLevel(columns[1].getStringText());
							before.setProjects(columns[2].getStringText());
							before.setPlcs(columns[3].getStringText());
							addNomalTaskVo(taskVos, row);
						}
					} else {

					}
				} else {
					String taskText = columns[0].getStringText();
					if("运费险项目".equals(before.getProjects())){
						System.out.println("Hello World");
					}
					//全部都是数据，则表示存在是分开的plcs
					if(StringUtils.isNumeric(taskText)){
						before.setPlcs(taskText);
						addAllNumberSpecial1(taskVos, before, row);
					}else if (!StringUtils.isEmpty(taskText)) {
						addSpecial(taskVos, before, row);
					} else {
						taskText  = columns[1].getStringText();
						if(StringUtils.isEmpty(taskText)){
							System.out.println("job is empty, the program ignore");
						}else{
							addAllNumberSpecial2(taskVos, before, row);
						}
						
					}
				}
			}
			index++;
		}
		System.out.println(JsonUtils.object2String(taskVos));
		return taskVos;
	}

	private static void addSpecial(List<TaskVo> taskVos, HeadVo headVo, TableRow row) {
		TableColumn[] columns = row.getColumns();
		TaskVo taskVo = new TaskVo();
		taskVo.setVpmm(headVo.getVpmm());
		taskVo.setLevel(headVo.getLevel());
		taskVo.setProjects(headVo.getProjects());
		taskVo.setPlcs(headVo.getPlcs());
		int j = 1;
		for (TableColumn column : columns) {
			String text = column.getStringText();
			switch (j) {
			case 1:
				taskVo.setJob(text);
				break;
			case 3:
				taskVo.setStartDate(text);
				break;
			case 4:
				taskVo.setTestDate(text);
				break;
			case 5:
				taskVo.setOnlineDate(text);
				break;
			default:
				break;
			}
			if ("Y".equals(text.trim())) {
				taskVo.setName(headMap.get(j + headVo.getSize()));
			}
			String main = headMap.get(j + headVo.getSize());
			try {
				if (main == null) {
					main = StringUtils.EMPTY;
				}
				if (mainDevelop.equals(main.trim())) {
					taskVo.setMain(text);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			j++;
		}
		taskVos.add(taskVo);
	}
	private static void addAllNumberSpecial1(List<TaskVo> taskVos, HeadVo headVo, TableRow row) {
		TableColumn[] columns = row.getColumns();
		TaskVo taskVo = new TaskVo();
		taskVo.setVpmm(headVo.getVpmm());
		taskVo.setLevel(headVo.getLevel());
		taskVo.setProjects(headVo.getProjects());
		taskVo.setPlcs(headVo.getPlcs());
		int j = 1;
		for (TableColumn column : columns) {
			String text = column.getStringText();
			switch (j) {
			case 1:
				taskVo.setPlcs(text);
				break;
			case 2:
				taskVo.setJob(text);
				break;
			case 3:
				taskVo.setProcess(text);
				break;
			case 4:
				taskVo.setStartDate(text);
				break;
			case 5:
				taskVo.setTestDate(text);
				break;
			case 6:
				taskVo.setOnlineDate(text);
				break;
			default:
				break;
			}
			if ("Y".equals(text.trim())) {
				taskVo.setName(headMap.get(j + headVo.getSize()));
			}
			String main = headMap.get(j + headVo.getSize());
			try {
				if (main == null) {
					main = StringUtils.EMPTY;
				}
				if (mainDevelop.equals(main.trim())) {
					taskVo.setMain(text);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			j++;
		}
		taskVos.add(taskVo);
	}

	
	private static void addAllNumberSpecial2(List<TaskVo> taskVos, HeadVo headVo, TableRow row) {
		TableColumn[] columns = row.getColumns();
		TaskVo taskVo = new TaskVo();
		taskVo.setVpmm(headVo.getVpmm());
		taskVo.setLevel(headVo.getLevel());
		taskVo.setProjects(headVo.getProjects());
		int j = 1;
		for (TableColumn column : columns) {
			String text = column.getStringText();
			switch (j) {
			case 1:
				taskVo.setPlcs(text);
				break;
			case 2:
				taskVo.setJob(text);
				break;
			case 3:
				taskVo.setProcess(text);
				break;
			case 4:
				taskVo.setStartDate(text);
				break;
			case 5:
				taskVo.setTestDate(text);
				break;
			case 6:
				taskVo.setOnlineDate(text);
				break;
			default:
				break;
			}
			if ("Y".equals(text.trim())) {
				taskVo.setName(headMap.get(j + headVo.getSize()));
			}
			String main = headMap.get(j + headVo.getSize());
			try {
				if (main == null) {
					main = StringUtils.EMPTY;
				}
				if (mainDevelop.equals(main.trim())) {
					taskVo.setMain(text);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			j++;
		}
		taskVos.add(taskVo);
	}
	
	private static void addNomalTaskVo(List<TaskVo> taskVos, TableRow row) {
		TableColumn[] columns = row.getColumns();
		TaskVo taskVo = new TaskVo();
		int j = 1;
		for (TableColumn column : columns) {
			String text = column.getStringText();
			switch (j) {
			case 1:
				taskVo.setVpmm(text);
				break;
			case 2:
				taskVo.setLevel(text);
				break;
			case 3:
				taskVo.setProjects(text);
				break;
			case 4:
				taskVo.setPlcs(text);
				break;
			case 5:
				taskVo.setJob(text);
				break;
			case 7:
				taskVo.setStartDate(text);
				break;
			case 8:
				taskVo.setTestDate(text);
				break;
			case 9:
				taskVo.setOnlineDate(text);
				break;
			default:
				break;
			}
			if ("Y".equals(text.trim())) {

				taskVo.setName(headMap.get(j));
			}
			String main = headMap.get(j);
			if (mainDevelop.equals(main)) {
				taskVo.setMain(text);
			}
			j++;
		}
		taskVos.add(taskVo);
	}
}
