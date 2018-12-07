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
     * 已重试次数
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
     * 返回已重试次数
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

        mIsStarted = true;
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
                if (mIsRetrySuccess)
                {
                    stop();
                } else
                {
                    if (mRetryCount >= mMaxRetryCount)
                    {
                        // 达到最大重试次数
                        stop();
                        onRetryFailedOnMaxRetryCount();
                    } else
                    {
                        if (onRetry())
                            mRetryCount++;
                    }
                }
            }
        }
    };

    /**
     * 停止重试
     */
    private void stop()
    {
        mIsStarted = false;
        mHandler.removeCallbacks(mRetryRunnable);
    }

    /**
     * 重试，只有{@link #isStarted()}为true，此方法才有效
     *
     * @param delayMillis 延迟多少毫秒
     */
    public final synchronized void retry(long delayMillis)
    {
        if (!mIsStarted)
            return;

        mHandler.removeCallbacks(mRetryRunnable);
        mHandler.postDelayed(mRetryRunnable, delayMillis);
    }

    /**
     * 设置重试成功
     */
    public final synchronized void setRetrySuccess()
    {
        mIsRetrySuccess = true;
        stop();
    }

    /**
     * 执行重试任务（UI线程）
     *
     * @return true-发起了一次重试，false-没有发起重试
     */
    protected abstract boolean onRetry();

    /**
     * 达到最大重试次数，并且重试失败
     */
    protected abstract void onRetryFailedOnMaxRetryCount();
}
