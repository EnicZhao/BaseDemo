package com.project.enic.basedemo.module;

import android.os.Bundle;

import com.project.enic.basedemo.R;
import com.project.enic.basedemo.base_module.base.BaseActivity;
import com.project.enic.basedemo.base_module.base_entity.ImplEntity;
import com.project.enic.basedemo.base_module.interfaces.OnRecyclerLoadListener;
import com.project.enic.basedemo.base_module.recycler.CommonDividerItemDecoration;
import com.project.enic.basedemo.base_module.rxjava.RxHelper;
import com.project.enic.basedemo.base_module.util.Config;
import com.project.enic.basedemo.entity.UserEntity;
import com.project.enic.basedemo.recycler.CommonSingleViewTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import io.reactivex.functions.Consumer;

public class RecyclerListActivity extends BaseActivity {

    @BindView(R.id.rvDataView)
    RecyclerView rvDataView;
    @BindView(R.id.srlRefresh)
    SwipeRefreshLayout srlRefresh;

    private List<ImplEntity> data = new ArrayList<>();
    private CommonSingleViewTypeAdapter adapter;
    private int total = 100;
    private int row = 20;
    private int page;

    @Override
    protected int _attachLayoutRes() {
        return R.layout.activity_recycler_list;
    }

    @Override
    protected void _initView(Bundle savedInstanceState) {
        adapter = new CommonSingleViewTypeAdapter(currentActivity,data, Config.ViewType.USER_ENTITY);
        rvDataView.setAdapter(adapter);
        rvDataView.setLayoutManager(new LinearLayoutManager(currentActivity,LinearLayoutManager.VERTICAL,false));
        rvDataView.addItemDecoration(new CommonDividerItemDecoration(//添加分割线
                currentActivity,
                LinearLayoutManager.HORIZONTAL,
                getResources().getDimension(R.dimen.divider_item_decoration_height),
                R.drawable.recycler_divider_shadow_bg));
        adapter.setOnLoadListener(new OnRecyclerLoadListener() {
            @Override
            public void load() {//设置加载更多
                page++;
                loadData("加载更多数据");
            }
        });
        srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                loadData("刷新数据");
            }
        });
    }

    @Override
    protected void _initData() {
        loadData("初始化数据");
    }

    /**
     * 这里需要注意一点，当加载速度过快，或许报错，报错原因为rvDataView.isComputingLayout()为false时正好刷新列表导致的错误，
     * 如果遇到加载本地的资源数据，加载速度过快，可能会导致这个异常，如果加载网络数据，不需要延时
     */
    private void loadData(String name){
        RxHelper.countdownByMillisecond(200)//毫秒计时器，返回两次结果，第一次返回为0，开始计时返回，第二次返回1，为计时时间到达之后返回
                .compose(bindToLife())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if(aLong == 0){
                            return;
                        }
                        srlRefresh.setRefreshing(false);
                        if(page == 0){
                            data.clear();
                        }
                        for (int i = page * row; i < (page + 1) * row; i++) {
                            data.add(new UserEntity(name,"男",i));
                        }
                        adapter.loadResult(page,row,total);
                    }
                });
    }
}
