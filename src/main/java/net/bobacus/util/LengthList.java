package net.bobacus.util;

import java.util.List;

/**
 * Given a list of numbers, this class will return the index of the
 * number whose cumulative value is past a threshold.
 * 
 * i.e. a sequence s(i) i>=0
 * has cumulative sequence S(i) i>=0
 * defined as
 * S(0) = 0
 * S(1) = s(0)
 * S(2) = s(0)+s(1) = S(1) + s(1)
 * S(n) = S(n-1) + s(n-1)
 * 
 * so for a given threshold t we are finding the highest n such that
 * 
 * S(n) <= t
 * 
 * s: 1, 2, 3, 4, 5, 6
 * S: 0, 1, 3, 6, 10, 15, 21
 * 
 * t = 0 => n = 0
 * t = 1 => n = 1
 * t = 2 => n = 1
 * t = 3 => n = 2
 * 
 * n: 0 1  2   3    4     5
 *    . .. ... .... ..... ......
 * 
 * we want to find the n with the t-th dot (counting from zero)
 * 
 * Probably best not to put zeroes in the list. It makes things complicated.
 * 
 * @author rob
 *
 */
public class LengthList {

	public LengthList(List<Integer> list) {
		this.list = list;
	}
	
	private final List<Integer> list;
	
	/**
	 * Searches for the index of the length which contains the nth dot
	 * @param position n
	 * @return index of length in list
	 */
	public int getIndexForPosition(int position) {
		int i = -1;
		int sum = 0;
		for (int s : list) {
			if (sum > position)
				break;
			sum += s;
			i ++;
		}
		return i;
	}
	
	/**
	 * Calculates the offset of the given position from the start of its length
	 * @param position the position
	 * @return positional offset (0 means the position was the first one in a particular length)
	 */
	public int getOffsetForPosition(int position) {
		int index = getIndexForPosition(position);
		int start = getStartPosition(index);
		return position-start;
	}
	
	/**
	 * Calculates the starting position of a given length
	 * @param index index of the length
	 * @return the position of the first point in the length
	 */
	private int getStartPosition(int index) {
		int sum = 0;
		int i = 0;
		for (int s : list) {
			if (i >= index)
				break;
			sum += s;
			i ++;
		}
		return sum;
	}


}
