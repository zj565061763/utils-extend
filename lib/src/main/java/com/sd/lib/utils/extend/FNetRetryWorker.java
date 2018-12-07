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
    private NetworkReceiver mNetworkReceiver;

    public FNetRetryWorker(int maxRetryCount, Context context)
    {
        super(maxRetryCount);
        mNetworkReceiver = new NetworkReceiver(context);
        mNetworkReceiver.register();
    }

    @Override
    protected final void onRetry()
    {
        if (mNetworkReceiver == null)
            throw new RuntimeException("current instance has been destroyed");

        if (!mNetworkReceiver.isNetworkConnected())
            return;

        onRetryImpl();
    }

    /**
     * 执行重试任务（UI线程）
     */
    protected abstract void onRetryImpl();

    /**
     * 销毁
     */
    public final synchronized void destroy()
    {
        stop();
        if (mNetworkReceiver != null)
        {
            mNetworkReceiver.unregister();
            mNetworkReceiver = null;
        }
    }

    private final class NetworkReceiver extends BroadcastReceiver
    {
        private final Context mContext;

        public NetworkReceiver(Context context)
        {
            mContext = context.getApplicationContext();
        }

        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction()))
            {
                if (isNetworkConnected())
                {
                    if (!isRetrySuccess())
                        retry(0);
                }
            }
        }

        public boolean isNetworkConnected()
        {
            final ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo info = manager.getActiveNetworkInfo();
            return info == null ? false : info.isConnected();
        }

        public void register()
        {
            final IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            mContext.registerReceiver(this, filter);
        }

        public void unregister()
        {
            mContext.unregisterReceiver(this);
        }
    }
}
