package com.edu.html;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.junit.Test;

public class HtmlParser {
	/** 根路径 */
	private static String root;

	static {
		root = System.getProperty("user.dir") + File.separator + "file";
	}

	@Test
	public void test_parser_table() throws Exception {
		// 2.本地HTML
		String file = root + File.separator + "ing.html";
		File f = new File(file);
		InputStreamReader isr1 = new InputStreamReader(new FileInputStream(f));
		BufferedReader br = new BufferedReader(isr1);

		String s;

		String AllContent = "";
		while ((s = br.readLine()) != null) {
			AllContent = AllContent + s;
		}

		// 使用后HTML Parser 控件

		Parser myParser;

		NodeList nodeList = null;

		myParser = Parser.createParser(AllContent, "utf-8");

		NodeFilter tableFilter = new NodeClassFilter(TableTag.class);

		OrFilter lastFilter = new OrFilter();

		lastFilter.setPredicates(new NodeFilter[] { tableFilter });

		try {

			// 获取标签为table的节点列表

			nodeList = myParser.parse(lastFilter);

			// 循环读取每个table
			int size = nodeList.size();
			for (int i = 0; i <= size; i++) {
				if (nodeList.elementAt(i) instanceof TableTag) {

					TableTag tag = (TableTag) nodeList.elementAt(i);
					
					TableRow[] rows =tag.getRows();
					int rowLen = rows.length;
					for(int j=0; j< rowLen; j++){
						TableRow tr =(TableRow) rows[j];
						System.out.println(tr.getTagName());
					}
					
					/*
					 * TableRow[] rows =tag.getRows();
					 * 
					 * System.out.println("----------------------table "
					 * +i+"--------------------------------");
					 * 
					 * //循环读取每一行
					 * 
					 * for (int j = 0; j <rows.length; j++) {
					 * 
					 * TableRow tr =(TableRow) rows[j];
					 * 
					 * TableColumn[] td =tr.getColumns();
					 * 
					 * //读取每行的单元格内容
					 * 
					 * for (int k = 0; k< td.length; k++) {
					 * 
					 * System.out.println(td[k].getStringText());//（按照自己需要的格式输出）
					 * 
					 * }
					 * 
					 * }
					 */

				}

			}

		} catch (ParserException e) {

			e.printStackTrace();

		}

	}
}
