package com.project.enic.basedemo.base_module.base;
import com.trello.rxlifecycle3.LifecycleTransformer;

public interface IBaseView {

    void showLoading();//显示加载控件

    void hideLoading();//隐藏加载控件

    <T> LifecycleTransformer<T> bindToLife();//RxJava生命周期控制，防止内存泄漏
}
