package com.sd.lib.utils.extend;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 只有目标Activity已经在栈中才执行的任务，如果目标Activity不在栈中，则会一直等到目标Activity入栈的时候执行
 */
public abstract class FActivityLaunchTask
{
    /**
     * 初始化，需要在Application创建的时候初始化
     *
     * @param context
     */
    public final static void init(Context context)
    {
        Manager.INSTANCE.init(context);
    }

    /**
     * 提交当前任务
     */
    public final void submit()
    {
        Manager.INSTANCE.submit(this);
    }

    /**
     * 返回目标Activity的Class
     *
     * @return
     */
    protected abstract Class<? extends Activity> getTargetClass();

    /**
     * 执行任务
     *
     * @param activity 目标Activity
     */
    protected abstract void execute(Activity activity);

    private static class Manager
    {
        private static final Manager INSTANCE = new Manager();

        private Application mApplication;
        private final List<Activity> mListActivity = new CopyOnWriteArrayList<>();
        private final List<FActivityLaunchTask> mListTask = new CopyOnWriteArrayList<>();

        public void init(Context context)
        {
            if (mApplication != null)
                return;

            mApplication = (Application) context.getApplicationContext();
            mApplication.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks()
            {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState)
                {
                    mListActivity.add(activity);

                    for (FActivityLaunchTask item : mListTask)
                    {
                        if (activity.getClass() == item.getTargetClass())
                        {
                            mListTask.remove(item);
                            item.execute(activity);
                        }
                    }
                }

                @Override
                public void onActivityStarted(Activity activity)
                {
                }

                @Override
                public void onActivityResumed(Activity activity)
                {
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
                    mListActivity.remove(activity);
                }
            });
        }

        public final void submit(FActivityLaunchTask task)
        {
            final int size = mListActivity.size();
            if (size > 0)
            {
                for (Activity activity : mListActivity)
                {
                    if (activity.getClass() == task.getTargetClass())
                    {
                        task.execute(activity);
                        return;
                    }
                }
            }

            mListTask.add(task);
        }
    }
}
