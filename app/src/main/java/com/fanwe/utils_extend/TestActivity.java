package com.fanwe.utils_extend;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.fanwe.lib.utils.extend.FViewVisibilityHandler;
import com.fanwe.lib.utils.extend.FViewVisibilityListener;

/**
 * Created by Administrator on 2018/2/8.
 */
public class TestActivity extends AppCompatActivity
{
    public static final String TAG = TestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_test);

        mViewVisibilityListener.setView(findViewById(R.id.btn));

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FViewVisibilityHandler.get(v).setGone(true);
            }
        });
    }

    private FViewVisibilityListener mViewVisibilityListener = new FViewVisibilityListener()
    {
        @Override
        protected void onViewVisibilityChanged(View view, int visibility)
        {
            Log.i(TAG, view + ":" + visibility);
        }
    };
}
