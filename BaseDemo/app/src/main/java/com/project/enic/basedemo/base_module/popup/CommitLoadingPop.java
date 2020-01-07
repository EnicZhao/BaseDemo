package com.project.enic.basedemo.base_module.popup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.project.enic.basedemo.R;

/**
 * Created by dell on 2019/9/20.
 */

public class CommitLoadingPop extends PopupWindow {
    private Context context;
    public CommitLoadingPop(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init(){
        View view = LayoutInflater.from(context).inflate(R.layout.popup_loading_layout,null);
        setContentView(view);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setOutsideTouchable(false);
        setFocusable(false);
        setTouchable(true);
        setBackgroundDrawable(new ColorDrawable());
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
}
