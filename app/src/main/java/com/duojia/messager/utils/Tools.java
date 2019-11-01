package com.duojia.messager.utils;

import android.content.Context;

import com.duojia.messager.database.DBHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {

    private static DBHelper mDBHelper;

    public static DBHelper getHelper(Context mContext) {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper(mContext, DBHelper.class);
        }
        return mDBHelper;
    }


    public static boolean isPhone(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
            Logger.d(DJConstant.TAG, "手机号应为11位数");
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            if (!isMatch) {
                Logger.d(DJConstant.TAG, "请填入正确的手机号");
            }
            return isMatch;
        }
    }
}
