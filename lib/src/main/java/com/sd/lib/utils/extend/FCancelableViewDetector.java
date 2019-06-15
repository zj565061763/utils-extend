package com.sd.lib.utils.extend;

import android.os.Build;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public abstract class FCancelableViewDetector
{
    private final View mView;
    private final int[] mLocation = new int[2];

    public FCancelableViewDetector(View view)
    {
        if (view == null)
            throw new IllegalArgumentException("view is null when create " + FCancelableViewDetector.class.getName());
        mView = view;
    }

    /**
     * 分发key事件
     *
     * @param event
     * @return
     */
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (mView.getVisibility() == View.VISIBLE && isAttached(mView))
        {
            switch (event.getAction())
            {
                case KeyEvent.ACTION_DOWN:
                    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
                    {
                        return onBackPressed();
                    }
                    break;

                default:
                    break;
            }
        }
        return false;
    }

    /**
     * 按下返回键回调
     *
     * @return true-消费掉此次返回事件
     */
    protected abstract boolean onBackPressed();

    /**
     * 分发触摸事件
     *
     * @param event
     * @return
     */
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        if (mView.getVisibility() == View.VISIBLE)
        {
            final boolean isViewUnder = isViewUnder(mView, (int) event.getRawX(), (int) event.getRawY(), mLocation);
            return onTouch(isViewUnder);
        }

        return false;
    }

    /**
     * 触摸事件回调
     *
     * @param inside true-触摸在View的范围之内，false-触摸在View的范围之外
     * @return true-消费掉此次返回事件
     */
    protected abstract boolean onTouch(boolean inside);

    private static boolean isViewUnder(View view, int x, int y, int[] outLocation)
    {
        if (view == null)
            return false;

        if (!isAttached(view))
            return false;

        final int[] location = getLocationOnScreen(view, outLocation);
        final int left = location[0];
        final int top = location[1];
        final int right = left + view.getWidth();
        final int bottom = top + view.getHeight();

        return left < right && top < bottom
                && x >= left && x < right && y >= top && y < bottom;
    }

    private static int[] getLocationOnScreen(View view, int[] outLocation)
    {
        if (outLocation == null || outLocation.length != 2)
            outLocation = new int[]{0, 0};

        if (view != null)
            view.getLocationOnScreen(outLocation);

        return outLocation;
    }

    private static boolean isAttached(View view)
    {
        if (view == null)
            return false;

        if (Build.VERSION.SDK_INT >= 19)
            return view.isAttachedToWindow();
        else
            return view.getWindowToken() != null;
    }
}
