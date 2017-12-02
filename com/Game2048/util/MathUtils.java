package com.Game2048.util;

import java.text.DecimalFormat;

public class MathUtils {
	
	private final static DecimalFormat df = new DecimalFormat("#.##");
	
	public static String formatNumber(double number){
		return df.format(number);
	}

	public static int log2floor(int number) {
		return 31 - Integer.numberOfLeadingZeros(number);
	}

	public static int log2ceil(int number) {
		return 32 - Integer.numberOfLeadingZeros(number - 1);
	}
}
