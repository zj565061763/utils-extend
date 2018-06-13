package com.fanwe.lib.utils.extend;

import android.view.View;
import android.view.ViewTreeObserver;

import java.lang.ref.WeakReference;

/**
 * 监听view宽高变化
 */
public abstract class FViewSizeListener
{
    private WeakReference<View> mView;
    private int mWidth;
    private int mHeight;

    public final View getView()
    {
        return mView == null ? null : mView.get();
    }

    /**
     * 设置要监听的view
     *
     * @param view
     */
    public final void setView(View view)
    {
        final View old = getView();
        if (old != view)
        {
            if (old != null)
            {
                final ViewTreeObserver observer = old.getViewTreeObserver();
                if (observer.isAlive())
                    observer.removeGlobalOnLayoutListener(mOnGlobalLayoutListener);
            }

            reset();
            mView = view == null ? null : new WeakReference<>(view);

            if (view != null)
            {
                final ViewTreeObserver observer = view.getViewTreeObserver();
                if (observer.isAlive())
                    observer.addOnGlobalLayoutListener(mOnGlobalLayoutListener);
            }
        }
    }

    private void reset()
    {
        mWidth = 0;
        mHeight = 0;
    }

    private final ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener()
    {
        @Override
        public void onGlobalLayout()
        {
            process();
        }
    };

    private void process()
    {
        final View view = getView();
        if (view == null)
            return;

        final int oldWidth = mWidth;
        final int oldHeight = mHeight;

        final int newWidth = onGetWidth(view);
        final int newHeight = onGetHeight(view);

        if (newWidth != oldWidth)
        {
            mWidth = newWidth;
            onWidthChanged(newWidth, oldWidth, view);
        }

        if (newHeight != oldHeight)
        {
            mHeight = newHeight;
            onHeightChanged(newHeight, oldHeight, view);
        }
    }

    protected int onGetWidth(View view)
    {
        return view.getWidth();
    }

    protected int onGetHeight(View view)
    {
        return view.getHeight();
    }

    protected abstract void onWidthChanged(int newWidth, int oldWidth, View view);

    protected abstract void onHeightChanged(int newHeight, int oldHeight, View view);
}
