package com.copypaste.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GenericUtil {
	
	public static <E> List<E> toList(E... arrays){
		ArrayList<E> list = new ArrayList<>();
		for(E  element: arrays){
			list.add(element);
		}
		return list;
	}


}
