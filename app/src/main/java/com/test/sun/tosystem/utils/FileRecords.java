package com.test.sun.tosystem.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ZS27 on 2016/12/21.
 */

public class FileRecords {
    public static final String TAG = "TestVar";

    public static final String SYSTEMAPPS_NAME = "systemapp";
    public static final String SYSTEMAPPS_KEY = "systemapp_name_set";

    public synchronized static Set<String> getRecord(Context context) {
        SharedPreferences systemapp = context.getSharedPreferences(SYSTEMAPPS_NAME, Context.MODE_PRIVATE);
        Set<String> set = new HashSet<>();
        set = systemapp.getStringSet(SYSTEMAPPS_KEY, set);

        Log.i(TAG, "getRecord:" + set.size());
        return set;
    }

    public synchronized static boolean fileRecord(Context context, String name) {
        Log.i(TAG, "FileRecords:fileRecord----------------");
        SharedPreferences systemapp = context.getSharedPreferences(SYSTEMAPPS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = systemapp.edit();

        Set<String> record = getRecord(context);
        record.add(name);
        edit.putStringSet(SYSTEMAPPS_KEY, record);

        return edit.commit();
    }

    public synchronized static boolean delRecord(Context context) {
        Log.i(TAG, "FileRecords:delRecord----------------");
        SharedPreferences systemapp = context.getSharedPreferences("systemapps", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = systemapp.edit();

        Set<String> record = getRecord(context);
        record.clear();
        edit.putStringSet(SYSTEMAPPS_KEY, record);

        return edit.commit();
    }
}
