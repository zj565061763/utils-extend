package com.sd.lib.utils.extend;

public abstract class FLoopList<T>
{
    public static final int EMPTY_INDEX = Integer.MIN_VALUE;

    private int mIndex = EMPTY_INDEX;

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
     */
    public void setIndex(int index)
    {
        final int size = size();
        if (size <= 0)
        {
            index = EMPTY_INDEX;
        } else
        {
            if (index >= size)
                index = size - 1;

            if (index < 0)
                index = 0;
        }

        final int old = mIndex;
        if (old != index)
        {
            mIndex = index;
            onIndexChanged(old, mIndex);
        }
    }

    /**
     * 移动索引到后面的位置
     *
     * @param count 移动几个位置
     */
    public void moveIndexNext(int count)
    {
        final int index = calculateIndex(true, count);
        setIndex(index);
    }

    /**
     * 移动索引到前面的位置
     *
     * @param count 移动几个位置
     */
    public void moveIndexPrevious(int count)
    {
        final int index = calculateIndex(false, count);
        setIndex(index);
    }

    /**
     * 返回当前索引所指向的对象
     *
     * @return
     */
    public T getCurrent()
    {
        setIndex(mIndex);
        return mIndex < 0 ? null : get(mIndex);
    }

    /**
     * 返回索引后面第几个位置的对象
     *
     * @param count 索引后面第几个位置
     * @return
     */
    public T getNext(int count)
    {
        final int index = calculateIndex(true, count);
        return index < 0 ? null : get(index);
    }

    /**
     * 返回索引前面第几个位置的对象
     *
     * @return 索引前面第几个位置
     */
    public T getPrevious(int count)
    {
        final int index = calculateIndex(false, count);
        return index < 0 ? null : get(index);
    }

    private int calculateIndex(boolean next, int count)
    {
        if (count <= 0)
            throw new IllegalArgumentException("count is out of range (count > 0)");

        final int size = size();
        if (size <= 0)
            return EMPTY_INDEX;

        final int tempIndex = next ? mIndex + count : mIndex - count;

        int index = 0;
        if (next)
        {
            index = tempIndex < size ? tempIndex : tempIndex % size;
        } else
        {
            index = tempIndex >= 0 ? tempIndex : size + tempIndex % size;
        }
        return index;
    }

    protected void onIndexChanged(int oldIndex, int newIndex)
    {
    }

    protected abstract int size();

    protected abstract T get(int index);
}
