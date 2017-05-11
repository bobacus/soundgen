package net.bobacus.fn;

import java.util.Iterator;
import java.util.NoSuchElementException;



public class FilterIterator<E> implements Iterator<E> {

	public FilterIterator(Iterator<? extends E> i, Predicate<? super E> p) {
		this.iter = i;
		this.pred = p;
		advanceToNextOrEnd();
	}
	
	private final Iterator<? extends E> iter;
	private final Predicate<? super E> pred;
	
	private E nextElement;
	
	public boolean hasNext() {
		return nextElement != null;
	}

	public E next() {
		if (nextElement==null) 
			throw new NoSuchElementException();
		E e = nextElement;
		advanceToNextOrEnd();
		return e;
	}

	public void remove() {
		iter.remove();
	}
	
	private void advanceToNextOrEnd() {
		while (iter.hasNext()) {
			nextElement = iter.next();
			if (pred.test(nextElement))
				return;
		}
		nextElement = null;
	}

}
