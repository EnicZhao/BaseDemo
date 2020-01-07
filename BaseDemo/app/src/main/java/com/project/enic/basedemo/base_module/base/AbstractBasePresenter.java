package com.project.enic.basedemo.base_module.base;

public abstract class AbstractBasePresenter<M extends IBaseModel,V extends IBaseView> {
    protected M model;
    protected V view;

    public void attach(V view){
        this.view = view;
        this.model = getModel();
    }

    public void deAttach(){
        this.view = null;
        this.model = null;
    }
    protected abstract M getModel();
}
