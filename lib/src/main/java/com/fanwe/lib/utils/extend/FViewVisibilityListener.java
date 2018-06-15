package com.fanwe.lib.utils.extend;

import android.view.View;

/**
 * view的显示隐藏监听
 */
public abstract class FViewVisibilityListener
{
    /**
     * 当前view的visibility状态
     */
    private int mVisibility = View.VISIBLE;

    /**
     * 获得设置的view
     *
     * @return
     */
    public final View getView()
    {
        return mOnPreDrawListener.getView();
    }

    /**
     * 设置要监听的view
     *
     * @param view
     */
    public final void setView(View view)
    {
        mOnPreDrawListener.setView(view);
        mOnPreDrawListener.register();
    }

    private final FOnPreDrawListener mOnPreDrawListener = new FOnPreDrawListener()
    {
        @Override
        protected void onRegisterChanged(boolean register)
        {
            super.onRegisterChanged(register);
            notifyIfNeed();
        }

        @Override
        public boolean onPreDraw()
        {
            notifyIfNeed();
            return true;
        }
    };

    private void notifyIfNeed()
    {
        final View view = getView();
        if (view == null)
            return;

        final int visibility = view.getVisibility();
        if (mVisibility != visibility)
        {
            mVisibility = visibility;
            onVisibilityChanged(visibility, view);
        }
    }

    /**
     * View的可见状态发生变化
     *
     * @param visibility
     * @param view
     */
    public abstract void onVisibilityChanged(int visibility, View view);
}
