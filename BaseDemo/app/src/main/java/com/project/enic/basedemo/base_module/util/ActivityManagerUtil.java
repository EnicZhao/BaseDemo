package com.project.enic.basedemo.base_module.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityManagerUtil {

    public static List<Activity> activityStack = new ArrayList<Activity>();

    public static void addActivity(Activity activity){
        activityStack.add(activity);
    }

    //关闭指定activity
    public static void finishActivity(Activity activity){
        if (activity != null){
            activityStack.remove(activity);
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }

    //关闭指定类名activity
    public static void finishActivity(Class<?> cls){
        for (Activity activity : activityStack) {
            if(activity.getClass().equals(cls)){
                finishActivity(activity);
            }
        }
    }

    //结束所有的activity
    public static void finishAllActivity(){
        for (Activity activity : activityStack) {
            if(activity != null && !activity.isFinishing()){
                activity.finish();
            }
        }
        activityStack.clear();
    }

    //结束程序
    public static void exitApp(){
        finishAllActivity();
        //System.exit(0);
    }
}
