package com.test.sun.tosystem;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.test.sun.tosystem.utils.Commands;
import com.test.sun.tosystem.utils.FileRecords;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "TestVar";

    public static final int REQUEST_FILE_APK = 0x10;

    private Button bt_root, bt_reboot, bt_select, bt_delall;
    private TextView tv_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();

    }

    private void initView() {
        bt_root = (Button) findViewById(R.id.main_bt_root);
        bt_reboot = (Button) findViewById(R.id.main_bt_reboot);
        bt_select = (Button) findViewById(R.id.main_bt_select);
        bt_delall = (Button) findViewById(R.id.main_bt_delall);
        tv_show = (TextView) findViewById(R.id.main_tv_showlist);

        refreshTv();
    }

    private void initEvent() {
        bt_root.setOnClickListener(this);
        bt_reboot.setOnClickListener(this);
        bt_select.setOnClickListener(this);
        bt_delall.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_bt_root:
                Commands.RootCommand("chmod 777 " + getPackageCodePath());
                break;
            case R.id.main_bt_reboot:
                new AlertDialog.Builder(this)
                        .setTitle("警告：系统将重启")
                        .setMessage("是否确定重启？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Commands.reboot();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
                break;
            case R.id.main_bt_select:
                getFile();
                break;
            case R.id.main_bt_delall:
                removeFromSystem();
                break;
        }
    }

    private void getFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"), REQUEST_FILE_APK);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "请安装文件管理器", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeFromSystem() {
        Set<String> record = FileRecords.getRecord(this);
        if (record != null && record.size() > 0) {
            Iterator<String> iterator = record.iterator();
            while (iterator.hasNext()) {
                Commands.delFromSystemApp(iterator.next());
            }
            FileRecords.delRecord(this);
            refreshTv();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || requestCode != REQUEST_FILE_APK) {
            return;
        }
        String scheme = data.getScheme();
        String path = data.getData().getPath();
        if ("file".equals(scheme) && path.endsWith(".apk")) {
            File file = new File(path);
            Log.i(TAG, "file:" + file.getAbsolutePath());
            if (file.exists()) {
                boolean result = Commands.writeToSystemApp(file);
                if (result) {
                    FileRecords.fileRecord(this, file.getName());
                }
                Toast.makeText(this, result ? "植入成功" : "植入失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "请选择apk文件", Toast.LENGTH_SHORT).show();
        }
        refreshTv();
    }

    private void refreshTv() {
        tv_show.setText("");
        Set<String> record = FileRecords.getRecord(this);
        if (record != null && record.size() > 0) {
            Iterator<String> iterator = record.iterator();
            while (iterator.hasNext()) {
                tv_show.append("--" + iterator.next() + "\n");
            }
        }
    }
}
