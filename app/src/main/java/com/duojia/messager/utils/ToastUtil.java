package com.duojia.messager.utils;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by lisheng on 2017/8/1.
 */

public class ToastUtil {

    private static Toast toast = null;

    public static void showMessage(final Context act, final String msg) {
        showMessage(act, msg, Toast.LENGTH_SHORT);
    }

    public static void showMessage(final Context act, final int msg) {
        showMessage(act, msg, Toast.LENGTH_SHORT);
    }

    public static void showMessage(final Context act, final String msg, final int len) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(act, msg, len);
        ViewGroup viewGroup = (ViewGroup) toast.getView();
        if (viewGroup != null) {
            TextView tv = (TextView) viewGroup.getChildAt(0);
            if (tv != null) {
                tv.setBackground(null);
            }
        }
        toast.show();
    }

    public static void showMessage(final Context act, final int msg, final int len) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(act, msg, len);
        ViewGroup viewGroup = (ViewGroup) toast.getView();
        if (viewGroup != null) {
            TextView tv = (TextView) viewGroup.getChildAt(0);
            if (tv != null) {
                tv.setBackground(null);
            }
        }
        toast.show();
    }
}
