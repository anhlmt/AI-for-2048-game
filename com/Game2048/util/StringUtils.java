package com.Game2048.util;

public class StringUtils {
	public static boolean isPositiveInteger(String input) {
		String regex = "^[1-9]\\d*$";
		if (input.matches(regex))
			return true;
		return false;
	}
}
