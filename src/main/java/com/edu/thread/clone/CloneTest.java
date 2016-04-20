package com.edu.thread.clone;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

public class CloneTest {

	@Test
	public void test_clone() throws Exception {
		Message message = Message.valueOf(1, "����");
		Person p1 = Person.valueOf("����", 10, Sex.MALE, message);

		Person p2 = Person.class.cast(p1.clone());

		// ��¡�����Ķ�����ͬһ������
		Assert.assertThat(p1 == p2, Is.is(false));
		// �����������ͣ��Զ�����
		Assert.assertThat(p2.getAge(), Is.is(p1.getAge()));

		// ��������(����ö������),ָ�����ͬһ������
		Assert.assertThat(p1.getSex() == p2.getSex(), Is.is(true));
		Assert.assertThat(p2.getName(), Is.is(p1.getName()));
		Assert.assertThat(p2.getName() == p1.getName(), Is.is(true));
		Assert.assertThat(p1.getMessage() == p2.getMessage(), Is.is(true));

	}

	public void test_deep_clone() throws Exception {

	}
}
