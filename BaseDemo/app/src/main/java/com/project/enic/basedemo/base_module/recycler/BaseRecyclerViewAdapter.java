package com.project.enic.basedemo.base_module.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.enic.basedemo.R;
import com.project.enic.basedemo.base_module.interfaces.OnRecyclerItemClickListener;
import com.project.enic.basedemo.base_module.interfaces.OnRecyclerLoadListener;
import com.project.enic.basedemo.base_module.recycler.holder.FooterViewHolder;
import com.project.enic.basedemo.base_module.rxjava.RxHelper;
import com.project.enic.basedemo.base_module.util.Config;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import io.reactivex.functions.Consumer;

public abstract class BaseRecyclerViewAdapter<V extends ViewHolder> extends Adapter<ViewHolder> implements View.OnClickListener {
    public static final int TYPE_FOOTER = -1001;
    private static final int LOADING = 0;
    private static final int EMPTY = 1;
    private static final int ERROR = 2;
    private static final int LOAD_ALL = 3;
    private int currentStatus;

    protected Context context;
    protected LayoutInflater inflater;
    private OnRecyclerLoadListener listener;
    protected OnRecyclerItemClickListener onRecyclerItemClickListener;
    protected boolean isLoading;
    private boolean isInit = true;

    public void setOnLoadListener(OnRecyclerLoadListener listener) {
        this.listener = listener;
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        int count = getItemCount();
        if(listener != null && position == count - 1){
            return TYPE_FOOTER;
        }
        return getItemViewTypeChild(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER && inflater != null){
            View view = inflater.inflate(R.layout.recycler_foot_view,parent,false);
            return new FooterViewHolder(view);
        }else {
            return onCreateViewHolderChild(parent,viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        int type = getItemViewType(position);
        if(type == TYPE_FOOTER){
            FooterViewHolder holder = (FooterViewHolder) h;
            if(currentStatus == EMPTY){
                holder.tvHint.setText("暂无数据");
                holder.tvHint.setVisibility(View.VISIBLE);
                holder.pbLoading.setVisibility(View.GONE);
            }else if(currentStatus == ERROR){
                holder.tvHint.setText("数据加载异常");
                holder.tvHint.setVisibility(View.VISIBLE);
                holder.pbLoading.setVisibility(View.GONE);
            }else if(currentStatus == LOAD_ALL){
                holder.tvHint.setText("已加载全部数据");
                holder.tvHint.setVisibility(View.VISIBLE);
                holder.pbLoading.setVisibility(View.GONE);
            }else{
                holder.tvHint.setVisibility(View.GONE);
                holder.pbLoading.setVisibility(View.VISIBLE);
                if(!isLoading && !isInit){
                    isLoading = true;
                    if(listener != null){
                        listener.load();
                    }
                }
            }
        }else {
            V v = (V) h;
            onBindViewHolderChild(v,position);
        }
    }

    public void loadResult(int page,int rows,int total){
        isInit = false;
        if(total == -1){
            showLoadError();
        }else if(total == 0){
            showEmpty();
        }else {
            if((page + 1) * rows >= total){
                showLoadSuccess(false);
            }else {
                showLoadSuccess(true);
            }
        }
    }

    public void showLoadError(){
        currentStatus = ERROR;
        isLoading = false;
        notifyDataSetChanged();
    }

    public void showEmpty(){
        currentStatus = EMPTY;
        isLoading = false;
        notifyDataSetChanged();
    }

    public void showLoadSuccess(boolean hasMore){
        if(!hasMore){
            currentStatus = LOAD_ALL;
        }else {
            currentStatus = LOADING;
        }
        isLoading = false;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        int count = getItemCountChild();
        if(listener != null){
            count ++;
        }
        return count;
    }

    protected abstract V onCreateViewHolderChild(@NonNull ViewGroup parent, int viewType);
    protected abstract void onBindViewHolderChild(@NonNull V h, int position);
    protected abstract int getItemCountChild();
    protected abstract int getItemViewTypeChild(int position);


    protected long lastClickTime = 0;//记录上次点击时间
    protected boolean checkClickLimit(){
        if(System.currentTimeMillis() - lastClickTime < Config.VALUE.CLICK_LIMIT_TIME){
            return true;
        }
        lastClickTime = System.currentTimeMillis();
        return false;
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
