package com.project.enic.basedemo.base_module.util;

import java.util.HashMap;

public class Config {
    public static final String API_DOMAIN = "http://172.16.0.13:10089/spcp_lz/";//服务器地址
    public interface API{
        String DOWNLOAD_FILE = "";
    }

    //配置值
    public interface VALUE{
        int CLICK_LIMIT_TIME = 1000;//点击事件限制连续点击间隔
    }

    public interface ViewType{
        int USER_ENTITY = 101;
        int MENU_DATA = 102;
    }

    //map类型
    public interface MAP{
        HashMap<String,String> NOTICE_DATA = new HashMap<String,String>(){
            {
                put("1","一般隐患待确认提醒");
            }
        };
    }

}
