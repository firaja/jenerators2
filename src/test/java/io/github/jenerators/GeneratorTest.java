package io.github.jenerators;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

import io.github.firaja.jenerators.Generator;
import io.github.firaja.jenerators.ThreadedGenerator;


public class GeneratorTest
{
    @Test
    public void test()
    {
        Generator<Integer> simpleGenerator = new ThreadedGenerator<Integer>() {
            public void generate()
            {
                //System.out.println("uno");
                yield(1);
                //System.out.println("due");
                yield(2);
            }
        };

        Generator<Integer> simpleGenerator2 = new ThreadedGenerator<Integer>() {
            public void generate()
            {
                //System.out.println("uno");
                this.yield(3);
                //System.out.println("due");
                this.yield(4);
            }
        };



        for (Integer s : simpleGenerator)
        {
            System.out.println(s);
        }

        simpleGenerator.reset();

        for (Integer s : simpleGenerator)
        {
            System.out.println(s);
        }





    }
}
