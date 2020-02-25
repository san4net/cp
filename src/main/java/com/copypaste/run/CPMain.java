package com.copypaste.run;

import com.copypaste.impls.CopyPasteUIImpl;

public class CPMain {
 /**
  * Main class for running copy paste with logic
  */
	public static void main(String[] args) {
		new CopyPasteUIImpl.CopyPasteUIBuilder().withTitle("copy paste").build().showUI();

	}
}
