package com.fanwe.lib.utils.extend;

import java.io.InputStream;

/**
 * 输入流读取线程
 */
public abstract class FInputStreamReadThread extends Thread
{
    private InputStream mInputStream;

    private byte[] mBuffer;
    private int mBufferSize;
    private long mSleepMillis;

    public FInputStreamReadThread(int bufferSize, long sleepMillis)
    {
        super("FInputStreamReadThread");
        if (bufferSize <= 0)
        {
            throw new IllegalArgumentException("bufferSize must > 0");
        }
        mBufferSize = bufferSize;
        mSleepMillis = sleepMillis;
    }

    public void setInputStream(InputStream inputStream)
    {
        mInputStream = inputStream;
    }

    private void trySleep() throws InterruptedException
    {
        if (mSleepMillis > 0)
        {
            sleep(mSleepMillis);
        }
    }

    private byte[] getBuffer()
    {
        if (mBuffer == null)
        {
            mBuffer = new byte[mBufferSize];
        }
        return mBuffer;
    }

    @Override
    public void run()
    {
        super.run();

        while (!isInterrupted())
        {
            try
            {
                if (mInputStream == null)
                {
                    trySleep();
                } else
                {
                    int readSize = mInputStream.read(getBuffer());
                    if (readSize > 0)
                    {
                        onReadData(getBuffer(), readSize);
                    } else
                    {
                        trySleep();
                    }
                }
            } catch (Exception e)
            {
                if (e instanceof InterruptedException)
                {
                    return;
                } else
                {
                    onError(e);
                }
            }
        }
    }

    /**
     * 读取到输入流数据
     *
     * @param data
     * @param readSize
     */
    protected abstract void onReadData(byte[] data, int readSize);

    /**
     * 异常
     *
     * @param e
     */
    protected abstract void onError(Exception e);
}
