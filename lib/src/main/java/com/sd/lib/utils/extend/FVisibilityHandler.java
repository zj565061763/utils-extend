package com.sd.lib.utils.extend;

import android.view.View;

import java.util.Map;
import java.util.WeakHashMap;

public class FVisibilityHandler {
    private final Map<View, Integer> mMapView = new WeakHashMap<>();

    /**
     * 添加到{@link View#VISIBLE}
     *
     * @param views
     */
    public void visible(View... views) {
        if (views == null)
            return;

        for (View item : views) {
            if (item != null)
                mMapView.put(item, View.VISIBLE);
        }
    }

    /**
     * 添加到{@link View#INVISIBLE}
     *
     * @param views
     */
    public void invisible(View... views) {
        if (views == null)
            return;

        for (View item : views) {
            if (item != null)
                mMapView.put(item, View.INVISIBLE);
        }
    }

    /**
     * 添加到{@link View#GONE}
     *
     * @param views
     */
    public void gone(View... views) {
        if (views == null)
            return;

        for (View item : views) {
            if (item != null)
                mMapView.put(item, View.GONE);
        }
    }

    /**
     * 移除View
     *
     * @param views
     */
    public void remove(View... views) {
        if (views == null)
            return;

        for (View item : views) {
            if (item != null)
                mMapView.remove(item);
        }
    }

    /**
     * 触发所有View的可见状态变化
     */
    public void invoke() {
        if (mMapView.isEmpty())
            return;

        for (Map.Entry<View, Integer> item : mMapView.entrySet()) {
            item.getKey().setVisibility(item.getValue());
        }
    }
}
