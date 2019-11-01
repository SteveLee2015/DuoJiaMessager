package com.duojia.messager.listener;

import com.duojia.messager.entity.LoadResult;

public interface ILoadPhoneListener {

    public void onStartLoad();
    public void onCompleteLoad(LoadResult result);
}
