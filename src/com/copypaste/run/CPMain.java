package com.copypaste.run;

import com.copypaste.CopyPasteUI;
import com.copypaste.MemoryMappedCopyLogicImpl;

public class CPMain {
 /**
  * Main class for running copy paste with logic
  */
	public static void main(String[] args) {
		if(args[0].equalsIgnoreCase("cp")){
			CopyPasteUI<String> cp = new CopyPasteUI<>();
			cp.injectLogic(new MemoryMappedCopyLogicImpl());
			cp.showGui();
		}
	}
}
