package net.bobacus.util;

import java.util.Arrays;
import java.util.List;

public class NumberListTest {

	/*
	 * s: 1, 2, 3, 4, 5, 6
	 * S: 0, 1, 3, 6, 10, 15, 21
	 * 
	 * t = 0 => n = 0
	 * t = 1 => n = 1
	 * t = 2 => n = 1
	 * t = 3 => n = 2

 * n: 0 1  2   3    4     5
 *    . .. ... .... ..... ......

	 */
	
	public static void main(String[] args) {
		List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
		LengthList nl = new LengthList(list);
		
		for (int t = 0; t<30; t++) {
			System.out.println("t = "+t+", n = "+nl.getIndexForPosition(t) + ", offset = "+nl.getOffsetForPosition(t));
		}
	}
}
