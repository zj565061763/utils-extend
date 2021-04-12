package com.sd.utils_extend;

import android.app.Application;
import android.util.Log;

import com.sd.lib.utils.extend.FNotificationManager;
import com.sd.lib.utils.extend.FVersionCodeHandler;

/**
 * Created by Administrator on 2018/2/2.
 */
public class App extends Application {
    public static final String TAG = App.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        FNotificationManager.getInstance().init(this);

        new FVersionCodeHandler(this).check(new FVersionCodeHandler.Callback() {
            @Override
            public void onVersionCode(String versionType, long oldVersion, long currentVersion) {
                Log.i(TAG, "onVersionCode"
                        + " versionType:" + versionType
                        + " oldVersion:" + oldVersion
                        + " currentVersion:" + currentVersion);
            }
        });
    }
}
