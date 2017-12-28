package com.fanwe.utils_extend;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.fanwe.lib.utils.extend.FImageCompressor;

import java.io.File;

public class MainActivity extends AppCompatActivity
{
    public static final String TAG = MainActivity.class.getSimpleName();

    private FImageCompressor mCompressor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCompressor = new FImageCompressor(this);

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
