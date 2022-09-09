package com.nineone.verificationcode.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Book1 implements Parcelable {
    public String name;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    public void readFromParcel(Parcel source) {
        this.name = source.readString();
    }

    public Book1() {

    }

    public Book1(String name) {
        this.name = name;
        Log.e("Book1", name+"===" + Thread.currentThread().getName());
    }

    protected Book1(Parcel in) {
        this.name = in.readString();
    }

    public static final Creator<Book1> CREATOR = new Creator<Book1>() {
        @Override
        public Book1 createFromParcel(Parcel source) {
            return new Book1(source);
        }

        @Override
        public Book1[] newArray(int size) {
            return new Book1[size];
        }
    };
}
