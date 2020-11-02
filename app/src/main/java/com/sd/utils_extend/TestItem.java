package com.sd.utils_extend;

import android.app.Activity;
import android.util.Log;

import com.sd.lib.utils.extend.FActivityObjectHolder;

public class TestItem implements FActivityObjectHolder.Item
{
    public static final String TAG = TestItem.class.getSimpleName();

    @Override
    public void init(Activity activity)
    {
        Log.i(TAG, "init activity" + activity);
    }

    public void test()
    {
        Log.i(TAG, "test");
    }

    @Override
    public void destroy()
    {
        Log.i(TAG, "destroy");
    }
}
