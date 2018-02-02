package com.fanwe.lib.utils.extend;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FActivityStack
{
    private static FActivityStack sInstance;

    private Context mContext;
    private List<Activity> mActivityHolder = new CopyOnWriteArrayList<>();

    private boolean mIsDebug;

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

    public void setDebug(boolean debug)
    {
        mIsDebug = debug;
    }

    public void init(Context context)
    {
        if (mContext != null)
        {
            return;
        }

        synchronized (this)
        {
            if (mContext == null)
            {
                mContext = context.getApplicationContext();

                Application application = (Application) mContext;
                application.unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
                application.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
            }
        }
    }

    private Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks()
    {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState)
        {
            addActivity(activity);
        }

        @Override
        public void onActivityStarted(Activity activity)
        {
        }

        @Override
        public void onActivityResumed(Activity activity)
        {
            final int index = mActivityHolder.indexOf(activity);
            if (index < 0)
            {
                return;
            }

            if (index != (mActivityHolder.size() - 1))
            {
                removeActivity(activity);
                addActivity(activity);

                if (mIsDebug)
                {
                    Log.e(FActivityStack.class.getSimpleName(), activity + "is order to top, old index " + index + " current index " + mActivityHolder.indexOf(activity));
                }
            }
        }

        @Override
        public void onActivityPaused(Activity activity)
        {
        }

        @Override
        public void onActivityStopped(Activity activity)
        {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState)
        {
        }

        @Override
        public void onActivityDestroyed(Activity activity)
        {
            removeActivity(activity);
        }
    };

    private void printCurrentStack()
    {
        Object[] arrActivity = mActivityHolder.toArray();
        if (arrActivity != null)
        {
            Log.i(FActivityStack.class.getSimpleName(), Arrays.toString(arrActivity));
        }
    }

    /**
     * 添加对象
     *
     * @param activity
     */
    public void addActivity(Activity activity)
    {
        if (mActivityHolder.contains(activity))
        {
            return;
        }
        mActivityHolder.add(activity);
        if (mIsDebug)
        {
            Log.i(FActivityStack.class.getSimpleName(), "addActivity:" + activity);
            printCurrentStack();
        }
    }

    /**
     * 移除对象
     *
     * @param activity
     */
    public void removeActivity(Activity activity)
    {
        if (mActivityHolder.remove(activity))
        {
            if (mIsDebug)
            {
                Log.i(FActivityStack.class.getSimpleName(), "removeActivity:" + activity);
                printCurrentStack();
            }
        }
    }

    /**
     * 返回最后一个对象
     *
     * @return
     */
    public Activity getLastActivity()
    {
        try
        {
            return mActivityHolder.get(mActivityHolder.size() - 1);
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * 是否为空
     *
     * @return
     */
    public boolean isEmpty()
    {
        return mActivityHolder.isEmpty();
    }

    /**
     * 结束指定类的对象
     *
     * @param clazz
     */
    public void finishActivity(Class<?> clazz)
    {
        for (Activity item : mActivityHolder)
        {
            if (item.getClass() == clazz)
            {
                item.finish();
            }
        }
    }

    /**
     * 是否包含指定类的对象
     *
     * @param clazz
     * @return
     */
    public boolean containActivity(Class<?> clazz)
    {
        for (Activity item : mActivityHolder)
        {
            if (item.getClass() == clazz)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 结束除了activity外的所有对象
     *
     * @param activity
     */
    public void finishAllActivityExcept(Activity activity)
    {
        for (Activity item : mActivityHolder)
        {
            if (item != activity)
            {
                item.finish();
            }
        }
    }

    /**
     * 结束除了指定类外的所有对象
     *
     * @param clazz
     */
    public void finishAllActivityExcept(Class<?> clazz)
    {
        for (Activity item : mActivityHolder)
        {
            if (item.getClass() != clazz)
            {
                item.finish();
            }
        }
    }

    /**
     * 结束除了activity外的所有activity类的对象
     *
     * @param activity
     */
    public void finishAllClassActivityExcept(Activity activity)
    {
        for (Activity item : mActivityHolder)
        {
            if (item.getClass() == activity.getClass())
            {
                if (item != activity)
                {
                    item.finish();
                }
            }
        }
    }

    /**
     * 结束所有对象
     */
    public void finishAllActivity()
    {
        for (Activity item : mActivityHolder)
        {
            item.finish();
        }
    }
}