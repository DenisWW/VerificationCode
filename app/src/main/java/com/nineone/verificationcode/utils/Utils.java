package com.nineone.verificationcode.utils;

import android.util.Log;

import com.example.annotation.IAddActivity;

import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static Map<String, Class<?>> stringClassMap;

    public static void addActivity() {
        stringClassMap = new HashMap<>();
        try {
            Class<?> c = Class.forName("com.nineone.verificationcode.ActivityAddUtils");
            IAddActivity addActivity = (IAddActivity) c.newInstance();
            addActivity.addActivity(stringClassMap);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        Log.e("stringClassMap", "===" + stringClassMap.toString());
    }
}
