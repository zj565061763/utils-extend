package com.sd.utils_extend;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.sd.lib.utils.extend.FDelayTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v)
    {
        mDelayTask.runDelayOrImmediately(1000);
    }

    private final FDelayTask mDelayTask = new FDelayTask()
    {
        @Override
        protected void onRun()
        {
            Log.i(TAG, "onRun");
        }

        @Override
        protected void onPost()
        {
            super.onPost();
            Log.i(TAG, "onPost");
        }

        @Override
        protected void onRemove()
        {
            super.onRemove();
            Log.i(TAG, "onRemove");
        }
    };
}
