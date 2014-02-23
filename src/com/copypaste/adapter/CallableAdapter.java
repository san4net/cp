package com.copypaste.adapter;

import java.util.concurrent.Callable;

import com.copypaste.logic.CopyPasteLogic;

public class CallableAdapter<V> implements Callable<V> {
	CopyPasteLogic<V> logic;

	public CallableAdapter(CopyPasteLogic logic ) {
		super();
		this.logic = logic;
	}

	@Override
	public V call() throws Exception {
		return  logic.copyPaste();
	}

}
