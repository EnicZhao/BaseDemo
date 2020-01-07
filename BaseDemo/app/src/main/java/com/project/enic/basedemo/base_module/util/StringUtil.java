package com.project.enic.basedemo.base_module.util;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/8/24.
 */
public class StringUtil {

    //对string类型数据进行判断，为空值转换成""
    public static String getStringFormater(String value){
        if(TextUtils.isEmpty(value)){
            return "";
        }
        return value;
    }

    //16进制转ASCII码
    public static String hex2Str(String hex) {
        StringBuilder sb = new StringBuilder();
        String[] split = hex.split(",");
        for (String str : split) {
            int i = Integer.parseInt(str, 16);
            sb.append((char) i);
        }
        return sb.toString();
    }

    //ASCII码转16进制
    public static String str2Hex(String letter) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < letter.length(); i++) {
            char c = letter.charAt(i);
            sb.append(Integer.toHexString(c));
            sb.append(", ");
        }
        sb.deleteCharAt(sb.length() - 2);
        return sb.toString();
    }

    //把原始字符串分割成指定长度的字符串列表
    public static List<String> getStrList(String inputString, int length) {
        int size = inputString.length() / length;
        if (inputString.length() % length != 0) {
            size += 1;
        }
        return getStrList(inputString, length, size);
    }

    public static List<String> getStrList(String inputString, int length, int size) {
        List<String> list = new ArrayList<String>();
        for (int index = 0; index < size; index++) {
            String childStr = substring(inputString, index * length, (index + 1) * length);
            list.add(childStr);
        }
        return list;
    }

    public static String substring(String str, int f, int t) {
        if (f > str.length())
            return null;
        if (t > str.length()) {
            return str.substring(f, str.length());
        } else {
            return str.substring(f, t);
        }
    }

}
