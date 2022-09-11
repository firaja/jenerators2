package io.github.firaja.jenerators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;


@RunWith(JUnitPlatform.class)
@SelectPackages("io.github.firaja.jenerators")
@SelectClasses({ ThreadedGenerator.class})
public class ThreadedGeneratorTest
{

    private static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet consectetur adipiscing elit sed do eiusmod tempor incididunt ut labore et dolore magna aliqua ";

    private Generator<Long> fibonacciGenerator;

    private Generator<Long> naturalsGenerator;

    private Generator<String> loremIpsumGenerator;

    @BeforeEach
    void before()
    {
        fibonacciGenerator = new ThreadedGenerator<Long>()
        {
            @Override
            public void generate()
            {
                long n1 = 0;
                long n2 = 1;
                int n = 0;
                while (true)
                {
                    this.yield(n1);
                    n2 = n1 + n2;
                    this.yield(n2);
                    n1 = n1 + n2;

                }
            }
        };

        naturalsGenerator = new ThreadedGenerator<Long>()
        {
            @Override
            public void generate()
            {
                long n = 0;
                while (true)
                {
                    this.yield(n);
                    n++;
                }

            }
        };

        loremIpsumGenerator = new ThreadedGenerator<String>()
        {
            @Override
            public void generate()
            {
                this.yield("Lorem");
                this.yield("ipsum");
                this.yield("dolor");
                this.yield("sit");
                this.yield("amet");
                this.yield("consectetur");
                this.yield("adipiscing");
                this.yield("elit");
                this.yield("sed");
                this.yield("do");
                this.yield("eiusmod");
                this.yield("tempor");
                this.yield("incididunt");
                this.yield("ut");
                this.yield("labore");
                this.yield("et");
                this.yield("dolore");
                this.yield("magna");
                this.yield("aliqua");


            }
        };


    }


    @Test
    public void testNaturals1()
    {
        long result = counterTest(naturalsGenerator, 15487);

        Assertions.assertEquals(15487, result);
    }

    @Test
    public void testNaturals2()
    {
        long result = counterTest(naturalsGenerator, 10);

        Assertions.assertEquals(10, result);
    }

    @Test
    public void testNaturals3()
    {
        long result = counterTest(naturalsGenerator, 10);

        //naturalsGenerator.reset();

        result += counterTest(naturalsGenerator, 20);

        Assertions.assertEquals(30, result);
    }

    @Test
    public void testFibonacci1()
    {
        long result = counterTest(fibonacciGenerator, 10);

        Assertions.assertEquals(55, result);
    }

    @Test
    public void testFibonacci2()
    {
        long result = counterTest(fibonacciGenerator, 50);

        Assertions.assertEquals(12586269025L, result);
    }

    @Test
    public void testLoremIpsum1()
    {
        StringBuilder sb = new StringBuilder();
        for (String li: loremIpsumGenerator)
        {
            sb.append(li).append(" ");
        }
        Assertions.assertEquals(LOREM_IPSUM, sb.toString());
    }

    @Test
    public void testLoremIpsum2()
    {
        StringBuilder sb = new StringBuilder();
        for (String li: loremIpsumGenerator)
        {
            sb.append(li).append(" ");
        }

        loremIpsumGenerator.reset();

        for (String li: loremIpsumGenerator)
        {
            sb.append(li).append(" ");
        }


        Assertions.assertEquals(LOREM_IPSUM + LOREM_IPSUM, sb.toString());
    }

    @Test
    public void testLoremIpsum3()
    {
        StringBuilder sb = new StringBuilder();
        for (String li: loremIpsumGenerator)
        {
            sb.append(li).append(" ");
        }

        for (String li: loremIpsumGenerator)
        {
            sb.append(li).append(" ");
        }


        Assertions.assertEquals(LOREM_IPSUM, sb.toString());
    }


    private static <T> T counterTest(Generator<T> gen, long counter)
    {
        long i = 0;
        for (T f : gen)
        {
            if (i >= counter)
            {
                break;
            }
            i++;
        }
        return gen.last();
    }
}
