package com.fanwe.utils_extend;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.fanwe.lib.utils.extend.FViewSelectionListener;

/**
 * Created by Administrator on 2018/2/8.
 */
public class TestSelectionActivity extends AppCompatActivity
{
    private static final String TAG = TestSelectionActivity.class.getSimpleName();

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_test_selection);
        mButton = findViewById(R.id.btn);
        mButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                v.setSelected(!v.isSelected());
            }
        });

        mViewSelectionListener.setView(mButton);
    }

    private final FViewSelectionListener<Button> mViewSelectionListener = new FViewSelectionListener<Button>()
    {
        @Override
        public void onSelectionChanged(boolean selected, Button view)
        {
            Log.i(TAG, "onSelectionChanged:" + selected);

            if (selected)
                view.setTextColor(Color.RED);
            else
                view.setTextColor(Color.BLACK);
        }
    };
}
