package com.edu.java8.stream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class StreamTestStart {

	@Test
	public void test_collection_tradition() {
		List<Dish> lowCaloricDishes = new ArrayList<>();
		List<Dish> menu = init();
		for (Dish d : menu) {
			if (d.getCalories() < 400) {
				lowCaloricDishes.add(d);
			}
		}
		Collections.sort(lowCaloricDishes, new Comparator<Dish>() {
			public int compare(Dish d1, Dish d2) {
				return Integer.compare(d1.getCalories(), d2.getCalories());
			}
		});
		List<String> lowCaloricDishesName = new ArrayList<>();
		for (Dish d : lowCaloricDishes) {
			lowCaloricDishesName.add(d.getName());
		}
	}

	@Test
	public void test_collection_stream() throws Exception {
		List<Dish> menu = init();
		List<String> lowCaloricDishesName = menu.stream().filter(d -> {
			System.out.println(d.getName());
			return d.getCalories() < 400;
		}) // 选出400卡路里以下的菜肴
				.sorted(Comparator.comparing(Dish::getCalories)) // 按照卡路 里排序
				.map(d -> {
					System.out.println(d.getName());
					return d.getName();
				}) // 提取菜肴的名称
				.collect(Collectors.toList()); // 将所有名称保存在List中
		System.out.println(lowCaloricDishesName);
	}

	@Test
	public void test_collection_parallelStream() throws Exception {
		List<Dish> menu = init();
		// 利用多核架构并行执行这段代码
		List<String> lowCaloricDishesName = menu.parallelStream().filter(d -> d.getCalories() < 400) // 选出400卡路里以下的菜肴
				.sorted(Comparator.comparing(Dish::getCalories)) // 按照卡路 里排序
				.map(Dish::getName) // 提取菜肴的名称
				.limit(2) // 选出头两个
				.collect(Collectors.toList()); // 将所有名称保存在List中
		System.out.println(lowCaloricDishesName);
	}

	private List<Dish> init() {
		List<Dish> result = new LinkedList<>();
		Dish dish = new Dish("dish1", 100, "fish", true);
		result.add(dish);
		dish = new Dish("dish2", 200, "fish", true);
		result.add(dish);
		dish = new Dish("dish3", 300, "meat", true);
		result.add(dish);
		dish = new Dish("dish4", 400, "meat", true);
		result.add(dish);
		dish = new Dish("dish5", 500, "other", false);
		result.add(dish);
		dish = new Dish("dish6", 600, "other", false);
		result.add(dish);
		dish = new Dish("dish7", 600, "other", false);
		result.add(dish);
		return result;
	}
}
