package com.edu.test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.junit.Test;

import com.edu.utils.IdGenerator.IdInfo;
import com.edu.utils.json.JsonUtils;

public class CurrencyTest {

	@Test
	public void test_currency() throws Exception {
		File file = new File("E:\\workspace_study\\coreJava\\src\\test\\java\\com\\edu\\test\\0510_re.txt");
		LineIterator it = FileUtils.lineIterator(file);

		Map<Long, Integer> count = new HashMap<>();

		while (it.hasNext()) {
			String line = it.nextLine();
			CurrencyRecord record = JsonUtils.string2Object(line, CurrencyRecord.class);

			Integer amount = count.get(record.getUserId());
			if (amount == null) {
				amount = 0;
			}
			count.put(record.getUserId(), amount + record.getChange());
		}
		
		for(Entry<Long, Integer> entry : count.entrySet()){
			IdInfo idInfo = new IdInfo(entry.getKey());
			System.out.println("玩家ID："+entry.getKey()+"\t运营:" + idInfo.getOperator() + "\t服务器:" + idInfo.getServer() + "\t数量:" + entry.getValue());
		}
	}
}
