package com.project.enic.basedemo.base_module.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtil {

    //浮点型判断
    public static boolean isDecimal(String str) {
        if(str==null || "".equals(str))
            return false;
        Pattern pattern = Pattern.compile("[0-9]*(\\.?)[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static String getValueNumberFormat(String value){
        if(!TextUtils.isEmpty(value.trim()) && isNumeric(value)){
            return value;
        }else {
            return "0";
        }
    }

    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if(!isNum.matches()){
            return false;
        }
        return true;
    }

}
