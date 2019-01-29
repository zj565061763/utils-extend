package com.sd.lib.utils.extend;

import java.io.InputStream;

/**
 * 输入流读取线程
 */
public abstract class FInputStreamReadThread extends Thread
{
    private final InputStream mInputStream;
    private final byte[] mBuffer;

    public FInputStreamReadThread(InputStream inputStream, int bufferSize)
    {
        if (inputStream == null)
            throw new NullPointerException();

        if (bufferSize <= 0)
            throw new IllegalArgumentException();

        mInputStream = inputStream;
        mBuffer = new byte[bufferSize];
    }

    @Override
    public final void run()
    {
        super.run();

        while (!isInterrupted())
        {
            try
            {
                final int readSize = mInputStream.read(mBuffer);
                final long sleepTime = onReadData(mBuffer, readSize);

                if (sleepTime > 0)
                    sleep(sleepTime);

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
     * @return 返回线程睡眠时间，睡眠多久后进行下一次循环
     */
    protected abstract long onReadData(byte[] data, int readSize);

    /**
     * 读取异常
     *
     * @param e
     */
    protected abstract void onReadError(Exception e);
}
