package com.project.enic.basedemo.base_module.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.enic.basedemo.R;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopBarView extends LinearLayout {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.btnLeftIcon)
    ImageView btnLeftIcon;
    @BindView(R.id.btnLeftText)
    TextView btnLeftText;
    @BindView(R.id.btnRightIcon)
    ImageView btnRightIcon;
    @BindView(R.id.btnRightText)
    TextView btnRightText;
    @BindView(R.id.bgContainer)
    FrameLayout bgContainer;

    private String title;
    private int leftIconId;
    private int rightIconId;
    private int backGroundColorId;
    private boolean showBack;
    private String leftString;
    private String rightString;
    private TopBarViewCallBack callBack;
    private Context context;

    public void setTitle(String title) {
        this.title = title;
        tvTitle.setText(title);
    }

    public void setLeftIconId(@DrawableRes int leftIconId) {
        this.leftIconId = leftIconId;
        btnLeftIcon.setImageResource(leftIconId);
        btnLeftText.setVisibility(GONE);
        btnLeftIcon.setVisibility(VISIBLE);
        btnBack.setVisibility(GONE);
    }

    public void setRightIconId(@DrawableRes int rightIconId) {
        this.rightIconId = rightIconId;
        btnRightIcon.setImageResource(rightIconId);
        btnRightText.setVisibility(GONE);
        btnRightIcon.setVisibility(VISIBLE);
    }

    public void setLeftString(String leftString) {
        this.leftString = leftString;
        btnLeftText.setText(leftString);
        btnLeftText.setVisibility(VISIBLE);
        btnLeftIcon.setVisibility(GONE);
        btnBack.setVisibility(GONE);
    }

    public void setRightString(String rightString) {
        this.rightString = rightString;
        btnRightText.setText(rightString);
        btnRightText.setVisibility(VISIBLE);
        btnRightIcon.setVisibility(GONE);
}

    public void setCallBack(TopBarViewCallBack callBack) {
        this.callBack = callBack;
    }

    public TopBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TopBarView);
        title = a.getString(R.styleable.TopBarView_title);
        leftString = a.getString(R.styleable.TopBarView_left_text);
        rightString = a.getString(R.styleable.TopBarView_right_text);
        leftIconId = a.getResourceId(R.styleable.TopBarView_left_icon, -1);
        rightIconId = a.getResourceId(R.styleable.TopBarView_right_icon, -1);
        showBack = a.getBoolean(R.styleable.TopBarView_show_back, true);
        backGroundColorId = a.getColor(R.styleable.TopBarView_background_color, context.getResources().getColor(R.color.app_theme_color));
        a.recycle();
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View.inflate(context, R.layout.widget_top_bar_view, this);
        ButterKnife.bind(this);
        tvTitle.setText(title);
        bgContainer.setBackgroundColor(backGroundColorId);
        btnBack.setVisibility(showBack?VISIBLE:GONE);

        if (!TextUtils.isEmpty(leftString)) {
            btnLeftText.setText(leftString);
            btnLeftText.setVisibility(VISIBLE);
            btnBack.setVisibility(GONE);
        }

        if (leftIconId != -1) {
            btnLeftIcon.setImageResource(leftIconId);
            btnLeftIcon.setVisibility(VISIBLE);
            btnBack.setVisibility(GONE);
        }

        if (!TextUtils.isEmpty(rightString)) {
            btnRightText.setText(rightString);
            btnRightText.setVisibility(VISIBLE);
        }

        if (rightIconId != -1) {
            btnRightIcon.setImageResource(rightIconId);
            btnRightIcon.setVisibility(VISIBLE);
        }
    }

    @OnClick({R.id.btnBack,R.id.btnLeftIcon,R.id.btnLeftText,R.id.btnRightIcon,R.id.btnRightText})
    public void doClick(View view){
        switch (view.getId()){
            case R.id.btnBack:
                if(callBack != null){
                    if(!callBack.clickBack() && context instanceof Activity){
                        ((Activity) context).finish();
                    }
                }else {
                    if(context instanceof Activity){
                        ((Activity) context).finish();
                    }
                }
                break;
            case R.id.btnLeftIcon:
            case R.id.btnLeftText:
                if(callBack != null){
                    callBack.clickLeft();
                }
                break;
            case R.id.btnRightIcon:
            case R.id.btnRightText:
                if(callBack != null){
                    callBack.clickRight();
                }
                break;
        }
    }

    public interface TopBarViewCallBack {
        void clickLeft();
        void clickRight();
        boolean clickBack();
    }

    public static abstract class TopBarViewSimpleCallBack implements TopBarViewCallBack {

        @Override
        public void clickLeft() {

        }

        @Override
        public void clickRight() {

        }

        @Override
        public boolean clickBack() {
            return false;
        }
    }

}
