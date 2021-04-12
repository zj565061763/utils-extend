package com.sd.lib.utils.extend;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class FVersionCodeHandler {
    private static final String KEY_PREFIX = "app_version_code#";

    private final Context mContext;

    public FVersionCodeHandler(Context context) {
        mContext = context.getApplicationContext();
    }

    /**
     * 检查版本
     *
     * @param callback
     */
    public void check(Callback callback) {
        check(null, callback);
    }

    /**
     * 检查版本
     *
     * @param versionType 版本类型
     */
    public void check(String versionType, Callback callback) {
        if (callback == null) {
            return;
        }

        final Context context = mContext;
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences == null) {
            return;
        }

        final PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo == null) {
            return;
        }

        if (TextUtils.isEmpty(versionType)) {
            versionType = context.getPackageName();
        }

        final String cacheKey = KEY_PREFIX + versionType;
        final long cacheVersion = sharedPreferences.getLong(cacheKey, 0);

        final long currentVersion = packageInfo.versionCode;
        if (currentVersion > cacheVersion) {
            // 如果版本比较高，更新缓存版本号
            sharedPreferences.edit().putLong(cacheKey, currentVersion).commit();
        }

        callback.onVersionCode(versionType, cacheVersion, currentVersion);
    }

    private static PackageInfo getPackageInfo(Context context) {
        try {
            final PackageManager manager = context.getPackageManager();
            final String packageName = context.getPackageName();
            return manager.getPackageInfo(packageName, PackageManager.GET_CONFIGURATIONS);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public interface Callback {
        /**
         * 版本号回调
         *
         * @param versionType    版本类型
         * @param oldVersion     旧版本
         * @param currentVersion 当前版本
         */
        void onVersionCode(String versionType, long oldVersion, long currentVersion);
    }
}
