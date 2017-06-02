package net.bobacus.fn;

public interface Function<T,U> {
	U eval(T op);
}
