package com.copypaste.run;

import javax.swing.JFrame;

import com.copypaste.CopyPasteUI;
import com.copypaste.impls.CopyPasteUIImpl;
import com.copypaste.logic.CopyPasteLogic;

/**
 * 
 * @author santosh kumar
 * 
 */
public class CPBuilder {

	public void build() {
		CopyPasteUI<String> copyPaste = buildUI();
		copyPaste.showUI();
	}

	private CopyPasteUI buildUI() {
		return new CopyPasteUIImpl<>("Copy Paste").buildUI();
	}

	private CopyPasteLogic buildLogic() {
		return null;
	}

	public static void main(String[] args) {
		CPBuilder builder = new CPBuilder();
		builder.build();
	}
}
