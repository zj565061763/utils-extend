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
    private int mVisibility;

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
        protected void onRegisterStateChanged(boolean register)
        {
            super.onRegisterStateChanged(register);
        }

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
        onVisibilityChanged(mVisibility, view);
    }

    /**
     * View的可见状态发生变化
     *
     * @param visibility
     * @param view
     */
    protected abstract void onVisibilityChanged(int visibility, View view);
}
