package com.nineone.verificationcode.utils;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.annotation.GetAnnotation;
import com.example.annotation.IAddActivity;
import com.nineone.verificationcode.bean.GetTest;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
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
    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> t) {

        return (T) Proxy.newProxyInstance(t.getClassLoader(), new Class[]{t},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (method.getDeclaringClass() == Object.class) {
                            return method.invoke(this, args);
                        }
                        Annotation[] annotation = method.getAnnotations();
                        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                        Log.e("method", "==" + annotation[0]
                                + " args  == " + args[0]
                                + "  object == " +proxy
                                + "  object === "+((GetAnnotation)annotation[0]).v()
                                + "  object === "+(annotation[0] instanceof GetAnnotation)


                        );

                        return null;
                    }
                });
    }
}
