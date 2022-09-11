package io.github.jenerators;

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
                yield(3);
                //System.out.println("due");
                yield(4);
            }
        };



        /*for (Integer s : simpleGenerator)
        {
            System.out.println(s);
        }

        for (Integer s : simpleGenerator2)
        {
            System.out.println(s);
        }*/

        Iterator<Integer> i1 = simpleGenerator.iterator();
        Iterator<Integer> i2 = simpleGenerator.iterator();
        System.out.println(i1.next());
        System.out.println(i2.next());



    }
}
