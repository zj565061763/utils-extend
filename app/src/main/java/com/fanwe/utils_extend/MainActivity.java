package com.fanwe.utils_extend;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fanwe.lib.utils.extend.bitmap.FBitmapCompressor;

public class MainActivity extends AppCompatActivity
{

    private FBitmapCompressor mBitmapCompressor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBitmapCompressor = new FBitmapCompressor(this);
    }
}
