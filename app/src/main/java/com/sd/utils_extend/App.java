package com.sd.utils_extend;

import android.app.Application;
import android.util.Log;

import com.sd.lib.utils.extend.FNotificationManager;
import com.sd.lib.utils.extend.FVersionCodeChecker;

/**
 * Created by Administrator on 2018/2/2.
 */
public class App extends Application {
    public static final String TAG = App.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        FNotificationManager.getInstance().init(this);

        final FVersionCodeChecker.CheckResult checkResult = new FVersionCodeChecker(this).check();
        if (checkResult != null) {
            Log.i(TAG, "onVersionCode"
                    + " isUpgraded:" + checkResult.isUpgraded()
                    + " versionType:" + checkResult.versionType
                    + " oldVersion:" + checkResult.oldVersion
                    + " currentVersion:" + checkResult.currentVersion);
            checkResult.commit();
        }
    }
}
