package com.copypaste.logic;

public interface CopyPasteLogic<T> {
	T copyPaste() throws Exception;
	boolean isCopied();
	long size();
}
