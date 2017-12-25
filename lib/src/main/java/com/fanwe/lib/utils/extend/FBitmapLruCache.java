package com.fanwe.lib.utils.extend;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Bitmap的lru算法缓存管理
 */
public class FBitmapLruCache extends LruCache<String, Bitmap>
{
    public FBitmapLruCache()
    {
        this((int) ((double) Runtime.getRuntime().maxMemory() / 16));
    }

    public FBitmapLruCache(int maxSize)
    {
        super(maxSize);
    }

    @Override
    protected final int sizeOf(String key, Bitmap value)
    {
        return value.getByteCount();
    }
}
