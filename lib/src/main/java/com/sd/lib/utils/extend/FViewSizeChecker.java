package com.sd.lib.utils.extend;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FViewSizeChecker
{
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Map<View, String> mViewHolder = new ConcurrentHashMap<>();

    private long mCheckDelay;
    private Callback mCallback;

    /**
     * 设置延迟多少毫秒开始检查
     *
     * @param delay
     */
    public void setCheckDelay(long delay)
    {
        if (delay < 0)
            delay = 0;
        mCheckDelay = delay;
    }

    /**
     * 检查View
     *
     * @param view
     * @param callback
     * @return true-成功发起检测，false-未发起检测
     */
    public boolean check(View view, Callback callback)
    {
        return check(new View[]{view}, callback);
    }

    /**
     * 检查View
     *
     * @param views
     * @param callback
     * @return true-成功发起检测，false-未发起检测
     */
    public boolean check(View[] views, Callback callback)
    {
        destroy();

        if (views == null || views.length <= 0)
            return false;

        for (View view : views)
        {
            if (view == null)
                continue;

            if (mViewHolder.containsKey(view))
                continue;

            mViewHolder.put(view, "");
            view.addOnLayoutChangeListener(mOnLayoutChangeListener);
        }

        if (mViewHolder.size() > 0)
        {
            mCallback = callback;
            startCheckSize();
            return true;
        } else
        {
            return false;
        }
    }

    private final View.OnLayoutChangeListener mOnLayoutChangeListener = new View.OnLayoutChangeListener()
    {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom)
        {
            startCheckSize();
        }
    };

    private void startCheckSize()
    {
        stopCheckSize();
        mHandler.postDelayed(mCheckSizeRunnable, mCheckDelay);
    }

    private void stopCheckSize()
    {
        mHandler.removeCallbacks(mCheckSizeRunnable);
    }

    private final Runnable mCheckSizeRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            boolean isReady = true;
            for (View view : mViewHolder.keySet())
            {
                if (!checkReady(view))
                {
                    isReady = false;
                    break;
                }
            }

            if (isReady)
            {
                final Callback callback = mCallback;
                destroy();

                onSizeReady();
                if (callback != null)
                    callback.onSizeReady();
            }
        }
    };

    /**
     * 检查view是否准备好
     *
     * @param view
     * @return
     */
    public boolean checkReady(View view)
    {
        return view.getWidth() > 0 && view.getHeight() > 0 && isAttached(view);
    }

    protected void onSizeReady()
    {
    }

    /**
     * 销毁
     */
    public void destroy()
    {
        stopCheckSize();

        for (View view : mViewHolder.keySet())
        {
            view.removeOnLayoutChangeListener(mOnLayoutChangeListener);
        }
        mViewHolder.clear();
        mCallback = null;
    }

    private static boolean isAttached(View view)
    {
        if (Build.VERSION.SDK_INT >= 19)
            return view.isAttachedToWindow();
        else
            return view.getWindowToken() != null;
    }

    public interface Callback
    {
        void onSizeReady();
    }
}
