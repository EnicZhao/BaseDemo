package com.project.enic.basedemo.base_module.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.lky.toucheffectsmodule.factory.TouchEffectsFactory;
import com.lky.toucheffectsmodule.types.TouchEffectsWholeType;
import com.project.enic.basedemo.R;
import com.project.enic.basedemo.base_module.popup.CommitLoadingPop;
import com.project.enic.basedemo.base_module.receiver.NetStateChangeReceiver;
import com.project.enic.basedemo.base_module.util.ActivityManagerUtil;
import com.project.enic.basedemo.base_module.util.Config;
import com.project.enic.basedemo.base_module.util.PermissionUtil;
import com.project.enic.basedemo.base_module.util.statusbar.StatusBarUtil;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import butterknife.ButterKnife;

/**
 * 1.权限管理
 * 2.activity管理
 * 3.网络变化监听
 * 4.过滤点击按钮速度过快（如果需要给view添加点击事件，在setOnClickId(@IdRes int ...ids)方法中插入view ID，并重写doClick(View v)事件）
 * 5.给需要点击事件的view添加点击效果，TouchEffectsFactory.initTouchEffects(this, TouchEffectsWholeType.NONE);默认无效果，需要在xml布局文件中配置
 * @param <P>
 */

public abstract class BaseActivity<P extends AbstractBasePresenter> extends RxAppCompatActivity implements IBaseView, View.OnClickListener {
    protected P presenter;
    protected Activity currentActivity;
    private NetStateChangeReceiver receiver;
    private CommitLoadingPop loadingPop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        TouchEffectsFactory.initTouchEffects(this, TouchEffectsWholeType.NONE);//按钮点击效果，默认无效果，可在xml文件中通过属性配置
        super.onCreate(savedInstanceState);
        setContentView(_attachLayoutRes());
        ButterKnife.bind(this);
        currentActivity = this;
        ActivityManagerUtil.addActivity(this);
        presenter = getPresenter();
        if (presenter != null){
            presenter.attach(this);
        }
        initWindow();
        _initView(savedInstanceState);
        _initData();
        initReceiver();
    }

    //监听网络变化的情况
    private void initReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        receiver = new NetStateChangeReceiver();
        registerReceiver(receiver,intentFilter);
    }

    private void unRegisterReceiver(){
        unregisterReceiver(receiver);
    }

    protected P getPresenter(){
        return null;
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.<T>bindUntilEvent(ActivityEvent.DESTROY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterReceiver();
        if (presenter != null){
            presenter.deAttach();
            presenter = null;
        }
        ActivityManagerUtil.finishActivity(this);
    }

    private void initWindow() {
        StatusBarUtil.setTranslucentForImageView(this,0,null);
    }

    protected void toActivity(Class<?> cls){
        Intent intent = new Intent(this,cls);
        startActivity(intent);
    }

    protected void toActivityForResult(Class<?> cls,int requestCode){
        Intent intent = new Intent(this,cls);
        startActivityForResult(intent,requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getPermissionResult(requestCode,true);
            }else {
                showWaringDialog(requestCode);
            }
        }else {
            showWaringDialog(requestCode);
        }
    }

    //权限动态申请，可在PermissionUtil文件中了解具体
    protected void requestPermission(int request){
        String[] permissions = PermissionUtil.getPermissions(request);
        if(PermissionUtil.hasPermission(currentActivity,permissions)){
            getPermissionResult(request,true);
        }else {
            PermissionUtil.requestPermission(currentActivity,permissions,request);
        }
    }

    //请求权限结果，可在实际使用中重写该方法
    protected void getPermissionResult(int requestCode,boolean result){

    }

    private void showWaringDialog(int request) {
        String appName = getResources().getString(R.string.app_name);
        String permissionName = PermissionUtil.getPermissionName(request);
        new AlertDialog.Builder(this)
                .setTitle("警告")
                .setMessage("请在"+ appName + "权限中打开" + permissionName + "，否则功能无法正常运行！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getPermissionResult(request,false);
                    }
                }).show();
    }


    protected abstract int _attachLayoutRes();
    protected abstract void _initView(Bundle savedInstanceState);
    protected abstract void _initData();

    @Override
    public void showLoading() {
        if(loadingPop == null){
            loadingPop = new CommitLoadingPop(currentActivity);
        }
        loadingPop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER,0,0);
    }

    @Override
    public void hideLoading() {
        if(loadingPop != null){
            loadingPop.dismiss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideLoading();
    }

    protected long lastClickTime = 0;//记录上次点击时间
    protected boolean checkClickLimit(){
        if(System.currentTimeMillis() - lastClickTime < Config.VALUE.CLICK_LIMIT_TIME){
            return true;
        }
        lastClickTime = System.currentTimeMillis();
        return false;
    }

    //过滤点击太快的问题
    protected void setOnClickId(@IdRes int ...ids){
        if(ids.length > 0){
            for (int id : ids) {
                findViewById(id).setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(checkClickLimit()){
            return;
        }
        doClick(v);
    }

    protected void doClick(View v){

    }

}
