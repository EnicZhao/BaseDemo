package com.project.enic.basedemo.module;


import android.os.Bundle;

import com.project.enic.basedemo.R;
import com.project.enic.basedemo.base_module.base.BaseActivity;
import com.project.enic.basedemo.base_module.widget.TopBarView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import butterknife.BindView;

public class ViewPagerActivity extends BaseActivity {

    @BindView(R.id.tbvTitle)
    TopBarView tbvTitle;
    @BindView(R.id.vpPager)
    ViewPager2 vpPager;

    @Override
    protected int _attachLayoutRes() {
        return R.layout.activity_view_pager;
    }

    @Override
    protected void _initView(Bundle savedInstanceState) {
        tbvTitle.setCallBack(new TopBarView.TopBarViewSimpleCallBack() {
            @Override
            public void clickRight() {
                super.clickRight();
                if(vpPager.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL){
                    vpPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
                    tbvTitle.setRightString("横向");
                }else {
                    vpPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
                    tbvTitle.setRightString("竖向");
                }
            }
        });
    }

    @Override
    protected void _initData() {
        vpPager.setAdapter(new FragmentStateAdapter(ViewPagerActivity.this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return TestFragment.getInstance(position);
            }

            @Override
            public int getItemCount() {
                return 20;
            }
        });
    }

}
