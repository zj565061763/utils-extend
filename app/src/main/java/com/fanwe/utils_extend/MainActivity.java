package com.fanwe.utils_extend;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.fanwe.lib.utils.extend.FBitmapCompressor;

import java.io.File;

public class MainActivity extends AppCompatActivity
{
    public static final String TAG = MainActivity.class.getSimpleName();

    private FBitmapCompressor mCompressor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCompressor = new FBitmapCompressor(this);

        File file = mCompressor.compressFileToFile("/sdcard/test.png");
        if (file != null)
        {
            Log.i(TAG, file.getAbsolutePath());
        } else
        {
            Log.e(TAG, "saveBitmapToFile:" + mCompressor.getException());
        }
    }
}
