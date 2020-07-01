package com.sd.lib.utils.extend;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 扫描文件添加到相册
 */
public abstract class FMediaScanner
{
    private final MediaScannerConnection mConnection;
    private final List<File> mListFile = new ArrayList<>();

    private boolean mIsScanFile = false;

    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Map<Runnable, String> mMapRunnable = new HashMap<>();

    public FMediaScanner(Context context)
    {
        context = context.getApplicationContext();
        mConnection = new MediaScannerConnection(context, mConnectionClient);
    }

    private final MediaScannerConnection.MediaScannerConnectionClient mConnectionClient = new MediaScannerConnection.MediaScannerConnectionClient()
    {
        @Override
        public void onMediaScannerConnected()
        {
            scanFileInternal();
        }

        @Override
        public void onScanCompleted(final String path, final Uri uri)
        {
            final Runnable runnable = new Runnable()
            {
                @Override
                public void run()
                {
                    synchronized (FMediaScanner.this)
                    {
                        mMapRunnable.remove(this);
                    }
                    FMediaScanner.this.onScanCompleted(path, uri);
                }
            };

            synchronized (FMediaScanner.this)
            {
                mMapRunnable.put(runnable, "");
                mHandler.post(runnable);

                mIsScanFile = false;
            }

            scanFileInternal();
        }
    };

    /**
     * 扫描文件
     *
     * @param file
     */
    public synchronized void scanFile(File file)
    {
        if (file == null)
            return;

        if (mListFile.contains(file))
            return;

        mListFile.add(file);
        scanFileInternal();
    }

    private synchronized void scanFileInternal()
    {
        if (mListFile.isEmpty())
            return;

        if (!mConnection.isConnected())
        {
            mConnection.connect();
            return;
        }

        if (mIsScanFile)
            return;

        final File file = mListFile.remove(0);
        if (!file.exists())
            scanFileInternal();

        final String path = file.getAbsolutePath();
        final String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        final String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

        mIsScanFile = true;
        mConnection.scanFile(path, mimeType);
    }

    private void cancelRunnable()
    {
        for (Runnable item : mMapRunnable.keySet())
        {
            mHandler.removeCallbacks(item);
        }
        mMapRunnable.clear();
    }

    /**
     * 取消
     */
    public synchronized void cancel()
    {
        cancelRunnable();
        mConnection.disconnect();
    }

    protected abstract void onScanCompleted(String path, Uri uri);
}
