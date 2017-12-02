package com.Game2048.util;

import java.util.List;

public class ListUtils {
	
	public static <T extends Comparable<T>> int insert(List<T> list, T object) {
		int index = 0;
		for(T item : list){
			if(object.compareTo(item) > 0)
				break;
			index++;
		}
		list.add(index, object);
			
		return index;
	}
}
