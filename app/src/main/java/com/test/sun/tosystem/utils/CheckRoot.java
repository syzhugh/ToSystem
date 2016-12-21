package com.test.sun.tosystem.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by ZS27 on 2016/12/21.
 */

public class CheckRoot {

    public static final String TAG = "TestVar";

    @Deprecated
    public static boolean isDeviceRooted() {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3() || checkRootMethod4();
    }

    @Deprecated
    public static boolean checkRootMethod1() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    @Deprecated
    public static boolean checkRootMethod2() {
        return new File("/system/app/Superuser.apk").exists();
    }

    @Deprecated
    public static boolean checkRootMethod3() {
        String[] paths = {"/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su"};
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    @Deprecated
    public static boolean checkRootMethod4() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));

            if (in.readLine() != null) {
                process.destroy();
                Log.i(TAG, "CheckRoot:checkRootMethod4----------------");
                return true;
            }
            return false;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }
}
