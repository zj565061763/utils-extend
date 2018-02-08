package com.fanwe.utils_extend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fanwe.lib.utils.extend.FViewVisibilityHandler;

/**
 * Created by Administrator on 2018/2/8.
 */

public class TestActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FViewVisibilityHandler.get(findViewById(R.id.iv_image)).addCallback(mVisibilityCallback);
    }

    private FViewVisibilityHandler.Callback mVisibilityCallback = new FViewVisibilityHandler.Callback()
    {
        @Override
        public void onViewVisibilityChanged(View view, int visibility)
        {

        }
    };
}
