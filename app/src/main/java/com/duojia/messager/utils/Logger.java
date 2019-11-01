package com.duojia.messager.utils;

import android.util.Log;

import com.duojia.messager.BuildConfig;

/**
 * Created by gaoqian on 2017/10/25.
 */

public class Logger {


    public static void v(String TAG, String msg) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, msg);
        }
    }

    public static void v(String TAG, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, msg, tr);
        }
    }

    public static void d(String TAG, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, msg);
        }
    }


    public static void d(String TAG, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, msg, tr);
        }
    }

    public static void d(String TAG, String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }


    public static void i(String TAG, String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, msg);
        }
    }

    public static void i(String TAG, String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void i(String TAG, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, msg, tr);
        }
    }

    public static void w(String TAG, String msg) {
        if (BuildConfig.DEBUG) {
            Log.w(TAG, msg);
        }
    }

    public static void w(String TAG, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.w(TAG, msg, tr);
        }
    }

    public static void w(String TAG, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.w(TAG, tr);
        }
    }

    public static void e(String TAG, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, msg);
        }
    }

    public static void e(String TAG, String msg, Throwable tr) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, msg, tr);
        }
    }

}
