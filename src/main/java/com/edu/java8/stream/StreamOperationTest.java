package com.edu.java8.stream;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class StreamOperationTest {

	@Test
	public void test_filter() throws Exception {

		List<Dish> vegetarianDishes = new ArrayList<>();
		List<Dish> initList = init();
		// 通过传统的方式过滤
		for (Dish d : initList) {
			if (d.isVegetarian()) {
				vegetarianDishes.add(d);
			}
		}

		// 使用流式编程过滤
		vegetarianDishes = initList.stream().filter(Dish::isVegetarian).collect(Collectors.toList());

	}

	@Test
	public void test_distinct() throws Exception {
		List<Integer> nums = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
		nums.stream().distinct().forEach(System.out::println);
	}

	@Test
	public void test_limit() throws Exception {
		List<Integer> nums = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
		nums.stream().distinct().limit(1).forEach(System.out::println);
	}

	/**
	 * 使用map方法某个属性值
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_map() throws Exception {
		List<Dish> initList = init();
		initList.stream().map(Dish::getName).forEach(System.out::println);

		List<String> words = Arrays.asList("Java 8", "Lambdas", "In", "Action");
		List<Integer> wordLengths = words.stream().map(StringUtils::length).collect(Collectors.toList());
		System.out.println(wordLengths);
	}

	/**
	 * flatmap方法让你把一个流中的每个值都换成另一个流，然后把所有的流连接起来成为一个流。
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_flatMap() throws Exception {
		String[] arrayOfWords = new String[] { "Hello", "World" };
		Stream<String> streamOfwords = Arrays.stream(arrayOfWords);

		// 这种方式得到的两个流列表
		streamOfwords.map(word -> word.split("")).map(Arrays::stream).distinct().forEach(System.out::println);

		// 使用flapMap
		streamOfwords = Arrays.stream(arrayOfWords);
		List<String> uniqueCharacters = streamOfwords.map(w -> w.split("")).flatMap(Arrays::stream).distinct()
				.collect(Collectors.toList());
		System.out.println("使用flapMap：" + uniqueCharacters);
	}

	/**
	 * 查找和匹配 allMatch、anyMatch、noneMatch ： 短路操作 findFirst和findAny
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_find_and_match() throws Exception {
		List<Dish> initList = init();
		// anyMatch 检查谓词是否至少匹配一个元素
		if (initList.stream().anyMatch(Dish::isVegetarian)) {
			System.out.println("The menu is (somewhat) vegetarian friendly!!");
		}

		// allMatch，noneMatch 检查谓词是否匹配所有元素
		if (initList.stream().allMatch(d -> d.getCalories() < 7000)) {
			System.out.println("allMatch");
		}

		// findAny方法将返回当前流中的任意元素
		Optional<Dish> dishOptioin = initList.stream().filter(Dish::isVegetarian).findAny();
		System.out.println(dishOptioin.get().getName());

		// findFirst 查找第一个元素
		dishOptioin = initList.stream().filter(Dish::isVegetarian).findFirst();
		System.out.println(dishOptioin.get().getName());

	}

	/**
	 * 
	 * @throws Exception
	 *             1.isPresent()将在Optional包含值的时候返回true, 否则返回false。 2.
	 *             ifPresent(Consumer<T> block)会在值存在的时候执行给定的代码块。我们在第3章
	 *             介绍了Consumer函数式接口；它让你传递一个接收T类型参数，并返回void的Lambda表达式。 3. T
	 *             get()会在值存在时返回值，否则抛出一个NoSuchElement异常。 4. T orElse(T
	 *             other)会在值存在时返回值，否则返回一个默认值。
	 */
	@Test
	public void test_optional() throws Exception {
		List<Dish> initList = init();
		// orElse方法测试
		Dish defaultOption = new Dish("OTHER", false, 100);
		Dish dish = initList.stream().filter(d -> d.getCalories() > 7000).findAny().orElse(defaultOption);
		assertEquals("OTHER", dish.getName());

		// isPresent
		boolean present = initList.stream().filter(d -> d.getCalories() > 7000).findAny().isPresent();
		assertEquals(false, present);

		// ifPresent
		initList.stream().filter(d -> d.getCalories() == 6000).findAny().ifPresent(d -> System.out.println("存在"));

	}

	@Test
	public void test_reduce() throws Exception {
		//传统做法
		List<Integer> numbers = Arrays.asList(1,2,3,4,5,6,7);
		int sum = 0;
		for (int x : numbers) {
			sum += x;
		}
		System.out.println("sum1:"+sum);
		
		//采用reduce求和
		sum = 0;
		sum = numbers.stream().reduce(0, (a, b) -> a + b);
		System.out.println("sum2:"+sum);
		
		//求最大值和最小值
		Optional<Integer> max = numbers.stream().reduce(Integer::max);
		Optional<Integer> min = numbers.stream().reduce(Integer::min);
		System.out.println("max:"+max.get()+",min:"+min.get());
	}
	
	@Test
	public void test_create_stream() throws Exception{
		Stream<String> stream = Stream.of("Java 8 ", "Lambdas ", "In ", "Action");
		stream.map(String::toUpperCase).forEach(System.out::println);
		
		Stream<String> emptyStream = Stream.empty();
		
		int[] numbers = {2, 3, 5, 7, 11, 13};
		int sum = Arrays.stream(numbers).sum();
	}

	private List<Dish> init() {
		List<Dish> result = new LinkedList<>();
		result.add(new Dish("dish1", true, 1000));
		result.add(new Dish("dish2", true, 2000));
		result.add(new Dish("dish3", false, 3000));
		result.add(new Dish("dish4", false, 4000));
		result.add(new Dish("dish5", false, 5000));
		result.add(new Dish("dish6", false, 6000));
		return result;
	}

	private class Dish {
		private String name;
		private boolean vegetarian;
		private int calories;

		private Dish(String name, boolean vegetarian, int calories) {
			this.name = name;
			this.vegetarian = vegetarian;
			this.calories = calories;
		}

		public boolean isVegetarian() {
			return vegetarian;
		}

		public int getCalories() {
			return calories;
		}

		public String getName() {
			return name;
		}
	}
}
