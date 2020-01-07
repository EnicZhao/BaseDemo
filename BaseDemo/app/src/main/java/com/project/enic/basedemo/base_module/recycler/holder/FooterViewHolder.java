package com.project.enic.basedemo.base_module.recycler.holder;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project.enic.basedemo.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

public class FooterViewHolder extends ViewHolder {
    public TextView tvHint;
    public ProgressBar pbLoading;
    public FooterViewHolder(@NonNull View itemView) {
        super(itemView);
        tvHint = itemView.findViewById(R.id.tvHint);
        pbLoading = itemView.findViewById(R.id.pbLoading);
    }
}
