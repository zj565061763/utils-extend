package com.fanwe.lib.utils.extend;

import android.animation.Animator;
import android.view.View;
import android.view.ViewTreeObserver;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * view的显示隐藏处理
 */
public class FViewVisibilityHandler
{
    private static final Map<View, FViewVisibilityHandler> MAP_HANDLER = new WeakHashMap<>();

    private WeakReference<View> mView;
    /**
     * 显示动画
     */
    private Animator mVisibleAnimator;
    /**
     * 隐藏动画
     */
    private Animator mInvisibleAnimator;
    /**
     * true-gone，false-invisible
     */
    private boolean mIsGoneMode = true;
    /**
     * 当前view的visibility状态
     */
    private int mVisibility;
    private final Map<Callback, Object> mCallbackHolder = new WeakHashMap<>();

    private FViewVisibilityHandler(View view)
    {
        if (view == null)
        {
            throw new NullPointerException("view is null");
        }
        mView = new WeakReference<>(view);

        mVisibility = view.getVisibility();
        view.getViewTreeObserver().removeOnPreDrawListener(mInternalOnPreDrawListener);
        view.getViewTreeObserver().addOnPreDrawListener(mInternalOnPreDrawListener);
    }

    /**
     * 返回view对应的处理类
     *
     * @param view
     * @return
     */
    public static FViewVisibilityHandler get(View view)
    {
        if (view == null)
        {
            return null;
        }
        FViewVisibilityHandler handler = MAP_HANDLER.get(view);
        if (handler == null)
        {
            handler = new FViewVisibilityHandler(view);
            MAP_HANDLER.put(view, handler);
        }
        return handler;
    }

    /**
     * 获得设置的view
     *
     * @return
     */
    public final View getView()
    {
        return mView == null ? null : mView.get();
    }

    private ViewTreeObserver.OnPreDrawListener mInternalOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener()
    {
        @Override
        public boolean onPreDraw()
        {
            notifyVisiblityCallbackIfNeed();
            return true;
        }
    };

    /**
     * 当visibility发生变化的时候通知回调
     */
    private void notifyVisiblityCallbackIfNeed()
    {
        final View view = getView();
        if (view == null)
        {
            return;
        }

        final int visibility = view.getVisibility();
        if (mVisibility != visibility)
        {
            mVisibility = visibility;
            notifyVisiblityCallback();
        }
    }

    /**
     * 添加回调
     *
     * @param callback
     */
    public void addCallback(Callback callback)
    {
        if (callback == null)
        {
            return;
        }
        mCallbackHolder.put(callback, 0);
    }

    /**
     * 移除回调
     *
     * @param callback
     */
    public void removeCallback(Callback callback)
    {
        mCallbackHolder.remove(callback);
    }

    /**
     * 清空回调
     */
    public void clearCallback()
    {
        mCallbackHolder.clear();
    }

    /**
     * 设置显示动画
     *
     * @param visibleAnimator
     */
    public void setVisibleAnimator(Animator visibleAnimator)
    {
        if (mVisibleAnimator != visibleAnimator)
        {
            if (mVisibleAnimator != null)
            {
                mVisibleAnimator.removeListener(mVisibleListener);
            }
            mVisibleAnimator = visibleAnimator;
            if (visibleAnimator != null)
            {
                visibleAnimator.addListener(mVisibleListener);
            }
        }
    }

    /**
     * 设置隐藏动画
     *
     * @param invisibleAnimator
     */
    public void setInvisibleAnimator(Animator invisibleAnimator)
    {
        if (mInvisibleAnimator != invisibleAnimator)
        {
            if (mInvisibleAnimator != null)
            {
                mInvisibleAnimator.removeListener(mInvisibleListener);
            }
            mInvisibleAnimator = invisibleAnimator;
            if (invisibleAnimator != null)
            {
                invisibleAnimator.addListener(mInvisibleListener);
            }
        }
    }

    /**
     * 显示view（View.VISIBLE）
     *
     * @param anim true-执行动画
     */
    public void setVisible(boolean anim)
    {
        if (getView() == null || isVisible())
        {
            return;
        }

        if (anim)
        {
            startVisibleAnimator();
        } else
        {
            setVisibleInternal();
        }
    }

    /**
     * 开始显示动画
     */
    private void startVisibleAnimator()
    {
        if (isVisibleAnimatorStarted())
        {
            return;
        }

        cancelInvisibleAnimatorIfNeed();
        if (mVisibleAnimator != null)
        {
            mVisibleAnimator.start();
        } else
        {
            setVisibleInternal();
        }
    }

    private void setVisibleInternal()
    {
        final View view = getView();
        if (view == null)
        {
            return;
        }
        view.setVisibility(View.VISIBLE);
        notifyVisiblityCallbackIfNeed();
    }

    /**
     * 隐藏view（View.GONE）
     *
     * @param anim
     */
    public void setGone(boolean anim)
    {
        if (getView() == null || isGone())
        {
            return;
        }

        if (anim)
        {
            startInvisibleAnimator(true);
        } else
        {
            setGoneInternal();
        }
    }

    private void setGoneInternal()
    {
        final View view = getView();
        if (view == null)
        {
            return;
        }
        view.setVisibility(View.GONE);
        notifyVisiblityCallbackIfNeed();
    }

    /**
     * 开始隐藏动画
     *
     * @param isGoneMode
     */
    private void startInvisibleAnimator(boolean isGoneMode)
    {
        if (isInvisibleAnimatorStarted())
        {
            return;
        }

        mIsGoneMode = isGoneMode;
        cancelVisibleAnimatorIfNeed();
        if (mInvisibleAnimator != null)
        {
            mInvisibleAnimator.start();
        } else
        {
            if (isGoneMode)
            {
                setGoneInternal();
            } else
            {
                setInvisibleInternal();
            }
        }
    }

    /**
     * 隐藏view（View.INVISIBLE）
     *
     * @param anim
     */
    public void setInvisible(boolean anim)
    {
        if (getView() == null || isInvisible())
        {
            return;
        }

        if (anim)
        {
            startInvisibleAnimator(false);
        } else
        {
            setInvisibleInternal();
        }
    }

    private void setInvisibleInternal()
    {
        final View view = getView();
        if (view == null)
        {
            return;
        }
        view.setVisibility(View.INVISIBLE);
        notifyVisiblityCallbackIfNeed();
    }

    /**
     * 显示动画是否已经启动
     */
    private boolean isVisibleAnimatorStarted()
    {
        return mVisibleAnimator != null && mVisibleAnimator.isStarted();
    }

    /**
     * 隐藏动画是否已经启动
     *
     * @return
     */
    private boolean isInvisibleAnimatorStarted()
    {
        return mInvisibleAnimator != null && mInvisibleAnimator.isStarted();
    }

    /**
     * 取消显示动画
     */
    private void cancelVisibleAnimatorIfNeed()
    {
        if (mVisibleAnimator != null)
        {
            mVisibleAnimator.cancel();
        }
    }

    /**
     * 取消隐藏动画
     */
    private void cancelInvisibleAnimatorIfNeed()
    {
        if (mInvisibleAnimator != null)
        {
            mInvisibleAnimator.cancel();
        }
    }

    /**
     * 显示监听
     */
    private Animator.AnimatorListener mVisibleListener = new Animator.AnimatorListener()
    {
        @Override
        public void onAnimationStart(Animator animation)
        {
            setVisibleInternal();
        }

        @Override
        public void onAnimationEnd(Animator animation)
        {
        }

        @Override
        public void onAnimationCancel(Animator animation)
        {
        }

        @Override
        public void onAnimationRepeat(Animator animation)
        {
        }
    };

    /**
     * 隐藏监听
     */
    private Animator.AnimatorListener mInvisibleListener = new Animator.AnimatorListener()
    {
        @Override
        public void onAnimationStart(Animator animation)
        {
        }

        @Override
        public void onAnimationEnd(Animator animation)
        {
            if (mIsGoneMode)
            {
                setGoneInternal();
            } else
            {
                setInvisibleInternal();
            }
            resetView(getView());
        }

        @Override
        public void onAnimationCancel(Animator animation)
        {
            if (mIsGoneMode)
            {
                setGoneInternal();
            } else
            {
                setInvisibleInternal();
            }
            resetView(getView());
        }

        @Override
        public void onAnimationRepeat(Animator animation)
        {
        }
    };

    private static void resetView(View view)
    {
        if (view != null)
        {
            view.setAlpha(1.0f);
            view.setRotation(0.0f);
            view.setRotationX(0.0f);
            view.setRotationY(0.0f);
            view.setTranslationX(0.0f);
            view.setTranslationY(0.0f);
            view.setScaleX(1.0f);
            view.setScaleY(1.0f);
        }
    }

    /**
     * 通知回调
     */
    public final void notifyVisiblityCallback()
    {
        final View view = getView();
        if (view == null)
        {
            return;
        }

        int visibility = view.getVisibility();
        for (Callback item : mCallbackHolder.keySet())
        {
            item.onViewVisibilityChanged(view, visibility);
        }
    }

    /**
     * view的状态是否处于View.VISIBLE
     *
     * @return
     */
    public boolean isVisible()
    {
        final View view = getView();
        if (view == null)
        {
            return false;
        }
        return view.getVisibility() == View.VISIBLE;
    }

    /**
     * view的状态是否处于View.GONE
     *
     * @return
     */
    public boolean isGone()
    {
        final View view = getView();
        if (view == null)
        {
            return false;
        }
        return view.getVisibility() == View.GONE;
    }

    /**
     * view的状态是否处于View.INVISIBLE
     *
     * @return
     */
    public boolean isInvisible()
    {
        final View view = getView();
        if (view == null)
        {
            return false;
        }
        return view.getVisibility() == View.INVISIBLE;
    }

    /**
     * 在View.VISIBLE和View.GONE之前切换
     *
     * @param anim
     */
    public void toggleVisibleOrGone(boolean anim)
    {
        final View view = getView();
        if (view == null)
        {
            return;
        }

        if (isVisible())
        {
            setGone(anim);
        } else
        {
            setVisible(anim);
        }
    }

    /**
     * 在View.VISIBLE和View.INVISIBLE之前切换
     *
     * @param anim
     */
    public void toggleVisibleOrInvisible(boolean anim)
    {
        final View view = getView();
        if (view == null)
        {
            return;
        }

        if (isVisible())
        {
            setInvisible(anim);
        } else
        {
            setVisible(anim);
        }
    }

    public interface Callback
    {
        void onViewVisibilityChanged(View view, int visibility);
    }
}
