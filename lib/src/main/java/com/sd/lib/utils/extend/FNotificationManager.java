package com.sd.lib.utils.extend;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通知管理
 */
public class FNotificationManager
{
    private static FNotificationManager sInstance;

    private Context mContext;
    private NotificationManager mManager;
    private final Map<String, NotificationChannel> mChannelHolder = new ConcurrentHashMap<>();

    public static FNotificationManager getInstance()
    {
        if (sInstance == null)
        {
            synchronized (FNotificationManager.class)
            {
                if (sInstance == null)
                    sInstance = new FNotificationManager();
            }
        }
        return sInstance;
    }

    private FNotificationManager()
    {
    }

    /**
     * 初始化
     *
     * @param context
     */
    public synchronized void init(Context context)
    {
        if (mContext != null || mManager != null)
            return;

        mContext = context.getApplicationContext();
        mManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 26)
        {
            final NotificationChannel defaultChannel = getDefaultChannel();
            final String channelName = mContext.getResources().getString(R.string.lib_utils_extend_default_channel_name);
            if (!TextUtils.isEmpty(channelName))
                defaultChannel.setName(channelName);

            mManager.createNotificationChannel(defaultChannel);
        }
    }

    /**
     * 通知管理对象
     *
     * @return
     */
    public NotificationManager getManager()
    {
        if (mManager == null)
            throw new RuntimeException("you must call init(context) before this");
        return mManager;
    }

    /**
     * 默认通道
     *
     * @return
     */
    public NotificationChannel getDefaultChannel()
    {
        return getChannel(mContext.getPackageName());
    }

    /**
     * 返回通知通道
     *
     * @param channelId
     * @return
     */
    public NotificationChannel getChannel(String channelId)
    {
        if (TextUtils.isEmpty(channelId))
            return null;

        if (Build.VERSION.SDK_INT >= 26)
        {
            NotificationChannel channel = mChannelHolder.get(channelId);
            if (channel == null)
            {
                channel = new NotificationChannel(channelId, "channel name", NotificationManager.IMPORTANCE_DEFAULT);
                mChannelHolder.put(channelId, channel);
            }
            return channel;
        }
        return null;
    }

    /**
     * 通知构建对象
     *
     * @return
     */
    public Notification.Builder newBuilder()
    {
        return newBuilder(null);
    }

    /**
     * 通知构建对象
     *
     * @param channelId
     * @return
     */
    public Notification.Builder newBuilder(String channelId)
    {
        if (Build.VERSION.SDK_INT >= 26)
        {
            if (TextUtils.isEmpty(channelId))
                channelId = getDefaultChannel().getId();

            return new Notification.Builder(mContext, channelId);
        } else
        {
            return new Notification.Builder(mContext);
        }
    }
}
