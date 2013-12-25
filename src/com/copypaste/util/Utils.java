package com.copypaste.util;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Utils {

	public static WindowAdapter getWindowListener(){
		return new MyWindowAdapter();
	}
	
	private static class MyWindowAdapter extends WindowAdapter{
		@Override
		public void windowClosing(WindowEvent e) {
			
		}
	}
}
