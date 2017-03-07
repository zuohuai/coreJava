package com.edu.scheduler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Component
public class SchedulerCronTest {

	@Scheduled(name = "CRON定时任务", value = "*/1 * * * * ?", type = ValueType.EXPRESSION)
	public void myTask() {
		System.out.println("定时任务执行，O(∩_∩)O哈哈~");
	}
	
	@Scheduled(name = "CRON定时任务2", value = "*/1 * * * * ?", type = ValueType.EXPRESSION)
	public void myTaks2(){
		System.out.println("定时任务执行2，O(∩_∩)O哈哈~");
	}

	@Test
	public void test_cron_task() throws Exception {
		System.out.println("线程睡眠5s");
		Thread.sleep(5000);
	}

}
