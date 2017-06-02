package net.bobacus.fn;


public interface PositionalFunction<T, U> {
    U eval(T op, Position s);
}
