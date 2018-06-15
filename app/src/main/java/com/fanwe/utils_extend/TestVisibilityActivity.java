package com.fanwe.utils_extend;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.fanwe.lib.utils.extend.FViewVisibilityListener;

/**
 * Created by Administrator on 2018/2/8.
 */
public class TestVisibilityActivity extends AppCompatActivity
{
    public static final String TAG = TestVisibilityActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_test_visibility);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                v.setVisibility(View.GONE);
            }
        });

        mViewVisibilityListener.setView(findViewById(R.id.btn));
    }

    private final FViewVisibilityListener mViewVisibilityListener = new FViewVisibilityListener()
    {
        @Override
        public void onVisibilityChanged(int visibility, final View view)
        {
            Log.i(TAG, "visibility:" + visibility);

            if (visibility != View.VISIBLE)
            {
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        view.setVisibility(View.VISIBLE);
                    }
                }, 2000);
            }
        }
    };
}
