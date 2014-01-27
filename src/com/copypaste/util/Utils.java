package com.copypaste.util;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import com.copypaste.CopyPasteUI;

public class Utils<T> {

	public static <E> WindowAdapter getWindowListener(CopyPasteUI<E> comp){
		return new MyWindowAdapter(comp);
	}
	
	private static class MyWindowAdapter<E> extends WindowAdapter{
		CopyPasteUI<E> c;
		public MyWindowAdapter(CopyPasteUI<E>comp) {
			c = comp;
		}
		@Override
		public void windowClosing(WindowEvent e) {
				try {
					c.serialize();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		
		}
	}
	
  public static<T> T checkNotNull(T obj){
	  if(obj == null) throw new NullPointerException();
	  return obj;
  }
  
}
