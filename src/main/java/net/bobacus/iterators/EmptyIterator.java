package net.bobacus.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class EmptyIterator<E> implements Iterator<E> {

	@SuppressWarnings("unchecked")
	public static <T> EmptyIterator<T> get() {
		return (EmptyIterator<T>) instance;
	}
	
	private final static EmptyIterator<Object> instance = new EmptyIterator<>();
	
	private EmptyIterator() { /* empty */ }
	
	public boolean hasNext() {
		return false;
	}

	public E next() {
		throw new NoSuchElementException();
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}
