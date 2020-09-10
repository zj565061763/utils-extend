package com.sd.lib.utils.extend;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class FViewSizeReady
{
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Map<View, String> mViewHolder = new ConcurrentHashMap<>();
    private boolean mIsReady;

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
     * 设置view
     *
     * @param views
     */
    public void setView(View... views)
    {
        destroy();

        if (views == null || views.length <= 0)
            return;

        for (View view : views)
        {
            if (mViewHolder.containsKey(view))
                continue;

            mViewHolder.put(view, "");

            view.addOnAttachStateChangeListener(mOnAttachStateChangeListener);
            view.addOnLayoutChangeListener(mOnLayoutChangeListener);
        }

        startCheckSize();
    }

    /**
     * 检查view是否准备好
     *
     * @param view
     * @return
     */
    protected boolean checkReady(View view)
    {
        return view.getWidth() > 0 && view.getHeight() > 0;
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
        mHandler.postDelayed(mCheckSizeRunnable, 500);
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
            }
        }
    };

    protected abstract void onReadyChanged(boolean isReady);

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

        stopCheckSize();
    }
}
