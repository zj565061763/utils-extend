package com.sd.lib.utils.extend;

import java.io.IOException;
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
            int readSize = -1;
            Exception exception = null;

            try
            {
                readSize = mInputStream.read(mBuffer);
            } catch (IOException e)
            {
                exception = e;
            }

            final long sleepTime = exception == null ? onReadData(mBuffer, readSize) : onReadError(exception);

            if (sleepTime > 0)
            {
                try
                {
                    sleep(sleepTime);
                } catch (InterruptedException e)
                {
                    break;
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
     * @return 返回线程睡眠时间，睡眠多久后进行下一次循环
     */
    protected abstract long onReadError(Exception e);
}
