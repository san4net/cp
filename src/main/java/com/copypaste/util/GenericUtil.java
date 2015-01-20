package com.copypaste.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GenericUtil {
//	public static <E> List<E>  toList(E[] array){
//		ArrayList<E> list = new ArrayList<>();
//		for(E  element: array){
//			list.add(element);
//		}
//		
//		return list;
//	}
	
	public static <E> List<E> toList(E... arrays){
		ArrayList<E> list = new ArrayList<>();
		for(E  element: arrays){
			list.add(element);
		}
		return list;
	}
	
	public static void main(String[] args) {
		Integer[] ab = { 1 };
		toList(ab);
		List<Object> objs = Arrays.<Object>asList(1,2,"sonu");
		List<Integer> ints = Arrays.asList(3,4);
		
		Collections.copy(objs, ints);
		System.out.println(objs);
		List<Integer> intList = null;
		Object o = new Object();
		List<? super Number> suInt = new ArrayList<>();
		
	}
}
