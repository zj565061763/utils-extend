package com.fanwe.lib.utils.extend;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * app前后台切换监听
 */
public class FAppBackgroundListener
{
    private static FAppBackgroundListener sInstance;

    private Context mContext;
    private boolean mIsBackground;
    private long mBackgroundTime;

    private final List<Callback> mListCallback = new CopyOnWriteArrayList<>();

    private FAppBackgroundListener()
    {
    }

    public static FAppBackgroundListener getInstance()
    {
        if (sInstance == null)
        {
            synchronized (FAppBackgroundListener.class)
            {
                if (sInstance == null)
                {
                    sInstance = new FAppBackgroundListener();
                }
            }
        }
        return sInstance;
    }

    public synchronized void init(Context context)
    {
        if (mContext == null)
        {
            mContext = context.getApplicationContext();

            Application application = (Application) mContext;
            application.unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
            application.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        }
    }

    /**
     * 返回最后一次App进入后台的时间点
     *
     * @return
     */
    public long getBackgroundTime()
    {
        return mBackgroundTime;
    }

    private void checkContext()
    {
        if (mContext == null)
        {
            throw new NullPointerException("you must invoke init(context) method before this");
        }
    }

    public void addCallback(Callback callback)
    {
        if (callback == null || mListCallback.contains(callback))
        {
            return;
        }
        checkContext();
        mListCallback.add(callback);
    }

    public void removeCallback(Callback callback)
    {
        checkContext();
        mListCallback.remove(callback);
    }

    private Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks()
    {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState)
        {

        }

        @Override
        public void onActivityStarted(Activity activity)
        {

        }

        @Override
        public void onActivityResumed(Activity activity)
        {
            if (mIsBackground)
            {
                mIsBackground = false;

                for (Callback item : mListCallback)
                {
                    item.onResumeFromBackground();
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
            if (!mIsBackground)
            {
                if (isAppBackground(mContext))
                {
                    mIsBackground = true;
                    mBackgroundTime = System.currentTimeMillis();

                    for (Callback item : mListCallback)
                    {
                        item.onBackground();
                    }
                }
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState)
        {

        }

        @Override
        public void onActivityDestroyed(Activity activity)
        {

        }
    };

    public static boolean isAppBackground(Context context)
    {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> listProcess = activityManager.getRunningAppProcesses();

        String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo item : listProcess)
        {
            if (item.processName.equals(packageName))
            {
                if (item.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                {
                    return true;
                } else
                {
                    return false;
                }
            }
        }
        return false;
    }

    public interface Callback
    {
        void onBackground();

        void onResumeFromBackground();
    }
}
