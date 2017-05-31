package net.bobacus.fn;

public interface Function<T,U> {
	public U eval(T op);
}
