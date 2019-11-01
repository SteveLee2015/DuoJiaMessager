package com.duojia.messager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.duojia.messager.entity.Phone;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;


public class DBHelper extends OrmLiteSqliteOpenHelper {

    private static final String DB_NAME = "duo_jia.db";
    private static final int DB_VERSION = 1;
    private RuntimeExceptionDao<Phone, Integer> phoneDao = null;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            initPhone(connectionSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initPhone(ConnectionSource connectionSource) throws SQLException {
        TableUtils.createTable(connectionSource, Phone.class);
    }

    public RuntimeExceptionDao<Phone, Integer> getPhoneDao() {
        if (phoneDao == null) {
            phoneDao = getRuntimeExceptionDao(Phone.class);
        }
        return phoneDao;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i2) {

    }

    @Override
    public void close() {
        super.close();
    }
}
