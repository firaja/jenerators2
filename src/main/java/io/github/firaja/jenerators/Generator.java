package io.github.firaja.jenerators;

import java.util.Iterator;


public interface Generator<T> extends Iterable<T>
{

    void generate();

    T last();

    void reset();



}
