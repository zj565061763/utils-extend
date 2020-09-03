package com.sd.lib.utils.extend;

import android.os.Handler;
import android.os.Looper;

public abstract class FDelayTask
{
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    private boolean mHasPost;

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
        onPost(delay);
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
        if (mHasPost)
        {
            mHasPost = false;
            onPostRemoved();
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
                mHasPost = false;
            }

            FDelayTask.this.onRun();
        }
    };

    protected abstract void onRun();

    protected void onPost(long delay)
    {
    }

    protected void onPostRemoved()
    {
    }
}
