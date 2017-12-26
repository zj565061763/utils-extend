package com.fanwe.lib.utils.extend.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Administrator on 2017/12/26.
 */
public class FBitmapCompressor
{
    private int mMaxWidth;
    private int mMaxFileSize = 1024 * 1024;

    private Bitmap mBitmap;

    private FBitmapCompressor(Context context)
    {
        mMaxWidth = context.getResources().getDisplayMetrics().widthPixels;
    }

    public void setMaxWidth(int maxWidth)
    {
        if (maxWidth > 0)
        {
            mMaxWidth = maxWidth;
        }
    }

    public void setMaxFileSize(int maxFileSize)
    {
        if (maxFileSize > 0)
        {
            mMaxFileSize = maxFileSize;
        }
    }

    private void saveBitmap(Bitmap bitmap)
    {
        if (mBitmap != null)
        {
            mBitmap.recycle();
        }
        mBitmap = bitmap;
    }

    public FBitmapCompressor decodeFile(String filePath)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        int maxHeight = getScaleHeight(options.outWidth, options.outHeight, mMaxWidth);

        options.inSampleSize = calculateInSampleSize(options, mMaxWidth, maxHeight);
        options.inJustDecodeBounds = false;

        try
        {
            Bitmap bitmap = scaleBitmapIfNeed(BitmapFactory.decodeFile(filePath, options),
                    mMaxWidth, maxHeight, true);
            saveBitmap(bitmap);
        } catch (Throwable e)
        {
        }
        return this;
    }

    public Bitmap getBitmap()
    {
        return mBitmap;
    }

    protected int calculateInSampleSize(BitmapFactory.Options options, int maxWidth, int maxHeight)
    {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (width > maxWidth || height > maxHeight)
        {
//            float original = (float) width / (float) height;
//            float target = (float) maxWidth / (float) maxHeight;
//            float differ = original - target;
//            if (differ >= 0)
//            {
//                inSampleSize = Math.round((float) width / (float) maxWidth);
//            } else
//            {
//                inSampleSize = Math.round((float) height / (float) maxHeight);
//            }


            if (width > height)
            {
                inSampleSize = Math.round((float) height / (float) maxHeight);
            } else
            {
                inSampleSize = Math.round((float) width / (float) maxWidth);
            }

            final float totalPixels = width * height;
            final float maxTotalPixels = maxWidth * maxHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > maxTotalPixels)
            {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    private static int getScaleHeight(int scaleWidth, int scaleHeight, int width)
    {
        if (scaleWidth == 0)
        {
            return 0;
        }
        return scaleHeight * width / scaleWidth;
    }

    private static Bitmap scaleBitmapIfNeed(Bitmap bitmap, int targetWidth, int targetHeight, boolean recycle)
    {
        if (bitmap.getWidth() > targetHeight || bitmap.getHeight() > targetHeight)
        {
            Bitmap result = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);
            if (recycle)
            {
                bitmap.recycle();
            }
            return result;
        } else
        {
            return bitmap;
        }
    }
}
