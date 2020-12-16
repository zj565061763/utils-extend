package com.sd.utils_extend;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.sd.lib.utils.extend.FViewSizeTracker;
import com.sd.utils_extend.databinding.ActivitySizeTrackerBinding;

public class SizeTrackerActivity extends AppCompatActivity
{
    public static final String TAG = SizeTrackerActivity.class.getSimpleName();

    private ActivitySizeTrackerBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySizeTrackerBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        // 设置源View
        mViewSizeTracker.setSource(mBinding.tvSource);
        // 设置目标View
        mViewSizeTracker.setTarget(mBinding.tvTarget);

        mBinding.tvTarget.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final ViewGroup.LayoutParams params = v.getLayoutParams();
                params.width = v.getWidth() + 10;
                params.height = v.getHeight() + 10;
                v.setLayoutParams(params);
            }
        });
    }

    private final FViewSizeTracker mViewSizeTracker = new FViewSizeTracker()
    {
        @Override
        protected void updateSourceSize(View source, int width, int height)
        {
            super.updateSourceSize(source, width, height);
            Log.i(TAG, "updateSourceSize width:" + width + " height:" + height);
        }
    };
}
