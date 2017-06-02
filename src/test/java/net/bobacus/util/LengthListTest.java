package net.bobacus.util;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LengthListTest {

    @Test
    public void empty_pos_0_gives_minus_1_0() {
        LengthList list = new LengthList(emptyList());
        assertThat("index", list.getIndexForPosition(0), is(-1));
        assertThat("offset", list.getOffsetForPosition(0), is(0));
    }

    @Test
    public void empty_pos_1_gives_minus_1_1() {
        LengthList list = new LengthList(emptyList());
        assertThat("index", list.getIndexForPosition(1), is(-1));
        assertThat("offset", list.getOffsetForPosition(1), is(1));
    }

    @Test
    public void empty_pos_minus_1_gives_minus_1_minus_1() {
        LengthList list = new LengthList(emptyList());
        assertThat("index", list.getIndexForPosition(-1), is(-1));
        assertThat("offset", list.getOffsetForPosition(-1), is(-1));
        // although maybe we should not define this for negative positions - there could be an exception thrown instead?
    }

    @Test
    public void single_zero() {
        LengthList list = new LengthList(singletonList(0));
        assertThat("index", list.getIndexForPosition(0), is(0));
        assertThat("offset", list.getOffsetForPosition(0), is(0));
    }

    @Test
    public void single_one() {
        LengthList list = new LengthList(singletonList(1));

        assertThat("index of 0", list.getIndexForPosition(0), is(0));
        assertThat("offset of 0", list.getOffsetForPosition(0), is(0));

        assertThat("index of 1", list.getIndexForPosition(1), is(0));
        assertThat("offset of 1", list.getOffsetForPosition(1), is(1));
    }

    @Test
    public void double_one() {
        LengthList list = new LengthList(asList(1, 1));

        assertThat("index of 0", list.getIndexForPosition(0), is(0));
        assertThat("offset of 0", list.getOffsetForPosition(0), is(0));

        assertThat("index of 1", list.getIndexForPosition(1), is(1));
        assertThat("offset of 1", list.getOffsetForPosition(1), is(0));

        assertThat("index of 2", list.getIndexForPosition(2), is(1));
        assertThat("offset of 2", list.getOffsetForPosition(2), is(1));
    }

    @Test
    public void one_two() {
        LengthList list = new LengthList(asList(1, 2));

        assertThat("index of 0", list.getIndexForPosition(0), is(0));
        assertThat("offset of 0", list.getOffsetForPosition(0), is(0));

        assertThat("index of 1", list.getIndexForPosition(1), is(1));
        assertThat("offset of 1", list.getOffsetForPosition(1), is(0));

        assertThat("index of 2", list.getIndexForPosition(2), is(1));
        assertThat("offset of 2", list.getOffsetForPosition(2), is(1));

        assertThat("index of 3", list.getIndexForPosition(3), is(1));
        assertThat("offset of 3", list.getOffsetForPosition(3), is(2));
    }

    /*
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
	 */

    public static void main(String[] args) {
        List<Integer> list = asList(1, 2, 3, 4, 5, 6);
        LengthList nl = new LengthList(list);

        for (int t = 0; t < 30; t++) {
            System.out.println("t = " + t + ", n = " + nl.getIndexForPosition(t) + ", offset = " + nl.getOffsetForPosition(t));
        }
    }
}
