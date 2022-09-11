package io.github.firaja.jenerators;



public interface Generator<T> extends Iterable<T>
{

    void generate();

    T last();

    void reset();


}
