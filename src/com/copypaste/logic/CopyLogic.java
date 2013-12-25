package com.copypaste.logic;
/**
 * 
 * @author santosh kumar
 *
 */
public interface CopyLogic {
	int startCopy(String src, String dest) throws Exception;
	boolean isCopyDone();
}
