package com.project.enic.basedemo.base_module.util.sharepreference;

import android.content.Context;

import com.project.enic.basedemo.base_module.base.BaseApp;

/**
 * 作者： Enic
 * 时间:  2017/3/21.
 * 介绍： 封装SharePreference工具类
 */

public class SharePreferenceUtil extends BaseSharePreference{

    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";
    public static final String REMEMBER_PASSWORD = "remember_password";
    public static final String AUTO_LOGIN = "auto_login";
    public static final String USER_ENTITY = "user_entity";
    public static final String COOKIE = "cookie";
    public static final String HISTORY = "history";
    public static final String NOT_FIRST_LAUNCH = "not_first_launch";//是否是第一次启动项目

    public static SharePreferenceUtil mShareUtil;
    private SharePreferenceUtil(Context context) {
        super(context);
    }

    public static SharePreferenceUtil init(){
        if(mShareUtil == null){
            synchronized (SharePreferenceUtil.class){
                mShareUtil = new SharePreferenceUtil(BaseApp.getAppContext());
            }
        }
        if (!getBooleanMemory(NOT_FIRST_LAUNCH)){
            setBooleanMemory(NOT_FIRST_LAUNCH,true);
            initConfig();
        }
        return mShareUtil;
    }

    public static void clear(){
        mShareUtil.clearAll();
    }

    //初始化软件配置
    public static void initConfig(){
        mShareUtil.setBoolean(REMEMBER_PASSWORD,true);
        mShareUtil.setBoolean(AUTO_LOGIN,true);
    }

    public static void setStringMemory(String key, String value){
        mShareUtil.setString(key,value);
    }

    public static String getStringMemory(String key){
        return mShareUtil.getString(key);
    }

    public static void setLongMemory(String key, Long value){
        mShareUtil.setLong(key,value);
    }

    public static long getLongMemory(String key){
        return mShareUtil.getLong(key);
    }

    public static void setIntMemory(String key, int value){
        mShareUtil.setInt(key,value);
    }

    public static int getIntMemory(String key){
        return mShareUtil.getInt(key);
    }

    public static void
    setBooleanMemory(String key, boolean value){
        mShareUtil.setBoolean(key,value);
    }

    public static boolean getBooleanMemory(String key){
        return mShareUtil.getBoolean(key);
    }

    public static void setObjectMemory(String key, Object object){
        mShareUtil.setObject(key,object);
    }

    public static Object getObjectMemory(String key){
        return mShareUtil.getObject(key);
    }

}
