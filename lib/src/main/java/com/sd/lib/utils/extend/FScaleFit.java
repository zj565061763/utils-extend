package com.sd.lib.utils.extend;

/**
 * 缩放计算处理
 */
public class FScaleFit
{
    private final Type mType;

    private int mContainerWidth;
    private int mContainerHeight;

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
     * 缩放内容
     *
     * @param width
     * @param height
     * @param output 保存缩放结果的数组
     * @return
     */
    public boolean scaleContent(int width, int height, int[] output)
    {
        if (output == null)
            throw new IllegalArgumentException("output is null");

        if (mContainerWidth <= 0 || mContainerHeight <= 0)
            return false;

        if (width <= 0 || height <= 0)
            return false;

        switch (mType)
        {
            case FitWidth:
                scaleFitWidth(width, height, output);
                break;
            case FitHeight:
                scaleFitHeight(width, height, output);
                break;
            case FitMax:
                scaleFitMax(width, height, output);
                break;
            default:
                break;
        }
        return true;
    }

    private void scaleFitWidth(int width, int height, int[] output)
    {
        final float scale = (float) mContainerWidth / width;
        final float finalHeight = scale * height;

        output[0] = mContainerWidth;
        output[1] = (int) (finalHeight + 0.5f);
    }

    private void scaleFitHeight(int width, int height, int[] output)
    {
        final float scale = (float) mContainerHeight / height;
        final float finalWidth = scale * width;

        output[0] = (int) (finalWidth + 0.5f);
        output[1] = mContainerHeight;
    }

    private void scaleFitMax(int width, int height, int[] output)
    {
        final int deltaWidth = Math.abs(width - mContainerWidth);
        final int deltaHeight = Math.abs(height - mContainerHeight);

        if (deltaWidth < deltaHeight)
        {
            scaleFitWidth(width, height, output);
        } else
        {
            scaleFitHeight(width, height, output);
        }
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
