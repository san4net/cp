package com.copypaste.util;

import java.util.HashMap;
import java.util.Map;

public interface Constants {
	public static enum COPY_OPTION {
		FILE, DIRECTORY;
	}
	
	public static enum STATE {
		READEY, DOING, DONE, TERMINATE
	}
	String[] actionCommands = { "FILE", "DIR" };


	Map<String, COPY_OPTION> actionCommandsToCopyMap = new HashMap<>();
   
}
