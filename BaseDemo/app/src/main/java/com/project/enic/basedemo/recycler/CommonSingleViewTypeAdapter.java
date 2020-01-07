package com.project.enic.basedemo.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.enic.basedemo.R;
import com.project.enic.basedemo.base_module.base_entity.ImplEntity;
import com.project.enic.basedemo.base_module.recycler.BaseRecyclerViewAdapter;
import com.project.enic.basedemo.base_module.recycler.holder.CommonViewHolder;
import com.project.enic.basedemo.base_module.util.Config;
import com.project.enic.basedemo.entity.UserEntity;

import java.util.List;
import androidx.annotation.NonNull;

public class CommonSingleViewTypeAdapter extends BaseRecyclerViewAdapter<CommonViewHolder> {

    private List<ImplEntity> data;
    private int viewType;

    public CommonSingleViewTypeAdapter(Context context,List<ImplEntity> data,int viewType) {
        this.context = context;
        this.data = data;
        this.viewType = viewType;
        inflater = LayoutInflater.from(context);
    }

    @Override
    protected CommonViewHolder onCreateViewHolderChild(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType){
            case Config.ViewType.USER_ENTITY:
                view = inflater.inflate(R.layout.recycler_user_item_layout,parent,false);
                break;
        }
        return new CommonViewHolder(view);
    }

    @Override
    protected void onBindViewHolderChild(@NonNull CommonViewHolder h, int position) {
        ImplEntity entity = data.get(position);
        switch (viewType){
            case Config.ViewType.USER_ENTITY:
                UserEntity userEntity = (UserEntity) entity;
                h.setText(R.id.tvName,userEntity.getName());
                h.setText(R.id.tvSex,userEntity.getSex());
                h.setText(R.id.tvAge,userEntity.getAge() +  "");
                break;
        }
        h.setOnItemClickListener(position,this);
    }

    @Override
    protected int getItemCountChild() {
        return data.size();
    }

    @Override
    protected int getItemViewTypeChild(int position) {
        return viewType;
    }


}
