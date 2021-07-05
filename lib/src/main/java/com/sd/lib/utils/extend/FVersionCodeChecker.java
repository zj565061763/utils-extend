package com.sd.lib.utils.extend;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FVersionCodeChecker {
    private static final String KEY_PREFIX = "app_version_code#";

    private final Context mContext;

    public FVersionCodeChecker(@NonNull Context context) {
        mContext = context.getApplicationContext();
    }

    /**
     * 检查版本
     */
    @Nullable
    public CheckResult check() {
        return check(null);
    }

    /**
     * 检查版本
     *
     * @param versionType 版本类型，如果为null，则默认值为包名
     */
    @Nullable
    public CheckResult check(@Nullable String versionType) {
        final Context context = mContext;
        final PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo == null) {
            return null;
        }

        if (TextUtils.isEmpty(versionType)) {
            versionType = context.getPackageName();
        }

        final String cacheKey = KEY_PREFIX + versionType;
        final long cacheVersion = getSharedPreferences(context).getLong(cacheKey, 0);
        final long currentVersion = packageInfo.versionCode;

        return new CheckResult(cacheKey, versionType, cacheVersion, currentVersion);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        final String filename = context.getPackageName() + "_preferences_version_code";
        return context.getSharedPreferences(filename, Context.MODE_PRIVATE);
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

    public class CheckResult {
        private final String cacheKey;
        /** 版本类型 */
        public final String versionType;
        /** 旧版本 */
        public final long oldVersion;
        /** 当前版本 */
        public final long currentVersion;

        private CheckResult(String cacheKey, String versionType, long oldVersion, long currentVersion) {
            this.cacheKey = cacheKey;
            this.versionType = versionType;
            this.oldVersion = oldVersion;
            this.currentVersion = currentVersion;
        }

        public boolean isUpgraded() {
            return currentVersion > oldVersion;
        }

        public void commit() {
            if (isUpgraded()) {
                getSharedPreferences(mContext).edit()
                        .putLong(cacheKey, currentVersion)
                        .commit();
            }
        }
    }
}
