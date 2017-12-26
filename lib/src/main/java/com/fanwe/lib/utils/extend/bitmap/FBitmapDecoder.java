package com.fanwe.lib.utils.extend.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

/**
 * Created by Administrator on 2017/12/26.
 */
public class FBitmapDecoder
{
    private Bitmap mBitmap;

    public FBitmapDecoder()
    {
    }

    public FBitmapDecoder(Bitmap bitmap)
    {
        mBitmap = bitmap;
    }

    public FBitmapDecoder decodeFile(String filePath, int targetWidth)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        int originalWidth = options.outWidth;
        int originalHeight = options.outHeight;
        int targetHeight = getScaleHeight(originalWidth, originalHeight, targetWidth);

        options.inSampleSize = calculateInSampleSize(options, targetWidth, targetHeight);
        options.inJustDecodeBounds = false;

        try
        {
            Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
            mBitmap = scaleBitmapIfNeed(bmp, targetWidth, targetHeight, true);
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
        if (bitmap.getWidth() == targetHeight && bitmap.getHeight() == targetHeight)
        {
            return bitmap;
        } else
        {
            Bitmap result = ThumbnailUtils.extractThumbnail(bitmap, targetWidth, targetHeight);
            if (recycle)
            {
                bitmap.recycle();
            }
            return result;
        }
    }
}
