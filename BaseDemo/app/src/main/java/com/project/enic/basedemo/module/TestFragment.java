package com.project.enic.basedemo.module;


import android.os.Bundle;
import android.widget.TextView;

import com.project.enic.basedemo.R;
import com.project.enic.basedemo.base_module.base.BaseFragment;

import java.util.HashMap;

import androidx.annotation.NonNull;
import butterknife.BindView;


public class TestFragment extends BaseFragment {

    @BindView(R.id.tvContent)
    TextView tvContent;

    private int pager;

    public static TestFragment getInstance(int pager) {
        TestFragment fragment = new TestFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pager", pager);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int inflateLayoutRes() {
        return R.layout.fragment_test;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        if(bundleData == null){
            Bundle bundle = getArguments();
            pager = bundle.getInt("pager");
            tvContent.setText("初始化数据" + pager);
        }else {
            HashMap<String, Object> data = bundleData.getData();
            pager = (int) data.get("pager");
            tvContent.setText("来源于储存数据" + pager);
        }
    }

    @Override
    protected void saveDataToInstanceState(@NonNull Bundle outState) {
        super.saveDataToInstanceState(outState);
        HashMap<String, Object> data = new HashMap<>();
        data.put("pager",pager);
        bundleData.setData(data);
        outState.putSerializable("data",bundleData);
    }
}
