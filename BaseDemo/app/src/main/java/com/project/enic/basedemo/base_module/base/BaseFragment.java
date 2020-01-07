package com.project.enic.basedemo.base_module.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.enic.basedemo.R;
import com.project.enic.basedemo.base_module.base_entity.FragmentEntity;
import com.project.enic.basedemo.base_module.popup.CommitLoadingPop;
import com.project.enic.basedemo.base_module.util.Config;
import com.project.enic.basedemo.base_module.util.PermissionUtil;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.trello.rxlifecycle3.components.support.RxFragment;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment<P extends AbstractBasePresenter> extends RxFragment implements IBaseView, View.OnClickListener {

    protected Context context;
    protected View rootView;
    private Unbinder unbinder;
    protected boolean isLoaded ;
    protected P presenter;
    protected FragmentEntity bundleData;
    private CommitLoadingPop loadingPop;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(inflateLayoutRes(),container,false);
            unbinder = ButterKnife.bind(this, rootView);
            presenter = getPresenter();
            if (presenter != null){
                presenter.attach(this);
            }
            initView();
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null){
            bundleData = (FragmentEntity) savedInstanceState.getSerializable("data");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isLoaded){
            isLoaded = true;
            initData();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        saveDataToInstanceState(outState);
    }

    protected void saveDataToInstanceState(@NonNull Bundle outState){
        if(bundleData == null){
            bundleData = new FragmentEntity();
        }
    }

    @Override
    public void onDestroyView() {
        if(rootView != null){
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if(parent != null){
                parent.removeView(rootView);
            }
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(unbinder != null){
            unbinder.unbind();
        }
        if(presenter != null){
            presenter.deAttach();
        }
    }

    protected void toActivity(Class<?> cls){
        Intent intent = new Intent(context,cls);
        context.startActivity(intent);
    }

    @Override
    public void showLoading() {
        if(loadingPop == null){
            loadingPop = new CommitLoadingPop(getContext());
        }
        loadingPop.showAtLocation(rootView, Gravity.CENTER,0,0);
    }

    @Override
    public void hideLoading() {
        if(loadingPop != null){
            loadingPop.dismiss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        hideLoading();
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.<T>bindUntilEvent(FragmentEvent.DESTROY_VIEW);
    }

    protected P getPresenter(){
        return null;
    }
    protected abstract int inflateLayoutRes();//填充layout
    protected abstract void initView();//初始化控件，数据加载及耗时操作不要在这个方法中执行
    protected abstract void initData();//只有第一次进入该页面是才会调用，所有数据加载都应该在这里进行

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
                rootView.findViewById(id).setOnClickListener(this);
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

    protected void requestPermission(int request){
        String[] permissions = PermissionUtil.getPermissions(request);
        if(PermissionUtil.hasPermission(getActivity(),permissions)){
            getPermissionResult(request,true);
        }else {
            PermissionUtil.requestPermission(getActivity(),permissions,request);
        }
    }

    protected void getPermissionResult(int requestCode,boolean result){

    }

    private void showWaringDialog(int request) {
        String appName = getResources().getString(R.string.app_name);
        String permissionName = PermissionUtil.getPermissionName(request);
        new AlertDialog.Builder(getContext())
                .setTitle("警告")
                .setMessage("请在"+ appName + "权限中打开" + permissionName + "，否则功能无法正常运行！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getPermissionResult(request,false);
                    }
                }).show();
    }

}
