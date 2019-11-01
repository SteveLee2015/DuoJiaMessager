package com.duojia.messager.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.SmsManager;

import com.duojia.messager.database.DBManager;
import com.duojia.messager.entity.Phone;
import com.duojia.messager.entity.SendMsgResult;
import com.duojia.messager.listener.ISendMsgListener;
import com.duojia.messager.utils.DJConstant;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SendMessengerService extends Service {

    private ExecutorService executors = Executors.newSingleThreadExecutor();
    private SmsManager smsManager;
    private DBManager dbManager;

    private ISendMsgListener sendMsgListener;

    public void setSendMsgListener(ISendMsgListener listener) {
        this.sendMsgListener = listener;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new CustomBinder();
    }

    public class CustomBinder extends Binder {
        public SendMessengerService getBinder() {
            return SendMessengerService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        smsManager = SmsManager.getDefault();
        dbManager = DBManager.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //String sendPhoneNumber = intent.getStringExtra("SEND_PHONE");
        final String msg = intent.getStringExtra("SMS_CONTENT");

        executors.submit(new Runnable() {
            @Override
            public void run() {
                sendMsgListener.onStartSend();
                int pageIndex = 0;
                SendMsgResult sendMsgResult = new SendMsgResult();
                while (true) {
                    List<Phone> list = dbManager.queryList(pageIndex, DJConstant.DB_CREATE_NUM);
                    if (list == null) {
                        break;
                    }
                    for (Phone phone : list) {
                        for (String content : smsManager.divideMessage(msg)) {
                            smsManager.sendTextMessage(phone.getPhoneNum(), "", content, null, null);
                        }
                        try {
                            Thread.sleep(60000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        sendMsgResult.setSendMsgNum(sendMsgResult.getSendMsgNum() +1);
                    }
                    pageIndex++;
                }
                sendMsgListener.onCompleteSend(sendMsgResult);
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
