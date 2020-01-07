package com.project.enic.basedemo.base_module.util.sharepreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 作者： Enic
 * 时间:  2017/3/21.
 * 介绍：封装share_preference底层操作
 */

public class BaseSharePreference {

    private Context context;
    private SharedPreferences sp;
    private SharedPreferences.Editor mEditor;
    private String FILE_NAME = "BASE_APP_DATA";

    protected BaseSharePreference(Context context){
        this.context = context;
        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        mEditor = sp.edit();
    }

    /**
     * 存储或获取字符串类型数据
     */
    protected void setString(String key, String value){
        mEditor.putString(key,value);
        mEditor.apply();
    }

    protected String getString(String key){
        return sp.getString(key,"");
    }

    /**
     * 存储或获取Long类型数据
     */
    protected void setLong(String key, Long value){
        mEditor.putLong(key,value);
        mEditor.apply();
    }

    protected Long getLong(String key){
        return sp.getLong(key,0L);
    }

    /**
     * 存储或获取int类型数据
     */
    protected void setInt(String key, int value){
        mEditor.putInt(key,value);
        mEditor.apply();
    }
    protected int getInt(String key){
        return sp.getInt(key,0);
    }

    /**
     * 存储或获取boolean类型数据
     */
    protected void setBoolean(String key, boolean value){
        mEditor.putBoolean(key,value);
        mEditor.apply();
    }
    protected boolean getBoolean(String key){
        return sp.getBoolean(key,false);
    }

    /**
     * 存储或获取float类型数据
     */
    protected void setFloat(String key, float value){
        mEditor.putFloat(key,value);
        mEditor.apply();
    }
    protected float getFloat(String key){
        return sp.getFloat(key,0);
    }

    /**
     * 存储获取一个字符串数组
     */
    protected void setHashSet(String key, HashSet<String> datas){
        mEditor.putStringSet(key,datas);
        mEditor.apply();
    }

    protected Set<String> getHashSet(String key){
        sp.getStringSet(key,null);
        return sp.getStringSet(key,null);
    }

    /**
     * 存储获取一个对象
     */
    protected void setObject(String key, Object object){
        if (object instanceof Serializable) {// obj必须实现Serializable接口，否则会出问题
            ByteArrayOutputStream baos = null;
            ObjectOutputStream oos = null;
            try {
                baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
                oos.writeObject(object);
                String string64 = new String(Base64.encode(baos.toByteArray(), 0));
                mEditor.putString(key, string64).apply();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    baos.close();
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            throw new IllegalArgumentException(
                    "the obj must implement Serializble");
        }
    }
    protected Object getObject(String key){
        Object obj = null;
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            String base64 = sp.getString(key, "");
            if (base64.equals("")) {
                return null;
            }
            byte[] base64Bytes = Base64.decode(base64.getBytes(), 1);
            bais = new ByteArrayInputStream(base64Bytes);
            ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(bais != null){
                    bais.close();
                }
                if(ois != null){
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

    /**
     * 存储数组数据
     */
    protected void setStringList(String key, List<String> datas){
        StringBuilder builder = new StringBuilder();
        for (String data : datas) {
            builder.append(data+" ");
        }
        mEditor.putString(key,builder.toString());
        mEditor.apply();
    }

    protected List<String> getStringList(String key){
        String value = sp.getString(key, null);
        List<String> mDatas = new ArrayList<String>();
        String[] datas = value.split(" ");
        for (String data : datas) {
            mDatas.add(data);
        }
        return mDatas;
    }


    /**
     * 移除某个key
     */
    protected boolean removeKey(String key){
        mEditor.remove(key);
        return mEditor.commit();
    }

    /**
     * 清楚所有数据
     */
    protected boolean clearAll(){
        mEditor.clear();
        return mEditor.commit();
    }

    /**
     * 查看是否包含某个key
     */
    protected boolean contain(String key){
        return sp.contains(key);
    }
}
