package com.fanwe.lib.utils.extend;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.LruCache;

public class BitmapLruCache
{
    private LruCache<String, Bitmap> mCache;
    private int mMaxCacheSize;

    public BitmapLruCache()
    {
        this((int) ((double) Runtime.getRuntime().maxMemory() / 16));
    }

    /**
     * @param maxSize 缓存的最大值
     */
    public BitmapLruCache(int maxSize)
    {
        this.mMaxCacheSize = maxSize;
    }

    private LruCache<String, Bitmap> getCache()
    {
        if (mCache == null)
        {
            mCache = new LruCache<String, Bitmap>(mMaxCacheSize)
            {
                @Override
                protected int sizeOf(String key, Bitmap value)
                {
                    return value.getByteCount();
                }
            };
        }
        return mCache;
    }

    private String createKey(String url)
    {
        if (url == null)
        {
            return null;
        }
        return Base64.encodeToString(url.getBytes(), Base64.DEFAULT);
    }

    public boolean put(String url, Bitmap bitmap)
    {
        try
        {
            getCache().put(createKey(url), bitmap);
            return true;
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public Bitmap get(String url)
    {
        try
        {
            return getCache().get(createKey(url));
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public int size()
    {
        return getCache().size();
    }

    public void clear()
    {
        getCache().evictAll();
    }
}
