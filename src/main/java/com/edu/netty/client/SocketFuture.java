package com.edu.netty.client;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SocketFuture<T> implements Future<T> {
	/** 回调函数 */
	private final SocketCallback<T> callBack;
	/** 是否完成 */
	private volatile boolean completed;
	/** 是否取消 */
	private volatile boolean cancelled;
	/** 异常信息 */
	private volatile Exception ex;
	/** 结果 */
	private volatile T result;
	/** 序列号 */
	private final long serialNumber;
	/***/
	@SuppressWarnings("rawtypes")
	private final ConcurrentMap<Long, SocketFuture> futures;

	@SuppressWarnings("rawtypes")
	public SocketFuture(ConcurrentMap<Long, SocketFuture> futures, long sn, final SocketCallback<T> callback) {
		this.callBack = callback;
		this.futures = futures;
		this.serialNumber = sn;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		futures.remove(serialNumber);
		synchronized (this) {
			if (this.completed) {
				return false;
			}

			this.completed = true;
			this.cancelled = true;

			// TODO
			notifyAll();
		}

		if (this.callBack != null) {
			this.callBack.onCancel();
		}
		return true;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public boolean isDone() {
		return this.completed;
	}

	private T getResult() throws ExecutionException {
		// TODO 为什么要那么做
		if (this.ex != null) {
			throw new ExecutionException(this.ex);
		}
		return this.result;
	}

	@Override
	public T get() throws InterruptedException, ExecutionException {
		while (!this.completed) {
			wait();
		}
		return getResult();
	}

	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		final long msecs = unit.toMillis(timeout);
		final long startTime = (msecs <= 0) ? 0 : System.currentTimeMillis();

		long waitTime = msecs;
		if (this.completed) {
			return getResult();
		}
		// 存在小于0 的情况？！！ TODO
		else if (waitTime <= 0) {
			throw new TimeoutException();
		} else {
			for (;;) {
				wait(waitTime);
				if (this.completed) {
					return getResult();
				} else {
					waitTime = msecs - (System.currentTimeMillis() - startTime);
					if (waitTime <= 0) {
						throw new TimeoutException();
					}
				}
			}
		}
	}

	public boolean onSuccess(final T result) {
		futures.remove(serialNumber);
		synchronized (this) {
			if (this.completed) {
				return false;
			}
			this.completed = true;
			this.result = result;
			notifyAll();
		}
		if (this.callBack != null) {
			try {
				this.callBack.onSuccess(result);
			} catch (Exception e) {
				onError(e);
			}
		}
		return true;
	}

	public boolean onError(final Exception e) {
		futures.remove(serialNumber);
		synchronized (this) {
			if (this.completed) {
				return false;
			}
			this.completed = true;
			this.ex = e;
			notifyAll();
		}
		if (this.callBack != null) {
			this.callBack.onError(e);
		}
		return true;
	}

}
