package net.bobacus.fn;

import java.util.Iterator;


public class PositionalFunctionIterator<T, U> implements Iterator<U> {

    public static <E, F> PositionalFunctionIterator<E, F> create(Iterator<? extends E> iter, PositionalFunction<E, F> fn) {
        return new PositionalFunctionIterator<>(iter, fn);
    }

    private PositionalFunctionIterator(Iterator<? extends T> iter, PositionalFunction<T, U> fn) {
        this.iter = iter;
        this.fn = fn;
        this.pos = new PositionImpl();
    }

    private final Iterator<? extends T> iter;
    private final PositionalFunction<T, U> fn;
    private final PositionImpl pos;

    public boolean hasNext() {
        return iter.hasNext();
    }

    public U next() {
        U result = fn.eval(iter.next(), pos);
        pos.index++;
        return result;
    }

    public void remove() {
        iter.remove();
    }

    class PositionImpl implements Position {
        int index = 0;

        public boolean isFirst() {
            return index == 0;
        }

        public boolean isLast() {
            return !iter.hasNext();
        }

        public int getIndex() {
            return index;
        }
    }
}

