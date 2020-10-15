package com.sd.utils_extend;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.sd.lib.utils.extend.FShakeListener;

public class ShakeListenerActivity extends AppCompatActivity
{
    public static final String TAG = ShakeListenerActivity.class.getSimpleName();

    private FShakeListener mShakeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mShakeListener = new FShakeListener(this);
        mShakeListener.setCallback(new FShakeListener.Callback()
        {
            @Override
            public void onShake()
            {
                Log.i(TAG, "onShake");
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mShakeListener.start();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        mShakeListener.stop();
    }
}
