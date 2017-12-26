package com.fanwe.utils_extend;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.fanwe.lib.utils.extend.bitmap.FBitmapCompressor;

import java.io.File;

public class MainActivity extends AppCompatActivity
{
    public static final String TAG = MainActivity.class.getSimpleName();

    private FBitmapCompressor mBitmapCompressor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBitmapCompressor = new FBitmapCompressor(this);

        if (mBitmapCompressor.decodeFile("/sdcard/test.png"))
        {
            File file = mBitmapCompressor.saveToFile();
            Log.i(TAG, file.getAbsolutePath());
        }
    }
}
