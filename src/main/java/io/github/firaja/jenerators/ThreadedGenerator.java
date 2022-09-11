package io.github.firaja.jenerators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.Executors;


public abstract class ThreadedGenerator<T> implements Generator<T>
{

    private Lock producerLock;

    private Lock consumerLock;

    private Thread thread;

    private boolean hasFinished;

    private T yielded;

    private boolean yieldCalled;

    public ThreadedGenerator()
    {
        initialize();
    }

    private void initialize()
    {
        if (thread != null)
        {
            try
            {
                thread.interrupt();
                thread.join();
            }
            catch (InterruptedException e)
            {
                thread.interrupt();
            }
        }
        hasFinished = false;
        thread = null;
        yielded = null;
        yieldCalled = false;

        producerLock = new Lock();
        consumerLock = new Lock();
    }

    @Override
    public T last()
    {
        return yielded;
    }

    @Override
    public void reset()
    {
        initialize();
    }

    @Override
    public Iterator<T> iterator()
    {
        return new Iterator<T>()
        {
            @Override
            public boolean hasNext()
            {
                if (yieldCalled)
                {
                    return true;
                }
                if (hasFinished)
                {
                    return false;
                }
                if (thread == null)
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
                yieldCalled = false;
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
    protected void yield(T value)
    {
        yielded = value;
        yieldCalled = true;
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
        thread = Executors.defaultThreadFactory().newThread(this::wrapperGenerate);
        thread.start();
    }

    @SuppressWarnings("java:S1113")
    @Override
    protected void finalize() throws Throwable
    {
        thread.interrupt();
        thread.join();
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
