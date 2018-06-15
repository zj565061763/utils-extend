package com.fanwe.lib.utils.extend;

import android.view.View;
import android.view.ViewTreeObserver;

import java.lang.ref.WeakReference;

public abstract class FOnPreDrawListener implements ViewTreeObserver.OnPreDrawListener
{
    private WeakReference<View> mView;
    private boolean mIsRegister;

    /**
     * 返回设置的view
     *
     * @return
     */
    public final View getView()
    {
        return mView == null ? null : mView.get();
    }

    /**
     * 设置要监听的view
     * <br>
     * 注意如果view不为null的话，当前对象会被view持有，如果要释放当前对象，传入的view为null既可
     *
     * @param view
     */
    public final void setView(View view)
    {
        final View old = getView();
        if (old != view)
        {
            unregister();
            if (old != null)
                old.removeOnAttachStateChangeListener(mOnAttachStateChangeListener);

            mView = view == null ? null : new WeakReference<>(view);

            if (view != null)
                view.addOnAttachStateChangeListener(mOnAttachStateChangeListener);
        }
    }

    private final View.OnAttachStateChangeListener mOnAttachStateChangeListener = new View.OnAttachStateChangeListener()
    {
        @Override
        public void onViewAttachedToWindow(View v)
        {
            register();
        }

        @Override
        public void onViewDetachedFromWindow(View v)
        {
        }
    };

    /**
     * 是否已经注册监听
     *
     * @return
     */
    public final boolean isRegister()
    {
        return mIsRegister;
    }

    private void setRegister(boolean register)
    {
        if (mIsRegister != register)
        {
            mIsRegister = register;
            onStateChanged(register);
        }
    }

    private ViewTreeObserver getViewTreeObserver()
    {
        final View view = getView();
        if (view != null)
        {
            final ViewTreeObserver observer = view.getViewTreeObserver();
            if (observer.isAlive())
                return observer;
        }
        setRegister(false);
        return null;
    }

    /**
     * 开始监听
     */
    public final void register()
    {
        final ViewTreeObserver observer = getViewTreeObserver();
        if (observer.isAlive())
        {
            observer.removeOnPreDrawListener(this);
            observer.addOnPreDrawListener(this);
            setRegister(true);
        }
    }

    /**
     * 停止监听
     */
    public final void unregister()
    {
        final ViewTreeObserver observer = getViewTreeObserver();
        if (observer.isAlive())
            observer.removeOnPreDrawListener(this);

        setRegister(false);
    }

    protected void onStateChanged(boolean register)
    {
    }
}
