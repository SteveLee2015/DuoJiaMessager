package com.duojia.messager.entity;

import java.util.ArrayList;
import java.util.List;

public class SendMsgResult {

    private int sendMsgNum;
    private int sendSuccessNum;
    private int sendFailureNum;
    private int sendMultiNum;
    private List<Phone> errorList = new ArrayList<>();

    public int getSendMsgNum() {
        return sendMsgNum;
    }

    public void setSendMsgNum(int sendMsgNum) {
        this.sendMsgNum = sendMsgNum;
    }

    public int getSendSuccessNum() {
        return sendSuccessNum;
    }

    public void setSendSuccessNum(int sendSuccessNum) {
        this.sendSuccessNum = sendSuccessNum;
    }

    public int getSendFailureNum() {
        return sendFailureNum;
    }

    public void setSendFailureNum(int sendFailureNum) {
        this.sendFailureNum = sendFailureNum;
    }

    public int getSendMultiNum() {
        return sendMultiNum;
    }

    public void setSendMultiNum(int sendMultiNum) {
        this.sendMultiNum = sendMultiNum;
    }

    public List<Phone> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<Phone> errorList) {
        this.errorList = errorList;
    }

    public void addErrorPhone(Phone phone) {
        if (errorList == null) {
            errorList = new ArrayList<>();
        }
        errorList.add(phone);
    }
}
