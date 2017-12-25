package com.fanwe.lib.utils.extend;

import android.graphics.Rect;
import android.view.View;

/**
 * 监听view宽高变化
 */
public class FViewDisplayFrameListener extends FViewSizeListener
{
    private Rect mRect = new Rect();

    @Override
    protected int onGetHeight(View view)
    {
        view.getWindowVisibleDisplayFrame(mRect);
        return mRect.height();
    }

    @Override
    protected int onGetWidth(View view)
    {
        view.getWindowVisibleDisplayFrame(mRect);
        return mRect.width();
    }
}
