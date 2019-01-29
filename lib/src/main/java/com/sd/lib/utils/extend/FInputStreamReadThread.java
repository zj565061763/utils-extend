package com.sd.lib.utils.extend;

import java.io.InputStream;

/**
 * 输入流读取线程
 */
public abstract class FInputStreamReadThread extends Thread
{
    private final InputStream mInputStream;
    private ReadConfig mReadConfig;

    public FInputStreamReadThread(InputStream inputStream, ReadConfig config)
    {
        if (inputStream == null)
            throw new NullPointerException();

        mInputStream = inputStream;
        mReadConfig = config;
    }

    @Override
    public final void run()
    {
        super.run();

        while (!isInterrupted())
        {
            try
            {
                final byte[] buffer = mReadConfig.mBuffer;
                final int readSize = mInputStream.read(buffer);
                onReadData(buffer, readSize);

                if (readSize < 0)
                {
                    final long sleepTime = mReadConfig.mSleepTime;
                    if (sleepTime > 0)
                        sleep(sleepTime);
                }
            } catch (Exception e)
            {
                if (e instanceof InterruptedException)
                {
                    return;
                } else
                {
                    onReadError(e);
                }
            }
        }
    }

    /**
     * 读取数据
     *
     * @param data
     * @param readSize
     */
    protected abstract void onReadData(byte[] data, int readSize);

    /**
     * 读取异常
     *
     * @param e
     */
    protected abstract void onReadError(Exception e);

    /**
     * 读取配置
     */
    public static class ReadConfig
    {
        /**
         * 用于接收读取数据的字节数组
         */
        public final byte[] mBuffer;
        /**
         * 如果当次没读取到数据，睡眠多长时间
         */
        public final long mSleepTime;

        public ReadConfig()
        {
            this(64, 0);
        }

        public ReadConfig(int bufferSize, long sleepTime)
        {
            if (bufferSize <= 0)
                throw new IllegalArgumentException();

            if (sleepTime < 0)
                throw new IllegalArgumentException();

            mBuffer = new byte[bufferSize];
            mSleepTime = sleepTime;
        }
    }
}
