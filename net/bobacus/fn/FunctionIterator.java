package net.bobacus.fn;

import java.util.Iterator;



public class FunctionIterator<T,U> implements Iterator<U> {

	public static <E,F> FunctionIterator<E,F> create(Iterator<? extends E> i, Function<E,F> fn) {
		return new FunctionIterator<E,F>(i, fn);
	}
	
	public FunctionIterator(Iterator<? extends T> i, Function<T,U> fn) {
		this.iter = i;
		this.fn = fn;
	}
	
	private final Iterator<? extends T> iter;
	private final Function<T,U> fn;
	
	public boolean hasNext() {
		return iter.hasNext();
	}

	public U next() {
		return fn.eval(iter.next());
	}

	public void remove() {
		iter.remove();
	}

}
