package com.fanwe.lib.utils.extend;

/**
 * 可以限制Runnable最大重试次数
 */
public class FRunnableTryer
{
    private static final int DEFAULT_TRY_COUNT = 3;

    /**
     * 最大重试次数
     */
    private int mMaxTryCount = DEFAULT_TRY_COUNT;
    /**
     * 已重试次数
     */
    private int mTryCount = 0;
    /**
     * 重试Runnable
     */
    private Runnable mTryRunnable;

    /**
     * 设置重试次数
     *
     * @param maxTryCount
     * @return
     */
    public synchronized void setMaxTryCount(int maxTryCount)
    {
        this.mMaxTryCount = maxTryCount;
    }

    /**
     * 返回最大重试次数，默认3次
     *
     * @return
     */
    public synchronized int getMaxTryCount()
    {
        return mMaxTryCount;
    }

    /**
     * 返回已重试次数
     *
     * @return
     */
    public synchronized int getTryCount()
    {
        return mTryCount;
    }

    /**
     * 执行重试Runnable
     *
     * @param runnable
     * @return true-重试Runnable提交成功，false-超过最大重试次数
     */
    public boolean tryRun(Runnable runnable)
    {
        return tryRunDelayed(runnable, 0);
    }

    /**
     * 执行重试Runnable
     *
     * @param delay 延迟多久执行
     * @return true-重试Runnable提交成功，false-超过最大重试次数
     */
    public synchronized boolean tryRunDelayed(Runnable runnable, long delay)
    {
        mTryRunnable = runnable;

        if (mTryCount + 1 > mMaxTryCount)
        {
            //超过最大重试次数，不处理
            return false;
        } else
        {
            mTryCount++;
            mDelayRunnable.runDelayOrImmediately(delay);
            return true;
        }
    }

    private FDelayRunnable mDelayRunnable = new FDelayRunnable()
    {
        @Override
        public void run()
        {
            synchronized (FRunnableTryer.this)
            {
                if (mTryRunnable != null)
                {
                    mTryRunnable.run();
                }
            }
        }
    };

    /**
     * 重置重试次数
     */
    public synchronized void resetTryCount()
    {
        mTryCount = 0;
    }

    /**
     * 销毁
     */
    public void onDestroy()
    {
        mDelayRunnable.removeDelay();
        mTryRunnable = null;
    }
}
