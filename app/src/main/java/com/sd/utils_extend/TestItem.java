package com.sd.utils_extend;

import android.app.Activity;
import android.util.Log;

import com.sd.lib.utils.extend.FActivityObjectHolder;

public class TestItem extends FActivityObjectHolder.BaseItem
{
    public static final String TAG = TestItem.class.getSimpleName();

    public void test()
    {
        Log.i(TAG, "test " + this);
    }

    @Override
    protected void initImpl(Activity activity)
    {
        Log.i(TAG, "initImpl activity:" + activity + " " + this);
    }

    @Override
    protected void destroyImpl()
    {
        Log.i(TAG, "destroyImpl " + this);
    }
}
