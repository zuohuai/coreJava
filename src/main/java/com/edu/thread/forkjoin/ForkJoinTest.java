package com.edu.thread.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

import org.apache.commons.collections.map.MultiValueMap;
import org.junit.Test;

/**
 * 代码逻辑验证
 * ClassName: ForkJoinTest <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2016年6月28日 下午2:44:31 <br/>  
 *  
 * @author hison.zhang  
 * @version   
 * @since JDK 1.7
 */
public class ForkJoinTest {
	
	private static final String CONFIG = "config";
	@Test
	public void test_fork_join() throws Exception {
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		List<TaskConfig> list = new ArrayList<TaskConfig>();
		
		for (int i = 0; i < 128; i++) {
			list.add(new ForkJoinTest.TaskConfig(i));
		}
		Future<MultiValueMap> result = forkJoinPool.submit(new ForkJoinTest.MyTask(list));
		System.out.println(result.get());
	}

	class MyTask extends RecursiveTask<MultiValueMap> {
		private List<TaskConfig> taskList = null;

		public MyTask(List<TaskConfig> taskList) {
			this.taskList = taskList;
		}

		@Override
		protected MultiValueMap compute() {
			MultiValueMap result = new MultiValueMap();
			if (taskList.size() >= 2) {
				List<TaskConfig> list1 = new ArrayList<TaskConfig>();
				List<TaskConfig> list2 = new ArrayList<TaskConfig>();
				for (TaskConfig config : taskList) {
					if (list1.size() > list2.size()) {
						list2.add(new TaskConfig(config.getI()));
					} else {
						list1.add(new TaskConfig(config.getI()));
					}
				}
				//代码逻辑验证
				MyTask task1 = new MyTask(list1);
				MyTask task2 = new MyTask(list2);
				task1.fork();
				task2.fork();
				// TODO
				result.putAll(task1.join());
				result.putAll(task2.join());
				return result;
			}
			if (taskList.size() == 0) {
				/* 等于0直接返回 */
				return result;
			}
			TaskConfig config = taskList.get(0);
			try {
				result.put(CONFIG, config.getI());
			} catch (Exception e) {
				return result;
			}
			return result;
		}
	}

	public class TaskConfig {
		int i = 0;

		public TaskConfig(int i) {
			this.i = i;
		}

		public int getI() {
			return i;
		}
	}
}
