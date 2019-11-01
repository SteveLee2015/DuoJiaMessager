package com.duojia.messager.database;

import android.content.Context;

import com.duojia.messager.entity.Phone;
import com.duojia.messager.utils.Tools;
import com.j256.ormlite.android.AndroidDatabaseConnection;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;

public class DBManager {

    private static DBManager instance;
    private static DBHelper dbHelper;

    public static DBManager getInstance(Context context) {
        if (instance == null) {
            instance = new DBManager();
            dbHelper = Tools.getHelper(context);
        }
        return instance;
    }

    public void addPhoneList(List<Phone> list) {
        AndroidDatabaseConnection adc = null;
        adc = new AndroidDatabaseConnection(dbHelper.getWritableDatabase(), true);
        RuntimeExceptionDao<Phone, Integer> phoneDao = dbHelper.getPhoneDao();
        phoneDao.setAutoCommit(adc, false);
        Savepoint sp = null;

        try {
            sp = adc.setSavePoint("create_phone");

            long startTime = System.currentTimeMillis();
            for (Phone phone : list) {
                phoneDao.createOrUpdate(phone);
            }

            adc.commit(sp);
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                adc.rollback(sp);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

    }

}
