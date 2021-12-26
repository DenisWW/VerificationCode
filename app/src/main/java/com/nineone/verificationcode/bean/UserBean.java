package com.nineone.verificationcode.bean;

public class UserBean {
    public String name;
    public int age;

    public UserBean() {

    }

    public UserBean(String s, int i) {
        this.age = i;
        this.name = s;
    }
}
