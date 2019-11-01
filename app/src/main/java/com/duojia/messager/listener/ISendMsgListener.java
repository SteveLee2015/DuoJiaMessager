package com.duojia.messager.listener;

import com.duojia.messager.entity.SendMsgResult;

public interface ISendMsgListener {

    public void onStartSend();
    public void onCompleteSend(SendMsgResult result);
}
