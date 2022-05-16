package com.sd.lib.utils.extend;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FBackgroundListener {
    private static FBackgroundListener sInstance;

    private Application mApplication;
    private boolean mIsBackground;
    private long mBackgroundTime;

    private final List<Callback> mListCallback = new CopyOnWriteArrayList<>();

    private FBackgroundListener() {
    }

    public static FBackgroundListener getInstance() {
        if (sInstance == null) {
            synchronized (FBackgroundListener.class) {
                if (sInstance == null)
                    sInstance = new FBackgroundListener();
            }
        }
        return sInstance;
    }

    /**
     * 初始化
     *
     * @param application
     */
    public synchronized void init(Application application) {
        if (application == null)
            throw new NullPointerException("application is null");

        if (mApplication == null) {
            mApplication = application;

            application.unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
            application.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        }
    }

    /**
     * 是否处于后台状态
     *
     * @return
     */
    public boolean isBackground() {
        return mIsBackground;
    }

    /**
     * 返回最后一次App进入后台的时间点
     *
     * @return
     */
    public long getBackgroundTime() {
        return mBackgroundTime;
    }

    /**
     * 添加回调
     *
     * @param callback
     */
    public void addCallback(Callback callback) {
        if (callback == null || mListCallback.contains(callback))
            return;

        if (mApplication == null)
            throw new NullPointerException("you must invoke init(Application) method before this");

        mListCallback.add(callback);
    }

    /**
     * 移除回调
     *
     * @param callback
     */
    public void removeCallback(Callback callback) {
        mListCallback.remove(callback);
    }

    private Activity mTopActivity;

    private final Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
            if (mTopActivity == null) {
                mIsBackground = false;
                notifyForeground();
            }

            mTopActivity = activity;
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
            if (mTopActivity == activity) {
                mIsBackground = true;
                mBackgroundTime = System.currentTimeMillis();
                notifyBackground();

                mTopActivity = null;
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    };

    private void notifyBackground() {
        for (Callback item : mListCallback) {
            item.onBackground();
        }
    }

    private void notifyForeground() {
        for (Callback item : mListCallback) {
            item.onForeground();
        }
    }

    public interface Callback {
        void onBackground();

        void onForeground();
    }
}