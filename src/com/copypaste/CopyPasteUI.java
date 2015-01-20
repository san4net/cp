package com.copypaste;

import javax.swing.JFrame;

import com.copypaste.logic.CopyPasteLogic;

public abstract class CopyPasteUI<E> extends JFrame {

	public CopyPasteUI(String title) {
		super(title);
	}

	public abstract CopyPasteUI build();
	public abstract void showUI();
}
