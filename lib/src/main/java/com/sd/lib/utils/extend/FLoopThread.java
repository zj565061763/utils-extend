package com.sd.lib.utils.extend;

/**
 * 循环线程
 */
public abstract class FLoopThread extends Thread
{
    private final long mInterval;
    private boolean mIsPaused = false;

    public FLoopThread(long interval)
    {
        if (interval < 0)
            throw new IllegalArgumentException("interval must be >= 0");

        mInterval = interval;
    }

    /**
     * 是否处于暂停循环状态
     *
     * @return
     */
    public synchronized final boolean isPaused()
    {
        return mIsPaused;
    }

    /**
     * 暂停循环
     */
    public synchronized final void pauseLoop()
    {
        mIsPaused = true;
    }

    /**
     * 恢复循环
     */
    public synchronized final void resumeLoop()
    {
        mIsPaused = false;
        notifyAll();
    }

    /**
     * 停止循环
     */
    public final void stopLoop()
    {
        interrupt();
    }

    @Override
    public final void run()
    {
        super.run();
        try
        {
            onStart();
            while (!isInterrupted())
            {
                if (mIsPaused)
                {
                    onPaused();

                    try
                    {
                        synchronized (this)
                        {
                            wait();
                        }
                    } catch (InterruptedException e)
                    {
                        break;
                    }

                    onResume();
                }
                onLoop();

                if (mInterval > 0)
                {
                    try
                    {
                        sleep(mInterval);
                    } catch (InterruptedException e)
                    {
                        break;
                    }
                }
            }
        } finally
        {
            onFinally();
        }
    }

    protected abstract void onLoop();

    protected void onStart()
    {
    }

    protected void onPaused()
    {
    }

    protected void onResume()
    {
    }

    protected void onFinally()
    {
    }
}
