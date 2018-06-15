package com.fanwe.lib.utils.extend;

import android.view.View;

/**
 * view的选中状态变化监听
 *
 * @param <T>
 */
public abstract class FViewSelectionListener<T extends View>
{
    private boolean mSelected;

    public final T getView()
    {
        return (T) mOnPreDrawListener.getView();
    }

    public final void setView(T view)
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
            if (register)
                notifyStateIfNeed();
        }

        @Override
        public boolean onPreDraw()
        {
            notifyStateIfNeed();
            return true;
        }
    };

    private void notifyStateIfNeed()
    {
        final T view = getView();
        if (view == null)
            return;

        final boolean selected = view.isSelected();
        if (mSelected != selected)
        {
            mSelected = selected;
            view.post(new Runnable()
            {
                @Override
                public void run()
                {
                    onSelectionChanged(selected, view);
                }
            });
        }
    }

    protected abstract void onSelectionChanged(boolean selected, T view);
}
