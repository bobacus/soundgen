package net.bobacus.io;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * @author rob
 *
 */
public class LineReader implements Iterable<String>,Closeable {


	public LineReader(File file) throws FileNotFoundException {
		this(new BufferedReader(new FileReader(file)));
	}

	private LineReader(BufferedReader r) {
		reader = r;
	}
	
	private final BufferedReader reader;
	
	public Iterator<String> iterator() {
		try {
			return new Iterator<String>() {
				private String nextString = reader.readLine();
				public String next() {
					String l = nextString;
					if (l==null) {
						throw new NoSuchElementException();
					}
					try {
						nextString = reader.readLine();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					return l;
				}
				public boolean hasNext() {
					return nextString != null;
				}
				public void remove() {
					throw new UnsupportedOperationException();
				}
			};
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void close() throws IOException {
		reader.close();
	}
}
