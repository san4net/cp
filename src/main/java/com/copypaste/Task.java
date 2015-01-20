package com.copypaste;
/**
 * Task can be broadly divided in  start , stop , terminate finish 
 *  phase
 * @author kumar
 *
 * @param <T>
 */
public interface Task<T> {
	/**
	 * 
	 */
	void injectLogic(String logic);
	
	void start();

	boolean stop();

	boolean terminate();

	void finish();
}
