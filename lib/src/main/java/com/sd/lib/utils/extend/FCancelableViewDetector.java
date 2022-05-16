package com.sd.lib.utils.extend;

import android.os.Build;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public abstract class FCancelableViewDetector {
    private final View mView;
    private int[] mLocation;

    public FCancelableViewDetector(View view) {
        if (view == null)
            throw new IllegalArgumentException("view is null when create " + FCancelableViewDetector.class.getName());
        mView = view;
    }

    private int[] getLocation() {
        if (mLocation == null)
            mLocation = new int[2];
        return mLocation;
    }

    /**
     * 分发key事件
     *
     * @param event
     * @return
     */
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (mView.getVisibility() != View.VISIBLE)
            return false;

        if (!isAttached(mView))
            return false;

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                return onBackPressed();
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
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (mView.getVisibility() != View.VISIBLE)
            return false;

        if (!isAttached(mView))
            return false;

        final boolean isViewUnder = isViewUnder(mView, (int) event.getRawX(), (int) event.getRawY(), getLocation());
        return onTouch(isViewUnder);
    }

    /**
     * 触摸事件回调
     *
     * @param inside true-触摸在View的范围之内，false-触摸在View的范围之外
     * @return true-消费掉此次返回事件
     */
    protected boolean onTouch(boolean inside) {
        return false;
    }

    private static boolean isViewUnder(View view, int x, int y, int[] location) {
        view.getLocationOnScreen(location);

        final int left = location[0];
        final int top = location[1];
        final int right = left + view.getWidth();
        final int bottom = top + view.getHeight();

        return left < right && top < bottom
                && x >= left && x < right && y >= top && y < bottom;
    }

    private static boolean isAttached(View view) {
        if (Build.VERSION.SDK_INT >= 19)
            return view.isAttachedToWindow();
        else
            return view.getWindowToken() != null;
    }
}
