package com.sd.lib.utils.extend;

import android.view.View;
import android.view.ViewGroup;

/**
 * 锁定某个View的宽高比例
 */
public class FViewScaleLock {
    private final ScaleSide mScaleSide;

    private float mWHScale;
    private View mView;

    private View mContainer;
    private int mContainerWidth;
    private int mContainerHeight;

    public FViewScaleLock(ScaleSide scaleSide) {
        if (scaleSide == null)
            throw new IllegalArgumentException("scaleSide is null");
        mScaleSide = scaleSide;
    }

    /**
     * 设置宽高比例
     *
     * @param width
     * @param height
     */
    public void setWHScale(float width, float height) {
        if (width <= 0 || height <= 0) {
            setWHScale(0);
            return;
        }

        setWHScale(width / height);
    }

    /**
     * 设置宽高比例
     *
     * @param whScale
     */
    public void setWHScale(float whScale) {
        if (mWHScale != whScale) {
            mWHScale = whScale;
            scale();
        }
    }

    /**
     * 设置容器大小
     *
     * @param width
     * @param height
     */
    public void setContainer(int width, int height) {
        if (mContainerWidth != width || mContainerHeight != height) {
            mContainerWidth = width;
            mContainerHeight = height;
            scale();
        }
    }

    /**
     * 设置容器大小
     *
     * @param container
     */
    public void setContainer(View container) {
        final View old = mContainer;
        if (old != container) {
            if (old != null)
                old.removeOnLayoutChangeListener(mOnLayoutChangeListenerContainer);

            mContainer = container;

            if (mContainer != null) {
                mContainer.addOnLayoutChangeListener(mOnLayoutChangeListenerContainer);
                scale();
            }
        }
    }

    private final View.OnLayoutChangeListener mOnLayoutChangeListenerContainer = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            setContainer(v.getWidth(), v.getHeight());
        }
    };

    /**
     * 设置要缩放的View
     *
     * @param view
     */
    public void setView(View view) {
        final View old = mView;
        if (old != view) {
            if (old != null)
                old.removeOnLayoutChangeListener(mOnLayoutChangeListener);

            mView = view;

            if (mView != null) {
                mView.addOnLayoutChangeListener(mOnLayoutChangeListener);
                scale();
            }
        }
    }

    private final View.OnLayoutChangeListener mOnLayoutChangeListener = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            scale();
        }
    };

    private boolean checkParams() {
        if (mWHScale <= 0)
            return false;

        if (mView == null || mView.getLayoutParams() == null)
            return false;

        return true;
    }

    private void scale() {
        if (checkParams()) {
            switch (mScaleSide) {
                case Width:
                    scaleWidth();
                    break;
                case Height:
                    scaleHeight();
                    break;
                default:
                    break;
            }
        }
    }

    private void scaleWidth() {
        final int width = mView.getWidth();
        final int height = mView.getHeight();

        if (height <= 0)
            return;

        final int scaleWidth = getScaleWidth(mWHScale, height);
        if (mContainerWidth > 0 && scaleWidth > mContainerWidth) {
            // 修正宽高
            final int fixWidth = mContainerWidth;
            final int fixHeight = getScaleHeight(mWHScale, fixWidth);
            updateViewSize(fixWidth, fixHeight);
            return;
        }

        if (width != scaleWidth) {
            updateViewWidth(scaleWidth);
        }
    }

    private void scaleHeight() {
        final int width = mView.getWidth();
        final int height = mView.getHeight();

        if (width <= 0)
            return;

        final int scaleHeight = getScaleHeight(mWHScale, width);
        if (mContainerHeight > 0 && scaleHeight > mContainerHeight) {
            // 修正宽高
            final int fixHeight = mContainerHeight;
            final int fixWidth = getScaleWidth(mWHScale, fixHeight);
            updateViewSize(fixWidth, fixHeight);
            return;
        }

        if (height != scaleHeight) {
            updateViewHeight(scaleHeight);
        }
    }

    private void updateViewWidth(final int scaleWidth) {
        if (checkParams()) {
            final int height = mView.getLayoutParams().height;
            updateViewSize(scaleWidth, height);
        }
    }

    private void updateViewHeight(final int scaleHeight) {
        if (checkParams()) {
            final int width = mView.getLayoutParams().width;
            updateViewSize(width, scaleHeight);
        }
    }

    private void updateViewSize(final int width, final int height) {
        mView.post(new Runnable() {
            @Override
            public void run() {
                if (checkParams()) {
                    final ViewGroup.LayoutParams params = mView.getLayoutParams();
                    if (params.width != width || params.height != height) {
                        params.width = width;
                        params.height = height;
                        mView.setLayoutParams(params);
                    }
                }
            }
        });
    }

    /**
     * 要缩放的边
     */
    public enum ScaleSide {
        Width,
        Height
    }

    private static int getScaleWidth(float whScale, int height) {
        final float size = (whScale * height) + 0.5f;
        return (int) size;
    }

    private static int getScaleHeight(float whScale, int width) {
        final float size = (width / whScale) + 0.5f;
        return (int) size;
    }
}
