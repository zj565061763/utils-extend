package com.fanwe.lib.utils.extend;

import android.app.Activity;

import java.util.Iterator;
import java.util.Stack;

public class FActivityStack
{
    private static FActivityStack sInstance;
    private Stack<Activity> mStackActivity = new Stack<>();

    private FActivityStack()
    {
    }

    public static FActivityStack getInstance()
    {
        if (sInstance == null)
        {
            synchronized (FActivityStack.class)
            {
                if (sInstance == null)
                {
                    sInstance = new FActivityStack();
                }
            }
        }
        return sInstance;
    }

    // ----------------------------activity life method

    public void onCreate(Activity activity)
    {
        addActivity(activity);
    }

    public void onResume(Activity activity)
    {
        addActivity(activity);
    }

    /**
     * finish()和onDestroy()都要调用
     *
     * @param activity
     */
    public void onDestroy(Activity activity)
    {
        removeActivity(activity);
    }

    private void addActivity(Activity activity)
    {
        if (mStackActivity.contains(activity))
        {
            return;
        }
        mStackActivity.add(activity);
    }

    private void removeActivity(Activity activity)
    {
        try
        {
            if (mStackActivity.contains(activity))
            {
                mStackActivity.remove(activity);
            }
        } catch (Exception e)
        {
        }
    }

    public Activity getActivity(Class<?> clazz)
    {
        Iterator<Activity> it = mStackActivity.iterator();
        while (it.hasNext())
        {
            Activity item = it.next();
            if (item.getClass() == clazz)
            {
                return item;
            }
        }
        return null;
    }

    public Activity getLastActivity()
    {
        try
        {
            return mStackActivity.lastElement();
        } catch (Exception e)
        {
            return null;
        }
    }

    public boolean isLastActivity(Activity activity)
    {
        if (activity == null)
        {
            return false;
        }
        return activity == getLastActivity();
    }

    public boolean isEmpty()
    {
        return mStackActivity.isEmpty();
    }

    /**
     * 结束指定类的Activity
     *
     * @param clazz
     */
    public void finishActivity(Class<?> clazz)
    {
        Iterator<Activity> it = mStackActivity.iterator();
        while (it.hasNext())
        {
            Activity item = it.next();
            if (item.getClass() == clazz)
            {
                it.remove();
                item.finish();
            }
        }
    }

    public boolean containActivity(Class<?> clazz)
    {
        Iterator<Activity> it = mStackActivity.iterator();
        while (it.hasNext())
        {
            Activity item = it.next();
            if (item.getClass() == clazz)
            {
                return true;
            }
        }
        return false;
    }

    public void finishAllActivityExcept(Activity activity)
    {
        Iterator<Activity> it = mStackActivity.iterator();
        while (it.hasNext())
        {
            Activity act = it.next();
            if (act != activity)
            {
                it.remove();
                act.finish();
            }
        }
    }

    public void finishAllActivityExcept(Class<?> clazz)
    {
        Iterator<Activity> it = mStackActivity.iterator();
        while (it.hasNext())
        {
            Activity item = it.next();
            if (item.getClass() != clazz)
            {
                it.remove();
                item.finish();
            }
        }
    }

    public void finishAllClassActivityExcept(Activity activity)
    {
        Iterator<Activity> it = mStackActivity.iterator();
        while (it.hasNext())
        {
            Activity item = it.next();
            if (item.getClass() == activity.getClass() && item != activity)
            {
                it.remove();
                item.finish();
            }
        }
    }

    public void finishAllActivity()
    {
        Iterator<Activity> it = mStackActivity.iterator();
        while (it.hasNext())
        {
            Activity item = it.next();
            it.remove();
            item.finish();
        }
    }
}