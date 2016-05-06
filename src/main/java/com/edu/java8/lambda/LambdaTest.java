package com.edu.java8.lambda;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.edu.java8.lambda.Person.Sex;

public class LambdaTest {

	@Test
	public void test_lambda_start() throws Exception {
		List<Person> roster = new LinkedList<Person>();
		for (int i = 0; i < 10; i++) {
			Sex sex = null;
			if (i < 5) {
				sex = Sex.FEMALE;
			} else {
				sex = Sex.MALE;
			}
			roster.add(new Person("test" + i, sex));
		}

		System.out.println("采用普通创建对象的方式");
		printPersons(roster, new CheckPersonEligibleForSelectiveService());

		System.out.println("采用匿名对象的方式");
		printPersons(roster, new CheckPerson() {
			public boolean test(Person p) {
				return p.getGender() == Person.Sex.MALE && p.getAge() >= 18 && p.getAge() <= 25;
			}
		});

		System.out.println("采用lambda表达式的方式");
		/*
		 *lambda的语法格式是:
		 *包含三个部分
		 *1) 一个括号内用逗号分隔的形式参数，参数是函数式接口里面方法的参数
		 *2) 一个箭头符号：->
		 *3) 方法体，可以是表达式和代码块，方法体函数式接口里面方法的实现，如果是代码块，则必须用{}来包裹起来，且需要一个return 返回值，但有个例外，若函数式接口里面方法返回值是void，则无需{}
		 */
		printPersons(roster, (Person p) -> p.getGender() == Person.Sex.MALE && p.getAge() >= 18 && p.getAge() <= 25);
		
		/**
		 * 接口中的默认方法和静态方法
		 */
		CheckPerson.theStaticMethod();
		
	}
	
	/**
	 * 数组排序的应用
	 * @throws Exception
	 */
	@Test
	public void test_sort_array() throws Exception{
		Arrays.asList( "a", "b", "d" ).sort( ( e1, e2 ) -> {
		    int result = e1.compareTo( e2 );
		    return result;
		} );
	}
	
	/**
	 * 线程的使用
	 * @throws Exception
	 */
	public void test_thread() throws Exception{
		
	}

	public static void printPersonsWithinAgeRange(List<Person> roster, int low, int high) {
		for (Person p : roster) {
			if (low <= p.getAge() && p.getAge() < high) {
				p.printPerson();
			}
		}
	}

	public static void printPersonsOlderThan(List<Person> roster, int age) {
		for (Person p : roster) {
			if (p.getAge() >= age) {
				p.printPerson();
			}
		}
	}

	public static void printPersons(List<Person> roster, CheckPerson tester) {
		tester.defaultMethod();
		for (Person p : roster) {
			if (tester.test(p)) {
				p.printPerson();
			}
		}
	}
}
