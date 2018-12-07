package com.sd.lib.utils.extend;

import android.os.Handler;
import android.os.Looper;

/**
 * 重试帮助类
 */
public abstract class FRetryWorker
{
    /**
     * 最大重试次数
     */
    private final int mMaxRetryCount;

    /**
     * 重试是否已经开始
     */
    private boolean mIsStarted = false;
    /**
     * 是否重试成功
     */
    private boolean mIsRetrySuccess = true;
    /**
     * 当前第几次重试
     */
    private int mRetryCount = 0;

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public FRetryWorker(int maxRetryCount)
    {
        if (maxRetryCount <= 0)
            throw new IllegalArgumentException("maxRetryCount must > 0");
        mMaxRetryCount = maxRetryCount;
    }

    /**
     * 是否已经开始重试
     *
     * @return
     */
    public final boolean isStarted()
    {
        return mIsStarted;
    }

    /**
     * 是否重试成功
     *
     * @return
     */
    public final boolean isRetrySuccess()
    {
        return mIsRetrySuccess;
    }

    /**
     * 返回当前第几次重试
     *
     * @return
     */
    public final int getRetryCount()
    {
        return mRetryCount;
    }

    /**
     * 开始重试
     */
    public final synchronized void start()
    {
        stop();

        setStarted(true);
        mIsRetrySuccess = false;
        mRetryCount = 0;

        retry(0);
    }

    private final Runnable mRetryRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            synchronized (FRetryWorker.this)
            {
                if (!mIsStarted)
                    return;

                if (mIsRetrySuccess)
                {
                    stop();
                } else
                {
                    if (!isMaxRetry())
                    {
                        mRetryCount++;
                        onRetry();
                    }
                }
            }
        }
    };

    /**
     * 停止重试
     */
    public final synchronized void stop()
    {
        setStarted(false);
        mHandler.removeCallbacks(mRetryRunnable);
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
     * 重试，只有{@link #isStarted()}为true，此方法才有效
     *
     * @param delayMillis 延迟多少毫秒
     */
    protected final synchronized void retry(long delayMillis)
    {
        if (!mIsStarted)
            return;

        if (isMaxRetry())
            return;

        mHandler.removeCallbacks(mRetryRunnable);
        mHandler.postDelayed(mRetryRunnable, delayMillis);
    }

    private boolean isMaxRetry()
    {
        if (mRetryCount >= mMaxRetryCount)
        {
            // 达到最大重试次数
            stop();

            if (!mIsRetrySuccess)
                onRetryFailedAfterMaxRetryCount();

            return true;
        }
        return false;
    }

    /**
     * 设置重试成功
     */
    public final synchronized void setRetrySuccess()
    {
        mIsRetrySuccess = true;
        stop();
    }

    protected void onStateChanged(boolean isStarted)
    {
    }

    /**
     * 执行重试任务（UI线程）
     */
    protected abstract void onRetry();

    /**
     * 达到最大重试次数，并且重试失败
     */
    protected abstract void onRetryFailedAfterMaxRetryCount();
}
