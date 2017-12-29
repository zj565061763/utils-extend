package com.fanwe.utils_extend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.fanwe.lib.utils.extend.FImageGetter;

import java.io.File;

public class MainActivity extends AppCompatActivity
{
    public static final String TAG = MainActivity.class.getSimpleName();

    private FImageGetter mImageGetter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testImageGetter();
    }

    private void testImageGetter()
    {
        mImageGetter = new FImageGetter(this);
        mImageGetter.setCallback(new FImageGetter.Callback()
        {
            @Override
            public void onResultFromAlbum(File file)
            {
                Log.i(TAG, "onResultFromAlbum:" + file.getAbsolutePath());
            }

            @Override
            public void onResultFromCamera(File file)
            {
                Log.i(TAG, "onResultFromCamera:" + file.getAbsolutePath());
            }

            @Override
            public void onError(String desc)
            {
                Log.e(TAG, desc);
            }
        });
        mImageGetter.getImageFromCamera();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        mImageGetter.onActivityResult(requestCode, resultCode, data);
    }
}
