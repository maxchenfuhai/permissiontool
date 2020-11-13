package com.hongbo.mylibrary;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import java.io.File;

public class LogUtils {
    public static boolean isDebug;

    public static void showLog(Object object) {
        if (isDebug) {
            if (object == null) {
                Log.i("wo", "null");
                return;
            }
            String str = object.toString();
            int length = str.length();
            int k = length / 3910 + 1;
            if (k == 1) {
                Log.i("wo", str);
                return;
            }
            for (int i = 0; i < k; i++) {
                if (i != k - 1) {
                    Log.i("wo", str.substring(i * 3910, (i + 1) * 3910));
                } else {
                    Log.i("wo", str.substring(i * 3910, str.length()));
                }
            }
        }
    }

    public static boolean isApkDebugable(Context context) {
        if (!isDebug) {
            try {
                ApplicationInfo info = context.getApplicationInfo();
                return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            } catch (Exception e) {

            }
        }

        return false;
    }

    public static boolean isRoot() {
        if (!isDebug) {
            String binPath = "/system/bin/su";
            String xBinPath = "/system/xbin/su";

            if (new File(binPath).exists()) {
                return true;
            }
            if (new File(xBinPath).exists()) {
                return true;
            }
        }

        return false;
    }
}
