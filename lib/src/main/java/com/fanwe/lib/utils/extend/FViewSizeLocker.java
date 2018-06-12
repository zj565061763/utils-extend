package com.fanwe.lib.utils.extend;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * 锁定和解锁view宽高的类
 */
public class FViewSizeLocker
{
    private View mView;
    /**
     * 锁定前的宽度
     */
    private int mOriginalWidth;
    /**
     * 锁定前的高度
     */
    private int mOriginalHeight;
    /**
     * 锁定前的weight
     */
    private float mOriginalWeight;

    /**
     * 已锁定的宽度
     */
    private int mLockWidth;
    /**
     * 已锁定的高度
     */
    private int mLockHeight;

    /**
     * 宽度是否已经被锁住
     */
    private boolean mIsWidthLocked;
    /**
     * 高度是否已经被锁住
     */
    private boolean mIsHeightLocked;

    /**
     * 设置要处理的view
     *
     * @param view
     */
    public void setView(View view)
    {
        if (mView != view)
        {
            reset();
            mView = view;
        }
    }

    public View getView()
    {
        return mView;
    }

    private void reset()
    {
        mOriginalWidth = 0;
        mOriginalHeight = 0;
        mOriginalWeight = 0;

        mLockWidth = 0;
        mLockHeight = 0;

        mIsWidthLocked = false;
        mIsHeightLocked = false;
    }

    /**
     * 返回锁定前的宽度
     *
     * @return
     */
    public int getOriginalWidth()
    {
        return mOriginalWidth;
    }

    /**
     * 返回锁定前的高度
     *
     * @return
     */
    public int getOriginalHeight()
    {
        return mOriginalHeight;
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
     * 返回已锁定的宽度
     *
     * @return
     */
    public int getLockWidth()
    {
        return mLockWidth;
    }

    /**
     * 返回已锁定的高度
     *
     * @return
     */
    public int getLockHeight()
    {
        return mLockHeight;
    }

    /**
     * 宽度是否已经被锁住
     */
    public boolean isWidthLocked()
    {
        return mIsWidthLocked;
    }

    /**
     * 高度是否已经被锁住
     *
     * @return
     */
    public boolean isHeightLocked()
    {
        return mIsHeightLocked;
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
     * 锁定宽度
     *
     * @param lockWidth 要锁定的宽度
     */
    public void lockWidth(int lockWidth)
    {
        final ViewGroup.LayoutParams params = getLayoutParams();
        if (params == null)
            return;

        if (mIsWidthLocked)
        {
            //如果已经锁了，直接赋值
            params.width = lockWidth;
        } else
        {
            if (params instanceof LinearLayout.LayoutParams)
            {
                LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) params;
                mOriginalWeight = lParams.weight;
                lParams.weight = 0;
            }

            mOriginalWidth = params.width;
            params.width = lockWidth;
            mIsWidthLocked = true;
        }
        setLayoutParams(params);
    }

    /**
     * 解锁宽度
     */
    public void unlockWidth()
    {
        if (mIsWidthLocked)
        {
            ViewGroup.LayoutParams params = getLayoutParams();
            if (params != null)
            {
                if (params instanceof LinearLayout.LayoutParams)
                {
                    LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) params;
                    lParams.weight = mOriginalWeight;
                }
                params.width = mOriginalWidth;
                setLayoutParams(params);
            }
            mIsWidthLocked = false;
        }
    }

    /**
     * 锁定高度
     *
     * @param lockHeight 要锁定的高度
     */
    public void lockHeight(int lockHeight)
    {
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params == null)
        {
            return;
        }

        if (mIsHeightLocked)
        {
            //如果已经锁了，直接赋值
            params.height = lockHeight;
        } else
        {
            if (params instanceof LinearLayout.LayoutParams)
            {
                LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) params;
                mOriginalWeight = lParams.weight;
                lParams.weight = 0;
            }

            mOriginalHeight = params.height;
            params.height = lockHeight;
            mIsHeightLocked = true;
        }
        setLayoutParams(params);

    }

    /**
     * 解锁高度
     */
    public void unlockHeight()
    {
        if (mIsHeightLocked)
        {
            ViewGroup.LayoutParams params = getLayoutParams();
            if (params != null)
            {
                if (params instanceof LinearLayout.LayoutParams)
                {
                    LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) params;
                    lParams.weight = mOriginalWeight;
                }
                params.height = mOriginalHeight;
                setLayoutParams(params);
            }
            mIsHeightLocked = false;
        }
    }

}
