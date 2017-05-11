package net.bobacus.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SingleIterator<E> implements Iterator<E> {

	public static <T> SingleIterator<T> create(T o) {
		return new SingleIterator<T>(o);
	}
	
	public SingleIterator(E o) {
		if (o==null)
			throw new NullPointerException();
		this.object = o;
	}
	
	private E object;
	
	public boolean hasNext() {
		return object!=null;
	}

	public E next() {
		if (object==null)
			throw new NoSuchElementException();
		E o = object;
		object = null;
		return o;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}
