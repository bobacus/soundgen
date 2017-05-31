package net.bobacus.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterates through iterators, returning their elements.
 * 
 * @author rob
 *
 * @param <E>
 */
public class IteratorIterator<E> implements Iterator<E> {

	public IteratorIterator(Iterator<? extends Iterator<? extends E>> i) {
		this.iter = i;
		advanceToNextOrEnd();
	}
	
	private final Iterator<? extends Iterator<? extends E>> iter;
	
	private Iterator<? extends E> currentIter;
	
	public boolean hasNext() {
		return currentIter!=null;
	}

	public E next() {
		if (currentIter==null) 
			throw new NoSuchElementException();
		E e = currentIter.next();
		advanceToNextOrEnd();
		return e;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	private void advanceToNextOrEnd() {
		if (currentIter!=null && currentIter.hasNext())
			return;
		while (iter.hasNext()) {
			currentIter = iter.next();
			if (currentIter.hasNext())
				return;
		}
		currentIter = null;
	}

}
