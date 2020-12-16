package com.sd.utils_extend;

import android.app.Notification;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.sd.lib.utils.extend.FNotificationManager;
import com.sd.utils_extend.databinding.ActivityNotificationBinding;

public class NotificationActivity extends AppCompatActivity
{
    private ActivityNotificationBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mBinding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendNotification();
            }
        });
    }

    private void sendNotification()
    {
        final Notification notification = FNotificationManager.getInstance().newBuilder()
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setTicker("ticker")
                .setContentTitle("title")
                .setContentText("text")
                .setSubText("sub text")
                .build();

        FNotificationManager.getInstance().getManager().notify(1, notification);
    }
}
