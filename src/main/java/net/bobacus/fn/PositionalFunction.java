package net.bobacus.fn;


public interface PositionalFunction<T,U> {
	public U eval(T op, Position s);
}
