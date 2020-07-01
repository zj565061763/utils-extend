package com.sd.lib.utils.extend;

import android.content.Context;
import android.media.AudioManager;
import android.view.KeyEvent;

/**
 * 音量键拦截
 */
public class FVolumeInterceptor
{
    private final Context mContext;
    private final AudioManager mAudioManager;
    private final int mStreamType;

    private Callback mCallback;

    public FVolumeInterceptor(Context context)
    {
        this(context, AudioManager.STREAM_MUSIC);
    }

    public FVolumeInterceptor(Context context, int streamType)
    {
        mContext = context.getApplicationContext();
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mStreamType = streamType;
    }

    /**
     * 设置回调对象
     *
     * @param callback
     */
    public void setCallback(Callback callback)
    {
        mCallback = callback;
    }

    /**
     * 分发{@link KeyEvent}事件
     *
     * @param event
     * @return
     */
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        final int code = event.getKeyCode();
        final int action = event.getAction();
        if (code == KeyEvent.KEYCODE_VOLUME_UP && action == KeyEvent.ACTION_DOWN)
        {
            mAudioManager.adjustStreamVolume(mStreamType, AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
            notifyVolumeChanged();
            return true;
        } else if (code == KeyEvent.KEYCODE_VOLUME_DOWN && action == KeyEvent.ACTION_DOWN)
        {
            mAudioManager.adjustStreamVolume(mStreamType, AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
            notifyVolumeChanged();
            return true;
        }
        return false;
    }

    /**
     * 通知音量变化
     */
    public void notifyVolumeChanged()
    {
        final int maxVolume = mAudioManager.getStreamMaxVolume(mStreamType);
        final int currentVolume = mAudioManager.getStreamVolume(mStreamType);

        if (mCallback != null)
            mCallback.onVolumeChanged(maxVolume, currentVolume);
    }

    public interface Callback
    {
        /**
         * 音量变化回调
         *
         * @param max
         * @param current
         */
        void onVolumeChanged(int max, int current);
    }
}
