package com.copypaste.util;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import com.copypaste.impls.CopyPasteUIImpl;
import com.copypaste.impls.MemoryMappedCopyPasteImpl;
import com.copypaste.util.Constants.COPY_OPTION;

public class Utils<T> {
	public static Map<String, COPY_OPTION> actionCommandsToCopyMap = new HashMap<>();
	static {
		actionCommandsToCopyMap.put(Constants.actionCommands[0],
				Constants.COPY_OPTION.FILE);
		actionCommandsToCopyMap.put(Constants.actionCommands[1],
				Constants.COPY_OPTION.DIRECTORY);
	}

	
	
	public static final String SUCCESS = "0";
	
	public static <E> WindowAdapter getWindowListener(CopyPasteUIImpl<E> comp) {
		return new MyWindowAdapter(comp);
	}

	private static class MyWindowAdapter<E> extends WindowAdapter {
		CopyPasteUIImpl<E> c;

		public MyWindowAdapter(CopyPasteUIImpl<E> comp) {
			c = comp;
		}

		@Override
		public void windowClosing(WindowEvent e) {
			try {
				System.out.println("serialize");
				c.serialize();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}

		public void windowClosed(WindowEvent e) {
			System.out.println("closed");
		}
	}

	public static <T> T checkNotNull(T obj) {
		if (obj == null)
			throw new NullPointerException();
		return obj;
	}
	public static void showMsg(String message){
		JOptionPane.showMessageDialog(null,
					message);
	}
	
	public MemoryMappedCopyPasteImpl getMemoryCopyPaste(T srcFile, T destFile){
		return new MemoryMappedCopyPasteImpl<T>(srcFile, destFile);
	}
}
