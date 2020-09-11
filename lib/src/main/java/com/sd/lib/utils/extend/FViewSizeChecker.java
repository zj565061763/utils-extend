package com.sd.lib.utils.extend;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FViewSizeChecker
{
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Map<View, String> mViewHolder = new ConcurrentHashMap<>();
    private boolean mIsReady;

    private long mCheckDelay = 500;
    private Callback mCallback;

    /**
     * 设置延迟多少毫秒开始检查
     *
     * @param delay
     */
    public void setCheckDelay(long delay)
    {
        if (delay < 0)
            delay = 0;
        mCheckDelay = delay;
    }

    /**
     * 是否已经准备好
     *
     * @return
     */
    public boolean isReady()
    {
        return mIsReady;
    }

    /**
     * 检查View
     *
     * @param view
     * @param callback
     */
    public void check(View view, Callback callback)
    {
        check(new View[]{view}, callback);
    }

    /**
     * 检查View
     *
     * @param views
     * @param callback
     */
    public void check(View[] views, Callback callback)
    {
        destroy();

        if (views == null || views.length <= 0)
            return;

        mCallback = callback;
        for (View view : views)
        {
            if (view == null)
                continue;

            if (mViewHolder.containsKey(view))
                continue;

            mViewHolder.put(view, "");

            view.addOnAttachStateChangeListener(mOnAttachStateChangeListener);
            view.addOnLayoutChangeListener(mOnLayoutChangeListener);
        }

        startCheckSize();
    }

    private final View.OnAttachStateChangeListener mOnAttachStateChangeListener = new View.OnAttachStateChangeListener()
    {
        @Override
        public void onViewAttachedToWindow(View v)
        {
        }

        @Override
        public void onViewDetachedFromWindow(View v)
        {
            startCheckSize();
        }
    };

    private final View.OnLayoutChangeListener mOnLayoutChangeListener = new View.OnLayoutChangeListener()
    {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom)
        {
            startCheckSize();
        }
    };

    private void startCheckSize()
    {
        stopCheckSize();
        mHandler.postDelayed(mCheckSizeRunnable, mCheckDelay);
    }

    private void stopCheckSize()
    {
        mHandler.removeCallbacks(mCheckSizeRunnable);
    }

    private final Runnable mCheckSizeRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            boolean isReady = true;
            for (View view : mViewHolder.keySet())
            {
                if (!checkReady(view))
                {
                    isReady = false;
                    break;
                }
            }

            if (mIsReady != isReady)
            {
                mIsReady = isReady;
                onReadyChanged(isReady);

                if (mCallback != null)
                    mCallback.onReadyChanged(isReady);
            }
        }
    };

    /**
     * 检查view是否准备好
     *
     * @param view
     * @return
     */
    protected boolean checkReady(View view)
    {
        return view.getWidth() > 0 && view.getHeight() > 0 && isAttached(view);
    }

    protected void onReadyChanged(boolean isReady)
    {
    }

    /**
     * 销毁
     */
    public void destroy()
    {
        for (View view : mViewHolder.keySet())
        {
            view.removeOnAttachStateChangeListener(mOnAttachStateChangeListener);
            view.removeOnLayoutChangeListener(mOnLayoutChangeListener);
        }
        mViewHolder.clear();
        mIsReady = false;
        mCallback = null;

        stopCheckSize();
    }

    private static boolean isAttached(View view)
    {
        if (Build.VERSION.SDK_INT >= 19)
            return view.isAttachedToWindow();
        else
            return view.getWindowToken() != null;
    }

    public interface Callback
    {
        void onReadyChanged(boolean isReady);
    }
}
