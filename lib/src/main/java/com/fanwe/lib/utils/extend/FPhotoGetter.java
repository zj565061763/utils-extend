package com.fanwe.lib.utils.extend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

public class FPhotoGetter
{
    public static final int REQUEST_CODE_GET_PHOTO_FROM_CAMERA = 16542;
    public static final int REQUEST_CODE_GET_PHOTO_FROM_ALBUM = REQUEST_CODE_GET_PHOTO_FROM_CAMERA + 1;

    private Activity mActivity;
    private File mTakePhotoDir;
    private File mTakePhotoFile;

    private Callback mCallback;

    public FPhotoGetter(Activity activity)
    {
        mActivity = activity;
        if (activity == null)
        {
            throw new NullPointerException("activity is null");
        }
    }

    public void setCallback(Callback callback)
    {
        mCallback = callback;
    }

    private File getTakePhotoDir()
    {
        if (mTakePhotoDir == null)
        {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (dir == null)
            {
                dir = mActivity.getCacheDir();
            }
            mTakePhotoDir = dir;
        }
        if (!mTakePhotoDir.exists())
        {
            mTakePhotoDir.mkdirs();
        }
        return mTakePhotoDir;
    }

    /**
     * 跳转到系统相册获取图片
     */
    public void getPhotoFromAlbum()
    {
        try
        {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            mActivity.startActivityForResult(intent, REQUEST_CODE_GET_PHOTO_FROM_ALBUM);
        } catch (Exception e)
        {
            if (mCallback != null)
            {
                mCallback.onError(String.valueOf(e));
            }
        }
    }

    /**
     * 打开相机拍照
     */
    public void getPhotoFromCamera()
    {
        if (getTakePhotoDir() == null)
        {
            if (mCallback != null)
            {
                mCallback.onError("获取缓存目录失败");
            }
            return;
        }
        try
        {
            mTakePhotoFile = newFileUnderDir(getTakePhotoDir(), ".jpg");
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTakePhotoFile));
            mActivity.startActivityForResult(intent, REQUEST_CODE_GET_PHOTO_FROM_CAMERA);
        } catch (Exception e)
        {
            if (mCallback != null)
            {
                mCallback.onError(String.valueOf(e));
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (mCallback == null)
        {
            return;
        }
        switch (requestCode)
        {
            case REQUEST_CODE_GET_PHOTO_FROM_CAMERA:
                if (resultCode == Activity.RESULT_OK)
                {
                    scanFile(mActivity, mTakePhotoFile);
                    mCallback.onResultFromCamera(mTakePhotoFile);
                }
                break;
            case REQUEST_CODE_GET_PHOTO_FROM_ALBUM:
                if (resultCode == Activity.RESULT_OK)
                {
                    if (data == null)
                    {
                        mCallback.onError("从相册获取图片失败(intent为空)");
                        return;
                    }
                    Uri uri = data.getData();
                    if (uri == null)
                    {
                        mCallback.onError("从相册获取图片失败(intent数据为空)");
                        return;
                    }
                }
                break;
            default:
                break;
        }
    }

    private static File newFileUnderDir(File dir, String ext)
    {
        if (dir == null)
        {
            return null;
        }
        if (ext == null)
        {
            ext = "";
        }
        long current = System.currentTimeMillis();
        File file = new File(dir, String.valueOf(current + ext));
        while (file.exists())
        {
            current++;
            file = new File(dir, String.valueOf(current + ext));
        }
        return file;
    }

    private static void scanFile(Context context, File file)
    {
        if (file == null || !file.exists())
        {
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        context.sendBroadcast(intent);
    }

    public interface Callback
    {
        void onResultFromAlbum(File file);

        void onResultFromCamera(File file);

        void onError(String desc);
    }
}
