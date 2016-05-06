package com.edu.java8.methodreference;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class MethodReferenceTest {

	/**
	 * 方法应用测试
	 */
	@Test
	public void test_method_reference(){
		//第一种方法引用是构造器引用，它的语法是Class::new，或者更一般的Class< T >::new。请注意构造器没有参数。
		final Car car = Car.create( Car::new );
		final List< Car > cars = Arrays.asList( car );
		
		//第二种方法引用是静态方法引用，它的语法是Class::static_method。请注意这个方法接受一个Car类型的参数。
		cars.forEach( Car::collide );
		
		//第三种方法引用是特定类的任意对象的方法引用，它的语法是Class::method。请注意，这个方法没有参数。
		cars.forEach( Car::repair );
		
		//第四种方法引用是特定对象的方法引用，它的语法是instance::method。请注意，这个方法接受一个Car类型的参数
		final Car police = Car.create( Car::new );
		cars.forEach( police::follow );
	}
}
