package com.sd.lib.utils.extend;

import android.animation.Animator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FVisibilityAnimatorHandler
{
    private Animator mShowAnimator;
    private Animator mHideAnimator;

    private Animator.AnimatorListener mShowListener;
    private Animator.AnimatorListener mHideListener;

    private Map<Animator.AnimatorListener, String> mShowListenerHolder;
    private Map<Animator.AnimatorListener, String> mHideListenerHolder;

    //---------- Show start ----------

    /**
     * 设置显示动画
     *
     * @param animator
     */
    public void setShowAnimator(Animator animator)
    {
        final Animator old = mShowAnimator;
        if (old != animator)
        {
            if (old != null)
                old.removeListener(mShowAnimatorListener);

            mShowAnimator = animator;

            if (animator != null)
                animator.addListener(mShowAnimatorListener);
        }
    }

    /**
     * 设置显示动画监听
     *
     * @param listener
     */
    public void setShowAnimatorListener(Animator.AnimatorListener listener)
    {
        mShowListener = listener;
    }

    /**
     * 添加显示动画监听
     *
     * @param listener
     */
    public void addShowAnimatorListener(Animator.AnimatorListener listener)
    {
        if (listener == null)
            return;

        if (mShowListenerHolder == null)
            mShowListenerHolder = new ConcurrentHashMap<>();
        mShowListenerHolder.put(listener, "");
    }

    /**
     * 移除显示动画监听
     *
     * @param listener
     */
    public void removeShowAnimatorListener(Animator.AnimatorListener listener)
    {
        if (listener == null)
            return;

        if (mShowListenerHolder != null)
        {
            mShowListenerHolder.remove(listener);
            if (mShowListenerHolder.isEmpty())
                mShowListenerHolder = null;
        }
    }

    /**
     * 开始显示动画
     *
     * @return true-动画被执行
     */
    public boolean startShowAnimator()
    {
        if (mShowAnimator != null)
        {
            if (mShowAnimator.isStarted())
                return true;

            cancelHideAnimator();
            mShowAnimator.start();
            return true;
        }
        return false;
    }

    /**
     * 显示动画是否已经开始
     *
     * @return
     */
    public boolean isShowAnimatorStarted()
    {
        return mShowAnimator != null && mShowAnimator.isStarted();
    }

    /**
     * 取消显示动画
     */
    public void cancelShowAnimator()
    {
        if (mShowAnimator != null)
            mShowAnimator.cancel();
    }

    //---------- Show end ----------


    //---------- Hide start ----------

    /**
     * 设置隐藏动画
     *
     * @param animator
     */
    public void setHideAnimator(Animator animator)
    {
        final Animator old = mHideAnimator;
        if (old != animator)
        {
            if (old != null)
                old.removeListener(mHideAnimatorListener);

            mHideAnimator = animator;

            if (animator != null)
                animator.addListener(mHideAnimatorListener);
        }
    }

    /**
     * 设置隐藏动画监听
     *
     * @param listener
     */
    public void setHideAnimatorListener(Animator.AnimatorListener listener)
    {
        mHideListener = listener;
    }

    /**
     * 添加隐藏动画监听
     *
     * @param listener
     */
    public void addHideAnimatorListener(Animator.AnimatorListener listener)
    {
        if (listener == null)
            return;

        if (mHideListenerHolder == null)
            mHideListenerHolder = new ConcurrentHashMap<>();
        mHideListenerHolder.put(listener, "");
    }

    /**
     * 移除隐藏动画监听
     *
     * @param listener
     */
    public void removeHideAnimatorListener(Animator.AnimatorListener listener)
    {
        if (listener == null)
            return;

        if (mHideListenerHolder != null)
        {
            mHideListenerHolder.remove(listener);
            if (mHideListenerHolder.isEmpty())
                mHideListenerHolder = null;
        }
    }

    /**
     * 开始隐藏动画
     *
     * @return true-动画被执行
     */
    public boolean startHideAnimator()
    {
        if (mHideAnimator != null)
        {
            if (mHideAnimator.isStarted())
                return true;

            cancelShowAnimator();
            mHideAnimator.start();
            return true;
        }
        return false;
    }

    /**
     * 隐藏动画是否已经开始执行
     *
     * @return
     */
    public boolean isHideAnimatorStarted()
    {
        return mHideAnimator != null && mHideAnimator.isStarted();
    }

    /**
     * 取消隐藏动画
     */
    public void cancelHideAnimator()
    {
        if (mHideAnimator != null)
            mHideAnimator.cancel();
    }

    //---------- Hide end ----------

    private final Animator.AnimatorListener mShowAnimatorListener = new Animator.AnimatorListener()
    {
        @Override
        public void onAnimationStart(Animator animation)
        {
            if (mShowListener != null)
                mShowListener.onAnimationStart(animation);

            if (mShowListenerHolder != null)
            {
                for (Animator.AnimatorListener item : mShowListenerHolder.keySet())
                {
                    item.onAnimationStart(animation);
                }
            }
        }

        @Override
        public void onAnimationEnd(Animator animation)
        {
            if (mShowListener != null)
                mShowListener.onAnimationEnd(animation);

            if (mShowListenerHolder != null)
            {
                for (Animator.AnimatorListener item : mShowListenerHolder.keySet())
                {
                    item.onAnimationEnd(animation);
                }
            }
        }

        @Override
        public void onAnimationCancel(Animator animation)
        {
            if (mShowListener != null)
                mShowListener.onAnimationCancel(animation);

            if (mShowListenerHolder != null)
            {
                for (Animator.AnimatorListener item : mShowListenerHolder.keySet())
                {
                    item.onAnimationCancel(animation);
                }
            }
        }

        @Override
        public void onAnimationRepeat(Animator animation)
        {
            if (mShowListener != null)
                mShowListener.onAnimationRepeat(animation);

            if (mShowListenerHolder != null)
            {
                for (Animator.AnimatorListener item : mShowListenerHolder.keySet())
                {
                    item.onAnimationRepeat(animation);
                }
            }
        }
    };

    private final Animator.AnimatorListener mHideAnimatorListener = new Animator.AnimatorListener()
    {
        @Override
        public void onAnimationStart(Animator animation)
        {
            if (mHideListener != null)
                mHideListener.onAnimationStart(animation);

            if (mHideListenerHolder != null)
            {
                for (Animator.AnimatorListener item : mHideListenerHolder.keySet())
                {
                    item.onAnimationStart(animation);
                }
            }
        }

        @Override
        public void onAnimationEnd(Animator animation)
        {
            if (mHideListener != null)
                mHideListener.onAnimationEnd(animation);

            if (mHideListenerHolder != null)
            {
                for (Animator.AnimatorListener item : mHideListenerHolder.keySet())
                {
                    item.onAnimationEnd(animation);
                }
            }
        }

        @Override
        public void onAnimationCancel(Animator animation)
        {
            if (mHideListener != null)
                mHideListener.onAnimationCancel(animation);

            if (mHideListenerHolder != null)
            {
                for (Animator.AnimatorListener item : mHideListenerHolder.keySet())
                {
                    item.onAnimationCancel(animation);
                }
            }
        }

        @Override
        public void onAnimationRepeat(Animator animation)
        {
            if (mHideListener != null)
                mHideListener.onAnimationRepeat(animation);

            if (mHideListenerHolder != null)
            {
                for (Animator.AnimatorListener item : mHideListenerHolder.keySet())
                {
                    item.onAnimationRepeat(animation);
                }
            }
        }
    };
}
