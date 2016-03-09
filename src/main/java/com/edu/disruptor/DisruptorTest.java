package com.edu.disruptor;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.YieldingWaitStrategy;

public class DisruptorTest {

	private static final int RING_SIZE = 1024 * 8;

	private static int i = 0;
	// 处理
	private final static EventHandler<ValueEvent> handler = new EventHandler<ValueEvent>() {
		public void onEvent(final ValueEvent event, final long sequence, final boolean endOfBatch) throws Exception {
			byte[] packet = event.getValue();
			i++;
			System.out.println(i);
		}
	};

	private static final RingBuffer<ValueEvent> ringBuffer = RingBuffer.createSingleProducer(ValueEvent.EVENT_FACTORY,
			RING_SIZE, new YieldingWaitStrategy());

	private static final SequenceBarrier barrier = ringBuffer.newBarrier();;

	public static void main(String[] args) {

		// 启动消息处理线程
		startHandler();

		for (long i = 0; i < 1000000; i++) {
			// 提交事件
			submitTask();
		}

	}

	private static void submitTask() {
		long sequence = ringBuffer.next();
		ValueEvent event = ringBuffer.get(sequence);
		event.setValue(new byte[100]);
		ringBuffer.publish(sequence);
		
		// if (ringBuffer.remainingCapacity() < RING_SIZE * 0.01) {
		// System.out.println("容量剩余的大小不足 总大小的10%,请调整大小");
		// } else {
		//
		// }
	}

	public static void startHandler() {
		BatchEventProcessor<ValueEvent> eventProcessor = new BatchEventProcessor<ValueEvent>(ringBuffer, barrier,
				handler);
		ringBuffer.addGatingSequences(eventProcessor.getSequence());

		new Thread(eventProcessor).start();
	}
}
