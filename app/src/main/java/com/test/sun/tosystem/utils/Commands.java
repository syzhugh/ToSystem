package com.test.sun.tosystem.utils;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;

/**
 * Created by ZS27 on 2016/12/19.
 * <p>
 * app内申请root的权限
 */

public class Commands {

    private static String SYSTEMSTATE_RW = "mount -o remount,rw /system";
    private static String SYSTEMSTATE_RO = "mount -o remount,ro /system";

    /**
     * 应用程序运行命令获取 Root权限，设备必须获得ROOT权限
     *
     * @param command 命令：String apkRoot="chmod 777 "+getPackageCodePath(); RootCommand(apkRoot);
     * @return 应用程序是/否获取Root权限
     */

    public static boolean RootCommand(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        Log.d("*** DEBUG ***", "Root SUC ");
        return true;
    }


    public static boolean writeToSystemApp(File file) {
        if (!file.exists()) {
            return false;
        }
        StringBuilder moveFile = new StringBuilder("cp -f ");
        moveFile.append(file.getAbsoluteFile());
        moveFile.append(" /system/app");
        String chmod = "chmod 755 /system/app/" + file.getName();
        String chown = "chown root:root /system/app/" + file.getName();

        Process process = null;
        DataOutputStream os = null;

        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(SYSTEMSTATE_RW + "\n");
            os.writeBytes(moveFile.toString() + "\n");
            os.writeBytes(chmod + "\n");
            os.writeBytes(chown + "\n");
            os.writeBytes(SYSTEMSTATE_RO + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
            return false;
        }
        return true;
    }

    public static boolean delFromSystemApp(String name) {
        String delFile = "rm /system/app/" + name;

        Process process = null;
        DataOutputStream os = null;

        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(SYSTEMSTATE_RW + "\n");
            os.writeBytes(delFile + "\n");
            os.writeBytes(SYSTEMSTATE_RO + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
            return false;
        }
        return true;

    }

    public static boolean reboot() {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("reboot\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
            return false;
        }
        return true;
    }

}
