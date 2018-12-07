package com.sd.lib.utils.extend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 需要用到网络的重试帮助类
 */
public abstract class FNetRetryWorker extends FRetryWorker
{
    private final Context mContext;

    public FNetRetryWorker(int maxRetryCount, Context context)
    {
        super(maxRetryCount);
        mContext = context.getApplicationContext();

        final IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(mNetworkReceiver, filter);
    }

    private final BroadcastReceiver mNetworkReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction()))
            {
                if (isNetworkConnected(context))
                {
                    if (!isRetrySuccess())
                        retry(0);
                }
            }
        }
    };

    @Override
    protected final boolean onRetry()
    {
        if (!isNetworkConnected(mContext))
            return false;

        return onRetryImpl();
    }

    /**
     * 执行重试任务
     *
     * @return true-发起了一次重试，false-没有发起重试
     */
    protected abstract boolean onRetryImpl();

    private static boolean isNetworkConnected(Context context)
    {
        final ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null)
            return false;

        return info.isConnected();
    }
}
