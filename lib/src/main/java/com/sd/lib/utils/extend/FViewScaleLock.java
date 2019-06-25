package com.sd.lib.utils.extend;

import android.view.View;
import android.view.ViewGroup;

/**
 * 锁定某个View的宽高比
 */
public class FViewScaleLock
{
    private final float mWHScale;
    private final ScaleSideHandler mSideHandler;

    private View mView;

    public FViewScaleLock(ScaleSide scaleSide, int width, int height)
    {
        this(scaleSide, (float) width / height);
    }

    public FViewScaleLock(ScaleSide scaleSide, float whScale)
    {
        if (scaleSide == null)
            throw new IllegalArgumentException("scaleSide is null");

        if (whScale <= 0)
            throw new IllegalArgumentException("whScale is out of range (whScale > 0)");

        mWHScale = whScale;
        mSideHandler = scaleSide == ScaleSide.Width ? new WidthSideHandler() : new HeightSideHandler();
    }

    /**
     * 设置要处理的View
     *
     * @param view
     */
    public void setView(View view)
    {
        final View old = mView;
        if (old != view)
        {
            if (old != null)
                old.removeOnLayoutChangeListener(mOnLayoutChangeListener);

            mView = view;

            if (mView != null)
            {
                mView.addOnLayoutChangeListener(mOnLayoutChangeListener);
                mSideHandler.scaleIfNeed();
            }
        }
    }

    private final View.OnLayoutChangeListener mOnLayoutChangeListener = new View.OnLayoutChangeListener()
    {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom)
        {
            mSideHandler.scaleIfNeed();
        }
    };

    /**
     * 要缩放的边
     */
    public enum ScaleSide
    {
        Width,
        Height
    }

    private abstract class ScaleSideHandler
    {
        public void scaleIfNeed()
        {
            final ViewGroup.LayoutParams params = mView.getLayoutParams();
            if (params == null)
                return;

            final int otherSize = getOtherSideSize();
            if (otherSize == 0)
            {
                final int otherParamsSize = getOtherSideParamsSize(params);
                if (otherParamsSize == 0)
                {
                    final int currentParamsSize = getCurrentSideParamsSize(params);
                    if (currentParamsSize != 0)
                    {
                        setCurrentSideParamsSize(params, 0);
                        setLayoutParams(params);
                    }
                }
                return;
            }

            doScale(otherSize, params);
        }

        protected final void setLayoutParams(final ViewGroup.LayoutParams params)
        {
            final View view = mView;
            view.post(new Runnable()
            {
                @Override
                public void run()
                {
                    view.setLayoutParams(params);
                }
            });
        }

        protected abstract void doScale(int otherSize, ViewGroup.LayoutParams params);

        protected abstract int getOtherSideSize();

        protected abstract int getOtherSideParamsSize(ViewGroup.LayoutParams params);

        protected abstract int getCurrentSideParamsSize(ViewGroup.LayoutParams params);

        protected abstract void setCurrentSideParamsSize(ViewGroup.LayoutParams params, int size);
    }

    private final class WidthSideHandler extends ScaleSideHandler
    {
        @Override
        protected void doScale(int otherSize, ViewGroup.LayoutParams params)
        {
            final int width = (int) (mWHScale * otherSize);
            final int currentParamsSize = getCurrentSideParamsSize(params);
            if (currentParamsSize != width)
            {
                setCurrentSideParamsSize(params, width);
                setLayoutParams(params);
            }
        }

        @Override
        protected int getOtherSideSize()
        {
            return mView.getHeight();
        }

        @Override
        protected int getOtherSideParamsSize(ViewGroup.LayoutParams params)
        {
            return params.height;
        }

        @Override
        protected int getCurrentSideParamsSize(ViewGroup.LayoutParams params)
        {
            return params.width;
        }

        @Override
        protected void setCurrentSideParamsSize(ViewGroup.LayoutParams params, int size)
        {
            params.width = size;
        }
    }

    private final class HeightSideHandler extends ScaleSideHandler
    {
        @Override
        protected void doScale(int otherSize, ViewGroup.LayoutParams params)
        {
            final int height = (int) (otherSize / mWHScale);
            final int currentParamsSize = getCurrentSideParamsSize(params);
            if (currentParamsSize != height)
            {
                setCurrentSideParamsSize(params, height);
                setLayoutParams(params);
            }
        }

        @Override
        protected int getOtherSideSize()
        {
            return mView.getWidth();
        }

        @Override
        protected int getOtherSideParamsSize(ViewGroup.LayoutParams params)
        {
            return params.width;
        }

        @Override
        protected int getCurrentSideParamsSize(ViewGroup.LayoutParams params)
        {
            return params.height;
        }

        @Override
        protected void setCurrentSideParamsSize(ViewGroup.LayoutParams params, int size)
        {
            params.height = size;
        }
    }
}
