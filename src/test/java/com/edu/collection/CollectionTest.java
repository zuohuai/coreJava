package com.edu.collection;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edu.CodisTest;
import com.edu.utils.json.JsonUtils;

public class CollectionTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(CodisTest.class);
	
	@Test
	public void test_array_deque() throws Exception {
		Collection<String> deque = new ArrayDeque<>(100);
		for (int i = 0; i < 10; i++) {
			deque.add(String.valueOf(i));
		}
		printCollection(deque);
	}

	/**
	 * Iterator可以实现遍历中移除操作
	 * @throws Exception
	 */
	@Test
	public void test_iterator() throws Exception {
		List<Integer> values = new LinkedList<>();
		for (int i = 0; i < 10; i++) {
			values.add(i);
		}
		Iterator<Integer> iterator = values.iterator();
		while (iterator.hasNext()) {
			Integer data = iterator.next();
			if (data < 5) {
				iterator.remove();
			}
		}
		printCollection(values);
	}

	@Test
	public void test_for_list_remove_exception() throws Exception {
		List<Integer> arrayValues = new ArrayList<>();
		fillList(arrayValues);
		try {
			for (Integer value : arrayValues) {
				if (value % 2 == 0) {
					arrayValues.remove(value);
				}
			}
		} catch (Exception e) {
			LOGGER.error("ArrayList-foreach 移除出现的异常是:", e);
		}

		List<Integer> linkedValues = new LinkedList<>();
		fillList(linkedValues);
		try {
			for (Integer value : linkedValues) {
				if (value % 2 == 0) {
					linkedValues.remove(value);
				}
			}
		} catch (Exception e) {
			LOGGER.error("LinkedList-foreach 移除出现的异常是:", e);
		}
	}

	@Test
	public void test_for_list_remove_no_exception() throws Exception {
		List<Integer> arrayValues = new ArrayList<>();
		fillList(arrayValues);
		try {
			for (int i = 0; i < arrayValues.size(); i++) {
				Integer value = arrayValues.get(i);
				if (value % 2 == 0) {
					arrayValues.remove(value);
				}
			}
		} catch (Exception e) {
			LOGGER.error("ArrayList-foreach 移除出现的异常是:", e);
		}

		List<Integer> linkedValues = new LinkedList<>();
		fillList(linkedValues);
		try {
			for (int i = 0; i < linkedValues.size(); i++) {
				Integer value = linkedValues.get(i);
				if (value % 2 == 0) {
					linkedValues.remove(value);
				}
			}
		} catch (Exception e) {
			LOGGER.error("LinkedList-foreach 移除出现的异常是:", e);
		}
	}

	private void fillList(List<Integer> list) {
		for (int i = 0; i < 100; i++) {
			list.add(i);
		}
	}

	@Test
	public void test_tree_set() throws Exception {
		TreeSet<TreeVo> treeVos = new TreeSet<>();
		/**
		 * TreeSet类的add()方法中会把存入的对象提升为Comparable类型,调用对象的compareTo()方法和集合中的对象比较, 根据compareTo()方法返回的结果进行存储
		 */
		String content = "测试TreeSet的add方法";
		TreeVo t1 = TreeVo.valueOf(1, 2);
		TreeVo t2 = TreeVo.valueOf(2, 2);
		TreeVo t3 = TreeVo.valueOf(3, 4);
		TreeVo t4 = TreeVo.valueOf(4, 3);
		TreeVo t5 = TreeVo.valueOf(5, 5);
		TreeVo t6 = TreeVo.valueOf(6, -1);
		treeVos.add(t1);
		treeVos.add(t2);
		treeVos.add(t3);
		treeVos.add(t4);
		treeVos.add(t5);
		treeVos.add(t6);
		printTreeSet(content, treeVos);

		/**
		 * TreeSet的remove 方法
		 */
		content = "测试TreeSet的remove方法";
		TreeVo t7 = TreeVo.valueOf(4, 2);
		treeVos.remove(t4);
		printTreeSet(content, treeVos);
	}

	private <T> void printCollection(Collection<T> deque) {
		for (T t : deque) {
			System.out.println(JsonUtils.object2String(t));
		}
	}

	private void printTreeSet(String content, TreeSet<TreeVo> treeVos) {
		System.out.println(content);
		for (TreeVo treeVo : treeVos) {
			System.out.println(treeVo.getId() + "\t" + treeVo.getComparator());
		}
	}
}
