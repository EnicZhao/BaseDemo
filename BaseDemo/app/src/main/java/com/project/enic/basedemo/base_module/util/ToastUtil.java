package com.project.enic.basedemo.base_module.util;

import android.widget.Toast;

import com.project.enic.basedemo.base_module.base.BaseApp;

/**
 * 作者： Enic
 * 时间:  2017/5/2.
 * 介绍：
 */

public class ToastUtil {
    public static void showToast( String str) {
        Toast.makeText(BaseApp.getAppContext(),str, Toast.LENGTH_SHORT).show();
    }
}
