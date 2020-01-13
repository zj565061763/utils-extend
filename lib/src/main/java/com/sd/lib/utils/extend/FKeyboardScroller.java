package com.sd.lib.utils.extend;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

public class FKeyboardScroller
{
    private final View mView;
    private final View mScrollView;
    private final int mStatusBarHeight;

    private final Rect mRect = new Rect();
    private int mHeight;

    private Callback mCallback;

    public FKeyboardScroller(Activity activity, View scrollView)
    {
        if (activity == null || scrollView == null)
            throw new NullPointerException();

        final View view = activity.findViewById(android.R.id.content);
        if (view == null)
            throw new RuntimeException("view with id android.R.id.content was not found in:" + activity);

        mView = view;
        mScrollView = scrollView;
        mStatusBarHeight = getStatusBarHeight(activity);

        mView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener()
        {
            @Override
            public void onViewAttachedToWindow(View v)
            {
            }

            @Override
            public void onViewDetachedFromWindow(View v)
            {
                mView.removeCallbacks(mCheckRunnable);
            }
        });
    }

    public final void setCallback(Callback callback)
    {
        mCallback = callback;
    }

    public final boolean start()
    {
        final ViewTreeObserver observer = mView.getViewTreeObserver();
        if (observer.isAlive())
        {
            observer.removeOnPreDrawListener(mOnPreDrawListener);
            observer.addOnPreDrawListener(mOnPreDrawListener);
            return true;
        }
        return false;
    }

    public final void stop()
    {
        final ViewTreeObserver observer = mView.getViewTreeObserver();
        if (observer.isAlive())
            observer.removeOnPreDrawListener(mOnPreDrawListener);
    }

    private final ViewTreeObserver.OnPreDrawListener mOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener()
    {
        @Override
        public boolean onPreDraw()
        {
            processScroll();
            return true;
        }
    };

    private void processScroll()
    {
        mView.getWindowVisibleDisplayFrame(mRect);

        final int old = mHeight;
        final int height = mRect.height();
        if (height == old)
            return;

        mHeight = height;

        if (old > 0 && height > 0)
        {
            final int delta = old - height;
            if (Math.abs(delta) == mStatusBarHeight)
                return;

            mScrollView.scrollBy(0, delta);

            if (mCallback != null)
                mCallback.onScrolled(mScrollView, false);

            mView.postDelayed(mCheckRunnable, 100);
        }
    }

    private final Runnable mCheckRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            final int scrollY = mScrollView.getScrollY();
            if (scrollY < 0)
            {
                mScrollView.scrollTo(0, 0);
                if (mCallback != null)
                    mCallback.onScrolled(mScrollView, true);
            }
        }
    };

    private static int getStatusBarHeight(Context context)
    {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public interface Callback
    {
        void onScrolled(View scrollView, boolean isReset);
    }
}
