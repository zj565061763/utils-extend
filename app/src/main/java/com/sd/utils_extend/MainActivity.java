package com.sd.utils_extend;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.sd.lib.utils.extend.FActivityObjectHolder;
import com.sd.lib.utils.extend.FContinueTrigger;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    public static final String TAG = MainActivity.class.getSimpleName();

    private final FContinueTrigger mContinueTrigger = new FContinueTrigger(2);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FActivityObjectHolder.of(this).getOrCreateItem(TestItem.class);
    }

    @Override
    public void onClick(View v)
    {
        FActivityObjectHolder.of(this).getItem(TestItem.class).test();
    }

    @Override
    public void onBackPressed()
    {
        if (mContinueTrigger.trigger(2000))
        {
            finish();
        } else
        {
            Toast.makeText(this, "再按" + mContinueTrigger.getLeftTriggerCount() + "次退出", Toast.LENGTH_SHORT).show();
        }
    }
}
