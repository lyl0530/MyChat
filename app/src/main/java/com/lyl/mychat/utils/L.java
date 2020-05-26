package com.lyl.mychat.utils;

import android.util.Log;

import com.lyl.mychat.BuildConfig;

/**
 * Created by lym on 2020/5/26
 * Describe :
 */
public class L {
    private static final String TAG = "lyl123";

    private static final boolean sDebug = BuildConfig.DEBUG;
    public static void d(String msg, Object... args){
        if (!sDebug) return;

        // https://www.cnblogs.com/Dhouse/p/7776780.html
        // str=String.format("Hi,%s", "王力");
        // 类似于C语言的sprintf()方法
        Log.d(TAG, String.format(msg, args));//0530
    }
}
