package com.duojia.messager.utils;

import android.content.Context;

import com.duojia.messager.database.DBHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;

public class Tools {

    private static DBHelper mDBHelper;

    public static DBHelper getHelper(Context mContext) {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper(mContext, DBHelper.class);
        }
        return mDBHelper;
    }
}
