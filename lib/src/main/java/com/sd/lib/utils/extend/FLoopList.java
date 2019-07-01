package com.sd.lib.utils.extend;

public abstract class FLoopList<T>
{
    private int mIndex = 0;

    /**
     * 返回索引位置
     *
     * @return
     */
    public int getIndex()
    {
        return mIndex;
    }

    /**
     * 设置索引位置
     *
     * @param index
     * @return
     */
    public int setIndex(int index)
    {
        final int size = size();
        if (size <= 0)
        {
            index = -1;
        } else
        {
            if (index >= size)
                index = size - 1;

            if (index < 0)
                index = 0;
        }

        mIndex = index;
        return mIndex;
    }

    /**
     * 当前对象
     *
     * @return
     */
    public T current()
    {
        final int index = setIndex(mIndex);
        return index < 0 ? null : get(index);
    }

    /**
     * 下一个对象
     *
     * @return
     */
    public T next()
    {
        final int index = setIndex(newIndex(true));
        return index < 0 ? null : get(index);
    }

    /**
     * 前一个对象
     *
     * @return
     */
    public T previous()
    {
        final int index = setIndex(newIndex(false));
        return index < 0 ? null : get(index);
    }

    private int newIndex(boolean next)
    {
        final int size = size();
        if (size <= 0)
            return -1;

        int tempIndex = next ? mIndex + 1 : mIndex - 1;
        int index = 0;
        if (next)
        {
            index = tempIndex < size ? tempIndex : tempIndex % size;
        } else
        {
            if (tempIndex >= 0)
            {
                index = tempIndex;
            } else
            {
                tempIndex = tempIndex % size;
                index = size + tempIndex;
            }
        }
        return index;
    }

    protected abstract int size();

    protected abstract T get(int index);
}
