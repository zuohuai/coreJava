package com.edu.java8;

import java.util.Optional;

import org.junit.Test;

public class OptionTest {

	@Test
	public void test_create_option() throws Exception {
		// 创建一个空的Optional 对象
		Optional<Car> optCar = Optional.empty();

		// 依据一个非空值创建一个Optional对象：如果car是一个null，这段代码会立即抛出一个NullPointerException，而不是等到你试图访问car的属性值时才返回一个错误。
		Car car = new Car();
		optCar = Optional.of(car);

		// 你可以创建一个允许null值的Optional对象
		optCar = Optional.ofNullable(car);
	}

	@Test
	public void test_map_option() throws Exception {
		Insurance insurance = new Insurance("insurance");
		Optional<Insurance> optInsurance = Optional.ofNullable(insurance);
		Optional<String> nameOptional = optInsurance.map(Insurance::getName);
		System.out.println(nameOptional.get());
	}

	@Test
	public void test_flatMap_option() throws Exception {
		Insurance insurance = new Insurance("insurance");
		Optional<Insurance> insOptional = Optional.of(insurance);

		Car car = new Car();
		car.setInsurance(insOptional);
		Optional<Car> carOptional = Optional.of(car);
		
		Person person = new Person();
		person.setCar(carOptional);
		Optional<Person> personOptional = Optional.of(person);
		
		//得到person的insurance 的名字
		String personInsuranceName = personOptional.flatMap(Person::getCar)
		.flatMap(Car::getInsurance)
		.map(Insurance::getName)
		.orElse("Unknown");
		System.out.println(personInsuranceName);
	}

	public class Person {
		private Optional<Car> car;

		public Optional<Car> getCar() {
			return car;
		}

		public void setCar(Optional<Car> car) {
			this.car = car;
		}
	}

	public class Car {
		private Optional<Insurance> insurance;

		public Optional<Insurance> getInsurance() {
			return insurance;
		}

		public void setInsurance(Optional<Insurance> insurance) {
			this.insurance = insurance;
		}
	}

	public class Insurance {
		private String name;

		private Insurance(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}
