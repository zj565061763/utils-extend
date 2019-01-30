package com.sd.lib.utils.extend;

/**
 * 循环线程
 */
public abstract class FLoopThread extends Thread
{
    private boolean mIsPaused = false;

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

                final long sleepTime = onLoop();

                if (sleepTime > 0)
                {
                    try
                    {
                        sleep(sleepTime);
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

    /**
     * 每次循环触发
     *
     * @return 返回线程休眠多久后进行下一次循环
     */
    protected abstract long onLoop();

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
