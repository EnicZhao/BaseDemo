package com.project.enic.basedemo.base_module.base;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.lky.toucheffectsmodule.TouchEffectsManager;
import com.lky.toucheffectsmodule.types.TouchEffectsViewType;
import com.lky.toucheffectsmodule.types.TouchEffectsWholeType;
import com.project.enic.basedemo.base_module.api.ApiEngine;
import com.project.enic.basedemo.base_module.util.sharepreference.SharePreferenceUtil;

import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

public abstract class BaseApp extends Application {

    private static Context context;
    public static Context getAppContext(){
        return context;
    }
    static {
        TouchEffectsManager.build(TouchEffectsWholeType.SCALE)
                .addViewType(TouchEffectsViewType.ALL);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        context = BaseApp.this;
        init();
    }

    /**
     * 公共工具类初始化
     */
    protected void init(){
        SharePreferenceUtil.init();
        ApiEngine.init();
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e("rxjava-->",throwable.getMessage());
            }
        });
    }

}
