package com.sd.lib.utils.extend;

import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

/**
 * View大小追踪，让源View追踪目标View的大小
 */
public class FViewSizeTracker {
    private final BoundaryHandler mBoundaryHandler;
    private final ViewGroup.LayoutParams mLayoutParams = new ViewGroup.LayoutParams(0, 0);

    private WeakReference<View> mSource;
    private WeakReference<View> mTarget;

    public FViewSizeTracker() {
        this(Boundary.both);
    }

    public FViewSizeTracker(Boundary boundary) {
        if (boundary == null)
            throw new NullPointerException("boundary is null");

        switch (boundary) {
            case width:
                mBoundaryHandler = new WidthHandler();
                break;
            case height:
                mBoundaryHandler = new HeightHandler();
                break;
            case both:
                mBoundaryHandler = new BothHandler();
                break;
            default:
                throw new RuntimeException("unknown boundary:" + boundary);
        }
    }

    public final View getSource() {
        return mSource == null ? null : mSource.get();
    }

    public final View getTarget() {
        return mTarget == null ? null : mTarget.get();
    }

    /**
     * 设置源View
     *
     * @param source
     */
    public final void setSource(View source) {
        final View old = getSource();
        if (old != source) {
            mSource = source == null ? null : new WeakReference<>(source);
            checkSize();
        }
    }

    /**
     * 设置目标View
     *
     * @param target
     */
    public final void setTarget(View target) {
        final View old = getTarget();
        if (old != target) {
            if (old != null)
                old.removeOnLayoutChangeListener(mOnLayoutChangeListener);

            mTarget = target == null ? null : new WeakReference<>(target);

            if (target != null)
                target.addOnLayoutChangeListener(mOnLayoutChangeListener);

            checkSize();
        }
    }

    private final View.OnLayoutChangeListener mOnLayoutChangeListener = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            checkSize();
        }
    };

    private void checkSize() {
        final View source = getSource();
        if (source == null)
            return;

        final View target = getTarget();
        if (target == null)
            return;

        if (!mBoundaryHandler.isTargetReady(target))
            return;

        if (mBoundaryHandler.isSizeEquals(target, source))
            return;

        final ViewGroup.LayoutParams layoutParams = source.getLayoutParams();
        if (layoutParams == null)
            return;

        final ViewGroup.LayoutParams tempParams = mLayoutParams;
        tempParams.width = layoutParams.width;
        tempParams.height = layoutParams.height;

        if (mBoundaryHandler.updateSourceLayoutParams(target, source, tempParams)) {
            updateSourceSize(source, tempParams.width, tempParams.height);
        }
    }

    protected void updateSourceSize(View source, int width, int height) {
        final ViewGroup.LayoutParams layoutParams = source.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        source.setLayoutParams(layoutParams);
    }

    private static abstract class BoundaryHandler {
        public abstract boolean isTargetReady(View target);

        public abstract boolean isSizeEquals(View target, View source);

        public abstract boolean updateSourceLayoutParams(View target, View source, ViewGroup.LayoutParams layoutParams);
    }

    private static final class WidthHandler extends BoundaryHandler {
        @Override
        public boolean isTargetReady(View target) {
            return target.getWidth() > 0;
        }

        @Override
        public boolean isSizeEquals(View target, View source) {
            return target.getWidth() == source.getWidth();
        }

        @Override
        public boolean updateSourceLayoutParams(View target, View source, ViewGroup.LayoutParams layoutParams) {
            final int width = target.getWidth();
            if (layoutParams.width != width) {
                layoutParams.width = width;
                return true;
            }
            return false;
        }
    }

    private static final class HeightHandler extends BoundaryHandler {
        @Override
        public boolean isTargetReady(View target) {
            return target.getHeight() > 0;
        }

        @Override
        public boolean isSizeEquals(View target, View source) {
            return target.getHeight() == source.getHeight();
        }

        @Override
        public boolean updateSourceLayoutParams(View target, View source, ViewGroup.LayoutParams layoutParams) {
            final int height = target.getHeight();
            if (layoutParams.height != height) {
                layoutParams.height = height;
                return true;
            }
            return false;
        }
    }

    private static final class BothHandler extends BoundaryHandler {
        private final WidthHandler mWidthHandler = new WidthHandler();
        private final HeightHandler mHeightHandler = new HeightHandler();

        @Override
        public boolean isTargetReady(View target) {
            return mWidthHandler.isTargetReady(target) &&
                    mHeightHandler.isTargetReady(target);
        }

        @Override
        public boolean isSizeEquals(View target, View source) {
            return mWidthHandler.isSizeEquals(target, source) &&
                    mHeightHandler.isSizeEquals(target, source);
        }

        @Override
        public boolean updateSourceLayoutParams(View target, View source, ViewGroup.LayoutParams layoutParams) {
            final boolean width = mWidthHandler.updateSourceLayoutParams(target, source, layoutParams);
            final boolean height = mHeightHandler.updateSourceLayoutParams(target, source, layoutParams);
            return width || height;
        }
    }

    public enum Boundary {
        width,
        height,
        both
    }
}
