package com.edu.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.edu.codis.protocol.Transfer;
import com.edu.jackson.JsonCoder;
import com.edu.utils.RandomUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class JsonCoderTest {
	@Autowired
	private JsonCoder jsonCoder;

	@Test
	public void test_jsonCoder() throws Exception {
		List<User> users = new LinkedList<User>();

		String startTest = "圣识一切瓦齐尔达喇达赖喇嘛";// 这个是中国最长的名字了
		int amount = 1000000;
		for (int i = 0; i < amount; i++) {
			String name = startTest + RandomUtils.betweenInt(0, amount, true);
			users.add(User.valueOf(i, name));
		}

		byte[] values = jsonCoder.encode(users);
		jsonCoder.doDecode(values, List.class);

		List<Class<?>> list = new ArrayList<Class<?>>();
		list.add(User.class);
		Transfer transfer = new Transfer(list, 0);
		ByteBufAllocator alloc = new PooledByteBufAllocator();
		ByteBuf byteBuf = transfer.encode(alloc, users);
		transfer.decode(byteBuf);
	}
}
