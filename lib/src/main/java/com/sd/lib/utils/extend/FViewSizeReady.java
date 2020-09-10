package com.sd.lib.utils.extend;

import android.view.View;

import java.util.Map;
import java.util.WeakHashMap;

public abstract class FViewSizeReady
{
    private final Map<View, Boolean> mViewHolder = new WeakHashMap<>();
    private int mReadySize;
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

            final boolean ready = checkReady(view);
            mViewHolder.put(view, ready);

            if (ready)
                mReadySize++;

            view.addOnAttachStateChangeListener(mOnAttachStateChangeListener);
            view.addOnLayoutChangeListener(mOnLayoutChangeListener);
        }

        notifyReadyIfNeed();
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
            mViewHolder.put(v, false);
            mReadySize--;
            notifyReadyIfNeed();
        }
    };

    private final View.OnLayoutChangeListener mOnLayoutChangeListener = new View.OnLayoutChangeListener()
    {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom)
        {
            final boolean oldReady = mViewHolder.get(v);
            final boolean ready = checkReady(v);
            if (ready != oldReady)
            {
                mViewHolder.put(v, ready);
                if (ready)
                {
                    mReadySize++;
                    notifyReadyIfNeed();
                } else
                {
                    mReadySize--;
                    notifyReadyIfNeed();
                }
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
        return view.getWidth() > 0 && view.getHeight() > 0;
    }

    private void notifyReadyIfNeed()
    {
        final boolean isReady = mReadySize > 0 && mReadySize == mViewHolder.size();
        if (mIsReady != isReady)
        {
            mIsReady = isReady;
            onReadyChanged(isReady);
        }
    }

    protected abstract void onReadyChanged(boolean isReady);

    /**
     * 销毁
     */
    public void destroy()
    {
        if (mViewHolder.isEmpty())
            return;

        for (View view : mViewHolder.keySet())
        {
            view.removeOnAttachStateChangeListener(mOnAttachStateChangeListener);
            view.removeOnLayoutChangeListener(mOnLayoutChangeListener);
        }
        mViewHolder.clear();
        mReadySize = 0;
        mIsReady = false;
    }
}
