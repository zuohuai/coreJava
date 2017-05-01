package com.edu.java8.methodreference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Test;

import com.edu.java8.learning.model.Apple;

public class MethodReferenceTest {

	/**
	 * 方法引用就是让你根据已有的方法实现来创建Lambda表达式。 可以把方法引用看作针对仅仅涉及单一方法的Lambda的语法糖，
	 * 因为你表达同样的事情时要写的代码更少了。 方法应用测试
	 */
	@Test
	public void test_method_reference1() {
		// 第一种方法引用是构造器引用，它的语法是Class::new，或者更一般的Class< T >::new。请注意构造器没有参数。
		final Car car = Car.create(Car::new);
		final List<Car> cars = Arrays.asList(car);

		// 第二种方法引用是静态方法引用，它的语法是Class::static_method。请注意这个方法接受一个Car类型的参数。
		cars.forEach(Car::collide);

		// 第三种方法引用是特定类的任意对象的方法引用，它的语法是Class::method。请注意，这个方法没有参数。
		cars.forEach(Car::repair);

		// 第四种方法引用是特定对象的方法引用，它的语法是instance::method。请注意，这个方法接受一个Car类型的参数
		final Car police = Car.create(Car::new);
		cars.forEach(police::follow);
	}

	@Test
	public void test_method_reference2() {
		/*
		 * 代码段1
		 */
		// 1.构造函数引用指向默认的Apple()构造函数
		Supplier<Apple> appleSupplier = Apple::new;
		// 2. 调用Supplier的get方法将产生一个新的Apple
		Apple apple = appleSupplier.get();

		/*
		 * 代码段2 和代码段1等价的
		 */
		// 3.利用默认构造函数创建Apple的Lambda表达式
		Supplier<Apple> c1 = () -> new Apple();
		// 4.调用Supplier的get方法将产生一个新的Apple
		Apple a1 = c1.get();

		/*
		 * 代码段3
		 */
		// 5. 指向Apple(Integer weight) 的构造函数引用
		Function<Integer, Apple> c2 = Apple::new;
		// 6. 调用该Function函数的apply方法，并给出要求的重量，将产生一个Apple
		Apple a2 = c2.apply(110);

		/*
		 * 代码段4 和代码段5
		 */
		// 7.用要求的重量创建一个Apple的Lambda表达式
		Function<Integer, Apple> c3 = (weight) -> new Apple(weight);
		// 8. 调用该Function函数的apply方法，并给出要求的重量，将产生一个新的Apple对象
		Apple a3 = c3.apply(110);
		
		//9.将构造函数引用传递给map方法
		List<Integer> weights = Arrays.asList(7, 3, 4, 10);
		List<Apple> apples = map(weights, Apple::new);
	}

	public static List<Apple> map(List<Integer> list, Function<Integer, Apple> f) {
		List<Apple> result = new ArrayList<>();
		for (Integer e : list) {
			result.add(f.apply(e));
		}
		return result;
	}

	@Test
	public void test_method_ref_and_lambda() throws Exception {
		// 采用lambda 来做
		List<String> str = Arrays.asList("a", "b", "A", "B");
		str.sort((s1, s2) -> s1.compareToIgnoreCase(s2));
		System.out.println(str);

		// 采用方法引用来做，这个两者是等效的
		List<String> str2 = Arrays.asList("e", "z", "b", "m");
		str2.sort(String::compareToIgnoreCase);
		System.out.println(str2);
	}
}
