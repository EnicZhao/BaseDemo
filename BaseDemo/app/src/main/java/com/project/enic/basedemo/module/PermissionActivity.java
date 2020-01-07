package com.project.enic.basedemo.module;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.project.enic.basedemo.R;
import com.project.enic.basedemo.base_module.base.BaseActivity;
import com.project.enic.basedemo.base_module.util.PermissionUtil;
import com.project.enic.basedemo.base_module.util.ToastUtil;

import butterknife.BindView;

public class PermissionActivity extends BaseActivity {

    @BindView(R.id.tvCamera)
    TextView tvCamera;
    @BindView(R.id.tvStorage)
    TextView tvStorage;
    @BindView(R.id.tvAudio)
    TextView tvAudio;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvLocation)
    TextView tvLocation;
    @Override
    protected int _attachLayoutRes() {
        return R.layout.activity_permission;
    }

    @Override
    protected void _initView(Bundle savedInstanceState) {
        setOnClickId(R.id.tvCamera,R.id.tvStorage,R.id.tvAudio,R.id.tvPhone,R.id.tvLocation);
    }

    @Override
    protected void _initData() {

    }

    @Override
    protected void doClick(View v) {
        super.doClick(v);
        switch (v.getId()){
            case R.id.tvCamera:
                requestPermission(PermissionUtil.REQUEST_CAMERA);
                break;
            case R.id.tvStorage:
                requestPermission(PermissionUtil.REQUEST_STORAGE);
                break;
            case R.id.tvAudio:
                requestPermission(PermissionUtil.REQUEST_MICROPHONE);
                break;
            case R.id.tvPhone:
                requestPermission(PermissionUtil.REQUEST_PHONE);
                break;
            case R.id.tvLocation:
                requestPermission(PermissionUtil.REQUEST_LOCATION);
                break;
        }
    }

    @Override
    protected void getPermissionResult(int requestCode, boolean result) {
        super.getPermissionResult(requestCode, result);//当result为false时逻辑已经被处理，可以直接处理为true即可
        if(result){
            switch (requestCode){
                case PermissionUtil.REQUEST_CAMERA:
                    ToastUtil.showToast("已获取相机权限");
                    break;
                case PermissionUtil.REQUEST_STORAGE:
                    ToastUtil.showToast("已获取存储权限");
                    break;
                case PermissionUtil.REQUEST_MICROPHONE:
                    ToastUtil.showToast("已获取录音权限");
                    break;
                case PermissionUtil.REQUEST_PHONE:
                    ToastUtil.showToast("已获取打电话等权限");
                    break;
                case PermissionUtil.REQUEST_LOCATION:
                    ToastUtil.showToast("已获取定位权限");
                    break;
            }
        }
    }
}
