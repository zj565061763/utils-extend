package com.sd.utils_extend;

import android.app.Application;

import com.sd.lib.utils.extend.FNotificationManager;

/**
 * Created by Administrator on 2018/2/2.
 */
public class App extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        FNotificationManager.getInstance().init(this);
    }
}
