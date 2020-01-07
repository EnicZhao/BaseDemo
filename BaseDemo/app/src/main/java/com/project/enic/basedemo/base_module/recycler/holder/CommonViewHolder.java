package com.project.enic.basedemo.base_module.recycler.holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommonViewHolder extends RecyclerView.ViewHolder {

    private final Context context;
    private final SparseArray<View> mSparseArray;

    public CommonViewHolder(@NonNull View itemView) {
        super(itemView);
        mSparseArray = new SparseArray<>();
        context = itemView.getContext();
    }

    @SuppressWarnings("unchecked")
    public final <T extends View> T getView(@IdRes int viewId) {
        View view = mSparseArray.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mSparseArray.put(viewId, view);
        }
        return (T) view;
    }

    /********************************** 通用方法 ****************************************/
    public void setVisibility(int viewId, int visibility) {
        View view = getView(viewId);
        view.setVisibility(visibility);
    }

    public void setSelected(int viewId, boolean selected) {
        View view = getView(viewId);
        view.setSelected(selected);
    }

    /********************************** TextView 常用函数 ****************************************/

    public void setText(int viewId, CharSequence value) {
        TextView view = getView(viewId);
        view.setText(value);
    }

    public void setTextSize(int viewId, int testSize) {
        TextView view = getView(viewId);
        view.setTextSize(testSize);
    }

    public void setTextColor(int viewId, @ColorInt int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
    }

    /********************************** ImageView 相关函数 *****************************************/

    public void setImageResource(int viewId, int imageResId) {
        ImageView view = getView(viewId);
        view.setImageResource(imageResId);
    }

    public void setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
    }

    public void setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
    }

    /********************************** 常用监听 ****************************************/

    public void setOnClickListener(int viewId, View.OnClickListener listener,int position) {
        View view = getView(viewId);
        view.setTag(position);
        view.setOnClickListener(listener);
    }

    public void setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
    }

    public void setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
    }

    public void setOnItemClickListener(int position,View.OnClickListener listener) {
        itemView.setTag(position);
        itemView.setClickable(true);
        itemView.setOnClickListener(listener);
    }

    public void onItenLongClickListener(int position,View.OnLongClickListener listener) {
        itemView.setTag(position);
        itemView.setLongClickable(true);
        itemView.setOnLongClickListener(listener);
    }

}
