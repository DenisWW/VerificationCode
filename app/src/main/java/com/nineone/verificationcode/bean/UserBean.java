package com.nineone.verificationcode.bean;

import java.util.List;

public class UserBean {
    public String name;
    public int age;
    private List<String> data;

    public UserBean() {

    }

    public UserBean(String s, int i) {
        this.age = i;
        this.name = s;
    }

}
