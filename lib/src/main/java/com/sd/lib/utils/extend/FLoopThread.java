package com.sd.lib.utils.extend;

/**
 * 循环线程
 */
public abstract class FLoopThread
{
    private InternalThread mThread;
    private boolean mIsStarted = false;

    /**
     * 是否已经开始
     *
     * @return
     */
    public final boolean isStarted()
    {
        return mIsStarted;
    }

    public synchronized final void start()
    {
        if (mThread == null)
        {
            mThread = new InternalThread();
            mThread.start();
            setStarted(true);
        }
    }

    /**
     * 停止循环
     */
    public synchronized final void stop()
    {
        if (mThread != null)
        {
            mThread.interrupt();
            mThread = null;
            setStarted(false);
        }
    }

    private void setStarted(boolean started)
    {
        if (mIsStarted != started)
        {
            mIsStarted = started;
            onStateChanged(started);
        }
    }

    /**
     * 循环是否开始状态变化回调
     *
     * @param started
     */
    protected void onStateChanged(boolean started)
    {
    }

    /**
     * 每次循环触发
     *
     * @return 返回线程休眠多久后进行下一次循环
     */
    protected abstract long onLoop();

    private final class InternalThread extends Thread
    {
        @Override
        public void run()
        {
            while (!isInterrupted())
            {
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
        }
    }
}
