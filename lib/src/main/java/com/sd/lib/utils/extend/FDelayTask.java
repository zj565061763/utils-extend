package com.sd.lib.utils.extend;

import android.os.Handler;
import android.os.Looper;

public abstract class FDelayTask
{
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    private boolean mPost;

    /**
     * 延迟执行
     *
     * @param delay (单位毫秒)
     */
    public final synchronized void runDelay(long delay)
    {
        removeDelay();
        MAIN_HANDLER.postDelayed(mRunnable, delay);
        mPost = true;
        onPost();
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
     * 延迟或者立即执行
     *
     * @param delay (单位毫秒) 小于等于0-立即在当前线程执行，大于0-延迟执行
     */
    public final void runDelayOrImmediately(long delay)
    {
        if (delay > 0)
            runDelay(delay);
        else
            runImmediately();
    }

    /**
     * 移除延迟任务
     *
     * @return
     */
    public final synchronized boolean removeDelay()
    {
        MAIN_HANDLER.removeCallbacks(mRunnable);
        if (mPost)
        {
            mPost = false;
            onRemove();
            return true;
        }
        return false;
    }

    private final Runnable mRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            synchronized (FDelayTask.this)
            {
                mPost = false;
            }

            FDelayTask.this.onRun();
        }
    };

    protected abstract void onRun();

    protected void onPost()
    {
    }

    protected void onRemove()
    {
    }
}
