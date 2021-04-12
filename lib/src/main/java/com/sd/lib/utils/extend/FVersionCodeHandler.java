package com.sd.lib.utils.extend;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

public class FVersionCodeHandler {
    private static final String KEY_PREFIX = "app_version_code#";

    /**
     * 检查App版本号
     *
     * @param context
     * @param callback
     */
    public void check(Context context, Callback callback) {
        if (context == null || callback == null) {
            return;
        }

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences == null) {
            return;
        }

        final PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo == null) {
            return;
        }

        final String key = KEY_PREFIX + context.getPackageName();
        final long cachedVersion = sharedPreferences.getLong(key, 0);

        final long currentVersion = packageInfo.versionCode;
        if (currentVersion > cachedVersion) {
            // 如果版本比较高，更新缓存版本号
            sharedPreferences.edit().putLong(key, currentVersion).commit();
        }

        callback.onVersionCode(cachedVersion, currentVersion);
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
        void onVersionCode(long oldVersion, long currentVersion);
    }
}
