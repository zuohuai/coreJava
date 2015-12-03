package com.edu.codis.protocol.proxy;

import java.io.IOException;

import com.edu.codis.protocol.Context;

public interface Proxy<T> {

	T getValue(Context ctx, byte flag) throws IOException;

	void setValue(Context ctx, T value) throws IOException;

}