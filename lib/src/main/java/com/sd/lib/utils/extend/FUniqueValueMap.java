package com.sd.lib.utils.extend;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用https://github.com/zj565061763/collection-map替代
 *
 * @param <K>
 * @param <V>
 */
@Deprecated
public class FUniqueValueMap<K, V> {
    private final Map<K, V> mMap = new ConcurrentHashMap<>();
    private final Map<V, K> mMapReverse = new ConcurrentHashMap<>();

    public void put(K key, V value) {
        if (key == null || value == null)
            return;

        final K cacheKey = mMapReverse.get(value);
        if (cacheKey == null) {
            // 存储键值对
        } else {
            if (key.equals(cacheKey)) {
                // 已经存储过了
                return;
            } else {
                // 移除旧的键值对，存储新的键值对
                mMap.remove(cacheKey);
            }
        }

        mMap.put(key, value);
        mMapReverse.put(value, key);
    }

    public V remove(Object key) {
        if (key == null)
            return null;

        final V value = mMap.remove(key);
        if (value == null)
            return null;

        final K cacheKey = mMapReverse.remove(value);
        if (cacheKey == null)
            throw new RuntimeException("Cached key was not found");

        return value;
    }

    public V get(Object key) {
        if (key == null)
            return null;

        return mMap.get(key);
    }

    public K removeValue(Object value) {
        if (value == null)
            return null;

        final K key = mMapReverse.remove(value);
        if (key == null)
            return null;

        final V cacheValue = mMap.remove(key);
        if (cacheValue == null)
            throw new RuntimeException("Cached value was not found");

        return key;
    }

    public K getKey(Object value) {
        if (value == null)
            return null;

        return mMapReverse.get(value);
    }

    public int size() {
        return mMap.size();
    }

    public void clear() {
        mMap.clear();
        mMapReverse.clear();
    }

    public Map<K, V> toMap() {
        return new HashMap<>(mMap);
    }
}
