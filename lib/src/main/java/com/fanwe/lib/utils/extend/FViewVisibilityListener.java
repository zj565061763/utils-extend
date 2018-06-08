package com.fanwe.lib.utils.extend;

import android.view.View;
import android.view.ViewTreeObserver;

import java.lang.ref.WeakReference;

/**
 * view的显示隐藏监听
 */
public abstract class FViewVisibilityListener
{
    private WeakReference<View> mView;
    /**
     * 当前view的visibility状态
     */
    private int mVisibility;

    /**
     * 获得设置的view
     *
     * @return
     */
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
                final ViewTreeObserver observer = view.getViewTreeObserver();
                if (observer.isAlive())
                    observer.removeOnPreDrawListener(mOnPreDrawListener);
            }

            mView = view == null ? null : new WeakReference<>(view);

            if (view != null)
            {
                mVisibility = view.getVisibility();

                final ViewTreeObserver observer = view.getViewTreeObserver();
                if (observer.isAlive())
                    observer.addOnPreDrawListener(mOnPreDrawListener);
            }
        }
    }

    private final ViewTreeObserver.OnPreDrawListener mOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener()
    {
        @Override
        public boolean onPreDraw()
        {
            final View view = getView();
            if (view != null)
            {
                if (mVisibility != view.getVisibility())
                    notifyVisiblityChanged();
            }

            return true;
        }
    };

    public final void notifyVisiblityChanged()
    {
        final View view = getView();
        if (view == null)
            return;

        mVisibility = view.getVisibility();
        onViewVisibilityChanged(view, mVisibility);
    }

    /**
     * View的可见状态发生变化
     *
     * @param view
     * @param visibility
     */
    protected abstract void onViewVisibilityChanged(View view, int visibility);
}
