package com.sd.lib.utils.extend;

/**
 * 缩放计算处理
 */
public class FScaleFit
{
    private final Type mType;

    private int mContainerWidth;
    private int mContainerHeight;

    private int mScaledWidth;
    private int mScaledHeight;

    public FScaleFit()
    {
        this(Type.FitMax);
    }

    public FScaleFit(Type type)
    {
        if (type == null)
            throw new IllegalArgumentException("type is null");
        mType = type;
    }

    /**
     * 缩放模式
     *
     * @return
     */
    public Type getType()
    {
        return mType;
    }

    /**
     * 缩放后的宽
     *
     * @return
     */
    public int getScaledWidth()
    {
        return mScaledWidth;
    }

    /**
     * 缩放后的高
     *
     * @return
     */
    public int getScaledHeight()
    {
        return mScaledHeight;
    }

    /**
     * 设置容器大小
     *
     * @param width
     * @param height
     */
    public void setContainer(int width, int height)
    {
        mContainerWidth = width;
        mContainerHeight = height;
    }

    /**
     * 缩放
     *
     * @param width
     * @param height
     * @return
     */
    public boolean scale(int width, int height)
    {
        if (mContainerWidth <= 0 || mContainerHeight <= 0)
            return false;

        if (width <= 0 || height <= 0)
            return false;

        switch (mType)
        {
            case FitWidth:
                scaleFitWidth(width, height);
                break;
            case FitHeight:
                scaleFitHeight(width, height);
                break;
            case FitMax:
                scaleFitMax(width, height);
                break;
            default:
                throw new RuntimeException("Unknown type:" + mType);
        }

        return true;
    }

    private void scaleFitWidth(int width, int height)
    {
        final float scale = (float) mContainerWidth / width;
        final float finalHeight = scale * height;

        mScaledWidth = mContainerWidth;
        mScaledHeight = (int) (finalHeight + 0.5f);
    }

    private void scaleFitHeight(int width, int height)
    {
        final float scale = (float) mContainerHeight / height;
        final float finalWidth = scale * width;

        mScaledWidth = (int) (finalWidth + 0.5f);
        mScaledHeight = mContainerHeight;
    }

    private void scaleFitMax(int width, int height)
    {
        if (width == mContainerWidth && height == mContainerHeight)
        {
            mScaledWidth = width;
            mScaledHeight = height;
            return;
        }

        scaleFitWidth(width, height);

        if (mScaledWidth <= mContainerWidth && mScaledHeight <= mContainerHeight)
            return;

        scaleFitHeight(width, height);
    }

    public enum Type
    {
        /**
         * 缩放宽度到容器的宽度
         */
        FitWidth,
        /**
         * 缩放高度到容器的高度
         */
        FitHeight,
        /**
         * 在容器范围内缩放内容到最大值
         */
        FitMax,
    }
}
