package com.fanwe.utils_extend;

import android.app.Application;

import com.fanwe.lib.utils.extend.FActivityStack;

/**
 * Created by Administrator on 2018/2/2.
 */

public class App extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        FActivityStack.getInstance().setDebug(true);
        FActivityStack.getInstance().init(this);
    }
}
