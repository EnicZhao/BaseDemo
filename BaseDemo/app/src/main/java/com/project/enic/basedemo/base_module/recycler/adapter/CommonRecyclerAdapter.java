package com.project.enic.basedemo.base_module.recycler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.enic.basedemo.base_module.base_entity.ImplEntity;
import com.project.enic.basedemo.base_module.recycler.BaseRecyclerViewAdapter;
import com.project.enic.basedemo.base_module.recycler.holder.CommonViewHolder;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * 使用于数组数据为单一类型
 */
public class CommonRecyclerAdapter extends BaseRecyclerViewAdapter<CommonViewHolder> {
    private List<ImplEntity> data;
    private int viewType;
    public CommonRecyclerAdapter(Context context, List<ImplEntity> data,int viewType) {
        this.data = data;
        this.context = context;
        this.viewType = viewType;
        inflater = LayoutInflater.from(context);
    }

    @Override
    protected CommonViewHolder onCreateViewHolderChild(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType){

        }
        return new CommonViewHolder(view);
    }

    @Override
    protected void onBindViewHolderChild(@NonNull CommonViewHolder holder, int position) {
        ImplEntity implEntity = data.get(position);
        holder.setOnItemClickListener(position,this);
    }

    @Override
    protected int getItemCountChild() {
        return data.size();
    }

    @Override
    protected int getItemViewTypeChild(int position) {
        return this.viewType;
    }

    @Override
    protected void doClick(View v) {
        super.doClick(v);
        if(onRecyclerItemClickListener != null){
            int position = (int) v.getTag();
            onRecyclerItemClickListener.click(position);
        }
    }
}
