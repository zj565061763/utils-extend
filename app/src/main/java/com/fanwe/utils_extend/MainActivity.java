package com.fanwe.utils_extend;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.fanwe.lib.utils.extend.bitmap.FBitmapCompressor;

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

        if (mCompressor.decodeToBitmap("/sdcard/test.png"))
        {
            File file = mCompressor.saveBitmapToFile();
            if (file != null)
            {
                Log.i(TAG, file.getAbsolutePath());
            } else
            {
                Log.e(TAG, "saveBitmapToFile:" + mCompressor.getException());
            }
        } else
        {
            Log.e(TAG, "decodeToBitmap:" + mCompressor.getException());
        }
    }
}
