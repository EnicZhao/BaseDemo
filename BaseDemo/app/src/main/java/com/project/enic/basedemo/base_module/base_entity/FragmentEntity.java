package com.project.enic.basedemo.base_module.base_entity;

import java.util.HashMap;

public class FragmentEntity implements IEntity{

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    private HashMap<String,Object> data;
}
