package com.project.enic.basedemo.module;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.project.enic.basedemo.R;
import com.project.enic.basedemo.base_module.base.BaseActivity;
import com.project.enic.basedemo.base_module.rxjava.RxHelper;
import com.project.enic.basedemo.base_module.util.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tvPermission)
    TextView tvPermission;
    @BindView(R.id.tvList)
    TextView tvList;
    @BindView(R.id.tvViewPager)
    TextView tvViewPager;
    @BindView(R.id.tvEffects)
    TextView tvEffects;

    @Override
    protected int _attachLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void _initView(Bundle savedInstanceState) {
        setOnClickId(R.id.tvPermission, R.id.tvViewPager, R.id.tvList,R.id.tvEffects);//设置具有点击时间的控件ID，并重写doClick(View v)，过滤快速点击的问题
    }

    @Override
    protected void _initData() {
        RxHelper.countdownByMillisecond(5000)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Logger.e("------>" + aLong);
                    }
                });
    }

    @Override
    protected void doClick(View v) {
        super.doClick(v);
        switch (v.getId()) {
            case R.id.tvPermission:
                toActivity(PermissionActivity.class);
                break;
            case R.id.tvList:
                toActivity(RecyclerListActivity.class);
                break;
            case R.id.tvViewPager:
                toActivity(ViewPagerActivity.class);
                break;
            case R.id.tvEffects:
                toActivity(EffectsActivity.class);
                break;
        }
    }
}
