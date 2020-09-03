package com.sd.lib.utils.extend;

import android.os.Handler;
import android.os.Looper;

public abstract class FDelayTask
{
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    /** 是否已经被post到Handler */
    private volatile boolean mHasPost;
    /** 是否被暂停 */
    private volatile boolean mIsPaused;

    /**
     * 是否已经被post到Handler
     *
     * @return
     */
    public boolean hasPost()
    {
        return mHasPost;
    }

    /**
     * 是否被暂停
     *
     * @return
     */
    public boolean isPaused()
    {
        return mIsPaused;
    }

    /**
     * 延迟执行
     *
     * @param delay (单位毫秒)
     */
    public final synchronized void runDelay(long delay)
    {
        if (delay < 0)
            delay = 0;

        removeDelay();
        MAIN_HANDLER.postDelayed(mRunnable, delay);
        mHasPost = true;
        FDelayTask.this.onPost(delay);
    }

    /**
     * 立即在当前线程执行，如果有延迟任务会先移除延迟任务
     */
    public final synchronized void runImmediately()
    {
        removeDelay();
        mRunnable.run();
    }

    /**
     * 移除延迟任务
     *
     * @return
     */
    public final synchronized boolean removeDelay()
    {
        MAIN_HANDLER.removeCallbacks(mRunnable);
        mIsPaused = false;

        if (mHasPost)
        {
            mHasPost = false;
            FDelayTask.this.onPostRemove();
            return true;
        }
        return false;
    }

    /**
     * 暂停
     */
    public final synchronized void pause()
    {
        if (!mIsPaused)
        {
            if (mHasPost)
            {
                mIsPaused = true;
                onPause();
            }
        }
    }

    /**
     * 恢复
     */
    public final synchronized void resume()
    {
        if (mIsPaused)
        {
            mIsPaused = false;
            notifyRun();
        }
    }

    private final Runnable mRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            synchronized (FDelayTask.this)
            {
                mHasPost = false;

                if (mIsPaused)
                    return;
            }

            notifyRun();
        }
    };

    private void notifyRun()
    {
        FDelayTask.this.onRun();
    }

    /**
     * 任务执行回调
     */
    protected abstract void onRun();

    /**
     * 任务被post到Handler回调
     *
     * @param delay
     */
    protected void onPost(long delay)
    {
    }

    /**
     * 任务从Handler被取消回调
     */
    protected void onPostRemove()
    {
    }

    /**
     * 任务被暂停回调
     */
    protected void onPause()
    {
    }
}
