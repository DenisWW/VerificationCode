package com.nineone.verificationcode.utils

data class SimpleData(var name: String) {

    var age: Int = 0;
    var sex: Int = 0;

    constructor(name: String, age: Int) : this(name) {
        this.age = age;
    }


}