package io.github.firaja.jenerators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.Executors;


public abstract class ThreadedGenerator<T> implements Generator<T>
{

    private final Lock producerLock = new Lock();

    private final Lock consumerLock = new Lock();

    private Thread producer;

    private boolean hasFinished;

    private T yielded;

    private boolean nextItemAvailable;

    @Override
    public T last()
    {
        return yielded;
    }

    @Override
    public void reset()
    {

    }

    @Override
    public Iterator<T> iterator()
    {
        return new Iterator<T>()
        {

            @Override
            public boolean hasNext()
            {
                if (nextItemAvailable)
                {
                    return true;
                }
                if (hasFinished)
                {
                    return false;
                }
                if (producer == null)
                {
                    startThread();
                }
                consumerLock.start();

                producerLock.stop();

                return !hasFinished;
            }

            @Override
            public T next()
            {
                if (!hasNext())
                {
                    throw new NoSuchElementException();
                }
                nextItemAvailable = false;
                return yielded;
            }

            @Override
            public void remove()
            {
                throw new UnsupportedOperationException();
            }

        };
    }

    @SuppressWarnings("java:S6213")
    protected void yield(T element)
    {
        yielded = element;
        nextItemAvailable = true;
        producerLock.start();
        consumerLock.stop();
    }

    private void wrapperGenerate()
    {
        consumerLock.stop();
        generate();

        hasFinished = true;
        producerLock.start();
    }

    private void startThread()
    {
        producer = Executors.defaultThreadFactory().newThread(this::wrapperGenerate);
        producer.start();
    }

    @SuppressWarnings("java:S1113")
    @Override
    protected void finalize() throws Throwable
    {
        producer.interrupt();
        producer.join();
        super.finalize();
    }

    @SuppressWarnings("java:S2446")
    private static class Lock
    {
        private boolean isStarted;

        public synchronized void start()
        {
            isStarted = true;
            notify();
        }

        @SuppressWarnings("java:S2274")
        public synchronized void stop()
        {
            try
            {
                if (isStarted)
                {
                    return;
                }
                wait();
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
            finally
            {
                isStarted = false;
            }
        }
    }

}
