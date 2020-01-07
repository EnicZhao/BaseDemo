package com.project.enic.basedemo.base_module.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtil {

    /**
     * 危险权限组
     **/
    public static final String[] CALENDAR;   // 读写日历
    public static final String[] CAMERA;     // 相机
    public static final String[] CONTACTS;   // 读写联系人
    public static final String[] LOCATION;   // 读位置信息
    public static final String[] MICROPHONE; // 使用麦克风
    public static final String[] PHONE;      // 读电话状态、打电话、读写电话记录
    public static final String[] SENSORS;    // 传感器
    public static final String[] SMS;        // 读写短信、收发短信
    public static final String[] STORAGE;    // 读写存储卡
    public static final String[] INSTALLAPK;    // 安装APK权限

    public static final int REQUEST_STORAGE = 1000;
    public static final int REQUEST_CAMERA = 1001;
    public static final int REQUEST_CONTACTS = 1002;
    public static final int REQUEST_LOCATION = 1003;
    public static final int REQUEST_MICROPHONE = 1004;
    public static final int REQUEST_PHONE = 1005;
    public static final int REQUEST_INSTALL_APK = 1006;

    static {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {//当前版本小于6.0
            CALENDAR = new String[]{};
            CAMERA = new String[]{};
            CONTACTS = new String[]{};
            LOCATION = new String[]{};
            MICROPHONE = new String[]{};
            PHONE = new String[]{};
            SENSORS = new String[]{};
            SMS = new String[]{};
            STORAGE = new String[]{};
            INSTALLAPK = new String[]{};
        } else {
            INSTALLAPK = new String[]{
                    Manifest.permission.REQUEST_INSTALL_PACKAGES
            };
            CALENDAR = new String[]{
                    Manifest.permission.READ_CALENDAR,
                    Manifest.permission.WRITE_CALENDAR};

            CAMERA = new String[]{
                    Manifest.permission.CAMERA};

            CONTACTS = new String[]{
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.GET_ACCOUNTS};

            LOCATION = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION};

            MICROPHONE = new String[]{
                    Manifest.permission.RECORD_AUDIO};

            PHONE = new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.WRITE_CALL_LOG,
                    Manifest.permission.ADD_VOICEMAIL,
                    Manifest.permission.USE_SIP,
                    Manifest.permission.PROCESS_OUTGOING_CALLS};

            SENSORS = new String[]{
                    Manifest.permission.BODY_SENSORS};

            SMS = new String[]{
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_WAP_PUSH,
                    Manifest.permission.RECEIVE_MMS};

            STORAGE = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }
    }

    public static boolean hasPermission(Context context, String[] permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;
        for (String permission : permissions) {
            return hasPermission(context, permission);
        }
        return true;
    }

    public static boolean hasPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
            return false;
        return true;
    }

    public static void requestPermission(Activity activity, String[] permissions, int request) {
        ActivityCompat.requestPermissions(activity, permissions, request);
    }

    public static String[] getPermissions(int permissionCode){
        String[] result = null;
        switch (permissionCode){
            case REQUEST_CAMERA:
                result = CAMERA;
                break;
            case REQUEST_STORAGE:
                result = STORAGE;
                break;
            case REQUEST_CONTACTS:
                result = CONTACTS;
                break;
            case REQUEST_LOCATION:
                result = LOCATION;
                break;
            case REQUEST_MICROPHONE:
                result = MICROPHONE;
                break;
            case REQUEST_PHONE:
                result = PHONE;
                break;
            case REQUEST_INSTALL_APK:
                result = INSTALLAPK;
                break;
        }
        return result;
    }

    public static String getPermissionName(int permissionCode){
        String result = "相关权限";
        switch (permissionCode){
            case REQUEST_CAMERA:
                result = "相机权限";
                break;
            case REQUEST_STORAGE:
                result = "存储权限";
                break;
            case REQUEST_CONTACTS:
                result = "读写联系人权限";
                break;
            case REQUEST_LOCATION:
                result = "位置信息权限";
                break;
            case REQUEST_MICROPHONE:
                result = "麦克风权限";
                break;
            case REQUEST_PHONE:
                result = "电话权限";
                break;
            case REQUEST_INSTALL_APK:
                result = "安装APK权限";
                break;
        }
        return result;
    }

}
