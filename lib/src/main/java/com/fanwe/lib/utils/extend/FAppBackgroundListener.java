package com.fanwe.lib.utils.extend;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * app前后台切换监听
 */
public final class FAppBackgroundListener
{
    private static FAppBackgroundListener sInstance;

    private Context mContext;
    private boolean mIsBackground;
    private long mBackgroundTime;

    private List<Callback> mListCallback = new ArrayList<>();

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
        synchronized (this)
        {
            if (callback == null || mListCallback.contains(callback))
            {
                return;
            }
            checkContext();
            mListCallback.add(callback);
        }
    }

    public void removeCallback(Callback callback)
    {
        synchronized (this)
        {
            checkContext();
            mListCallback.remove(callback);
        }
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

                synchronized (FAppBackgroundListener.this)
                {
                    for (Callback item : mListCallback)
                    {
                        item.onResumeFromBackground();
                    }
                }

                mBackgroundTime = 0;
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

                    synchronized (FAppBackgroundListener.this)
                    {
                        for (Callback item : mListCallback)
                        {
                            item.onBackground();
                        }
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
