package com.duojia.messager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.duojia.messager.entity.LoadResult;
import com.duojia.messager.entity.SendMsgResult;
import com.duojia.messager.listener.ILoadPhoneListener;
import com.duojia.messager.listener.ISendMsgListener;
import com.duojia.messager.service.LoadPhoneService;
import com.duojia.messager.service.SendMessengerService;
import com.duojia.messager.utils.ToastUtil;
import com.duojia.messager.view.CommonProgressDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private boolean isBound = false;
    private boolean isSendBound = false;
    private LoadPhoneService service = null;
    private SendMessengerService sendService = null;

    private Activity activity;
    private static final int START_LOAD = 0x10001,
            COMPLETE_LOAD = 0x10002,
            START_SEND = 0x10003,
            COMPLETE_SEND = 0x10004;

    private CommonProgressDialog dialog = null;
    private Button loadBtn, sendMessagerBtn;

    private String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    private EditText phoneNumberEditText, msgContentEidtText;


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context _context, Intent _intent) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(activity, "短信发送成功", Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    break;
            }
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_LOAD:
                    dialog.setMessage(getResources().getString(R.string.load_from_sdcard));
                    dialog.show();
                    break;
                case COMPLETE_LOAD:
                    dialog.dismiss();
                    LoadResult loadResult = (LoadResult) msg.obj;
                    if (loadResult != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle(activity.getResources().getString(R.string.load_result));
                        builder.setMessage("大鹅鹅，加载成功" + loadResult.getLoadSuccessNum() + "条," + loadResult.getLoadFailureNum() + "条!");
                        builder.setPositiveButton(activity.getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    }
                    break;
                case START_SEND:
                    dialog.setMessage(getResources().getString(R.string.sending_msg));
                    dialog.show();
                    break;
                case COMPLETE_SEND:
                    dialog.dismiss();
                    SendMsgResult sendMsgResult = (SendMsgResult) msg.obj;
                    if (sendMsgResult != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setCancelable(false);
                        builder.setTitle(activity.getResources().getString(R.string.load_result));
                        builder.setMessage("大鹅鹅，成功发送"+ sendMsgResult.getSendMsgNum()  + "条短信!");
                        builder.setPositiveButton(activity.getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private ILoadPhoneListener loadPhoneListener = new ILoadPhoneListener() {
        @Override
        public void onStartLoad() {
            mHandler.sendEmptyMessage(START_LOAD);
        }

        @Override
        public void onCompleteLoad(LoadResult result) {
            Message message = mHandler.obtainMessage();
            message.obj = result;
            message.what = COMPLETE_LOAD;
            mHandler.sendMessage(message);
        }
    };

    private ISendMsgListener sendMsgListener = new ISendMsgListener() {
        @Override
        public void onStartSend() {
            mHandler.sendEmptyMessage(START_SEND);
        }

        @Override
        public void onCompleteSend(SendMsgResult result) {
            Message message = mHandler.obtainMessage();
            message.obj = result;
            message.what = COMPLETE_SEND;
            mHandler.sendMessage(message);
        }
    };

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            isBound = true;
            LoadPhoneService.CustomBinder binder = (LoadPhoneService.CustomBinder) iBinder;
            service = binder.getBinder();
            service.setLoadPhoneListener(loadPhoneListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            service.setLoadPhoneListener(null);
            service = null;
            isBound = false;
        }
    };

    private ServiceConnection sendConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            isSendBound = true;
            SendMessengerService.CustomBinder binder = (SendMessengerService.CustomBinder) iBinder;
            sendService = binder.getBinder();
            sendService.setSendMsgListener(sendMsgListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            sendService.setSendMsgListener(null);
            sendService = null;
            isSendBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        dialog = new CommonProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.load_from_sdcard));

        List<String> permissions = new ArrayList<>();
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS) != PackageManager.PERMISSION_GRANTED) {

            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            permissions.add(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
        }
        if (!permissions.isEmpty()) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), 1);
        }
        bindLoadService();
        bindSendService();

        phoneNumberEditText = findViewById(R.id.send_phone_editText);
        msgContentEidtText = findViewById(R.id.send_content_editText);

        loadBtn = findViewById(R.id.load_data_btn);
        sendMessagerBtn = findViewById(R.id.send_message_btn);
        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(activity.getResources().getString(R.string.ok));
                builder.setMessage(R.string.confirm_load_data);
                builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent mIntent = new Intent(activity, LoadPhoneService.class);
                        startService(mIntent);
                    }
                });
                builder.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
        sendMessagerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //final String phoneNumber = phoneNumberEditText.getText().toString();
                final String msgContent = msgContentEidtText.getText().toString();

//                if (TextUtils.isEmpty(phoneNumber)) {
//                    ToastUtil.showMessage(activity, R.string.please_send_phone);
//                    return;
//                }
                if (TextUtils.isEmpty(msgContent)) {
                    ToastUtil.showMessage(activity, R.string.please_send_content);
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(activity.getResources().getString(R.string.ok));
                builder.setMessage(R.string.confirm_send_msg);
                builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent mIntent = new Intent(activity, SendMessengerService.class);
                        //mIntent.putExtra("SEND_PHONE", phoneNumber);
                        mIntent.putExtra("SMS_CONTENT", msgContent);
                        startService(mIntent);
                    }
                });
                builder.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });

        //registerSMSReceiver();
    }

    private void registerSMSReceiver() {
        Intent sentIntent = new Intent(SENT_SMS_ACTION);
        PendingIntent sentPI = PendingIntent.getBroadcast(activity, 0, sentIntent, 0);
        registerReceiver(broadcastReceiver, new IntentFilter(SENT_SMS_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unBindService();
        unBindSendService();
        //unregisterReceiver(broadcastReceiver);
    }

    public void bindLoadService() {
        Intent intent = new Intent(this, LoadPhoneService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    public void unBindService() {
        if (isBound) {
            unbindService(connection);
        }
    }

    public void bindSendService() {
        Intent intent = new Intent(this, SendMessengerService.class);
        bindService(intent, sendConnection, BIND_AUTO_CREATE);
    }

    public void unBindSendService() {
        if (isSendBound) {
            unbindService(sendConnection);
        }
    }

}
