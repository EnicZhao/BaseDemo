package com.project.enic.basedemo.base_module.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.WindowManager;

import java.lang.reflect.Field;

import androidx.annotation.NonNull;
import androidx.customview.widget.ViewDragHelper;
import androidx.drawerlayout.widget.DrawerLayout;

/**
 * 工具类
 */
public class ToolUtil {

    /**
     * 获取随机字符串
     */
    public static String getUUID() {
        String uuid = java.util.UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

    /**
     * 获取设备软件版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = null;
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取设备屏幕宽度
     */
    public static int getScreenWidth(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int mScreenWidth = windowManager.getDefaultDisplay().getWidth();
        return mScreenWidth;
    }

    /**
     * 获取设备屏幕高度
     */
    public static int getScreenHeight(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int mScreenHeight = windowManager.getDefaultDisplay().getHeight();
        return mScreenHeight;
    }

    /**
     *  获取设备density
     */
    public static float getScreenDensity(Context context){
        float mDensity = context.getResources().getDisplayMetrics().density;
        return mDensity;
    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, int dp){
        if (dp == 0) {
            return 0;
        }
        return (int) (dp * getScreenDensity(context) + 0.5f);
    }

    /**
     * px转dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取本地软件版本号名称
     */
    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * drawerLayout设置拉动边距
     */
    public static void setCustomLeftEdgeSize(@NonNull DrawerLayout drawerLayout, float displayWidthPercentage) {
        try {
            Field leftDraggerField = drawerLayout.getClass().getDeclaredField("mLeftDragger");
            if (leftDraggerField == null) {
                return;
            }
            leftDraggerField.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField.get(drawerLayout);
            Field edgeSizeField = leftDragger.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(leftDragger);
            int widthPixels = getScreenWidth(drawerLayout.getContext());
            edgeSizeField.setInt(leftDragger, Math.max(edgeSize, (int) (widthPixels * displayWidthPercentage)));
            Object o = edgeSizeField.get(drawerLayout);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
