package com.project.enic.basedemo.module;

import android.os.Bundle;
import android.widget.TextView;

import com.project.enic.basedemo.R;
import com.project.enic.basedemo.base_module.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EffectsActivity extends BaseActivity {

    @BindView(R.id.single_none_txt)
    TextView singleNoneTxt;
    @BindView(R.id.single_scale_txt)
    TextView singleScaleTxt;
    @BindView(R.id.single_ripple_txt)
    TextView singleRippleTxt;
    @BindView(R.id.single_ripple_round_txt)
    TextView singleRippleRoundTxt;
    @BindView(R.id.single_ripple_color_txt)
    TextView singleRippleColorTxt;
    @BindView(R.id.single_ripple_1_txt)
    TextView singleRipple1Txt;
    @BindView(R.id.single_ripple_round_1_txt)
    TextView singleRippleRound1Txt;
    @BindView(R.id.single_ripple_color_1_txt)
    TextView singleRippleColor1Txt;
    @BindView(R.id.single_state_txt)
    TextView singleStateTxt;
    @BindView(R.id.single_state_round_txt)
    TextView singleStateRoundTxt;
    @BindView(R.id.single_state_color_txt)
    TextView singleStateColorTxt;
    @BindView(R.id.single_shake_txt)
    TextView singleShakeTxt;

    @Override
    protected int _attachLayoutRes() {
        return R.layout.activity_effects;
    }

    @Override
    protected void _initView(Bundle savedInstanceState) {
        setOnClickId(R.id.single_none_txt,
                R.id.single_scale_txt,
                R.id.single_ripple_txt,
                R.id.single_ripple_round_txt,
                R.id.single_ripple_color_txt,
                R.id.single_ripple_1_txt,
                R.id.single_ripple_round_1_txt,
                R.id.single_ripple_color_1_txt,
                R.id.single_state_txt,
                R.id.single_state_round_txt,
                R.id.single_state_color_txt,
                R.id.single_shake_txt);
    }

    @Override
    protected void _initData() {

    }

}
