package com.edu.java8;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.edu.java8.learning.filter.AppleFilter;
import com.edu.java8.learning.filter.AppleFilterLambda;
import com.edu.java8.learning.impl.AppleGreenColorPredicate;
import com.edu.java8.learning.model.Apple;
import com.edu.utils.json.JsonUtils;

/**
 * 
 * ClassName: AppleFilterLambdaTest <br/>
 * Function: 测试为什么要使用Lambda表达式 <br/>
 * date: 2017年3月7日 上午11:06:14 <br/>
 * 
 * @author hison.zhang
 * @version
 * @since JDK 1.7
 */
public class AppleFilterLambdaTest {

	/**
	 * 
	 * test_lambda:lambda来做过滤<br/>
	 * 
	 * @author hison.zhang
	 * @throws Exception
	 */
	@Test
	public void test_with_lambda() throws Exception {
		List<Apple> apples = initApples();
		// 采用lambda 表达式
		List<Apple> result = AppleFilterLambda.filter(apples, (Apple apple) -> "green".equals(apple.getColor()));
		System.out.println(JsonUtils.object2String(result));
	}

	/**
	 * 
	 * test_without_lambda:不采用lambda表达式来做过滤 <br/>
	 * 
	 * @author hison.zhang
	 * @throws Exception
	 */
	@Test
	public void test_without_lambda() throws Exception {
		List<Apple> apples = initApples();
		// 不采用lambda表达式
		List<Apple> result = AppleFilter.filterApple(apples, new AppleGreenColorPredicate()); // 这里会遇到一些问题，如果存在多个条件，那么就存在N
																								// 多类了
		System.out.println(JsonUtils.object2String(result));
	}

	private List<Apple> initApples() {
		List<Apple> apples = new LinkedList<>();
		Apple apple = new Apple("red", 1);
		apples.add(apple);
		apple = new Apple("red", 20);
		apples.add(apple);
		apple = new Apple("red", 10);
		apples.add(apple);
		apple = new Apple("green", 1);
		apples.add(apple);
		apple = new Apple("green", 10);
		apples.add(apple);
		apple = new Apple("green", 20);
		apples.add(apple);
		return apples;
	}

	@Test
	public void test_comparator() throws Exception {
		Comparator<Apple> comparator = new Comparator<Apple>() {
			@Override
			public int compare(Apple o1, Apple o2) {
				return o1.getWeight() - o2.getWeight();
			}
		};

		// 使用了lambda表达式之后
		Comparator<Apple> lambdaComparator = (Apple o1, Apple o2) -> o1.getWeight() - o2.getWeight();
	}

	@Test
	public void test_runnale() throws Exception {
		Runnable runnable = () -> System.out.println("Hello World"); // 等价于匿名类，以下的代码是等价的， lambda表达式，果然很简洁
		// runnable = new Runnable() {
		//
		// @Override
		// public void run() {
		// System.out.println("Hello World");
		// }
		// };
		Thread t = new Thread(runnable);
		t.start();
		TimeUnit.SECONDS.sleep(2);
	}
}
