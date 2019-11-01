package com.duojia.messager.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;

import com.duojia.messager.database.DBManager;
import com.duojia.messager.entity.LoadResult;
import com.duojia.messager.entity.Phone;
import com.duojia.messager.listener.ILoadPhoneListener;
import com.duojia.messager.utils.DJConstant;
import com.duojia.messager.utils.Tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadPhoneService extends Service {

    private ExecutorService executors = Executors.newSingleThreadExecutor();
    private LoadResult loadResult;
    private DBManager dbManager;
    private ILoadPhoneListener loadPhoneListener;

    public void setLoadPhoneListener(ILoadPhoneListener loadPhoneListener) {
        this.loadPhoneListener = loadPhoneListener;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new CustomBinder();
    }

    public  class CustomBinder extends Binder {
        public LoadPhoneService getBinder() {
            return LoadPhoneService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dbManager = DBManager.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        executors.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    if (loadPhoneListener != null) {
                        loadPhoneListener.onStartLoad();
                    }
                    loadResult = new LoadResult();
                    FileReader fileInputStream = new FileReader(DJConstant.SD_CARD_PATH);
                    BufferedReader reader = new BufferedReader(fileInputStream);
                    String line = "";
                    List<Phone> list = new ArrayList<>();
                    while ((line = reader.readLine()) != null) {
                        loadResult.setLoadTotalNum(loadResult.getLoadTotalNum() + 1);
                        line = line.replaceAll(" ", "");

                        if (!TextUtils.isEmpty(line)) {
                            if (Tools.isPhone(line)) {
                                loadResult.setLoadSuccessNum(loadResult.getLoadSuccessNum() + 1);
                                Phone phone = new Phone();
                                phone.setPhoneNum(line);
                                list.add(phone);
                                if (loadResult.getLoadSuccessNum() > DJConstant.DB_CREATE_NUM) {
                                    dbManager.addPhoneList(list);
                                    list.clear();
                                }
                            } else {
                                loadResult.setLoadFailureNum(loadResult.getLoadFailureNum() + 1);
                                Phone phone = new Phone();
                                phone.setPhoneNum(line);
                                loadResult.addErrorPhone(phone);
                            }
                        }
                    }
                    if (!list.isEmpty()) {
                        dbManager.addPhoneList(list);
                        list.clear();
                    }
                    if (loadPhoneListener != null) {
                        loadPhoneListener.onCompleteLoad(loadResult);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executors != null) {
            executors.shutdownNow();
        }
    }
}
