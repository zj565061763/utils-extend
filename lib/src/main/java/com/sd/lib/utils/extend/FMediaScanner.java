package com.sd.lib.utils.extend;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 扫描文件添加到相册
 */
public abstract class FMediaScanner
{
    private final MediaScannerConnection mConnection;
    private final List<File> mListFile = new ArrayList<>();

    private boolean mIsScanFile = false;

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
        public void onScanCompleted(String path, Uri uri)
        {
            FMediaScanner.this.onScanCompleted(path, uri);

            synchronized (FMediaScanner.this)
            {
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

    /**
     * 取消
     */
    public synchronized void cancel()
    {
        mConnection.disconnect();
    }

    protected abstract void onScanCompleted(String path, Uri uri);
}
