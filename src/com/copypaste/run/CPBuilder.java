package com.copypaste.run;

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
		return new CopyPasteUIImpl<>("Copy Paste").build();
	}

	private CopyPasteLogic buildLogic() {
		return null;
	}

	public static void main(String[] args) {
		CPBuilder builder = new CPBuilder();
		builder.build();
	}
}
