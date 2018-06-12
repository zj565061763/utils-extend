package com.fanwe.lib.utils.extend;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;

/**
 * 对view的宽或者高进行锁定和解锁
 */
public class FViewSizeLocker
{
    private WeakReference<View> mView;
    /**
     * 锁定前的宽度
     */
    private int mOriginalSize;
    /**
     * 锁定前的weight
     */
    private float mOriginalWeight;
    /**
     * 已锁定的宽度
     */
    private int mLockSize;
    /**
     * 宽度是否已经被锁住
     */
    private boolean mIsLocked;

    private final Parameter mParameter;

    public FViewSizeLocker(Boundary boundary)
    {
        if (boundary == null)
            throw new NullPointerException("boundary is null");

        mParameter = boundary == Boundary.Width ? new WidthParameter() : new HeightParameter();
    }

    /**
     * 设置要处理的view
     *
     * @param view
     */
    public final void setView(View view)
    {
        final View old = getView();
        if (old != view)
        {
            reset();
            mView = view == null ? null : new WeakReference<>(view);
        }
    }

    public final View getView()
    {
        return mView == null ? null : mView.get();
    }

    private void reset()
    {
        mOriginalSize = 0;
        mOriginalWeight = 0;
        mLockSize = 0;
        mIsLocked = false;
    }

    /**
     * 返回锁定前的宽度
     *
     * @return
     */
    public int getOriginalSize()
    {
        return mOriginalSize;
    }

    /**
     * 返回锁定前的weight
     *
     * @return
     */
    public float getOriginalWeight()
    {
        return mOriginalWeight;
    }

    /**
     * 返回已锁定的大小
     *
     * @return
     */
    public int getLockSize()
    {
        return mLockSize;
    }

    /**
     * 宽度是否已经被锁住
     */
    public boolean isLocked()
    {
        return mIsLocked;
    }

    private ViewGroup.LayoutParams getLayoutParams()
    {
        final View view = getView();
        if (view == null)
            return null;

        return view.getLayoutParams();
    }

    private void setLayoutParams(ViewGroup.LayoutParams params)
    {
        final View view = getView();
        if (view == null)
            return;

        view.setLayoutParams(params);
    }

    /**
     * 锁定
     *
     * @param size 要锁定的大小
     */
    public final void lock(int size)
    {
        final View view = getView();
        if (view == null)
            return;

        final ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null)
            return;

        if (mIsLocked)
        {
            mParameter.setLayoutParamsSize(params, size);
        } else
        {
            if (params instanceof LinearLayout.LayoutParams)
            {
                final LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) params;
                mOriginalWeight = lParams.weight;
                lParams.weight = 0;
            }

            mOriginalSize = mParameter.getLayoutParamsSize(params);
            mParameter.setLayoutParamsSize(params, size);
            mIsLocked = true;
        }

        mLockSize = size;
        setLayoutParams(params);
    }

    /**
     * 解锁
     */
    public final void unlock()
    {
        if (!mIsLocked)
            return;

        mIsLocked = false;

        final View view = getView();
        if (view == null)
            return;

        final ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null)
            return;

        if (params instanceof LinearLayout.LayoutParams)
        {
            final LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) params;
            lParams.weight = mOriginalWeight;
        }

        mParameter.setLayoutParamsSize(params, mOriginalSize);
        setLayoutParams(params);
    }

    public enum Boundary
    {
        Width,
        Height
    }

    private interface Parameter
    {
        int getLayoutParamsSize(ViewGroup.LayoutParams params);

        void setLayoutParamsSize(ViewGroup.LayoutParams params, int size);
    }

    private static final class WidthParameter implements Parameter
    {
        @Override
        public int getLayoutParamsSize(ViewGroup.LayoutParams params)
        {
            return params.width;
        }

        @Override
        public void setLayoutParamsSize(ViewGroup.LayoutParams params, int size)
        {
            params.width = size;
        }
    }

    private static final class HeightParameter implements Parameter
    {
        @Override
        public int getLayoutParamsSize(ViewGroup.LayoutParams params)
        {
            return params.height;
        }

        @Override
        public void setLayoutParamsSize(ViewGroup.LayoutParams params, int size)
        {
            params.height = size;
        }
    }
}
