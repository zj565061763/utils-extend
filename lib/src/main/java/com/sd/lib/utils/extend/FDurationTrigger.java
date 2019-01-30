package com.sd.lib.utils.extend;

public class FDurationTrigger
{
    /**
     * 满足触发条件的触发次数
     */
    private final int mTargetTriggerCount;

    private int mCurrentTriggerCount;
    private long mLastTriggerTime;

    public FDurationTrigger(int targetTriggerCount)
    {
        if (targetTriggerCount < 2)
            throw new IllegalArgumentException("targetTriggerCount must be >= 2");

        mTargetTriggerCount = targetTriggerCount;
    }

    /**
     * 返回满足触发条件的触发次数
     *
     * @return
     */
    public final int getTargetTriggerCount()
    {
        return mTargetTriggerCount;
    }

    /**
     * 返回当前触发次数
     *
     * @return
     */
    public final int getCurrentTriggerCount()
    {
        return mCurrentTriggerCount;
    }

    /**
     * 返回还要多少次有效触发才达到最大触发次数
     *
     * @return
     */
    public final int getLeftTriggerCount()
    {
        final int count = mTargetTriggerCount - mCurrentTriggerCount;
        return count < 0 ? 0 : count;
    }

    /**
     * 重置
     */
    public synchronized final void reset()
    {
        mCurrentTriggerCount = 0;
        mLastTriggerTime = 0;
    }

    /**
     * 触发逻辑
     *
     * @param triggerMaxDuration 两次触发之间的最大有效间隔
     * @return true-达到目标触发次数
     */
    public synchronized boolean trigger(long triggerMaxDuration)
    {
        if (triggerMaxDuration <= 0)
            throw new IllegalArgumentException("triggerMaxDuration must be > 0");

        if (mLastTriggerTime < 0)
            throw new RuntimeException();

        boolean result = false;

        final long currentTime = System.currentTimeMillis();
        final long delta = currentTime - mLastTriggerTime;

        if (delta > triggerMaxDuration)
        {
            // 超过最大有效间隔，重新开始计数
            mCurrentTriggerCount = 1;
        } else
        {
            mCurrentTriggerCount++;
            if (mCurrentTriggerCount >= mTargetTriggerCount)
                result = true;
        }

        mLastTriggerTime = currentTime;
        return result;
    }
}
