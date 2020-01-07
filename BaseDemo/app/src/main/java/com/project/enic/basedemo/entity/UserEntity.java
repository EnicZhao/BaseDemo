package com.project.enic.basedemo.entity;

import com.project.enic.basedemo.base_module.base_entity.ImplEntity;

public class UserEntity extends ImplEntity {

    private String name;
    private String sex;
    private int age;

    public UserEntity(String name, String sex, int age) {
        this.name = name;
        this.sex = sex;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
