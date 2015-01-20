package com.copypaste.run;

import com.copypaste.impls.CopyPasteUIImpl;

public class CPMain {
 /**
  * Main class for running copy paste with logic
  */
	public static void main(String[] args) {
		if(args[0].equalsIgnoreCase("cp")){
			CopyPasteUIImpl<String> cp = new CopyPasteUIImpl<>("Copy Paste");
			cp.showUI();
		}
	}
}
